import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Sorter {
    private final HashMap<Integer, Bottle> bottles = new HashMap<>();

    void init() {


        /*var scanner = new Scanner(System.in);
        System.out.println("Сколько бутылок?");
        var N = scanner.nextInt();
        System.out.println("Сколько жидкостей?");
        var M = scanner.nextInt();
        System.out.println("Объем?");
        var V = scanner.nextInt();*/
        var V = 4;
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("./src/main/java/file.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        var count = 0;
        for (String line : lines) {
            var bottle = new Bottle(4);

            String[] parts = line.trim().split("\\s+");

            if (parts.length != 4) continue;
            for (String x : parts) {
                bottle.add(Integer.parseInt(x));
            }
            bottles.put(count, bottle);
            count++;
        }



            /*System.out.printf("Бутылка %d:\n", i);
            for (int j = 0; j < V; j++) {
                int input = scanner.nextInt();
                bottle.add(input > 0 ? input : -1);
            }*/


        drawBottles(bottles, V);
        sort(bottles);
        drawBottles(bottles, V);


    }

    private void sort(HashMap<Integer, Bottle> bottles) {
        int count = 0;
        String lastMove = "";
        while (!isSorted(bottles)) {
            for (Integer fromBottle : bottles.keySet()) {
                for (Integer toBottle : bottles.keySet()) {
                    if (canSort(bottles, lastMove)) {
                        if (canPour(bottles, fromBottle, toBottle, lastMove)) {
                            pour(bottles, fromBottle, toBottle);
                            lastMove = String.format("%d%d", fromBottle, toBottle);
                            count++;
                        }
                    } else {
                        System.out.println("НЕТ РЕШЕНИЯ");
                        return;
                    }
                }
            }
        }
        System.out.println("Я ВСЕ, ХОДОВ: " + count);
    }


    private boolean isSorted(HashMap<Integer, Bottle> bottles) {
        for (Integer key : bottles.keySet()) {
            if (!bottles.get(key).isCollected()) {
                return false;
            }
        }
        return true;
    }

    private boolean canSort(HashMap<Integer, Bottle> bottles, String lastMove) {
        for (Integer fromBottle : bottles.keySet()) {
            for (Integer toBottle : bottles.keySet()) {
                if (!fromBottle.equals(toBottle) && canPour(bottles, fromBottle, toBottle, lastMove)) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean canPour(HashMap<Integer, Bottle> bottles, int from, int to, String lastMove) {

        Bottle bottleFrom = bottles.get(from);
        Bottle bottleTo = bottles.get(to);
        if (String.format("%d%d", to, from).equals(lastMove)) {
            return false;
        }
        if (from == to) {
            return false;
        }
        if (bottleFrom.isEmpty()) {
            return false;
        }
        if (bottleTo.isFull()) {
            return false;
        }
        int upperNumberFrom = bottleFrom.getFirst();
        int upperNumberTo = bottleTo.getFirst();
        if (upperNumberFrom != upperNumberTo && !bottleTo.isEmpty()) {
            return false;
        }
        return true;
    }


    public void pour(HashMap<Integer, Bottle> bottles, int from, int to) {
        Bottle bottleFrom = bottles.get(from);
        Bottle bottleTo = bottles.get(to);

        do{
            int upperNumberFrom = bottleFrom.removeFirst();
            bottleTo.addFirst(upperNumberFrom);
        }
        while (!bottleTo.isFull()&&!bottleTo.isFull()&&bottleFrom.getFirst()==bottleTo.getFirst());
        System.out.printf("%d - %d\n", from, to);
    }


    public void drawBottles(HashMap<Integer, Bottle> bottles, int volume) {
        // Преобразуем в список для удобного доступа по индексу
        List<Bottle> bottleList = new ArrayList<>(bottles.values());

        // Обрабатываем бутылки группами по 8
        for (int i = 0; i < bottleList.size(); i += 8) {
            // Выводим верхушки бутылок
            for (int j = i; j < i + 8 && j < bottleList.size(); j++) {
                System.out.print("  ╔    ╗  ");
            }
            System.out.println();

            // Выводим содержимое (5 строк)
            for (int line = 0; line < volume; line++) {
                for (int j = i; j < i + 8 && j < bottleList.size(); j++) {
                    Bottle bottle = bottleList.get(j);
                    int value = bottle.get(line);
                    System.out.printf("  ║ %-2s ║  ", value > 0 ? value : " ");
                }
                System.out.println();
            }

            // Выводим донышки бутылок
            for (int j = i; j < i + 8 && j < bottleList.size(); j++) {
                System.out.print("  ╚════╝  ");
            }
            System.out.println("\n");
        }
    }


}
