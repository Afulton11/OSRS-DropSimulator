package graphics.ui;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javafx.util.Pair;
import utils.Utils;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * Created by Andrew on 12/30/2016.
 */
public class UILabel extends UI {

    public Color labelColor;
    public String label;
    public final String constLabel;


    public UILabel(int x, int y, int width, int height, @NotNull String label, @Nullable final String constLabel, @Nullable final String constLabelEnd) {
        super(x, y, width, height);
        this.label = label;
        if(constLabel == null)
            this.constLabel = "";
        else
            this.constLabel = constLabel;
        bgColor = null;
        labelColor = Color.WHITE;
    }

    public void draw(Graphics g) {
        if(bgColor != null) {
            g.setColor(bgColor);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        g.setColor(labelColor);
        //center align the label text.
        FontMetrics fontMetrics = g.getFontMetrics(UI.DEFAULT_FONT);
        Pair<Integer, Integer> textPos = Utils.calcCenterAlignForText(bounds, fontMetrics.getStringBounds(constLabel + label, g));
        g.drawString(constLabel + label, textPos.getKey() - 10, textPos.getValue());
    }
}
