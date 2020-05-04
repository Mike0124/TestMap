package com.example.testmap.JavaBean;

public class Site {
    private String name;
    private String fullName;
    private String businessHours;
    private double longitude;
    private double latitude;

    public Site(String name, String fullName, String businessHours, double longitude, double latitude) {
        this.name = name;
        this.fullName = fullName;
        this.businessHours = businessHours;
        this.longitude = longitude;
        this.latitude = latitude;
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
}
