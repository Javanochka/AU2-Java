package ru.spbau.nikiforovskaya.client;

import com.google.common.primitives.Ints;
import ru.spbau.nikiforovskaya.utils.ProtoMessage;
import ru.spbau.nikiforovskaya.utils.ProtoMessageInterpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;

public class Client {

    private String hostName;
    private int port;
    private int numberOfElements;
    private int numberOfQueries;
    private int delta;

    private Random r;

    public Client(String hostName, int port, int numberOfElements,
                  int numberOfQueries, int delta) {
        this.hostName = hostName;
        this.port = port;
        this.numberOfElements = numberOfElements;
        this.numberOfQueries = numberOfQueries;
        this.delta = delta;
        r = new Random();
    }

    public int[] run() throws IOException {
        long clientTime = System.currentTimeMillis();
        long sortTime = 0;
        long processTime = 0;
        try (Socket socket = new Socket(hostName, port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            for (int i = 0; i < numberOfQueries; i++) {
                int[] array = generateRandomArray(numberOfElements);
                ProtoMessageInterpreter.writeArrayMessage(output, array);
                int size = input.readInt();
                ProtoMessage.Array result = ProtoMessageInterpreter.readArrayMessage(input, size);
                boolean sorted = checkSorted(Ints.toArray(result.getDataList()));
                if (!sorted) {
                    System.err.println("Didn't sort! " + Arrays.toString(Ints.toArray(result.getDataList())));
                }
                size = input.readInt();
                ProtoMessage.Timing times = ProtoMessageInterpreter.readTimingMessage(input, size);
                sortTime += times.getQueryTime();
                processTime += times.getProcessingTime();
                Thread.sleep(delta);
            }
            output.writeInt(0);
        } catch (InterruptedException e) {
            System.err.println("Why was I interrupted??");
        }
        clientTime = System.currentTimeMillis() - clientTime;
        clientTime /= numberOfQueries;
        sortTime /= numberOfQueries;
        processTime /= numberOfQueries;
        return new int[]{(int)clientTime, (int)sortTime, (int)processTime};
    }

    private boolean checkSorted(int[] ints) {
        for (int i = 1; i > ints.length; i++) {
            if (ints[i] < ints[i - 1]) {
                return false;
            }
        }
        return true;
    }

    private int[] generateRandomArray(int numberOfElements) {
        int[] result = new int[numberOfElements];
        for (int i = 0; i < result.length; i++) {
            result[i] = r.nextInt();
        }
        return result;
    }
}
