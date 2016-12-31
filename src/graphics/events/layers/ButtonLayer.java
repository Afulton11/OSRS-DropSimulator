package graphics.events.layers;

import graphics.GraphicApp;
import graphics.events.Event;
import graphics.events.EventDispatcher;
import graphics.events.types.button.ButtonSetEvent;

import java.awt.Color;
import java.text.NumberFormat;

/**
 * Created by Andrew on 12/30/2016.
 */
public class ButtonLayer extends EventHandlerLayer {

    public ButtonLayer(GraphicApp app) {
        super(app);
    }

    @Override
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
        dispatcher.dispatch(Event.Type.BUTTON_REFRESH, (Event e) -> onRefreshBtnClicked());
        dispatcher.dispatch(Event.Type.BUTTON_SET, (Event e) -> onSetBtnClicked((ButtonSetEvent) e));
    }

    private boolean onRefreshBtnClicked() {
        app.refreshLoot();
        return true;
    }

    private boolean onSetBtnClicked(ButtonSetEvent event) {
        if(!event.getNpcName().isEmpty()) {
            if(app.npcLabel.labelColor != Color.WHITE)
                app.npcLabel.labelColor = Color.WHITE;
            app.npcLabel.label = event.getNpcName() + "; " + NumberFormat.getInstance().format(event.getKills()) + " kill(s)";
            app.npcName = event.getNpcName();
            app.killAmt = event.getKills();
            app.refreshLoot();
        }
        return true;
    }
}
