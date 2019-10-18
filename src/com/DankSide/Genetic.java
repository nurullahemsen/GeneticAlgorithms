package com.DankSide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * @author dankSide
 *      17.01.2017
 */

public class Genetic {

    private static final int USER_SIZE = 30;
    private static final int GENERATION_SIZE =45; // BU KISIM DEĞİŞTİRİLEBİLİR (SADECE TEK SAYI OLMALI)
    private static final int GEN_TRESHOLD = 25000; //BURASI DEĞİŞTİRİLEBİLİR (MAX GENERATON SAYISI)
    private static final double CROSSOVER_PROBABILITY = 0.9; //BURASI DEĞİŞTİRİLEBİLİR SADECE 1 DEN KÜÇÜK 1 E YAKIN OLMALI
    private static final double MUTATION_PROBABILITY = 0.1; //BURASI DEĞİŞTİRİLEBİLİR SADECE 0 DAN BÜYÜK 0 A YAKIN OLMALI
    private static final double TRESHOLD = 230;
    public static final ArrayList<Person> userList = createConstantSet();


    public static void main(String[] args) {

        compute();

    }

    /**
     * Bu Genetik Algoritma her defasında içerdiği random değerlerden ötürü farklı sonuçlar verebildiğinden,
     * compute() metodu bestCase() metodunu 5 kez çağırır ve en iyi sonucu yazdırır.
     */
    public static void compute(){
        Chromosome c;
        c = bestCase(TRESHOLD, GEN_TRESHOLD,CROSSOVER_PROBABILITY,MUTATION_PROBABILITY);
        for(int i = 0; i < 4; i++){
            Chromosome c2 = bestCase(TRESHOLD, GEN_TRESHOLD,CROSSOVER_PROBABILITY,MUTATION_PROBABILITY);
            c = c.getFitnessValue() < c2.getFitnessValue() ? c : c2;
        }
        System.out.println("*******************\n\n"+c.getFitnessValue()+"\n"+c.getFIFOCount());
    }

    /**
     * targetFloorSet deki bilgiler kullanılarak kişi listesi oluşturma
     * @return
     */
    public static ArrayList<Person> createConstantSet(){
        ArrayList<Person> list = new ArrayList<>();
//        int[] targetFloorSet = {30, 43, 43, 31, 18, 21, 4, 43, 13, 17, 35, 39, 16, 5, 22, 9, 49, 19, 3,
//                38, 46, 41, 15, 29, 30, 43, 48, 19, 26, 32};
        int[] targetFloorSet = {9, 42, 50, 17, 14, 29, 34, 27, 35, 8, 49, 9, 37, 46, 11, 4, 37, 13, 28,
                25, 9, 21, 15, 5, 22, 37, 9, 48, 1, 14};
        for(int i = 0; i< USER_SIZE; i++){
            list.add(new Person(i,String.format("%d. kullanıcı",i+1), targetFloorSet[i]));
        }
        return list;
    }

    /**
     * Rulet seçme yöntemi
     * @param chromosomes girdi cromosome lar
     * @return
     */

    private static Chromosome selectRandomWeighted(ArrayList<Chromosome> chromosomes) {
        int selectedIndex = 0;
        double total = chromosomes.get(0).getInverseFitness();

        for( int i = 1; i < chromosomes.size(); i++ ) {
            total += chromosomes.get(i).getInverseFitness();
            if( new Random().nextDouble() <= (chromosomes.get(i).getInverseFitness() / total)) selectedIndex = i;
        }

        return chromosomes.get(selectedIndex);
    }

    /**
     * ilk nesli random oluşturan metod
     * @return
     */

    public static ArrayList<Chromosome> createFirstBatch(){
        ArrayList<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < GENERATION_SIZE; i++){
            chromosomes.add(new Chromosome());
        }
        return chromosomes;
    }

    /**
     * kromozomları fitness değerine göre sıralayan metod
     * @param chromosomes
     * @return
     */
    public static ArrayList<Chromosome> sortChromosomes(ArrayList<Chromosome> chromosomes){
        Collections.sort(chromosomes, new Comparator<Chromosome>() {
            @Override
            public int compare(Chromosome o1, Chromosome o2) {
                return o1.compareTo(o2);
            }
        });
        return chromosomes;
    }

    /**
     * Elite değere sahip olan kromozomu döndüren metod
     * @param chromosomes
     * @return
     */
    public static Chromosome findElite(ArrayList<Chromosome> chromosomes){
        return sortChromosomes(chromosomes).get(0);
    }

    /**
     * Halihazırdaki nesildeki Elite değeri ve geri kalanların Roulette Wheel yöntemi ile seçilip,
     * belirli bir ihtimale göre çaprazlanarak ve / veya mutasyona uğrayarak oluşan yeni kromozomlardan
     * oluşan yeni nesil kromozomların listesini döndürür.
     * @param chromosomes kromozomlar
     * @param probabilityC Crossover ihtimali
     * @param probabilityM mutasyon ihtimali
     * @return
     */
    public static ArrayList<Chromosome> nextGeneration(ArrayList<Chromosome> chromosomes, double probabilityC, double probabilityM){
        ArrayList<Chromosome> newGeneration = new ArrayList<>();
        newGeneration.add(findElite(chromosomes));
        Chromosome c1, c2;
        for (int i = 0; i < (GENERATION_SIZE - 1); i++){
            c1 = selectRandomWeighted(chromosomes);
            c2 = selectRandomWeighted(chromosomes);
            newGeneration.add(c1.crossinOver(c2,probabilityC,probabilityM)); //BURASI DEĞİŞTİRİLEBİLİR SADECE PROBABILITY LER
            newGeneration.add(c2.crossinOver(c1,probabilityC,probabilityM)); //BURASI DEĞİŞTİRİLEBİLİR SADECE PROBABILITY LER
        }
        return newGeneration;

    }

    /**
     *      * Eşik değerine veya eşik Generation değerine ulaşılasaya kadar yeni nesiller oluşturup  bulunan en iyi
     * değere sahip kromozomu döndüren metod
     * @param treshold eşik değeri
     * @param genTreshold Generation eşik değeri
     * @param probabilityC Crossover ihtimali
     * @param probabilityM mutasyon ihtimali
     * @return
     */
    public static Chromosome bestCase(double treshold, int genTreshold, double probabilityC, double probabilityM){
        ArrayList<Chromosome> samples = createFirstBatch();
        Chromosome best = findElite(samples);
        double bestCase = best.getFitnessValue();
        int generation = 0;
        while (bestCase >= treshold && generation < genTreshold){
            samples = nextGeneration(samples,probabilityC,probabilityM);
            best = findElite(samples);
            if (bestCase > best.getFitnessValue()) System.out.printf("%d. gen fValue: %.2f\n",generation,bestCase);
            bestCase = best.getFitnessValue();

            generation++;
        }
        return best;
    }






}
