package com.udemy.recyclerview.app.models;

public class PlanetModel {

    String planetName;
    int distanceFromSun;
    double gravity;
    int diameter;

    public PlanetModel(String planetName, int distanceFromSun, double gravity, int diameter) {
        this.planetName = planetName;
        this.distanceFromSun = distanceFromSun;
        this.gravity = gravity;
        this.diameter = diameter;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public int getDistanceFromSun() {
        return distanceFromSun;
    }

    public void setDistanceFromSun(int distanceFromSun) {
        this.distanceFromSun = distanceFromSun;
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }
}
