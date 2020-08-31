package com.example.demo.models;

public class LocationStats implements Comparable<LocationStats>{

    private String state;
    private String country;
    private int latestTotalCases;
    private int delta;
    private int position;
    private double incProportion;
    private String incShow;

    @Override
    public String toString() {
        return "LocationStats{" +
                "state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", latestTotalCases=" + latestTotalCases +
                '}';
    }

    @Override
    public int compareTo(LocationStats obj) {
        return (this.getDelta() > obj.getDelta() ? -1 :
                (this.getDelta() == obj.getDelta() ? 0 : 1));
    }

    public static int compareAlpha(LocationStats jc1, LocationStats jc2) {
        return (int) (jc1.getCountry().compareTo(jc2.getCountry()));
    }

    public int getPosition() {
        return position;
    }

    public String getIncShow() {
        return incShow;
    }

    public void setIncShow(String incShow) {
        this.incShow = incShow;
    }

    public double getIncProportion() {
        return incProportion;
    }

    public void setIncProportion(double incProportion) {
        this.incProportion = incProportion;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLatestTotalCases() {
        return latestTotalCases;
    }

    public void setLatestTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }
}
