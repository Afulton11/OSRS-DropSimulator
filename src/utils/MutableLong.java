package utils;

/**
 * Created by Andrew on 12/27/2016.
 */
public class MutableLong {

    private long value;

    public MutableLong(int initial) {
        value = initial;
    }

    public void incrementBy(int increment) {
        value += increment;
    }

    public long get() {
        return value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
