package com.hyperion.train_preserve_ticket.model;

import com.hyperion.train_preserve_ticket.dao.TripsDAO;

import java.io.Serializable;

public class TripsDetail implements Serializable {

    private String userId;
    private String tripsId;

    private int status;

    public TripsDetail() {
    }

    public TripsDetail(String userId, String tripsId) {
        this.userId = userId;
        this.tripsId = tripsId;
        status = 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTripsId() {
        return tripsId;
    }

    public void setTripsId(String tripsId) {
        this.tripsId = tripsId;
    }
}
