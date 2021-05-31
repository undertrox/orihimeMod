package de.undertrox.orihimemod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedRandom<T> {
    private List<Integer> weights;
    private int sum;
    private List<T> items;
    private static Random random = new Random();

    public WeightedRandom(){
        weights = new ArrayList<>();
        items = new ArrayList<>();
    }

    public void addItem(T item, int weight) {
        items.add(item);
        weights.add(weight);
        sum += weight;
    }

    public T getRandomItem() {
        if (sum == 0) return null;
        int rnd = random.nextInt(sum)+1;
        int index = -1;
        while (rnd > 0) {
            index++;
            rnd -= weights.get(index);
        }
        return items.get(index);
    }
}
