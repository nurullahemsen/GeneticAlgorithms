package com.DankSide;

import java.util.*;

/**
 * @author : 05160000657 Muhammed Nurullah Emsen
 *           05160000784 Elifnaz Öklü
 *           05160000283 Oğuzhan Katı
 *
 *           Java v9.0
 */

public class Main {

    private static ArrayList<ArrayList<Person>> listOfLists = new ArrayList<>();

    public static void main(String[] args){
//        menu();
        P_4A();

    }

    /**
     * P-4A da kullandığımız listenin P-2A.1 deki yöntemle yaptıran metod
     * Not: Liste hep aynı olduğundan sonuç hep 224.42 sn çıkmaktadır.
     */
    public static void P_4A(){
        ArrayList<Person> users = Genetic.userList;
//        users = sortByFloor(users);
        ArrayList<Person> bestCase = null;
        Double bestTime = Double.MAX_VALUE;
        for (int i = 5; i <= 8; i++){
            ArrayList<Person> list = bestCase(deepCopy(users), i);
            double oits = OITS(list);
            if (oits < bestTime){
                bestTime = oits;
                bestCase = list;
            }
        }
        System.out.printf("OITS %.2f seconds\n", bestTime);
        printList(bestCase);
    }

    /**
     * Programı kontrol etmeyi sağlayan statik menu metodu
     */

    private static void menu(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Lütfen projenin görmek istediğiniz bölümünün şıkkını giriniz:");
        String in = scanner.nextLine();
        switch (in.toUpperCase()){
            case "B":
                B();
                return;
            case "C":
                C();
                return;
            case "D":
                D();
                return;
            case "E":
                E();
                return;
            case "F":
                F();
        }
    }

    /**
     * B ŞIKKI:
     *
     * Hazır kullanıcı listesi çeşitli asasnsörlere istenilen şekkiilerde bindirilip
     * karşılaştırılır.
     */

    private static void B(){
        generalUsage(createSet());
    }

    /**
     * C ŞIKKI:
     *
     * Hazır kullanıcı listesinin OITS bakımından en optimum asansör kullanma kombinbasyonu
     * bulunur.
     *
     * Not: Intel i7-6700HQ işlemcide çalışma suresi yaklaşık 2 sn.
     */

    private static void C(){
        ArrayList<Person> users = createSet();
//        users = sortByFloor(users);
        ArrayList<Person> bestCase = null;
        Double bestTime = Double.MAX_VALUE;
        for (int i = 1; i <= users.size(); i++){
            ArrayList<Person> list = bestCase(deepCopy(users), i);
            double oits = OITS(list);
            if (oits < bestTime){
                bestTime = oits;
                bestCase = list;
            }
        }
        System.out.printf("OITS %.2f seconds\n", bestTime);
        printList(bestCase);
    }

    /**
     * D ŞIKKI:
     *
     * 1-50 katlara rastgele oluşturulmuş 30 kullanıcıyı içeren liste çeşitli asasnsörlere
     * istenilen şekkiilerde bindirilip karşılaştırılır.
     */

    private static void D(){
        generalUsage(createPeople(30));
    }

    /**
     * E ŞIKKI:
     *
     * 1-50 katlara rastgele oluşturulmuş 30 kullanıcıyı içeren listenin OITS bakımından
     * optimum asansör kullanma kombinasyonu bulunur. 30 kullanıcı her iki asansore 2^30
     * farklı kombinasyona binebieceğinden bu metodu yazarken bazı optimizasyonlar yapma
     * gereği duyduk.
     *
     * PQ asansör FIFO ya göre ortalama 3 - 4 kat daha efektif çalıştığını uyguladığımız
     * 5000 lik ve 10000 lik random çalışanlar listeleriyle asansörlerin kullanılma oran-
     * larından tespit ettik. Bunun üzerine yine yaptığımız deneylerden 30 kişilik bir
     * listenin elemanları asansörleri kullanırken en fazla 2 farklı grubun FIFO asansörü
     * kullanmasının bulabildiğimiz en optimum sonucları verdiğini gördük. Bundan ötürü
     * yazdığımız bir kümenin n elemanlı altkümelerini bulmaya yarayan combinations meto-
     * dunu sadece 5, 6, 7 ve 8 elemanlı altkümelerini aluşturacak şekilde çağırdık.
     *
     * Bu noktada 30 elemanlı bir kümenin 142.506 tane 5 elemanlı altkümesi, 593.775 tane
     * 6 elemanlı altkümesi, 2.035.800 tane 7 elemanlı altkümesi ve 5.852.925 tane 8 ele-
     * manlı altkümesi bulunmaktadır.
     *
     * Yine yaptığımız denemelerde random oluşturulan çalışan listesinin içinde bulunan
     * hedef katların medyanına göre optimum sonuç ağırlıklı 8li de olmak üzere 6 veya 8
     * FIFO kullanıcılı listelerde çıkmaktadır. Medyanın diğerlerine göre daha düşük olduğu
     * durumlarda 6lı FIFO kullanıcılarının optimum listede yeraldığı gözlenmektedir.
     *
     * Yaptığımız 9lu altküme denemelerinin sonuçlanması çok uzun sürmekle birlikte her
     * defasında OITS nin 8lilere göre daha yükseldiği görülmüştür.
     *
     * Combinations metodumuz recursive olduğundan bellek kullanımı göreceli olarak fazladır.
     * Bundan ötürü 10lu altküme denemelerimiz JVM in verdiği 2GB lık bellek kullanım
     * sınırını aştığı için sonuçsuz kalmıştır.
     *
     * Sonuç olarak bulduğumuz best case OITS durumlarını altkümelerin eleman sayılarına
     * göre bir grafikte tutacak olursak ters bir kıvrım ile karşılaşırız. Bu kıvrımın
     * minimum noktası (yani tepesi) açısından eğim 1 ila 5 alt kümeli best case durumlarda
     * fazla iken 6, 7 ve 8 li alt kümeleri durumlarda  sıfıra yaklaşmaktadır. 3. FIFO
     * asansöre geçilen 9lu altkümede ise eğim farkedilir bir şekilde artmıştır. Bu hareke-
     * tinden ötürü grafiğin tepesinin her halükarda 6, 7 veya 8 li altkümeli best case
     * durumluraında olması gerektiği ve 9 dan sonra OITS nin devamlı artacağı öngörülebilir.
     *
     * Bu şekide yaklaşık 1.000.000.000 tane ihtimali yaklaşık 9.000.000 ihtimale düşürüp
     * en optimal kombinasyonu bulabildik.
     *
     * Not: Intel i7-6700HQ işlemci ile çalışma süresi 35 - 45 sn.
     *
     */

    private static void E(){
        System.out.println("Lütfen Bekleyiniz...\n\n");

        ArrayList<Person> users = createPeople(30);
        ArrayList<Person> bestCase = null;
        double bestTime = Double.MAX_VALUE;
        for (int i = 5; i <= 8; i++){
            ArrayList<Person> thisCase = bestCase(deepCopy(users),i);
            double oits = OITS(thisCase);
//            System.out.println(i + " " + oits);
            if (oits < bestTime){
                bestTime = oits;
                bestCase = thisCase;
            }
        }
        System.out.printf("OITS %.2f seconds\n", bestTime);
        printList(bestCase);


        ArrayList<Person> onlyFIFO = operateOnlyFIFO(deepCopy(users));
        ArrayList<Person> onlyPQ = operateOnlyPQ(deepCopy(users));

        System.out.printf("Sadece FIFO kullanımına göre ort %.2f s kazanımıştır.\n", OITS(onlyFIFO) - bestTime);
        System.out.printf("Sadece PQ kullanımına göre ort %.2f s kazanımıştır.\n", OITS(onlyPQ) - bestTime);

    }

    /**
     * F ŞIKKI:
     *
     * E şıkkında yapılanlar asansörlerin hızları ikiye katlanarak tekrar edilir.
     */
    private static void F(){
        PQ.pqElevator.setSpeed(1);
        FIFO.fifoElevator.setSpeed(2.5);
        E();
    }

    /**
     * Listedeki kullanıcıların projede istenilen şekillerde asansöre bindirilme islemlerinin
     * uygulandığı metod
     *
     * @param userList işlenmemiş kullanıcı listesi
     */

    private static void generalUsage(ArrayList<Person> userList){
        System.out.println("\nNORMALLY OPERATED\n");
        ArrayList<Person> normallyOperated = operate(deepCopy(userList));
        printList(normallyOperated);
        System.out.printf("OITS: %.2f s\n",OITS(normallyOperated));
        System.out.println("\nFIFO\n");
        ArrayList<Person> onlyFIFO = operateOnlyFIFO(deepCopy(userList));
        printList(onlyFIFO);
        System.out.printf("OITS FIFO: %.2f s\n",OITS(onlyFIFO));
        System.out.println("\nPQ\n");
        ArrayList<Person> onlyPQ = operateOnlyPQ(deepCopy(userList));
        printList(onlyPQ);
        System.out.printf("OITS PQ: %.2f s\n",OITS(onlyPQ));
        System.out.println("\nRANDOMLY\n");
        ArrayList<Person> randomlyOperated = operateRandom(deepCopy(userList));
        printList(randomlyOperated);
        System.out.printf("OITS randomly: %.2f s\n",OITS(randomlyOperated));
        System.out.println("\nPQ COMPARED TO FIFO\n");
        compareLists(onlyFIFO, onlyPQ);
        System.out.println("\nRANDOMLY COMPARED TO FIFO\n");
        compareLists(onlyFIFO,randomlyOperated);
    }

    /**
     * PQ ve FIFO asansörlerin aynı anda çalıştırıldığı durumun simülasyonunu yapan metod.
     *
     * @param users işlemmemiş kullanıcı listesi
     * @return yeni bir ArrayList olarak işlenmiş şekildeki kullanıcılar döndürülür.
     */

    private static ArrayList<Person> operate( ArrayList<Person> users){
        ArrayList<Person> usedList = new ArrayList<>();
        ArrayList<Person> currentlyUsing = null;
        ArrayList<Person> copyList = deepCopy(users);
        while (copyList.size()>0){
            currentlyUsing = new ArrayList<>();
            if (FIFO.fifoElevator.getTimer() < PQ.pqElevator.getTimer()){
                for (int i = 0; i < 4 && copyList.size()>0; i++){
                    currentlyUsing.add(copyList.remove(0));
                }
                    FIFO.fifoElevator.operate(currentlyUsing);
            }else {
                ArrayList<Person> copyList2 = new ArrayList<>(copyList);
                copyList2 = sortByFloor(copyList2);
                for (int i = 0; i < 4 && copyList2.size()>0; i++){
                    Person p = copyList2.remove(0);
                    copyList.remove(p);
                    currentlyUsing.add(p);
                }
                PQ.pqElevator.operate(currentlyUsing);
            }
            usedList.addAll(currentlyUsing);
        }
        FIFO.fifoElevator.setTimer(0);
        PQ.pqElevator.setTimer(0);
        Collections.sort(usedList);
        return usedList;
    }

    /**
     * Kullancıların sadece FIFO asnsöre bindiği  durumun simülasyonunu yapan metod.
     *
     * @param userList işlemmemiş kullanıcı listesi
     * @return yeni bir ArrayList olarak işlenmiş şekildeki kullanıcılar döndürülür.
     */

    public static ArrayList<Person> operateOnlyFIFO(ArrayList<Person> userList){
        ArrayList<Person> usedList = new ArrayList<>();
        ArrayList<Person> currentlyUsing = null;
        ArrayList<Person> copyList = deepCopy(userList);
        while (copyList.size()>0){
            currentlyUsing = new ArrayList<>();
            for (int i = 0; i < 4 && copyList.size()>0; i++){
                currentlyUsing.add(copyList.remove(0));
            }
            FIFO.fifoElevator.operate(currentlyUsing);

            usedList.addAll(currentlyUsing);
        }
        FIFO.fifoElevator.setTimer(0);
        Collections.sort(usedList);
        return usedList;
    }

    /**
     * Kullancıların sadece PQ asnsöre bindiği  durumun simülasyonunu yapan metod.
     *
     * @param userList işlemmemiş kullanıcı listesi
     * @return yeni bir ArrayList olarak işlenmiş şekildeki kullanıcılar döndürülür.
     */

    public static ArrayList<Person> operateOnlyPQ(ArrayList<Person> userList){
        ArrayList<Person> usedList = new ArrayList<>();
        ArrayList<Person> currentlyUsing = null;
        ArrayList<Person> copyList = deepCopy(userList);
        copyList =sortByFloor(copyList);
        while (copyList.size()>0){
            currentlyUsing = new ArrayList<>();
            for (int i = 0; i < 4 && copyList.size()>0; i++){
                currentlyUsing.add(copyList.remove(0));
            }
            PQ.pqElevator.operate(currentlyUsing);

            usedList.addAll(currentlyUsing);
        }
        PQ.pqElevator.setTimer(0);
        Collections.sort(usedList);
        return usedList;
    }

    /**
     * Kullancıların rastgele iki gruba ayrıldığı ve bir grubun FIFO, diğerinin PQ asansöre
     * bindiği durumun simülasyonunu yapan metod.
     *
     * @param users işlemmemiş kullanıcı listesi
     * @return yeni bir ArrayList olarak işlenmiş şekildeki kullanıcılar döndürülür.
     */

    private static ArrayList<Person> operateRandom(ArrayList<Person> users){
        ArrayList<Person> usedList = new ArrayList<>();
        ArrayList[] lists = randomlyDivide(deepCopy(users));
        ArrayList<Person> FIFOlist = lists[0];
        ArrayList<Person> PQlist = lists[1];
        usedList.addAll(operateOnlyFIFO(FIFOlist));
        usedList.addAll(operateOnlyPQ(PQlist));
        Collections.sort(usedList);
        return usedList;
    }

    /**
     * Bir kullanıcı listesinde en düşük OITS değerine sahip olan asansöre binilme durumunu
     * hesaplayan metod.
     *
     * @param list işlemmemiş kullanıcı listesi
     * @param quantity kullanıcı listelerinin üretileceği altkümelerinin eleman sayısı
     * @return yeni bir ArrayList olarak en optimum kombinasyonu sağlayan liste döndürülür.
     */

    private static ArrayList<Person> bestCase(ArrayList<Person> list, int quantity){
        ArrayList<Person> bestScenario = null;
        ArrayList<Person> copyList = deepCopy(list);
        combinations(list, quantity, 0, new Person[quantity] );
        ArrayList<Person> usedList = new ArrayList<>();
        double max = Integer.MAX_VALUE;
        for (ArrayList FIFOList : listOfLists){
            ArrayList<Person> PQList = deepCopy(copyList);
            PQList.removeAll(FIFOList);
            usedList.addAll(operateOnlyFIFO(FIFOList));
            usedList.addAll(operateOnlyPQ(PQList));
            double oits = OITS(usedList);
            if (max > oits) {
                max = oits;
                bestScenario = deepCopy(usedList);
                usedList.clear();
            }
            else usedList.clear();
        }
        listOfLists.clear();
        return bestScenario;
    }

    /**
     * Asansörlere binmiş kullanıcıların bulunduğu bir listenin OITS değerini hesaplayan metod.
     *
     * @param list işlenmiş kullanıcı listesi
     * @return listenin OITS değeri
     */

    public static double OITS(ArrayList<Person> list){
        double time = 0.0;
        for (Person p : list){
            time += p.getUsageTime();
        }return time / (double) list.size();

//        double max = 0;
//        for (Person p : list){
//            if (p.getUsageTime() > max) max = p.getUsageTime();
//        }
//        return max / (double) list.size();
    }

    /**
     * Bir listenin istenen sayıda elemana sahip olan bütün altkümelerini (kombinasyonlarını)
     * oluşturup bunları statik listOfLists listesinde tutan bir metod.
     *
     * @param list kombinasyonlarının oluşturulacağı ana liste
     * @param len kombinasyonların eleman sayısı
     * @param startPosition kombinasyonu oluşturken alınan eleman sırası
     * @param result tamamlanmamış kombinasyonların tutulduğu array
     */

    private static void combinations(ArrayList<Person> list, int len, int startPosition, Person[] result){
        if (len == 0){
            ArrayList<Person> l = new ArrayList<>();
            for (Person p : result){
                l.add(p);
            }
            listOfLists.add(l);
            return;
        }
        for (int i = startPosition; i <= list.size()-len; i++){
            result[result.length - len] = list.get(i);
            combinations(list, len-1,i+1, result);
        }
    }

    /**
     * Bir listeyi rastgele uzunlukta iki listeye bölen bir metod.
     *
     * @param list işlenmemiş kullanıcı listesi
     * @return array olarak iki adat bölünmüş liste
     */

    private static ArrayList[] randomlyDivide(ArrayList<Person> list){
        ArrayList<Person> firstList = new ArrayList<>();
        ArrayList<Person> secondList = new ArrayList<>();

        for (Person p : list){
            if (new Random().nextInt(2) == 0) firstList.add(p);
            else secondList.add(p);
        }
        ArrayList[] lists = new ArrayList[2];
        lists[0] = firstList;
        lists[1] = secondList;
        return lists;
    }

    /**
     * Bir listeyi içindeki objeleriyle birlikte tamamen yeni objelere kopyalayan metod.
     *
     * @param list işlenmemiş kullanıcı listesi
     * @return Tamamen başka pointer lara sahip orjinalleriyle aynı objeler listesi
     */

    public static ArrayList<Person> deepCopy(ArrayList<Person> list){
        ArrayList<Person> newList = new ArrayList<>();
        for (Person p : list){
            newList.add(new Person(p));
        }return newList;
    }

    /**
     * Kullanıcıları liste içinde hedef katlarına göre sıralayan metod.
     *
     * @param list işlenmemiş kullanıcı listesi
     * @return kata göre sıralanmış işlenmemiş kullanıcı listesi
     */

    static ArrayList<Person> sortByFloor(ArrayList<Person> list){
        list.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return Integer.compare(o1.getTargetFloor(),o2.getTargetFloor());
            }
        });
        return list;
    }

    /**
     * Projede istediği şekilde 1 - 50 katları hedefleyen rastgele kullanıcılar oluşturan
     * metod.
     *
     * @param quantity oluşturulmak istenen kullanıcı sayısı
     * @return yeni bir işlenmemiş rastgele kullanıcıların bulunduğu liste
     */

    private static ArrayList<Person> createPeople(int quantity){
        ArrayList<Person> list = new ArrayList<>();
        for (int i = 1; i <= quantity; i++){
            list.add(new Person(i, String.format("%d. kişi", i), new Random().nextInt(50) + 1));
        }return list;
    }

    /**
     * Projede istenilen şekilde 18 kişilik bir kullanıcı listesini oluşturan metod.
     *
     * @returnyeni bir işlenmemiş 18 kullanıcının bulunduğu liste
     */

    private static ArrayList<Person> createSet(){
        ArrayList<Person> list = new ArrayList<>();

        int[] in = {5, 3, 9, 7, 4, 4, 1, 2, 3, 2, 5, 6, 8, 1, 5, 7, 6, 3};

        for(int i = 0; i< in.length; i++){
            list.add(new Person(i,String.format("%d. kullanıcı",i+1),in[i]));
        }
        return list;
    }

    /**
     * Liste elemanlarını sırayla yazdıran metod.
     *
     * @param list işlenmiş veya işlenmemiş kullanıcı listesi
     */

    private static void printList(ArrayList list ){
        for (Object o: list){
            System.out.println(o);
        }
    }

    /**
     * İki işlenmiş kullanıcıların bulunduğu listedeki elemanları eşleriyle karşılaştırp
     * ilk listedekine göre ITS si daha düşük olan kullanıcıların ekrana yazdıran metod.
     *
     * @param mainList ilk işlenmiş kullanıcıların bulunduğu liste
     * @param otherList ikinci işlenmiş kullanıcıların bulunduğu liste
     */

    private static void compareLists (ArrayList<Person> mainList , ArrayList<Person> otherList){
        for (int i = 0; i < mainList.size(); i++){
            if (mainList.get(i).getUsageTime() > otherList.get(i).getUsageTime())
                System.out.print(otherList.get(i) + " by " +
                        (mainList.get(i).getUsageTime() - otherList.get(i).getUsageTime()) + " seconds.\n");
        }
    }
}
