package net.jamosa.ixtens.test.core;

import net.jamosa.ixtens.test.core.exceptions.ServerException;

import java.io.Serializable;

public class ResponseMessage implements Serializable {

    private static final long serialVersionUID = 5983744583807607284L;

    private int seq;
    private Object result;

    private ServerException serverError;

    public ResponseMessage() {
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public ServerException getServerError() {
        return serverError;
    }

    public void setServerError(ServerException serverError) {
        this.serverError = serverError;
    }

    @Override
    public String toString() {
        return "[seq=" + seq + ", " + "result=" + result;
    }
}
