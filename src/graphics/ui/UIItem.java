package graphics.ui;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import graphics.events.Event;
import graphics.events.EventDispatcher;
import graphics.events.types.mouse.MouseMovedEvent;
import graphics.ui.listeners.HoverListener;
import utils.Utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by Andrew on 12/28/2016.
 */
public class UIItem extends UI {

    public Color labelColor, hoverColor, hoverLabelColor;
    public boolean  isHovering;
    public BufferedImage image;

    private String label, hoverLabel;
    private Font font, hoverFont;
    private int labelOffsetX, labelOffsetY;

    private HoverListener listener;


    public UIItem(int x, int y, int width, int height) {
        super(x, y, width, height);
        labelColor = UIButton.DEF_TEXT_COLOR;
        font = UI.DEFAULT_FONT;
        hoverFont = font;
        isHovering = false;
    }

    public UIItem setImage(@NotNull BufferedImage bufImg) {
        int imgHeight = bufImg.getHeight();
//        if(bufImg.getHeight() > 22)
//            imgHeight = (int) Math.floor(bounds.getHeight() / 1.5);
//        else if(bufImg.getHeight() > 18)
//            imgHeight = (int) Math.floor(bounds.getHeight() / 2);
//        else if(bufImg.getHeight() > 14)
//            imgHeight = (int) Math.floor(bounds.getHeight() / 2.5);
//        else if(bufImg.getHeight() > 10)
//            imgHeight = (int) Math.floor(bounds.getHeight() / 3);
//        else
//            imgHeight = (int) Math.floor(bounds.getHeight() / 6);
        //width is negative, b/c getScaledInstance will auto calc it.
        Image img = bufImg.getScaledInstance(-1, imgHeight, Image.SCALE_SMOOTH);
        this.image = new BufferedImage(img.getWidth(null), imgHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics bufImgGraphics = image.createGraphics();
        bufImgGraphics.drawImage(img, 0, 0, null);
        bufImgGraphics.dispose();
        return this;
    }

    public UIItem setLabel(String label, @Nullable Font font) {
        this.label = label;
        if(font != null)
            this.font = font;
        return this;
    }

    @Override
    public void onEvent(Event event) {
        if(listener != null) {
            EventDispatcher dispatcher = new EventDispatcher(event);
            dispatcher.dispatch(Event.Type.MOUSE_MOVED, (Event e) -> listener.onHover((MouseMovedEvent) e));
        }
    }

    public void draw(Graphics g) {
//        g.drawImage(image, bounds.x, bounds.y, null);
        int imgX = (int) Math.floor(Utils.calcCenterXForImg(image, bounds));
        int imgY = (int) Math.floor(Utils.calcBaselineYForImg(image, bounds));
        g.drawImage(image, imgX, imgY, null);

        if(label != null && !label.isEmpty()) {
            g.setColor(labelColor);
            Rectangle2D labelBounds = g.getFontMetrics(font).getStringBounds(label, g);
            int x = (int) (bounds.getCenterX() - labelBounds.getCenterX());
            int y = (int) (bounds.getCenterY() - labelBounds.getCenterY());
            g.drawString(label, x + labelOffsetX, y + labelOffsetY);
        }

        if(outlineColor != null) {
            g.setColor(outlineColor);
            g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        if(isHovering) {
            if (hoverColor != null) {
                g.setColor(hoverColor);
                g.fillRect(bounds.x , bounds.y + 8, bounds.width, bounds.height - 8);
            }
            //TODO: make this hover a separate object that has only one instance, all other things will send text to it. This would make it a bit more organized.
            if(hoverLabelColor == null) hoverLabelColor = labelColor;
            g.setColor(hoverLabelColor);
            Rectangle2D hoverLabelBounds = g.getFontMetrics(hoverFont).getStringBounds(hoverLabel, g);
            g.drawString(hoverLabel, 5, (int) hoverLabelBounds.getHeight() + (int) hoverLabelBounds.getCenterY());
        }
    }

    public void setLabelOffsets(int offsetX, int offsetY) {
        this.labelOffsetX = offsetX;
        this.labelOffsetY = offsetY;
    }

    public void setOnHover(HoverListener listener) {
        this.listener = listener;
    }

    public void setHoverLabel(String label, @Nullable Font font) {
        this.hoverLabel = label;
        if(font != null)
            this.hoverFont = font;
    }
}
