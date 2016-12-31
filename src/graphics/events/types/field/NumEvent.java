package graphics.events.types.field;

import graphics.events.Event;

/**
 * Created by Andrew on 12/30/2016.
 */
public class NumEvent<T extends Number> extends Event {

    private final T number;

    public NumEvent(T number, Type type) {
        super(type);
        this.number = number;
    }

    public final T getNumber() {
        return number;
    }
}
