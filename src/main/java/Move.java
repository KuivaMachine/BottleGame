import java.util.ArrayList;
import java.util.List;

public class Move {
    int from;
    int to;
    int priority;
    List<Move> alternatives;
    public Move(int from, int to) {
        this.from = from;
        this.to = to;
        this.priority=0;
        this.alternatives = new ArrayList<>();
    }

    public int getPriority() {
        return priority;
    }

    public Move() {
        this.from = 0;
        this.to =0;
        this.priority=0;
        this.alternatives = new ArrayList<>();
    }
    public void addAlternativeMove(Move move) {
        this.alternatives.add(move);
    }
}