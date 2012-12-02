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
    private final static int REMOTE_PORT = 2004;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String message;

    public MyClient() {
    }

    void run() {
        try {
            socket = new Socket(REMOTE_HOST, REMOTE_PORT);
            log.debug("Connected to localhost in port {}", REMOTE_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
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

    void sendMessage(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
            log.debug("client>{}", msg);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void main(String args[]) {
        MyClient client = new MyClient();
        client.run();
    }
}
