package graphics.events.types.keyboard;

/**
 * Created by Andrew on 12/29/2016.
 */
public class KeyReleasedEvent extends KeyButtonEvent {

    public KeyReleasedEvent(int button) {
        super(button, Type.KEY_RELEASED);
    }
}
