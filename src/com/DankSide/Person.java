package com.DankSide;

public class Person implements Comparable<Person>{
    private int no;
    private String name;
    private int targetFloor;
    private double usageTime;
    private Elevator usedElevator;

    public Person(int no, String name, int targetFloor) {
        this.no = no;
        this.name = name;
        this.targetFloor = targetFloor;
    }
    public Person(Person p){
        this.no = p.no;
        this.name = p.name;
        this.targetFloor = p.targetFloor;
        this.usageTime = p.usageTime;
        this.usedElevator = p.usedElevator;
    }
    public String getName() {
        return name;
    }

    public int getTargetFloor() {
        return targetFloor;
    }

    public double getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(double usageTime) {
        this.usageTime = usageTime;
    }

    public Elevator getUsedElevator() {
        return usedElevator;
    }

    public void setUsedElevator(Elevator usedElevator) {
        this.usedElevator = usedElevator;
    }

    public int getNo() {
        return no;
    }

    @Override
    public String toString() {
        return "Person" +
                " name='" + name + '\'' +
                ", targetFloor=" + targetFloor +
                ", usageTime=" + (usageTime==0 ? "Asansörü daha kullanmadı" : String.format("%.2f",usageTime)) +
                ", usedElevator=" + (usedElevator == null ? "Asansörü daha kullanmadı" : usedElevator instanceof FIFO ? "FIFO" : "PQ");
    }

    /**
     * Bu metod kullanıcıların bindikleri asansöre veya kullanım sürelerine bakmaksızın
     * numaralarına göre karşılaştırılacakları şekilde düzenlenmiştir.
     *
     * @param o karşılaştırılacak obje
     * @return eş olup olmama durumu
     */

    @Override
    public int compareTo(Person o) {
        return Integer.compare(this.no,o.no);
    }

    /**
     * Bu metod kullanıcıların bindikleri asansöre veya kullanım sürelerine bakmaksızın
     * numaralarından eş olduğu anlaşılacak şekilde düzenlenmiştir.
     *
     * @param obj karşılaştırılacak obje
     * @return eş olup olmama durumu
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Person) return this.no == ((Person) obj).no;
        return false;
    }
}
