package com.zhour.zhoursecurity.models;

import java.io.Serializable;

/**
 * Created by shankar on 7/17/2017.
 */

public class VisitorModel extends Model implements Serializable {
    private String visitorname;
    private String contactnumber;
    private String emailid;
    private String invitetypeid;
    private String invitetype;
    private String invitenote;
    private String residentid;
    private String residentname;
    private String residentcontact1;
    private String residentcontact2;
    private String flat;
    private String eventdate;
    private String eventtime;

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

    public String getResidentname() {
        return residentname;
    }

    public void setResidentname(String residentname) {
        this.residentname = residentname;
    }

    public String getResidentcontact1() {
        return residentcontact1;
    }

    public void setResidentcontact1(String residentcontact1) {
        this.residentcontact1 = residentcontact1;
    }

    public String getResidentcontact2() {
        return residentcontact2;
    }

    public void setResidentcontact2(String residentcontact2) {
        this.residentcontact2 = residentcontact2;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getEventdate() {
        return eventdate;
    }

    public void setEventdate(String eventdate) {
        this.eventdate = eventdate;
    }

    public String getEventtime() {
        return eventtime;
    }

    public void setEventtime(String eventtime) {
        this.eventtime = eventtime;
    }
}
