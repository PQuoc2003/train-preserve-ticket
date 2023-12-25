package com.hyperion.train_preserve_ticket.dao;

import com.hyperion.train_preserve_ticket.model.Trips;

import java.io.Serializable;

public class TripsDAO implements Serializable {

    private String documentID;
    private Trips trips;

    public TripsDAO(String documentID, Trips trips) {
        this.documentID = documentID;
        this.trips = trips;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public Trips getTrips() {
        return trips;
    }

    public void setTrips(Trips trips) {
        this.trips = trips;
    }
}
