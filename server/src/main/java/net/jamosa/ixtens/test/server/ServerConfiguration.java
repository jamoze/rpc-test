package net.jamosa.ixtens.test.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ServerConfiguration {

    private Logger log = LoggerFactory.getLogger(ServerConfiguration.class);

    private static final String MAX_CONNECTIONS = "20";
    private static final String DEFAULT_PORT = "1205";

    private int port;
    private int maxConnections;

    private Map services;

    public ServerConfiguration(InputStream configInputStream) throws IOException {
        Properties props = new Properties();
        props.load(configInputStream);

        log.info("Loading configuration");

        port = Integer.parseInt(props.getProperty("port", DEFAULT_PORT));
        log.info("Server port: {}", port);

        maxConnections = Integer.parseInt(props.getProperty("maxConnections", MAX_CONNECTIONS));
        log.info("Connection pool size: " + maxConnections);

        Set<String> propertyNames = props.stringPropertyNames();
        Map services = new HashMap();
        for (String propertyName : propertyNames) {
            if (propertyName.startsWith("service")) {
                String serviceName = propertyName.substring(propertyName.indexOf('.') + 1);
                String serviceClass = props.getProperty(propertyName);
                log.info("Binding service {} to class {}", serviceName, serviceClass);
                services.put(serviceName, serviceClass);
            }
        }
    }

    public int getPort() {
        return port;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public Map getServices() {
        return services;
    }
}
