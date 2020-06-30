package com.example.lakecircle.ui.home.activities;

public class Activities {

    /**
     * finished : true
     * id : 0
     * location : string
     * name : string
     * pictureUrl : string
     */

    private boolean finished;
    private int id;
    private String location;
    private String name;
    private String pictureUrl;

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
