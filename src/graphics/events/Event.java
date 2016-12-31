package graphics.events;

public class Event {

    public enum Type {
        MOUSE_PRESSED,
        MOUSE_RELEASED,
        MOUSE_MOVED,
        KEY_PRESSED,
        KEY_RELEASED,
        TEXT_NPC,
        NUM_KILLS,
        BUTTON_REFRESH,
        BUTTON_SET,
        EXCEPTION_FILENOTFOUND,
    }

    private Type _type;

    boolean handled;


    protected Event(Type type) {
        this._type = type;
    }

    public Type getType() {
        return _type;
    }
}
