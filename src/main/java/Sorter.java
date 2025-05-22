import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Sorter {
    private final HashMap<Integer, ArrayList<Integer>> bottles = new HashMap<>();

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
            lines = Files.readAllLines(Paths.get("C:/Users/Александр/IdeaProjects/BottleGame/src/main/java/file.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        var count = 0;
        for (String line : lines) {
            var bottle = new ArrayList<Integer>();

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

        System.out.println(bottles);
        drawBottles(bottles, V);
        pour(bottles,0,12);
        pour(bottles,0,13);
        drawBottles(bottles, V);
        System.out.println(bottles);


    }

    private void sort(HashMap<Integer, ArrayList<Integer>> bottles) {
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
        return;
    }


    private boolean isSorted(HashMap<Integer, ArrayList<Integer>> bottles) {
        for (Integer key : bottles.keySet()) {
            if (bottles.get(key).stream().distinct().count() > 1) {
                return false;
            }
        }
        return true;
    }

    private boolean canSort(HashMap<Integer, ArrayList<Integer>> bottles, String lastMove) {
        for (Integer fromBottle : bottles.keySet()) {
            for (Integer toBottle : bottles.keySet()) {
                if (!fromBottle.equals(toBottle) && canPour(bottles, fromBottle, toBottle, lastMove)) {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean canPour(HashMap<Integer, ArrayList<Integer>> bottles, int from, int to, String lastMove) {

        ArrayList<Integer> bottleFrom = bottles.get(from);
        ArrayList<Integer> bottleTo = bottles.get(to);
        if (String.format("%d%d", to, from).equals(lastMove)) {
            return false;
        }
        if (from == to) {
            return false;
        }
        if (bottleFrom.stream().filter((x) -> x < 0).count() == bottleFrom.size()) {
            return false;
        }
        if (bottleTo.stream().filter((x) -> x > 0).count() == bottleFrom.size()) {
            return false;
        }
        int upperNumberFrom = bottleFrom.stream().filter(x -> x > 0).findFirst().orElse(-1);
        int upperNumberTo = bottleTo.stream().filter(x -> x > 0).findFirst().orElse(-1);
        if (upperNumberFrom != upperNumberTo && upperNumberTo != -1) {
            return false;
        }
        return true;
    }


    public void pour(HashMap<Integer, ArrayList<Integer>> bottles, int from, int to) {
        ArrayList<Integer> bottleFrom = bottles.get(from);
        ArrayList<Integer> bottleTo = bottles.get(to);

        for (int i = 0; i < bottleFrom.size(); i++) {
            int current = bottleFrom.get(i);
            if (current > 0) {
                int number = bottleFrom.remove(i);
                bottleFrom.add(i, -1);
                for (int j = bottleTo.size() - 1; j >= 0; j--) {
                    if (bottleTo.get(j) < 0) {
                        bottleTo.remove(j);
                        bottleTo.add(j, number);
                        break;
                    }
                }
                if (((i + 1) < bottleFrom.size() && bottleFrom.get(i + 1) != current) || (bottleTo.stream().filter((x) -> x > 0).count()) == bottleTo.size()) {
                    break;
                }
            }
        }
        System.out.printf("%d - %d\n", from, to);
    }


    public void drawBottles(HashMap<Integer, ArrayList<Integer>> bottles, int volume) {
        // Преобразуем в список для удобного доступа по индексу
        List<ArrayList<Integer>> bottleList = new ArrayList<>(bottles.values());

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
                    ArrayList<Integer> bottle = bottleList.get(j);
                    // Получаем элементы
                    Integer value = bottle.get(line);

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
