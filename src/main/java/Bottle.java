import java.util.ArrayList;

public class Bottle {
    private final int size;
    private final ArrayList<Integer> array = new ArrayList<>();

    public Bottle(int size) {
        this.size = size;
    }

    public int size() {
        return this.size;
    }

    public void addFirst(int number) {
        for (int j = size - 1; j >= 0; j--) {
            if (array.get(j) < 0) {
                array.remove(j);
                array.add(j, number);
                break;
            }
        }
    }

    public int get(int index) {
        return array.get(index);
    }


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

    public int getFirst() {
        return array.stream().filter(x -> x > 0).findFirst().orElse(-1);
    }

    public boolean isEmpty() {
        return array.stream().filter((x) -> x < 0).count() == size;
    }
    public void add(int number){
        array.add(number);
    }
    public boolean isCollected() {
        return array.stream().distinct().count() == 1;
    }

    public boolean isFull() {
        return array.stream().filter((x) -> x > 0).count() == size;
    }
}
