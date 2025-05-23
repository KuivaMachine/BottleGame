import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Bottle {
    private final int size;
    private ArrayList<Integer> array = new ArrayList<>();

    public Bottle(int size) {
        this.size = size;

    }
    public Bottle(Bottle bottle) {
        this.size = bottle.size;
        this.array = new ArrayList<>(bottle.array);
    }

    /**
     * Объем пробирки
     */
    public int size() {
        return this.size;
    }

    /**
     * Добавляет число на верхнее место в пробирке, если оно есть
     */
    public void addFirst(int number) {
        for (int j = size - 1; j >= 0; j--) {
            if (array.get(j) < 0) {
                array.remove(j);
                array.add(j, number);
                break;
            }
        }
    }

    /**
     * Возвращает пустую пробирку
     */
    public void fillWithMinusOne() {
        this.array = new ArrayList<>(Collections.nCopies(this.size, -1));
    }

    /**
     * Возвращает элемент пробирки по индексу
     */
    public int get(int index) {
        return array.get(index);
    }

    /**
     * Удаляет и возвращает верхний элемент пробирки
     */
    public int removeFirst() {
        for (int i = 0; i < size; i++) {
            if (array.get(i) > 0) {
                int first = array.remove(i);
                array.add(i, -1);
                return first;
            }
        }
        return -1;
    }

    /**
     * Возвращает верхний элемент пробирки
     */
    public int getFirst() {
        return array.stream().filter(x -> x > 0).findFirst().orElse(-1);
    }

    /**
     * Возвращает true, если нет жидкости в пробирке
     */
    public boolean isEmpty() {
        return array.stream().filter((x) -> x < 0).count() == size;
    }

    /**
     * Добавляет элемент в пробирку
     */
    public void add(int number){
        array.add(number);
    }

    /**
     * Возвращает true, если пробирка отсортирована
     */
    public boolean isCollected() {
        return array.stream().distinct().count() == 1;
    }

    /**
     * Возвращает true, если в пробирке нет места
     */
    public boolean isFull() {
        return array.stream().filter((x) -> x > 0).count() == size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bottle bottle = (Bottle) o;
        return size == bottle.size && Objects.equals(array, bottle.array);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, array);
    }
}
