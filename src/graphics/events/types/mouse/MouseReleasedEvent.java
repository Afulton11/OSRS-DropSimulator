package graphics.events.types.mouse;


import graphics.events.Event;

/**
 * Created by andrew on 9/4/16.
 */
public class MouseReleasedEvent extends MouseButtonEvent {

    public MouseReleasedEvent(int button, int x, int y) {
        super(button, x, y, Event.Type.MOUSE_RELEASED);
    }

    @Override
    public int getButton() {
        return super.getButton();
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }
}
