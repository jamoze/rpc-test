package net.jamosa.ixtens.test.client;

import net.jamosa.ixtens.test.core.Client;
import net.jamosa.ixtens.test.core.RequestMessage;
import net.jamosa.ixtens.test.core.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.UnknownHostException;

public class MyClient extends Client {

    private Logger log = LoggerFactory.getLogger(MyClient.class);

    private final static String REMOTE_HOST = "localhost";
    private final static int REMOTE_PORT = 1205;
    private final static int MESSAGES_COUNT_TO_SEND = 1057;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private final Object lock = new Object();

    public MyClient(String host, int port) {
        super(host, port);
    }

    void run() {
        try {
            socket = new Socket(REMOTE_HOST, REMOTE_PORT);
            log.debug("Connected to localhost in port {}", REMOTE_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            for (int i = 0; i < MESSAGES_COUNT_TO_SEND; i++) {
                RequestMessage req = new RequestMessage();
                req.setSeq(i);
                req.setServiceName("dateTime");
                req.setMethodName("getDoomsdayLeft");
                req.setArgs(new Object[]{});

                ResponseMessage resp = remoteCall(req);
                log.info("Response from server: seq={}, result={}", resp.getSeq(), resp.getResult());

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


    @Override
    public ResponseMessage remoteCall(RequestMessage msg) {
        try {
            out.writeObject(msg);
            out.flush();
            log.debug("client > this:{}, msg:{}", this, msg);
            return (ResponseMessage) in.readObject();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    public static void main(String args[]) {
        MyClient client = new MyClient(REMOTE_HOST, REMOTE_PORT);
        client.run();
    }
}
