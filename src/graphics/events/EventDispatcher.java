package graphics.events;

/**
 * The Event Dispatcher: it dispatches the event to the appropiate event handler based on the event's type.
 */
public class EventDispatcher {

    private Event _event;

    public EventDispatcher(Event event) {
        this._event = event;
    }

    public void dispatch(Event.Type type, EventHandler handler) {
        if(_event.handled) return;
        if(_event.getType()  == type) _event.handled = handler.onEvent(_event);
    }
}
