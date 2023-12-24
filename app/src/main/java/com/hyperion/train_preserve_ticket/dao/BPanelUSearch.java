package com.hyperion.train_preserve_ticket.dao;

import java.io.Serializable;

public class BPanelUSearch implements Serializable {

    private String startStation;

    private String endStation;

    private String dateOfTrips;

    public BPanelUSearch(String startStation, String endStation, String dateOfTrips) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.dateOfTrips = dateOfTrips;
    }

    public String getStartStation() {
        return startStation;
    }

    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public String getDateOfTrips() {
        return dateOfTrips;
    }

    public void setDateOfTrips(String dateOfTrips) {
        this.dateOfTrips = dateOfTrips;
    }
}
