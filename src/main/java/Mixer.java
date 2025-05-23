import java.util.HashMap;

public class Mixer {

    private int N;
    private int M;
    private int V;

    public Mixer(int v, int m, int n) {
        V = v;
        M = m;
        N = n;
    }

    public HashMap<Integer, Bottle> generateBottlesList(){
        HashMap<Integer, Bottle> bottles = new HashMap<>();
        for (int i = 0; i < M; i++) {
            Bottle bottle = new Bottle(V);
            for (int j = 0; j < V; j++) {
                bottle.add(i+1);
            }
            bottles.put(i,bottle);
        }
        for (int i = 0; i < N-M; i++) {
            Bottle bottle = new Bottle(V);
            bottle.add(-1);
            bottle.add(-1);
            bottle.add(-1);
            bottle.add(-1);
            bottles.put(N-i, bottle);
        }

        return bottles;
    }

}
