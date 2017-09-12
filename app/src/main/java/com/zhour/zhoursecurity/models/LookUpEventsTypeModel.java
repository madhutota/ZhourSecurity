package com.zhour.zhoursecurity.models;

import java.util.ArrayList;

/**
 * Created by shankar on 7/17/2017.
 */

public class LookUpEventsTypeModel extends Model {
    private boolean IsError;
    private String Message;
    private String Output;
    private ArrayList<LookUpModel> lookUpModels;
    private ArrayList<SpinnerModel> lookupNames;

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

    public ArrayList<LookUpModel> getLookUpModels() {
        return lookUpModels;
    }

    public void setLookUpModels(ArrayList<LookUpModel> lookUpModels) {
        this.lookUpModels = lookUpModels;
    }

    public ArrayList<SpinnerModel> getLookupNames() {
        return lookupNames;
    }

    public void setLookupNames(ArrayList<SpinnerModel> lookupNames) {
        this.lookupNames = lookupNames;
    }

    public String getOutput() {
        return Output;
    }

    public void setOutput(String output) {
        Output = output;
    }
}
