package net.jamosa.ixtens.test.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServiceHandler extends Thread {

    private Logger log = LoggerFactory.getLogger(ServiceHandler.class);

    private Socket socket;

    public ServiceHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            String message;

            log.debug("Connection received from {}", socket.getInetAddress().getHostName());

            sendMessage("Connection successful", out);
            do {
                message = (String) in.readObject();
                log.debug("client>{}", message);
                if (message.equals("bye"))
                    sendMessage("bye", out);
            } while (!message.equals("bye"));
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    void sendMessage(String msg, ObjectOutputStream out) {
        try {
            out.writeObject(msg);
            out.flush();
            log.debug("server>{}", msg);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
