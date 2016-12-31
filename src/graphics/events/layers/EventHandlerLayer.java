package graphics.events.layers;

import graphics.GraphicApp;

/**
 * Created by Andrew on 12/30/2016.
 */
public class EventHandlerLayer extends Layer {

    protected final GraphicApp app;

    protected EventHandlerLayer(final GraphicApp app) {
        this.app = app;
    }
}
