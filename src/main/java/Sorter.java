import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Sorter {
    private final HashMap<Integer, Bottle> bottles = new HashMap<>();


    void init() {

        Mixer mixer = new Mixer(4, 10, 16);
        HashMap<Integer, Bottle> botles = mixer.generateBottlesList();
        drawBottles(botles, 4);
        mix(botles);
        drawBottles(botles, 4);
        /*var scanner = new Scanner(System.in);
        System.out.println("Сколько бутылок?");
        var N = scanner.nextInt();
        System.out.println("Сколько жидкостей?");
        var M = scanner.nextInt();
        System.out.println("Объем?");
        var V = scanner.nextInt();

        for (int i = 0; i < N; i++) {
            Bottle bottle = new Bottle(V);
            System.out.printf("Бутылка %d:\n", i);
            for (int j = 0; j < V; j++) {
                int input = scanner.nextInt();
                bottle.add(input > 0 ? input : -1);
            }
            bottles.put(i,bottle);
        }
*/
       /* var V = 4;
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
        }*/


//        LinkedList<Move> history = new LinkedList<>();
//        Move emptyMove = new Move();
//        history.add(emptyMove);
//        drawBottles(bottles, V);
//        sort(bottles, history );

        /*drawBottles(bottles, V);
        LinkedList<Move> history = new LinkedList<>();
        Move emptyMove = new Move();
        history.add(emptyMove);
        if (sort(bottles, history, 0)) {
            System.out.println("Решение найдено!");
        } else {
            System.out.println("Решение не найдено");
        }
        drawBottles(bottles, V);*/

    }


    private void mix(HashMap<Integer, Bottle> bottles) {
        int count = 0;
        while (count<100) {
            for (Integer fromBottle : bottles.keySet()) {
                for (Integer toBottle : bottles.keySet()) {
                    if (canPour(bottles, fromBottle, toBottle)) {

                        Bottle bottleFrom = bottles.get(fromBottle);
                        Bottle bottleTo = bottles.get(toBottle);
                        int upperNumberFrom = bottleFrom.removeFirst();
                        bottleTo.addFirst(upperNumberFrom);
                        count++;

                    }

                }
            }
        }
    }

    private boolean isMixed(HashMap<Integer,Bottle> bottles, int M) {
        int count = 0;
         for (Integer key : bottles.keySet()) {
             if (bottles.get(key).isFull()&&bottles.get(key).isMixed()){
                count++;
             }
         }
         return count==M;
    }


    private boolean sort(HashMap<Integer, Bottle> bottles, LinkedList<Move> history, int depth) {
        if (depth > 50) return false;
        if (isSorted(bottles)) {
            return true;
        }

        List<Move> possibleMoves = calculateMoves(bottles, history.getLast());
        Move bestMove = selectBestMove(possibleMoves);

        if (bestMove.priority == 0) {
            return false; // Нет допустимых ходов
        }

        // Пробуем основной ход
        pour(bottles, bestMove.from, bestMove.to);
        history.addLast(bestMove);

        if (!sort(bottles, history, depth + 1)) {
            // Если зашли в тупик - откатываемся и пробуем альтернативы
            undoLastPour(bottles, history);

            for (Move alternative : bestMove.alternatives) {
                pour(bottles, alternative.from, alternative.to);
                history.addLast(alternative);

                if (sort(bottles, history, depth + 1)) {
                    return true;
                }

                undoLastPour(bottles, history);
            }
            return false;
        }
        return true;
    }


    private void undoLastPour(HashMap<Integer, Bottle> bottles, LinkedList<Move> history) {
        if (history.isEmpty()) return;

        Move lastMove = history.removeLast();
        Bottle bottleFrom = bottles.get(lastMove.to); // Инвертируем направление
        Bottle bottleTo = bottles.get(lastMove.from);

        do {
            int upperNumberFrom = bottleFrom.removeFirst();
            bottleTo.addFirst(upperNumberFrom);
        }
        while (!bottleTo.isFull() && !bottleTo.isFull() && bottleFrom.getFirst() == bottleTo.getFirst());
    }

    private HashMap<Integer, Bottle> deepCopy(HashMap<Integer, Bottle> oldBottles) {
        return oldBottles.entrySet().stream()
                .collect(
                        HashMap::new,
                        (map, entry) -> map.put(entry.getKey(), new Bottle(entry.getValue())),
                        HashMap::putAll
                );
    }

    private List<Move> calculateMoves(HashMap<Integer, Bottle> bottles, Move lastMove) {
        List<Move> moves = new ArrayList<>();
        for (Integer from : bottles.keySet()) {
            for (Integer to : bottles.keySet()) {
                if (canPour(bottles, from, to)) {
                    Move move = new Move(from, to);
                    move.priority = calculateMovePriority(bottles, from, to, lastMove);
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    private int calculateMovePriority(HashMap<Integer, Bottle> bottles, int from, int to, Move lastMove) {
        Bottle bottleFrom = new Bottle(bottles.get(from));
        Bottle bottleTo = new Bottle(bottles.get(to));

        int priority = 0;

        do {
            int upperNumberFrom = bottleFrom.removeFirst();
            bottleTo.addFirst(upperNumberFrom);
        }
        while (!bottleTo.isFull() && bottleFrom.getFirst() == bottleTo.getFirst());

        // Высший приоритет - завершение пробирки
        if (bottleTo.isCollected() && !bottleFrom.isEmpty()) {
            priority += 10;
        }

        // Средний приоритет - освобождение пробирки
        if (bottleFrom.isEmpty() && !bottles.get(to).isEmpty()) {
            priority += 5;
        }
        // Низкий приоритет - обычное объединение
        priority += 3;

        //ЕСЛИ ХОД ОБРАТНЫЙ ПРОШЛОМУ
        if (from == lastMove.to && to == lastMove.from) {
            priority -= 1;
        }
        if (bottleTo.isCoupleBottom() && bottleTo.getFilling() > 2) {
            priority -= 2;
        }
        if (bottleTo.hasClosed()) {
            priority -= 1;
        }
        return priority;
    }


    private Move selectBestMove(List<Move> possibleMoves) {
        if (possibleMoves.isEmpty()) {
            return new Move();
        }

        // Находим максимальный приоритет
        int maxPriority = possibleMoves.stream()
                .mapToInt(Move::getPriority)
                .max()
                .orElse(0);

        // Фильтруем ходы с максимальным приоритетом
        List<Move> bestMoves = possibleMoves.stream()
                .filter(m -> m.priority == maxPriority)
                .toList();

        // Первому ходу добавляем альтернативы
        Move primaryMove = bestMoves.get(0);
        for (int i = 1; i < bestMoves.size(); i++) {
            primaryMove.addAlternativeMove(bestMoves.get(i));
        }

        return primaryMove;
    }


    private boolean isSorted(HashMap<Integer, Bottle> bottles) {
        for (Integer key : bottles.keySet()) {
            if (!bottles.get(key).isCollected()) {
                return false;
            }
        }
        return true;
    }

    private boolean canSort(HashMap<Integer, Bottle> bottles) {
        for (Integer fromBottle : bottles.keySet()) {
            for (Integer toBottle : bottles.keySet()) {
                if (!fromBottle.equals(toBottle) && canPour(bottles, fromBottle, toBottle)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean test_canPour(HashMap<Integer, Bottle> bottles, int from, int to) {

        Bottle bottleFrom = bottles.get(from);
        Bottle bottleTo = bottles.get(to);
        if (from == to) {
            return false;
        }
        if (bottleFrom.isEmpty()) {
            return false;
        }
        if (bottleTo.isFull()) {
            return false;
        }

        return true;
    }

    private boolean canPour(HashMap<Integer, Bottle> bottles, int from, int to) {

        Bottle bottleFrom = bottles.get(from);
        Bottle bottleTo = bottles.get(to);
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

        do {
            int upperNumberFrom = bottleFrom.removeFirst();
            bottleTo.addFirst(upperNumberFrom);
        }
        while (!bottleTo.isFull() && !bottleTo.isFull() && bottleFrom.getFirst() == bottleTo.getFirst());
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
