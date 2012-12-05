package net.jamosa.ixtens.test.server;

import net.jamosa.ixtens.test.core.exceptions.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyServer {

    private static Logger log = LoggerFactory.getLogger(MyServer.class);

    private ServerSocket serverSocket;
    private int connectionsCount = 0;

    private ServerConfiguration serverConfig;

    private ThreadPoolExecutor executor;

    public MyServer() {
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void init() throws ServerException {
        // Loading configuration
        InputStream configInputStream = ClassLoader.class.getResourceAsStream("/server.properties");
        serverConfig = new ServerConfiguration(configInputStream);

        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(100, true);
        executor = new ThreadPoolExecutor(
                serverConfig.getThreadPoolCoreSize(),
                serverConfig.getThreadPoolMaxSize(),
                serverConfig.getThreadPoolKeepAlive(),
                TimeUnit.MINUTES,
                queue
        );
    }

    public void start() throws IOException {

        serverSocket = new ServerSocket(serverConfig.getPort());

        // TODO: interrupt condition?
        while (true) {
            if (connectionsCount < serverConfig.getMaxConnections()) {
                log.debug("Waiting for clientSocket");
                Socket clientSocket = serverSocket.accept();

                ServiceHandler serviceHandler = new ServiceHandler(serverConfig, clientSocket);
                executor.execute(serviceHandler);

                connectionsCount++;
            }

            synchronized (this) {
                try {
                    wait(10L);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public static void main(String args[]) {
        MyServer server = new MyServer();
        ServerSocket socket = server.getServerSocket();
        try {
            server.init();
            server.start();
        } catch (ServerException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}