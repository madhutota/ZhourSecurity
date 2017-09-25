package com.zhour.zhoursecurity.models;

import java.io.Serializable;

/**
 * Created by shankar on 7/17/2017.
 */

public class SuccessModel extends Model {
    private boolean IsError;
    private String Error;
    private String Message;
    private String Output;

    public boolean isError() {
        return IsError;
    }

    public void setError(boolean error) {
        IsError = error;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    @Override
    public String getMessage() {
        return Message;
    }

    @Override
    public void setMessage(String message) {
        Message = message;
    }

    public String getOutput() {
        return Output;
    }

    public void setOutput(String output) {
        Output = output;
    }
}
