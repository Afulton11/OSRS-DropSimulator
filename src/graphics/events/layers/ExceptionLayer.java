package graphics.events.layers;

import graphics.GraphicApp;
import graphics.events.Event;
import graphics.events.EventDispatcher;
import graphics.events.types.exception.ExceptionFileNotFoundEvent;
import page.WikiConstants;

import java.awt.Color;

/**
 * Created by Andrew on 12/30/2016.
 */
public class ExceptionLayer extends EventHandlerLayer {

    public ExceptionLayer(GraphicApp app) {
        super(app);
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
        dispatcher.dispatch(Event.Type.EXCEPTION_FILENOTFOUND, (Event e) -> onFileNotFound((ExceptionFileNotFoundEvent) e));
    }

    private boolean onFileNotFound(ExceptionFileNotFoundEvent event) {
        if(event.getException().getMessage().contains(WikiConstants.baseUrl)) {
            app.npcLabel.labelColor = Color.RED;
            app.npcLabel.label = "'" + app.npcLabel.label + "' not found!";
        }
        return true;
    }
}
