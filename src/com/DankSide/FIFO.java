package com.DankSide;

import java.util.ArrayList;
import java.util.Collections;

public class FIFO extends Elevator{

    /**
     * Tek bir statik objeye sahip private constructor lu bir Elevator sınıfı. Kullanıcının statik
     * objeyi kullanarak operate metodunu vs kullanması beklenmektedir.
     */

    static FIFO fifoElevator = new FIFO(5.0);

    private FIFO(double speed){
        super();
        setSpeed(speed);
    }

    /**
     * Asansöre binen kullanıcıların istenilen katlara götürüldüğü ve buna göre kullanım sürelerinin
     * güncellenmesinin simülasynunun yapıldığı metod.
     *
     * @param currentlyUsing Asansöre binmeyi bekleyen işlenmemiş kullanıcıların listesi
     * @return Asansöre binmiş işlenmiş kullanıcıların listesi
     */

    @Override
    public ArrayList<Person> operate(ArrayList<Person> currentlyUsing){

        Collections.sort(currentlyUsing);
        currentlyUsing = Main.sortByFloor(currentlyUsing);
        ArrayList<Person> usedPersonList = new ArrayList<>();
        double usageTimer = 0;
        for (Person person : currentlyUsing){
            if (person.getTargetFloor() != fifoElevator.getCurrentFloor()) {
                double time = Math.abs(person.getTargetFloor() - fifoElevator.getCurrentFloor()) * getSpeed() + 4;
                fifoElevator.setCurrentFloor(person.getTargetFloor());
                usageTimer += time;
            }
            person.setUsageTime(usageTimer + fifoElevator.getTimer());
            person.setUsedElevator(fifoElevator);
            usedPersonList.add(person);
        }

        fifoElevator.setTimer(fifoElevator.getTimer() + usageTimer + fifoElevator.getCurrentFloor()*getSpeed());
        fifoElevator.setCurrentFloor(0);
        return usedPersonList;
    }

    @Override
    public String toString() {
        return "fifoElevator";
    }
}
