package net.jamosa.ixtens.test.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

    private Logger log = LoggerFactory.getLogger(MyServer.class);

    private ServerSocket serverSocket;
    private int connectionsCount = 0;

    private ServerConfiguration serverConfig;

    public MyServer() {
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void init() throws IOException {
        // Loading configuration
        InputStream configInputStream = ClassLoader.class.getResourceAsStream("/server.properties");
        serverConfig = new ServerConfiguration(configInputStream);
    }

    public void start() throws IOException {

        serverSocket = new ServerSocket(serverConfig.getPort());

        // TODO: Move to connections pool logic
        while (connectionsCount < serverConfig.getMaxConnections()) {
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
            server.init();
            server.start();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}