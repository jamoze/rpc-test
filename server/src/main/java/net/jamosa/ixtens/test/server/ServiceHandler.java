package net.jamosa.ixtens.test.server;

import net.jamosa.ixtens.test.core.RequestMessage;
import net.jamosa.ixtens.test.core.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
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
            } while (req.getSeq() < 1057);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    // TODO: to refactor
    private ResponseMessage processMessage(RequestMessage req) throws
            ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        ResponseMessage result = new ResponseMessage();
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

        return result;
    }
}
