package com.zhour.zhoursecurity.models;

/**
 * Created by Shankar on 1/4/2018.
 */

public class LogoutModel extends Model {
    private boolean IsError;
    private String Message;
    private String Output;

    public boolean isError() {
        return IsError;
    }

    public void setError(boolean error) {
        IsError = error;
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
