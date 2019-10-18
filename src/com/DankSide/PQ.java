package com.DankSide;

import java.util.ArrayList;

/**
 * Tek bir statik objeye sahip private constructor lu bir Elevator sınıfı. Kullanıcının statik
 * objeyi kullanarak operate metodunu vs kullanması beklenmektedir.
 */

public class PQ extends Elevator {

    static PQ pqElevator = new PQ(2);

    private PQ(double speed){
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
    public ArrayList<Person> operate(ArrayList<Person> currentlyUsing) {

        currentlyUsing = Main.sortByFloor(currentlyUsing);
        ArrayList<Person> usedPersonList = new ArrayList<>();
        double usageTimer = 0;
        for (Person person : currentlyUsing){
            if (person.getTargetFloor() != pqElevator.getCurrentFloor()) {
                double time = Math.abs(person.getTargetFloor() - pqElevator.getCurrentFloor()) * getSpeed() + 4;
                pqElevator.setCurrentFloor(person.getTargetFloor());
                usageTimer += time;
            }
            person.setUsageTime(usageTimer + pqElevator.getTimer());
            person.setUsedElevator(pqElevator);
            usedPersonList.add(person);
        }

        pqElevator.setTimer(pqElevator.getTimer() + usageTimer + pqElevator.getCurrentFloor() * getSpeed());
        pqElevator.setCurrentFloor(0);
        return usedPersonList;
    }
    @Override
    public String toString() {
        return "pqElevator";
    }
}
