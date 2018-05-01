package ru.spbau.nikiforovskaya.ftp;

import ru.spbau.nikiforovskaya.logger.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Ftp server manager.
 * Can get queries of two types and process them.
 */
public class Server {

    /**
     * Runs server in console
     * @param args args[0] -- on which port to run server
     * @throws IOException if something happened while opening console for writing/reading.
     */
    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);

        Thread server = new Thread(() -> {
            try {
                new Server().runServer(portNumber);
            } catch (FileNotFoundException e) {
                System.err.println("Didn't manage to start server.");
                System.err.println("Error while opening log file");
            }
        });
        server.setDaemon(false);
        server.start();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter output = new PrintWriter(System.out, true)) {
            output.println("Enter \"exit\" to stop running server.");
            while (true) {
                String what = input.readLine();
                if (what.equals("exit")) {
                    break;
                }
            }

        }

        while (server.isAlive()) {
            server.interrupt();
        }
    }

    private Logger logger;

    /**
     * Server manager constructor.
     * Preparing background for running server.
     * @throws FileNotFoundException if didn't manage to open file for server logging.
     */
    public Server() throws FileNotFoundException {
        logger = new Logger(Thread.currentThread().getName());
    }

    /**
     * Runs server.
     * @param portNumber a number of port on which to run.
     */
    public void runServer(int portNumber) {
        try (ServerSocket listener = new ServerSocket(portNumber)) {
            listener.setSoTimeout(2000);
            logger.log("Server " + listener.getInetAddress().getCanonicalHostName() +
                    " on port " + portNumber + " is running");
            while (!Thread.interrupted()) {
                try (Socket socket = listener.accept()) {
                    logger.log("Connected to " + socket.getInetAddress().getCanonicalHostName());
                    processConnection(socket);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                } finally {
                    logger.log("Disconnected");
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            logger.log("Server is closed");
        }
    }

    private void processConnection(Socket socket) throws ConnectionProtocolException {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            int type = input.readInt();
            String path = input.readUTF();

            if (type == 1) {
                processListQuery(output, path);
                output.flush();
                return;
            }
            if (type == 2) {
                processGetQuery(output, path);
                output.flush();
                return;
            }

            throw new ConnectionProtocolException();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processGetQuery(DataOutputStream output, String path) throws IOException {
        File file = new File(path);

        if (!file.exists() || !file.isFile()) {
            output.writeLong(0);
            return;
        }

        output.writeLong(file.length());
        try (DataInputStream input = new DataInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[1024];
            for (int read = input.read(buffer); read != -1; read = input.read(buffer)) {
                output.write(buffer, 0, read);
            }
        }
    }

    /*
    private static String getPath(DataInputStream input) throws IOException {
        return input.readUTF();
    }
    */

    private void processListQuery(DataOutputStream output, String path) throws IOException {
        File file = new File(path);

        if (!file.exists()) {
            output.writeInt(0);
            return;
        }

        ArrayList<String> result = new ArrayList<>();
        int size = getDirectoryTree(file, result);
        output.writeInt(size);
        for (String info : result) {
            output.writeUTF(info);
        }
    }

    private static int getDirectoryTree(File parent, ArrayList<String> list) {
        int size = 0;
        if (parent.isDirectory()) {
            File[] children = parent.listFiles();
            if (children == null) {
                return size;
            }
            for (File child : children) {
                list.add(child.getPath() + " " + child.isDirectory());
                size += 1 + getDirectoryTree(child, list);
            }
        }
        return size;
    }
}
