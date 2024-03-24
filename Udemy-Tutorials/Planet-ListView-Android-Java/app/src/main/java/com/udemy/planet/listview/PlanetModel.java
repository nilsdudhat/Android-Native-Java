package com.udemy.planet.listview;

public class PlanetModel {

    private String planetName;
    private int moonsCount;
    private int planetImage;

    public PlanetModel(String planetName, int moonsCount, int planetImage) {
        this.planetName = planetName;
        this.moonsCount = moonsCount;
        this.planetImage = planetImage;
    }

    public String getPlanetName() {
        return planetName;
    }

    public void setPlanetName(String planetName) {
        this.planetName = planetName;
    }

    public int getMoonsCount() {
        return moonsCount;
    }

    public void setMoonsCount(int moonsCount) {
        this.moonsCount = moonsCount;
    }

    public int getPlanetImage() {
        return planetImage;
    }

    public void setPlanetImage(int planetImage) {
        this.planetImage = planetImage;
    }
}
