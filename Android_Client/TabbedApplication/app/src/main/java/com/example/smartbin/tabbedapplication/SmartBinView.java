package com.example.smartbin.tabbedapplication;

/**
 * Created by Sandhya Chunduri on 9/24/2015.
 */



public class SmartBinView {

    private int binId;
    private Double longitude;
    private Double latitude;
    private int fillLevel;
    private int temperature;
    private int humidity;


    private String locationName;

    public SmartBinView(int binId, Double longitude, Double latitude, int fillLevel, int temperature, int humidity, String locationName) {
        this.binId = binId;
        this.longitude = longitude;
        this.latitude = latitude;
        this.fillLevel = fillLevel;
        this.temperature = temperature;
        this.humidity = humidity;
        this.locationName = locationName;
    }

    public int getBinId() {
        return binId;
    }
    public void setBinId(int binId) {
        this.binId = binId;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public String getFillLevel() {
        switch(fillLevel)
        {
            case 0: return "Empty";
            case 1: return "Half";
            case 2: return "Full";
        }
        return  "Empty";
    }
    public void setFillLevel(int fillLevel) {
        this.fillLevel = fillLevel;
    }
    public int getTemperature() {
        return temperature;
    }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
    public int getHumidity() {
        return humidity;
    }
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String toString()
    {
        return "{binId="+getBinId()+", filllevel=" + getFillLevel() + ", longitude=" + getLongitude()
                + ",latitude=" + getLatitude() + ", temperature=" + getTemperature() + ", humidity=" + getHumidity()
                + "}\n";
    }
}
