package ru.spbau.nikiforovskaya.server;

import com.google.common.primitives.Ints;
import ru.spbau.nikiforovskaya.utils.ProtoMessage;
import ru.spbau.nikiforovskaya.utils.ProtoMessageInterpreter;
import ru.spbau.nikiforovskaya.utils.Sorter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NonBlockingServer extends Server {
    public NonBlockingServer(int port) {
        super(port);
    }

    private Selector reader;
    private Selector writer;

    private ExecutorService threadPool = Executors.newFixedThreadPool(3);

    private ConcurrentLinkedQueue<ClientInstance> registeredToRead = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<ClientInstance> registeredToWrite = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
        ServerSocketChannel serverSocketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();

            try {
                serverSocketChannel.bind(new InetSocketAddress(port));
            } catch (IOException e) {
                e.printStackTrace();
            }

            reader = Selector.open();
            writer = Selector.open();

            Thread readerWorker = new Thread(readerRunner);
            Thread writerWorker = new Thread(writerRunner);
            readerWorker.start();
            writerWorker.start();

            while (!Thread.interrupted()) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                registeredToRead.add(new ClientInstance(socketChannel));
                reader.wakeup();
            }

            readerWorker.interrupt();
            writerWorker.interrupt();
            serverSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Runnable readerRunner = () -> {
        while (!Thread.interrupted()) {
            while (!registeredToRead.isEmpty()) {
                SocketChannel socketChannel = registeredToRead.poll().getSocketChannel();
                try {
                    socketChannel.configureBlocking(false);
                    socketChannel.register(reader, SelectionKey.OP_READ, new ClientInstance(socketChannel));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                int readyChannels = reader.select();
                if (readyChannels == 0) {
                    continue;
                }
                Iterator<SelectionKey> keyIterator = reader.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    ClientInstance client = (ClientInstance) key.attachment();

                    client.read();
                    keyIterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable writerRunner = () -> {
        while (!Thread.interrupted()) {
            while (!registeredToWrite.isEmpty()) {
                ClientInstance client = registeredToWrite.poll();
                SocketChannel socketChannel = client.getSocketChannel();
                try {
                    socketChannel.register(writer, SelectionKey.OP_WRITE, client);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                int readyChannels = writer.select();
                if (readyChannels == 0) {
                    continue;
                }
                Iterator<SelectionKey> keyIterator = writer.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    ClientInstance client = (ClientInstance) key.attachment();

                    client.write();
                    keyIterator.remove();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private class ClientInstance {

        private static final int MAX_SIZE = 100000;

        private SocketChannel socketChannel;
        private long timeToProcess;
        private long timeToSort;

        private final ByteBuffer readBuffer = ByteBuffer.allocate(MAX_SIZE);
        private final ByteBuffer writeBuffer = ByteBuffer.allocate(MAX_SIZE);

        boolean firstRead = true;

        private byte[] futureMessage = null;
        private int sizeOfRead = 0;
        private boolean hasFinishedWriting = false;

        public SocketChannel getSocketChannel() {
            return socketChannel;
        }

        public ClientInstance(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
            writeBuffer.flip();
        }

        public void read() throws IOException {
            if (firstRead) {
                timeToProcess = System.currentTimeMillis();
                firstRead = false;
            }
            int bytesRead = socketChannel.read(readBuffer);
            while (bytesRead > 0) {
                bytesRead = socketChannel.read(readBuffer);
            }
            readBuffer.flip();
            if (futureMessage == null && readBuffer.remaining() >= 4) {
                int size = readBuffer.getInt();
                if (size == 0) {
                    socketChannel.close();
                    return;
                }
                futureMessage = new byte[size];
            }
            while (futureMessage != null && sizeOfRead < futureMessage.length && readBuffer.remaining() > 0) {
                futureMessage[sizeOfRead++] = readBuffer.get();
            }
            readBuffer.compact();
            if (futureMessage != null && futureMessage.length == sizeOfRead) {
                byte[] data = futureMessage;
                futureMessage = null;
                sizeOfRead = 0;
                ProtoMessage.Array arrayToSort = ProtoMessageInterpreter.readArrayMessage(data);
                int[] toSort = Ints.toArray(arrayToSort.getDataList());
                threadPool.submit(() -> {
                    timeToSort = System.currentTimeMillis();
                    Sorter.sort(toSort);
                    timeToSort = System.currentTimeMillis() - timeToSort;
                    writeToBuffer(toSort);
                    hasFinishedWriting = false;
                    registeredToWrite.add(this);
                    writer.wakeup();
                });
            }
        }

        private void writeToBuffer(int[] toSort) {
            byte[] data = ProtoMessageInterpreter.getBytesFromArrayMessage(toSort);
            synchronized (writeBuffer) {
                writeBuffer.compact();
                writeBuffer.putInt(data.length);
                writeBuffer.put(data);
                writeBuffer.flip();
            }
        }

        public void write() throws IOException {
            synchronized (writeBuffer) {
                if (writeBuffer.remaining() == 0) {
                    return;
                }
                int written = socketChannel.write(writeBuffer);
                if (written > 0 && writeBuffer.remaining() == 0) {
                    if (hasFinishedWriting) {
                        firstRead = true;
                        return;
                    }
                    hasFinishedWriting = true;
                    timeToProcess = System.currentTimeMillis() - timeToProcess;
                    writeToBuffer(timeToSort, timeToProcess);
                }
            }
        }

        private void writeToBuffer(long timeToSort, long timeToProcess) {
            byte[] data = ProtoMessageInterpreter.getBytesFromTimingMessage((int)timeToSort, (int)timeToProcess);
            synchronized (writeBuffer) {
                writeBuffer.compact();
                writeBuffer.putInt(data.length);
                writeBuffer.put(data);
                writeBuffer.flip();
            }
        }
    }
}
