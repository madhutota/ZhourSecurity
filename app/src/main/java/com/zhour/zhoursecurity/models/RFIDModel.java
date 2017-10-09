package com.zhour.zhoursecurity.models;

/**
 * Created by Shankar on 10/9/2017.
 */

public class RFIDModel extends Model {

    private boolean IsError;
    private String Error;
    private String Message;
    private String Output;

    public boolean getIsError() {
        return IsError;
    }

    public void setIsError(boolean IsError) {
        this.IsError = IsError;
    }

    public String getError() {
        return Error;
    }

    public void setError(String Error) {
        this.Error = Error;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getOutput() {
        return Output;
    }

    public void setOutput(String Output) {
        this.Output = Output;
    }
}
