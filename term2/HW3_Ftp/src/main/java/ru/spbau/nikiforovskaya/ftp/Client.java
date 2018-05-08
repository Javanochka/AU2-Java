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
        String hostName = args[0];
        int port = Integer.parseInt(args[1]);
        try {
            new Client(hostName, port, System.in, System.out).runClient();
        } catch (ConnectionProtocolException e) {
            System.out.println("Sorry, you typed in wrong query");
            System.out.println("0 to exit");
            System.out.println("<1: Int> <path: String> -- to list all the " +
                    "files in the given path on server.");
            System.out.println("<2: Int> <path: String> -- to get the file from server.");
        } catch (IOException e) {
            System.out.println("Sorry, something wrong has happened while writing to or " +
                    "reading from server");
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
            int type = inputUser.nextInt();
            if (type == 0) {
                return;
            }
            if (type != 1 && type != 2) {
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
    private void sendQuery(int type, String path) throws IOException {
        try (Socket socket = new Socket(hostName, port);
             DataOutputStream outputServer = new DataOutputStream(socket.getOutputStream());
             DataInputStream inputServer = new DataInputStream(socket.getInputStream())) {

            outputServer.writeInt(type);
            outputServer.writeUTF(path);

            if (type == 1) {
                listFiles(inputServer);
            } else {
                saveFile(inputServer, new File(path).getName());
            }
        }
    }

    private void saveFile(DataInputStream inputServer, String name) throws IOException {
        try (OutputStream outputFile = new FileOutputStream(new File(name))) {
            long size = inputServer.readLong();
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
