package de.undertrox.orihimemod;

public class Pair<T,U> {
    T key;

    public T getKey() {
        return key;
    }

    public U getValue() {
        return value;
    }

    U value;

    public Pair(T key, U value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }
}
