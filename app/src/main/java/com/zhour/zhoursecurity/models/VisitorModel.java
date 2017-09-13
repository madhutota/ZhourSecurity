package com.zhour.zhoursecurity.models;

import java.io.Serializable;

/**
 * Created by shankar on 7/17/2017.
 */

public class VisitorModel extends Model implements Serializable {
    private boolean IsError;
    private String Message;
    private String visitorname;
    private String contactnumber;
    private String emailid;
    private String invitetypeid;
    private String invitetype;
    private String invitenote;
    private String residentid;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getVisitorname() {
        return visitorname;
    }

    public void setVisitorname(String visitorname) {
        this.visitorname = visitorname;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getInvitetypeid() {
        return invitetypeid;
    }

    public void setInvitetypeid(String invitetypeid) {
        this.invitetypeid = invitetypeid;
    }

    public String getInvitetype() {
        return invitetype;
    }

    public void setInvitetype(String invitetype) {
        this.invitetype = invitetype;
    }

    public String getInvitenote() {
        return invitenote;
    }

    public void setInvitenote(String invitenote) {
        this.invitenote = invitenote;
    }

    public String getResidentid() {
        return residentid;
    }

    public void setResidentid(String residentid) {
        this.residentid = residentid;
    }

    public boolean isError() {
        return IsError;
    }

    public void setError(boolean error) {
        IsError = error;
    }
}
