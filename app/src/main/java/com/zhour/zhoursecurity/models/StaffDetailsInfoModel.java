package com.zhour.zhoursecurity.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shankar on 7/17/2017.
 */

public class StaffDetailsInfoModel extends Model implements Serializable {
    private boolean IsError;
    private String Message;
    private String Output;
    private StaffDetailsModel staffDetailsModel;

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

    public StaffDetailsModel getStaffDetailsModel() {
        return staffDetailsModel;
    }

    public void setStaffDetailsModel(StaffDetailsModel staffDetailsModel) {
        this.staffDetailsModel = staffDetailsModel;
    }

    public String getOutput() {
        return Output;
    }

    public void setOutput(String output) {
        Output = output;
    }
}
