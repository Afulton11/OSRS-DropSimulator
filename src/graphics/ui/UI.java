package graphics.ui;

import graphics.events.layers.Layer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;

/**
 * Created by Andrew on 12/28/2016.
 */
public abstract class UI extends Layer {

    public static Font DEFAULT_FONT;

    static {
        Font loadFont = null;
        try {
            ClassLoader cldr = UI.class.getClassLoader();
            loadFont = Font.createFont(Font.TRUETYPE_FONT, cldr.getResourceAsStream("font/RobotoCondensed-Regular.ttf")).deriveFont(12.0f);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        DEFAULT_FONT = loadFont;
    }

    protected static Color def_bg_color = Color.WHITE;

    public Color bgColor, outlineColor;
    public boolean visible;
    protected Rectangle bounds;

    public UI(int x, int y, int width, int height) {
        this.bounds = new Rectangle(x, y, width, height);
        bgColor = def_bg_color;
        visible = true;
    }

    @Override
    public void onDraw(Graphics g) {
        if(visible){
            draw(g);
        }
    }

    public abstract void draw(Graphics g);

    public Rectangle getBounds() {
        return bounds;
    }
}
