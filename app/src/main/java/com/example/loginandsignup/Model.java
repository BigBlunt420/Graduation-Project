package com.example.loginandsignup;

public class Model {
    String id,title,description,startTime,endTime,location;

    public Model(){

    }

    public Model(String id, String tile, String description, String startTime, String endTime, String location) {
        this.id = id;
        this.title = tile;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTile() {
        return title;
    }

    public void setTile(String tile) {
        this.title = tile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
