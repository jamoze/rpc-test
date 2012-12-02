package net.jamosa.ixtens.test.client;

import net.jamosa.ixtens.test.core.RequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.UnknownHostException;

public class MyClient {

    private Logger log = LoggerFactory.getLogger(MyClient.class);

    private final static String REMOTE_HOST = "localhost";
    private final static int REMOTE_PORT = 1205;
    private final static int MESSAGES_COUNT_TO_SEND = 1057;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private final Object lock = new Object();

    public MyClient() {
    }

    void run() {
        try {
            socket = new Socket(REMOTE_HOST, REMOTE_PORT);
            log.debug("Connected to localhost in port {}", REMOTE_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            // TODO: Why do we need this?
            // out.flush();
            in = new ObjectInputStream(socket.getInputStream());
/*
            do {
                try {
                    message = (String) in.readObject();
                    log.debug("server>{}", message);
                    sendMessage("Hi my server");
                    message = "bye";
                    sendMessage(message);
                } catch (ClassNotFoundException e) {
                    log.error(e.getMessage(), e);
                }
            } while (!message.equals("bye"));
*/

            for (int i = 0; i < MESSAGES_COUNT_TO_SEND; i++) {
                RequestMessage message = new RequestMessage();
                message.setSeq(i);
                message.setServiceName("service1");
                message.setMethodName("method1");
                message.setArgs(new Object[]{"abv", 10l + i * 105, 1.2d / i});

                sendMessage(message);

                synchronized (lock) {
                    try {
                        lock.wait(1000L);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }

        } catch (UnknownHostException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void sendMessage(RequestMessage msg) {
        try {
            out.writeObject(msg);
            out.flush();
            log.debug("client > this:{}, msg:{}", this, msg);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void main(String args[]) {
        MyClient client = new MyClient();
        client.run();
    }
}
