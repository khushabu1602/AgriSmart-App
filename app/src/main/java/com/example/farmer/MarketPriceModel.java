package com.example.farmer;

public class MarketPriceModel {
    public String cropName, mandiName;
    public int minPrice, maxPrice;
    public long timestamp;

    public MarketPriceModel() {}  // Required for Firebase

    public MarketPriceModel(String cropName, int minPrice, int maxPrice, String mandiName, long timestamp) {
        this.cropName = cropName;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.mandiName = mandiName;
        this.timestamp = timestamp;
    }
}
