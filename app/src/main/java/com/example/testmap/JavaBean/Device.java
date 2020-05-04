package com.example.testmap.JavaBean;

public class Device {
    private String name;
    private String fullName;
    private String businessHours;
    private double longitude;
    private double latitude;
    private String telNum;
    private String exactPosition;

    public Device(String name, String fullName, String businessHours, double longitude, double latitude, String telNum, String exactPosition) {
        this.name = name;
        this.fullName = fullName;
        this.businessHours = businessHours;
        this.longitude = longitude;
        this.latitude = latitude;
        this.telNum = telNum;
        this.exactPosition = exactPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTelNum() { return telNum; }

    public void setTelNum(String telNum) { this.telNum = telNum; }

    public String getExactPosition() { return exactPosition; }

    public void setExactPosition(String exactPosition) { this.exactPosition = exactPosition; }

    @Override
    public String toString() {
        return "Site{" +
                "name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", businessHours='" + businessHours + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", telNum='" + telNum + '\'' +
                ", exactPosition='" + exactPosition + '\'' +
                '}';
    }
}
