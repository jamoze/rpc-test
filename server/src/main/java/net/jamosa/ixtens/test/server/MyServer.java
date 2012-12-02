package net.jamosa.ixtens.test.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

    private Logger log = LoggerFactory.getLogger(MyServer.class);

    // TODO: Use pool
    private static final int MAX_CONNECTIONS = 10;

    private ServerSocket serverSocket;
    private int connectionsCount = 0;

    public MyServer() {
        try {
            serverSocket = new ServerSocket(2004, 10);
        } catch (IOException e) {
            log.error("Can't open server socket on port {}", 2004);
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void start() throws IOException {
        while (connectionsCount < MAX_CONNECTIONS) {
                log.debug("Waiting for clientSocket");
                Socket clientSocket = serverSocket.accept();
                ServiceHandler serviceHandler = new ServiceHandler(clientSocket);
                serviceHandler.start();
                connectionsCount++;
        }
    }

    public static void main(String args[]) throws IOException {
        MyServer server = new MyServer();
        ServerSocket socket = server.getServerSocket();
        try {
            server.start();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}