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
import java.util.Arrays;

public class ThreadForEachServer extends Server {

    public ThreadForEachServer(int port) {
        super(port);
    }

    @Override
    public void run() {
        try (ServerSocket listener = new ServerSocket(port)) {
            listener.setSoTimeout(5000);
            while (!Thread.interrupted()) {
                try {
                    Socket socket = listener.accept();
                    Thread talker = new Thread(() -> processConnection(socket));
                    talker.start();
                } catch (Exception ignored) {
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processConnection(Socket socket) {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
                 DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            while (!Thread.interrupted()) {
                long timeToProcess = System.currentTimeMillis();
                int size = input.readInt();
                if (size == 0) {
                    break;
                }
                ProtoMessage.Array arrayMessage = ProtoMessageInterpreter.readArrayMessage(input, size);
                int[] arrayToSort = Ints.toArray(arrayMessage.getDataList());
                long timeToSort = System.currentTimeMillis();
                Sorter.sort(arrayToSort);
                timeToSort = System.currentTimeMillis() - timeToSort;
                ProtoMessageInterpreter.writeArrayMessage(output, arrayToSort);
                timeToProcess = System.currentTimeMillis() - timeToProcess;
                ProtoMessageInterpreter.writeTimingMessage(output, (int)timeToSort, (int)timeToProcess);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
