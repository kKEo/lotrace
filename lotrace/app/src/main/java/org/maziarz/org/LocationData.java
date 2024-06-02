package org.maziarz.org;

public class LocationData {
    private double latitude;
    private double longitude;
    private int track_id;

    public LocationData(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.track_id = 42;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getTrackId() {
        return track_id;
    }

    @Override
    public String toString() {
        return "LocationData{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

