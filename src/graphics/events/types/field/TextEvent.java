package graphics.events.types.field;

import graphics.events.Event;

/**
 * Created by Andrew on 12/30/2016.
 */
public class TextEvent extends Event {

    private final String text;

    protected TextEvent(final String text, Type type) {
        super(type);
        this.text = text;
    }

    public final String getText() {
        return text;
    }

}
