package Fauna;

import MapEngine.Island;

public class Herbs {
    private final String name = "Herb";
    private double weight = 1.0;
    private boolean alive = true;

    public Herbs() {}

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isAlive() {
        return alive;
    }

    public void beEaten() {
        this.alive = false;
        System.out.println("🌿 The herb was eaten.");
    }
}
