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
    private static final String DEFAULT_THREAD_POOL_CORE = "10";
    private static final String DEFAULT_THREAD_POOL_MAX = "20";
    private static final String DEFAULT_THREAD_POOL_KEEP_ALIVE = "1";

    private int port;
    private int maxConnections;

    private int threadPoolCoreSize;
    private int threadPoolMaxSize;
    private int threadPoolKeepAlive;

    private Map<String, String> services;

    public ServerConfiguration(InputStream configInputStream) throws IOException {
        Properties props = new Properties();
        props.load(configInputStream);

        log.info("Loading configuration");

        port = Integer.parseInt(props.getProperty("port", DEFAULT_PORT));
        log.info("Server port: {}", port);

        maxConnections = Integer.parseInt(props.getProperty("maxConnections", MAX_CONNECTIONS));
        log.info("Connection pool size: " + maxConnections);

        threadPoolCoreSize = Integer.parseInt(props.getProperty("threadPool.coreSize", DEFAULT_THREAD_POOL_CORE));
        log.info("Connection pool core size: " + threadPoolCoreSize);

        threadPoolMaxSize = Integer.parseInt(props.getProperty("threadPool.maxSize", DEFAULT_THREAD_POOL_MAX));
        log.info("Connection pool max size: " + threadPoolMaxSize);

        threadPoolKeepAlive = Integer.parseInt(props.getProperty("threadPool.keepAlive", DEFAULT_THREAD_POOL_KEEP_ALIVE));
        log.info("Connection pool keep alive: " + threadPoolKeepAlive);

        Set<String> propertyNames = props.stringPropertyNames();
        services = new HashMap<String, String>();
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

    public int getThreadPoolCoreSize() {
        return threadPoolCoreSize;
    }

    public void setThreadPoolCoreSize(int threadPoolCoreSize) {
        this.threadPoolCoreSize = threadPoolCoreSize;
    }

    public int getThreadPoolMaxSize() {
        return threadPoolMaxSize;
    }

    public void setThreadPoolMaxSize(int threadPoolMaxSize) {
        this.threadPoolMaxSize = threadPoolMaxSize;
    }

    public int getThreadPoolKeepAlive() {
        return threadPoolKeepAlive;
    }

    public void setThreadPoolKeepAlive(int threadPoolKeepAlive) {
        this.threadPoolKeepAlive = threadPoolKeepAlive;
    }

    public Map<String, String> getServices() {
        return services;
    }
}
