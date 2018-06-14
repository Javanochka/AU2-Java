package ru.spbau.nikiforovskaya.server;

import com.google.common.primitives.Ints;
import ru.spbau.nikiforovskaya.utils.ProtoMessage;
import ru.spbau.nikiforovskaya.utils.ProtoMessageInterpreter;
import ru.spbau.nikiforovskaya.utils.Sorter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolServer extends Server {

    public ThreadPoolServer(int port) {
        super(port);
    }

    @Override
    public void run() {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try (ServerSocket listener = new ServerSocket(port)) {
            while (!Thread.interrupted()) {
                Socket socket = listener.accept();
                ExecutorService answerSender = Executors.newSingleThreadExecutor();
                Thread talker = new Thread(() -> processConnection(socket, threadPool, answerSender));
                talker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processConnection(Socket socket,
                                   ExecutorService threadPool, ExecutorService answerSender) {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            while (!Thread.interrupted()) {
                final long[] timeToProcess = {System.currentTimeMillis()};
                int size = input.readInt();
                if (size == 0) {
                    break;
                }
                ProtoMessage.Array arrayMessage = ProtoMessageInterpreter.readArrayMessage(input, size);
                int[] arrayToSort = Ints.toArray(arrayMessage.getDataList());
                threadPool.submit(() -> {
                    long timeToSort = System.currentTimeMillis();
                    Sorter.sort(arrayToSort);
                    timeToSort = System.currentTimeMillis() - timeToSort;
                    long finalTimeToSort = timeToSort;
                    answerSender.submit(() -> {
                        try {
                            ProtoMessageInterpreter.writeArrayMessage(output, arrayToSort);
                            timeToProcess[0] = System.currentTimeMillis() - timeToProcess[0];
                            ProtoMessageInterpreter.writeTimingMessage(output, (int)finalTimeToSort, (int)timeToProcess[0]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
