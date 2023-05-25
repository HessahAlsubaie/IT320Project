package com.example.it320project;

import android.util.Log;

public class Space {
    private String Name;
    private String location;
    private String category;
    private double price;
    private int capacity;

    private String description;
    private int id;

    private int status;
    private byte[] photoData;


    public Space(int id, String Name, String location,
                 String category, double price, int capacity, String description, byte[] photoData) {
        this.id=id;
        this.Name = Name;
        this.location=location;
        this.category=category;
        this.price=price;
        this.capacity=capacity;
        this.description=description;
        status= 0;
        this.photoData = photoData;

    }

    public Space(){

    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

   //here(status type)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public byte[] getPhoto() {
        Log.d("Space", "Getting photo data for space: " + Name);
        return photoData;
    }

    public void setPhoto(byte[] photoData) {
        this.photoData = photoData;
    }

    @Override
    public String toString() {
        return
                "Name='" + Name + '\'' +
                        ", location='" + location + '\'' +
                        ", category='" + category + '\'' +
                        ", price=" + price +
                        ", capacity=" + capacity +
                        ", description='" + description + '\''
                ;
    }
}
