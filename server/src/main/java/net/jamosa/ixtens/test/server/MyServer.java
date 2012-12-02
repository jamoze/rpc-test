package net.jamosa.ixtens.test.server;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class MyServer {

    private Logger log = LoggerFactory.getLogger(MyServer.class);

    private static final int MAX_CONNECTIONS = 2831;
    private static final String DEFAULT_PORT = "1205";

    private ServerSocket serverSocket;
    private int connectionsCount = 0;

    private int port;

    public MyServer() {
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void init() throws IOException {
        // Loading configuration
        InputStream configInputStream = ClassLoader.class.getResourceAsStream("/server.properties");
        Properties serverConfig = new Properties();
        serverConfig.load(configInputStream);

        String portValue = serverConfig.getProperty("port", DEFAULT_PORT);
        port = Integer.parseInt(portValue);
    }

    public void start() throws IOException {

        serverSocket = new ServerSocket(port);

        // TODO: Move to connections pool logic
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
            server.init();
            server.start();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }
}