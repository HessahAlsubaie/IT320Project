package com.example.it320project;

public class RentedSpace {
    private int id;
    private int spaceId;
    private String startDate;
    private String endDate;


    public RentedSpace(int id, int spaceId, String startDate, String endDate) {
        this.id = id;
        this.spaceId = spaceId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
