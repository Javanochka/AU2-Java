package ru.spbau.nikiforovskaya.ftp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * A client (+console client) for ftp server.
 * Can send two types of queries to server on the given host + port.
 */
public class Client {

    private String hostName;
    private int port;
    private Scanner inputUser;
    private PrintWriter outputUser;


    /**
     * Run your client in console
     * @param args args[0], args[1] should be host name and port of the server.
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Run this program with " +
                    "host name and port number in arguments.");
            return;
        }
        String hostName = args[0];
        int port;

        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("The second argument should be port number.");
            return;
        }

        while (true) {
            try {
                new Client(hostName, port, System.in, System.out).runClient();
                return;
            } catch (ConnectionProtocolException e) {
                System.out.println("Sorry, you typed in wrong query");
                System.out.println("\"exit\" to exit");
                System.out.println("\"list\" <path: String> -- to list all the " +
                        "files in the given path on server.");
                System.out.println("\"get\" <path: String> -- to get the file from server.");
            } catch (IOException e) {
                System.out.println("Sorry, something wrong has happened while writing to or " +
                        "reading from server");
            }
        }
    }

    /**
     * A constructor of client.
     * @param hostName host name of the ftp server to connect to
     * @param port port number of the frp server to connect to
     * @param input an input stream with which client manager can interrupt with you
     * @param output an output stream with which client manager can interrupt with you
     */
    public Client(String hostName, int port, InputStream input, OutputStream output) {
        this.hostName = hostName;
        this.port = port;
        inputUser = new Scanner(input);
        outputUser = new PrintWriter(output, true);
    }

    /**
     * Start client manager.
     * @throws ConnectionProtocolException if you asked client to send
     * something not in protocol.
     * @throws IOException if something has happened while interacting with server.
     */
    public void runClient() throws ConnectionProtocolException, IOException {
        while (true) {
            String what = inputUser.next();
            if (what.equals("exit")) {
                return;
            }
            QueryType type = QueryType.fromString(what);
            if (type == null) {
                throw new ConnectionProtocolException();
            }
            String path = inputUser.next();
            sendQuery(type, path);
        }
    }

    /**
     * Sens query to ftp server.
     * @param type type of operation you ask to do.
     * @param path a path to the file/directory you want to get/list.
     */
    private void sendQuery(QueryType type, String path) throws IOException {
        try (Socket socket = new Socket(hostName, port);
             DataOutputStream outputServer = new DataOutputStream(socket.getOutputStream());
             DataInputStream inputServer = new DataInputStream(socket.getInputStream())) {

            outputServer.writeInt(type.getInt());
            outputServer.writeUTF(path);

            if (type == QueryType.LIST) {
                listFiles(inputServer);
            } else {
                outputUser.println("Type in filename:");
                String fileName = inputUser.next();
                saveFile(inputServer, fileName);
            }
        }
    }

    private void saveFile(DataInputStream inputServer, String name) throws IOException {
        long size = inputServer.readLong();
        if (size == -1) {
            outputUser.println("You have asked to download a directory.\n" +
                    "This command can only download files.");
            return;
        }

        try (OutputStream outputFile = new FileOutputStream(new File(name))) {
            byte[] buffer = new byte[1024];
            int read = 0;
            while (read < size) {
                int newRead = inputServer.read(buffer);
                if (newRead + read > size) {
                    newRead = (int)(size - read);
                }
                outputFile.write(buffer, 0, newRead);
                read += newRead;
            }
            outputUser.println("Successfully saved file to " + name);
        }
    }

    private void listFiles(DataInputStream inputServer) throws IOException {
        int numberOfFiles = inputServer.readInt();
        outputUser.println(numberOfFiles + " lines got");
        for (int i = 0; i < numberOfFiles; i++) {
            outputUser.println(inputServer.readUTF());
        }
    }
}
