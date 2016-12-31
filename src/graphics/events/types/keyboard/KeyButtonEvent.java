package graphics.events.types.keyboard;

import graphics.events.Event;

/**
 * Created by Andrew on 12/29/2016.
 */
public class KeyButtonEvent extends Event {

    protected int button;

    public KeyButtonEvent(int button, Event.Type type) {
        super(type);
        this.button = button;
    }

    public int getButton() {
        return button;
    }
}
