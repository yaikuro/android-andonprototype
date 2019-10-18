package com.example.andonprototype.Useless;

import java.util.Calendar;
import java.util.Date;

public class User {
    String userid;
    Date sessionExpiryDate;
    Date currentTime;

    public void setUserID(String id) {
        this.userid = userid;
    }

    public void setSessionExpiryDate(Date sessionExpiryDate) {
        this.sessionExpiryDate = sessionExpiryDate;
    }

    public String getUSERID() {
        return userid;
    }

    public Date getSessionExpiryDate() {
        return sessionExpiryDate;
    }
    public Date getDate() {
        return currentTime = Calendar.getInstance().getTime();
    }
}
