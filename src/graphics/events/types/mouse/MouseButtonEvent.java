package graphics.events.types.mouse;

import graphics.events.Event;

/**
 * Created by andrew on 9/4/16.
 */
public class MouseButtonEvent extends Event {

    protected int button;
    protected int x, y;

    public MouseButtonEvent(int button, int x, int y, Event.Type type) {
        super(type);
        this.button = button;
        this.x = x;
        this.y = y;
    }

    public int getButton() {
        return button;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
