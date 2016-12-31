package graphics.events.types.keyboard;

/**
 * Created by Andrew on 12/29/2016.
 */
public class KeyPressedEvent extends KeyButtonEvent {

    public KeyPressedEvent(int button) {
        super(button, Type.KEY_PRESSED);
    }
}
