package ru.spbau.nikiforovskaya.server;

import ru.spbau.nikiforovskaya.utils.ProtoMessage;
import ru.spbau.nikiforovskaya.utils.ProtoMessageInterpreter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManager {

    private static int port;

    public static void main(String[] args) throws IOException {
        port = Integer.parseInt(args[0]);
        ServerSocket serverManager = new ServerSocket(port);
        Socket socket = serverManager.accept();
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            int size = input.readInt();
            ProtoMessage.Settings settings = ProtoMessageInterpreter.readSettingsMessage(input, size);
            Server serverToRun = null;
            switch (settings.getType()) {
                case THREAD_EACH:
                    serverToRun = new ThreadForEachServer(settings.getPort());
                    break;
                case THREAD_POOL:
                    serverToRun = new ThreadPoolServer(settings.getPort());
                    break;
                case NONBLOCKING:
                    serverToRun = new NonBlockingServer(settings.getPort());
                    break;
            }

            Thread server = new Thread(serverToRun::run);
            server.start();
            output.writeInt(0);
            input.readInt();
            while (server.isAlive()) {
                server.interrupt();
            }

        }
        socket.close();
        serverManager.close();
    }
}
