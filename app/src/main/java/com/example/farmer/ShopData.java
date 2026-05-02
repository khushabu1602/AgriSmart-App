package com.example.farmer;

public class ShopData {
    private final String name;
    private final String address;
    private final float rating;
    private final String phone;
    private final double latitude;
    private final double longitude;

    public ShopData(String name, String address, float rating, String phone, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public float getRating() {
        return rating;
    }

    public String getPhone() {
        return phone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
