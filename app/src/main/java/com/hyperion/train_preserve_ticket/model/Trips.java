package com.hyperion.train_preserve_ticket.model;

public class Trips {

    private String startStation;
    private String endStation;
    private String train;
    private String date;
    private String time;
    private double price;

    private boolean isFull;

    private boolean isRun;

    private int seats;

    private int remainSeats;

    public Trips() {
    }

    public Trips(String startStation, String endStation, String train, String date, String time, double price) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.train = train;
        this.date = date;
        this.time = time;
        this.price = price;
        this.isFull = false;
        this.isRun = false;
        this.seats = 50;
        this.remainSeats = 50;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getRemainSeats() {
        return remainSeats;
    }

    public void setRemainSeats(int remainSeats) {
        this.remainSeats = remainSeats;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
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

    public String getTrain() {
        return train;
    }

    public void setTrain(String train) {
        this.train = train;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
