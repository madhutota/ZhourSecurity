package com.zhour.zhoursecurity.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Shankar on 11/16/2017.
 */

public class VisitorListModel extends Model implements Serializable {
    private ArrayList<VisitorModel> visitorModels;
    private boolean IsError;

    public ArrayList<VisitorModel> getVisitorModels() {
        return visitorModels;
    }

    public void setVisitorModels(ArrayList<VisitorModel> visitorModels) {
        this.visitorModels = visitorModels;
    }

    public boolean isError() {
        return IsError;
    }

    public void setError(boolean error) {
        IsError = error;
    }
}
