package net.jamosa.ixtens.test.server;

import net.jamosa.ixtens.test.core.RequestMessage;
import net.jamosa.ixtens.test.core.ResponseMessage;
import net.jamosa.ixtens.test.core.exceptions.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceHandler implements Runnable {

    private Logger log = LoggerFactory.getLogger(ServiceHandler.class);

    private ServerConfiguration serverConfig;
    private Socket socket;

    public ServiceHandler(ServerConfiguration serverConfig, Socket socket) {
        this.serverConfig = serverConfig;
        this.socket = socket;
    }

    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            log.debug("Connection received from {}", socket.getInetAddress().getHostName());

            RequestMessage req;
            do {
                req = (RequestMessage) in.readObject();
                log.debug("Message received, message: {}", req);

                ResponseMessage resp = processMessage(req);

                out.writeObject(resp);
                out.flush();
                log.debug("Message sent, message: {}", resp);
            } while (req.getSeq() < 1057);

        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private ResponseMessage processMessage(RequestMessage req) {
        ResponseMessage result = new ResponseMessage();
        try {
            result.setSeq(req.getSeq());

            Map<String, String> services = serverConfig.getServices();
            Class serviceClass = Class.forName(services.get(req.getServiceName()));

            List<Class<?>> argsTypeList = new ArrayList<Class<?>>();
            for (Object arg : req.getArgs()) {
                argsTypeList.add(arg.getClass());
            }

            Class[] par = new Class[]{};
            Method serviceMethod = serviceClass.getMethod(req.getMethodName(), argsTypeList.toArray(par));

            result.setResult(serviceMethod.invoke(serviceClass.newInstance(), req.getArgs()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setServerError(new ServerException(e.getMessage(), e));
        }

        return result;
    }
}
