package net.jamosa.ixtens.test.core;

import java.io.Serializable;

public class ResponseMessage implements Serializable {

    private static final long serialVersionUID = 5983744583807607284L;

    private Long seq;
    private Object result;

    public ResponseMessage() {
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "[seq=" + seq + ", " + "result=" + result;
    }
}
