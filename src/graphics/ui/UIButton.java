package graphics.ui;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import graphics.events.Event;
import graphics.events.EventDispatcher;
import graphics.events.types.mouse.MousePressedEvent;
import graphics.events.types.mouse.MouseReleasedEvent;
import graphics.ui.listeners.PressListener;
import graphics.ui.listeners.ReleaseListener;
import javafx.util.Pair;
import utils.Utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

/**
 * @author Andrew
 */
public class UIButton extends UI {

    public static Color DEF_TEXT_COLOR = Color.black;

    public String label;
    public Color labelColor;
    public Font font;
    public boolean isPressed;

    private PressListener pressListener;
    private ReleaseListener releaseListener;

    public UIButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        labelColor = DEF_TEXT_COLOR;
        font = UI.DEFAULT_FONT;
    }

    public UIButton setLabel(@NotNull String label, @Nullable Font font) {
        this.label = label;
        if(font != null)
            this.font = font;
        return this;
    }

    public UIButton setLabelColor(@NotNull Color color) {
        this.labelColor = color;
        return this;
    }

    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
        if(pressListener != null)
            dispatcher.dispatch(Event.Type.MOUSE_PRESSED, (Event e) -> pressListener.onPressed((MousePressedEvent) e));
        if(releaseListener != null)
            dispatcher.dispatch(Event.Type.MOUSE_RELEASED, (Event e) -> releaseListener.onRelease((MouseReleasedEvent) e));
    }

    public void draw(Graphics g) {
        g.setColor(this.bgColor);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        if(label != null && !label.isEmpty()) {
            g.setColor(labelColor);
            FontMetrics fontMetrics = g.getFontMetrics(font);
            g.setFont(font);
            Rectangle2D labelBounds = fontMetrics.getStringBounds(label, g);
            Pair<Integer, Integer> textPos = Utils.calcCenterAlignForText(bounds, labelBounds);
            g.drawString(label, textPos.getKey(), textPos.getValue());
        }
        if(outlineColor != null) {
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    public void setPressListener(PressListener listener) {
        this.pressListener = listener;
    }

    public void setReleaseListener(ReleaseListener listener) {
        this.releaseListener = listener;
    }

}
