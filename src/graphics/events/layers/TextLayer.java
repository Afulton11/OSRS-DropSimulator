package graphics.events.layers;

import graphics.GraphicApp;
import graphics.events.Event;
import graphics.events.EventDispatcher;
import graphics.events.types.field.NumKillsEvent;
import graphics.events.types.field.TextNpcEvent;

/**
 * Not needed anymore...
 */
public class TextLayer extends EventHandlerLayer {

    public TextLayer(final GraphicApp app) {
        super(app);
    }

    @Override
    public void onEvent(Event event) {
        if(event.getType() == Event.Type.TEXT_NPC) {
            EventDispatcher dispatcher = new EventDispatcher(event);
            dispatcher.dispatch(Event.Type.TEXT_NPC, (Event e) -> onTextNpcEvent((TextNpcEvent) e));
            dispatcher.dispatch(Event.Type.NUM_KILLS, (Event e) -> onNumKillsEvent((NumKillsEvent) e));
        }
    }

    private boolean onTextNpcEvent(TextNpcEvent event) {
        boolean handled = true;
        app.npcLabel.label = event.getText();
        return handled;
    }

    private boolean onNumKillsEvent(NumKillsEvent event) {
        boolean handled = true;
        app.killAmt = event.getNumber();
        return handled;
    }


}
