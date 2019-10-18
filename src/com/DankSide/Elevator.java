package com.DankSide;

import java.util.ArrayList;

public abstract class Elevator {
    private double timer = 0;
    private int currentFloor = 0;
    private double speed;
    private ArrayList<Person> currentlyUsing;

    public double getTimer() {
        return timer;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public ArrayList<Person> getCurrentlyUsing() {
        return currentlyUsing;
    }

    public void setCurrentlyUsing(ArrayList<Person> currentlyUsing) {
        this.currentlyUsing = currentlyUsing;
    }

    public abstract ArrayList<Person> operate(ArrayList<Person> currentlyUsing);
    public abstract String toString();

}

