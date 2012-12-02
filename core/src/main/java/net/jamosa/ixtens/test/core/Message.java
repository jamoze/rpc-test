package net.jamosa.ixtens.test.core;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = -7526888471310905868L;

    private Long seq;
    private String serviceName;
    private String methodName;
    private Object[] args;

    public Message() {
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        String header = "[seq=" + seq + ", " +
                "serviceName=" + serviceName + ", " +
                "methodName=" + serviceName;

        StringBuilder sb = new StringBuilder(header);
        for (Object arg : args) {
            sb.append('\t');
            sb.append(arg.toString());
            sb.append('\n');
        }

        return  sb.toString();
    }
}
