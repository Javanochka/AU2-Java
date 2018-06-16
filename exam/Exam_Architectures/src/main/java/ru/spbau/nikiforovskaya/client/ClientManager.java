package ru.spbau.nikiforovskaya.client;

import javafx.util.Pair;
import ru.spbau.nikiforovskaya.utils.ProtoMessage;
import ru.spbau.nikiforovskaya.utils.ProtoMessageInterpreter;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientManager {

    private String serverManagerHost;
    private int serverManagerPort;
    private ProtoMessage.Settings.ServerType type;
    private int numberOfQueries;
    private int[] startValues;
    private int whoChanges;
    private int step;
    private int finalValue;


    public ClientManager(String serverManagerHost, int serverManagerPort,
                         ProtoMessage.Settings.ServerType type,
                         int numberOfQueries, int[] startValues,
                         int whoChanges, int step, int finalValue) {
        this.serverManagerHost = serverManagerHost;
        this.serverManagerPort = serverManagerPort;
        this.type = type;
        this.numberOfQueries = numberOfQueries;
        this.startValues = startValues;
        this.whoChanges = whoChanges;
        this.step = step;
        this.finalValue = finalValue;
    }

    private ArrayList<Pair<Integer, Integer>> clientTimeList;
    private ArrayList<Pair<Integer, Integer>> sortTimeList;
    private ArrayList<Pair<Integer, Integer>> processTimeList;

    public ArrayList<Pair<Integer, Integer>> getClientTimeList() {
        return clientTimeList;
    }

    public ArrayList<Pair<Integer, Integer>> getProcessTimeList() {
        return processTimeList;
    }

    public ArrayList<Pair<Integer, Integer>> getSortTimeList() {
        return sortTimeList;
    }

    public void run() {
        try {
            Socket socketToServerManager = new Socket(serverManagerHost, serverManagerPort);
            try (DataInputStream input = new DataInputStream(socketToServerManager.getInputStream());
                 DataOutputStream output = new DataOutputStream(socketToServerManager.getOutputStream())) {
                ProtoMessageInterpreter.writeSettingsMessage(output, type, serverManagerPort + 1);
                int success = input.readInt();
                if (success != 0) {
                    System.err.println("Wasn't successful.");
                    return;
                }

                clientTimeList = new ArrayList<>();
                sortTimeList = new ArrayList<>();
                processTimeList = new ArrayList<>();

                switch (whoChanges) {
                    case 0: {
                        stepOverClients();
                        break;
                    }
                    case 1: {
                        stepOverElements();
                        break;
                    }
                    case 2: {
                        stepOverDelta();
                        break;
                    }
                }
                output.writeInt(0);
            }
            printOneMetric(clientTimeList, "time_on_client");
            printOneMetric(processTimeList, "time_processing");
            printOneMetric(sortTimeList, "time_sorting");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void printOneMetric(List<Pair<Integer, Integer>> values, String metricName) throws
            FileNotFoundException {
        String[] order = new String[] {
                "clients", "elements", "delta"
        };
        File file = new File(Paths.get("logs",type.toString() + "_" +
                metricName + "_" + whoChanges + ".txt").toString());
        try (PrintWriter out = new PrintWriter(file)) {
            out.println("Queries: " + numberOfQueries);
            out.println("Clients: " + startValues[0]);
            out.println("Elements: " + startValues[1]);
            out.println("Delta: " + startValues[2]);
            out.println("Changed parameter: " + order[whoChanges]);
            out.println("Statistics: (<parameter> <result>)");
            for (Pair<Integer, Integer> p : values) {
                out.println(p.getKey() + " " + p.getValue());
            }
        }
    }



    private void stepOverClients() {
        for (int nClients = startValues[0]; nClients < finalValue; nClients += step) {
            System.err.println(nClients);
            int[] result = runNClients(nClients, numberOfQueries, startValues[1], startValues[2]);
            clientTimeList.add(new Pair<>(nClients, result[0]));
            sortTimeList.add(new Pair<>(nClients, result[1]));
            processTimeList.add(new Pair<>(nClients, result[2]));
        }
    }

    private void stepOverElements() {
        for (int nElements = startValues[1]; nElements < finalValue; nElements += step) {
            int[] result = runNClients(startValues[0], numberOfQueries, nElements, startValues[2]);
            clientTimeList.add(new Pair<>(nElements, result[0]));
            sortTimeList.add(new Pair<>(nElements, result[1]));
            processTimeList.add(new Pair<>(nElements, result[2]));
        }
    }

    private void stepOverDelta() {
        for (int nDelta = startValues[2]; nDelta < finalValue; nDelta += step) {
            int[] result = runNClients(startValues[0], numberOfQueries, startValues[1], nDelta);
            clientTimeList.add(new Pair<>(nDelta, result[0]));
            sortTimeList.add(new Pair<>(nDelta, result[1]));
            processTimeList.add(new Pair<>(nDelta, result[2]));
        }
    }

    private int[] runNClients(int numberOfClients, int numberOfQueries, int numberOfElements, int delta) {
        AtomicInteger clientTime = new AtomicInteger(0);
        AtomicInteger sortTime = new AtomicInteger(0);
        AtomicInteger processTime = new AtomicInteger(0);
        Thread[] clients = new Thread[numberOfClients];
        for (int i = 0; i < clients.length; i++) {
            clients[i] = new Thread(() -> {
                try {
                    int[] times = new Client(serverManagerHost, serverManagerPort + 1,
                            numberOfElements, numberOfQueries, delta).run();
                    clientTime.getAndAdd(times[0]);
                    sortTime.getAndAdd(times[1]);
                    processTime.getAndAdd(times[2]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        }
        for (int i = 0; i < clients.length; i++) {
            clients[i].start();
        }
        for (int i = 0; i < clients.length; i++) {
            try {
                clients[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new int[]{clientTime.get() / numberOfClients, sortTime.get() / numberOfClients,
                        processTime.get() / numberOfClients};
    }
}
