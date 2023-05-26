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


}
