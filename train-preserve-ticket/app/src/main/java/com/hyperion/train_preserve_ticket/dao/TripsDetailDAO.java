package com.hyperion.train_preserve_ticket.dao;

import com.hyperion.train_preserve_ticket.model.Trips;
import com.hyperion.train_preserve_ticket.model.TripsDetail;

public class TripsDetailDAO {

    private String ticketId;

    private TripsDetail tripsDetail;

    private TripsDAO tripsDAO;

    public TripsDetailDAO() {
    }

    public TripsDetailDAO(String ticketId, TripsDetail tripsDetail, TripsDAO tripsDAO) {
        this.ticketId = ticketId;
        this.tripsDetail = tripsDetail;
        this.tripsDAO = tripsDAO;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public TripsDetail getTripsDetail() {
        return tripsDetail;
    }

    public void setTripsDetail(TripsDetail tripsDetail) {
        this.tripsDetail = tripsDetail;
    }

    public TripsDAO getTripsDAO() {
        return tripsDAO;
    }

    public void setTripsDAO(TripsDAO tripsDAO) {
        this.tripsDAO = tripsDAO;
    }
}
