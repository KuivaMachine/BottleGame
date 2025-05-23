import java.util.ArrayList;
import java.util.stream.Collectors;

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
    public boolean isMixed(){
        return array.stream().distinct().count() > 1;
    }
    public String getStateString() {
        return array.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
    public boolean hasClosed(){
        return (array.stream().filter((x) -> x> 0).count()>2)&&(array.stream().distinct().count() > 1);
    }
    public int size() {
        return this.size;
    }
    public int getFilling(){
        return (int) array.stream().filter((x) -> x> 0).count();
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
    public boolean isCoupleBottom(){
        if (array.size() < 2) return false;
        int last = array.get(array.size() - 1);
        int secondLast = array.get(array.size() - 2);
        return secondLast > 0 && last == secondLast;

    }
    public int countTopLayers() {
        if (isEmpty()) return 0;
        int top = getFirst();
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (array.get(i) == top) count++;
            else break;
        }
        return count;
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

    @Override
    public String toString() {
        return "Bottle{" +
                ", array=" + array.toString() +
                '}';
    }
}
