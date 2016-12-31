package graphics.events.types.exception;

import graphics.events.Event;

/**
 * Created by Andrew on 12/30/2016.
 */
public class ExceptionEvent<T extends Exception> extends Event {

    protected final T exception;

    protected ExceptionEvent(T exception, Type type) {
        super(type);
        this.exception = exception;
    }

    public T getException() {
        return exception;
    }
}
