package com.mobdeve.s16.druzali.shawn.mco2;

public class Office {
    private String name;
    private String address;
    private String type;
    private String contact;
    private String email;
    private double lat;
    private double lng;

    // Firestore requires empty constructor
    public Office() {}

    // Constructor for creating Office objects with basic info (used in MapSearchActivity)
    public Office(String name, String address, double lat, double lng) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    // Full constructor for all fields
    public Office(String name, String address, String type, String contact, String email, double lat, double lng) {
        this.name = name;
        this.address = address;
        this.type = type;
        this.contact = contact;
        this.email = email;
        this.lat = lat;
        this.lng = lng;
    }

    // Getters
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getType() { return type; }
    public String getContact() { return contact; }
    public String getEmail() { return email; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }

    // Setters (optional, but useful for Firestore)
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setType(String type) { this.type = type; }
    public void setContact(String contact) { this.contact = contact; }
    public void setEmail(String email) { this.email = email; }
    public void setLat(double lat) { this.lat = lat; }
    public void setLng(double lng) { this.lng = lng; }
}