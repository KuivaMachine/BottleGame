import java.util.*;

/**
 * Класс для сортировки жидкостей в пробирках
 */
public class Sorter {
    private final HashMap<Integer, Bottle> bottles = new HashMap<>();
    private int V = 0;

    /**
     * Основной метод инициализации и запуска сортировки
     */
    public void init() {
        readInputFromConsole();
        LinkedList<Move> history = new LinkedList<>();
        history.add(new Move()); // Пустой начальный ход

        System.out.println("Исходная позиция:");
        drawBottles(bottles);

        sort(bottles, history, 0);
        System.out.println("Результат:\n");
        drawBottles(bottles);
        System.out.println("Последовательность ходов:\n");
        printHistory(history);

    }

    /**
     * Чтение входных данных с консоли с валидацией
     */
    private void readInputFromConsole() {
        Scanner scanner = new Scanner(System.in);


        System.out.println("Сколько всего пробирок?");
        var N = scanner.nextInt();
        while (N <= 2) {
            System.out.println("Количество пробирок должно быть не меньше 2.\nВведите число пробирок:");
            N = scanner.nextInt();
        }

        System.out.println("Сколько всего жидкостей?");
        var M = scanner.nextInt();
        while (M >= N || M < 0) {
            System.out.printf("Количество жидкостей должно быть от 1 до %d.\nВведите количество жидкостей:\n", N - 1);
            M = scanner.nextInt();
        }

        System.out.println("Какой у пробирок объем?");
        V = scanner.nextInt();


        System.out.println("Введите содержимое пробирки, в одну строку через пробел.\nПример: \"1 3 4 1\"");
        for (int i = 0; i < M; i++) {
            Bottle bottle = new Bottle(V);
            System.out.printf("Пробирка %d:\n", i);
            for (int j = 0; j < V; j++) {
                int input = scanner.nextInt();
                bottle.add(input > 0 ? input : -1);
            }
            bottles.put(i, bottle);
        }

        // Добавление пустых пробирок
        for (int i = M; i < N; i++) {
            Bottle bottle = new Bottle(V);
            bottle.fillWithMinusOne();
            bottles.put(i, bottle);
        }
    }


    /**
     * Рекурсивный метод сортировки пробирок
     *
     * @param bottles текущее состояние пробирок
     * @param history история ходов
     * @param depth   текущая глубина рекурсии
     * @return true если решение найдено, false в противном случае
     */
    private boolean sort(HashMap<Integer, Bottle> bottles, LinkedList<Move> history, int depth) {
        //Защита от бесконечной рекурсии
        if (depth > 100) {
            System.out.println("Решение не найдено. Последняя позиция:");
            drawBottles(bottles);
            System.exit(0);
        }
        //Выход, если отсортировано
        if (isSorted(bottles)) {
            return true;
        }

        //Расчет возможных ходов
        List<Move> possibleMoves = calculateMoves(bottles, history.getLast());
        //Выбор лучшего хода
        Move bestMove = selectBestMove(possibleMoves);

        //Если нет допустимых ходов
        if (bestMove.priority == 0) {
            return false;
        }

        //Пробуем основной ход
        pour(bottles, bestMove.from, bestMove.to);
        //Записываем в историю
        history.addLast(bestMove);

        if (!sort(bottles, history, depth + 1)) {
            // Если зашли в тупик - откатываемся
            undoLastPour(bottles, history);
            // Пробуем альтернативные ходы
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

    /**
     * Отменяет последний ход
     */
    private void undoLastPour(HashMap<Integer, Bottle> bottles, LinkedList<Move> history) {
        if (history.isEmpty()) return;

        Move lastMove = history.removeLast();
        Bottle fromBottle = bottles.get(lastMove.to); // Инвертируем направление
        Bottle toBottle = bottles.get(lastMove.from);

        do {
            int liquid = fromBottle.removeFirst();
            toBottle.addFirst(liquid);
        } while (!toBottle.isFull() && fromBottle.getFirst() == toBottle.getFirst());
    }

    /**
     * Вычисляет все возможные ходы
     */
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

    /**
     * Вычисляет приоритет хода
     */
    private int calculateMovePriority(HashMap<Integer, Bottle> bottles, int from, int to, Move lastMove) {
        Bottle bottleFrom = new Bottle(bottles.get(from));
        Bottle bottleTo = new Bottle(bottles.get(to));

        int priority = 0;

        //Симулируем переливание
        do {
            int liquid = bottleFrom.removeFirst();
            bottleTo.addFirst(liquid);
        } while (!bottleTo.isFull() && bottleFrom.getFirst() == bottleTo.getFirst());

        //Приоритеты:
        if (bottleTo.isCollected() && !bottleFrom.isEmpty()) {
            priority += 10; //завершение пробирки
        }
        if (bottleFrom.isEmpty() && !bottles.get(to).isEmpty()) {
            priority += 5; //освобождение пробирки
        }
        priority += 3;

        // Штраф за обратный ход
        if (from == lastMove.to && to == lastMove.from) {
            priority -= 1;
        }

        return priority;
    }

    /**
     * Выбирает лучший ход из возможных
     */
    private Move selectBestMove(List<Move> possibleMoves) {
        if (possibleMoves.isEmpty()) {
            return new Move(); // Пустой ход
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

    /**
     * Проверяет, отсортированы ли пробирки
     */
    private boolean isSorted(HashMap<Integer, Bottle> bottles) {
        for (Integer key : bottles.keySet()) {
            if (!bottles.get(key).isCollected()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет возможность переливания
     */
    private boolean canPour(HashMap<Integer, Bottle> bottles, int from, int to) {
        Bottle bottleFrom = bottles.get(from);
        Bottle bottleTo = bottles.get(to);
        //Если пробирки совпадают
        if (from == to) {
            return false;
        }
        //Если пробирка А пустая
        if (bottleFrom.isEmpty()) {
            return false;
        }
        //Если пробирка В полная
        if (bottleTo.isFull()) {
            return false;
        }
        int upperNumberFrom = bottleFrom.getFirst();
        int upperNumberTo = bottleTo.getFirst();
        //Если в пробирке B верхняя жидкость того же цвета, либо пробирка B пуста
        return upperNumberFrom == upperNumberTo || bottleTo.isEmpty();
    }

    /**
     * Выполняет переливание
     */
    public void pour(HashMap<Integer, Bottle> bottles, int from, int to) {
        Bottle fromBottle = bottles.get(from);
        Bottle toBottle = bottles.get(to);

        do {
            int liquid = fromBottle.removeFirst();
            toBottle.addFirst(liquid);
        } while (!toBottle.isFull() && fromBottle.getFirst() == toBottle.getFirst());
    }

    /**
     * Выводит историю ходов
     */
    private void printHistory(LinkedList<Move> moves) {
        moves.stream().forEach(move -> System.out.printf("%d -> %d\n", move.from, move.to));
    }

    /**
     * Отрисовывает текущее состояние пробирок
     */
    public void drawBottles(HashMap<Integer, Bottle> bottles) {
        List<Bottle> bottleList = new ArrayList<>(bottles.values());

        for (int i = 0; i < bottleList.size(); i += 8) {
            // Верхушки пробирок
            for (int j = i; j < i + 8 && j < bottleList.size(); j++) {
                System.out.print("  ╔    ╗  ");
            }
            System.out.println();

            // Содержимое пробирок
            for (int line = 0; line < V; line++) {
                for (int j = i; j < i + 8 && j < bottleList.size(); j++) {
                    int value = bottleList.get(j).get(line);
                    System.out.printf("  ║ %-2s ║  ", value > 0 ? value : " ");
                }
                System.out.println();
            }

            // Основания пробирок
            for (int j = i; j < i + 8 && j < bottleList.size(); j++) {
                System.out.print("  ╚════╝  ");
            }
            System.out.println("\n");
        }
    }
}