package com.gooddata.rubik.hackathon.goodload.json.etl.engine;

public class ScriptRuntimeException extends RuntimeException{
    public ScriptRuntimeException(final String messege) {
        super(messege);
    }

    public ScriptRuntimeException(final String messege, final Exception ex) {
        super(messege, ex);
    }
}
