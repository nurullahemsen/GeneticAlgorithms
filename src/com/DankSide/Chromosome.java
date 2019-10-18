package com.DankSide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome>{
    public static final ArrayList<Person> userList = Genetic.userList;
    public static final int CONSTANT_SIZE = 30;
    private boolean[] chromosome;
    private double fitnessValue;


    /**
     * Random dizilimli kromozom oluşturur.
     * @return
     */
    public Chromosome(){
        this.chromosome = randomChromosome();
        this.fitnessValue = computeFitness();
    }

    public Chromosome(boolean[] chromosome) {
        this.chromosome = chromosome;
        this.fitnessValue = computeFitness();
    }

    /**
     * Başlangıç için random kromozom oluşturma
     * true -> PQ
     * false -> FIFO kullanacak
     * @return
     */
    private static boolean[] randomChromosome(){
        boolean[] random = new boolean[CONSTANT_SIZE];
        for (boolean b : random){
            b = new Random().nextBoolean();
        }
        return random;
    }
    private void mutate(double probability){
        if (probability >= new Random().nextDouble()){
            int index = new Random().nextInt(CONSTANT_SIZE);
            chromosome[index] = !chromosome[index];
        }
    }
    private Chromosome shuffle(Chromosome otherChromosome){
        boolean[] product = new boolean[CONSTANT_SIZE];
        for(int i = 0; i < CONSTANT_SIZE / 3; i++){
            product[i] = this.chromosome[i];
        }
        for(int i = CONSTANT_SIZE / 3; i < (2 * CONSTANT_SIZE) / 3; i++){
            product[i] = otherChromosome.chromosome[i];
        }
        for(int i = (2 * CONSTANT_SIZE) / 3; i < CONSTANT_SIZE; i++){
            product[i] = this.chromosome[i];
        }
        return new Chromosome(product);
    }
    public Chromosome crossinOver(Chromosome otherChromosome, double probabilityC, double probabilityM) {
        Chromosome c = this;
        if(probabilityC >= new Random().nextDouble()) c = this.shuffle(otherChromosome);
        c.mutate(probabilityM); //%1.5 mutasyon ihtimali
        return c;
    }
    private double computeFitness(){
        ArrayList<Person> FIFOList = Main.deepCopy(userList);
        ArrayList<Person> PQList = new ArrayList<>();
        for (int i = 0; i < CONSTANT_SIZE; i++){
            if (this.chromosome[i]) PQList.add(FIFOList.get(i)); // true olan indexler PQList e eklendi
        }
        FIFOList.removeAll(PQList); //Tüm elemanlara sahip olan FIFOList ten PQList dekiler çıkarıldı.
        ArrayList<Person> usedList = new ArrayList<>(); // kullanmış kişilerin tutulacağı liste
        usedList.addAll(Main.operateOnlyFIFO(FIFOList));
        usedList.addAll(Main.operateOnlyPQ(PQList));
        double oits = Main.OITS(usedList); // OITS değeri bize bu kullanım kombinasyonunun fitness ini verecek.
        return oits;
    }


    public int getFIFOCount(){
        int total = 0;
        for(boolean b: this.chromosome){
            if (!b) total++;
        }
        return total;
    }

    @Override
    public int compareTo(Chromosome o) {
        return Double.compare(this.fitnessValue, o.fitnessValue);
    }

    @Override
    public boolean equals(Object obj) {
        return Arrays.equals(this.chromosome,((Chromosome) obj).chromosome)
                || this.chromosome == ((Chromosome) obj).chromosome;
    }

    @Override
    public String toString() {
        return "Chromosome FitnessValue = " + this.fitnessValue;
    }

    public double getFitnessValue() {
        return fitnessValue;
    }
    public double getInverseFitness(){
        return 1.0/fitnessValue;
    }
}


