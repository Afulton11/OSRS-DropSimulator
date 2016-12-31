package graphics.ui;

import com.sun.istack.internal.NotNull;
import javafx.util.Pair;
import utils.LootUtils;
import utils.Utils;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

/**
 * A loader label created to work specifically with the LootUtils class.
 */
public class UILoaderLabel extends UILabel {

    private static final Color foreground = new Color(0xD10000);

    public UILoaderLabel(int x, int y, int width, int height, @NotNull String label) {
        super(x, y, width, height, label, null, null);
        outlineColor = foreground;
        bgColor = Color.BLACK;
        labelColor = Color.WHITE;
    }

    @Override
    public void onUpdate() {
        if(visible) {
            label = LootUtils.getProgressString();
            if (label == LootUtils.progressFinished) {
                this.visible = false;
                this.bgColor = Color.BLACK;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if(bgColor != null) {
            g.setColor(bgColor);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        g.setColor(outlineColor);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        int width = (int) Math.floor(bounds.width * (LootUtils.getProgress() / 100));
        if(width > bounds.width - 2) {
            width = bounds.width -2;
        }
        g.fillRect(bounds.x + 2, bounds.y + 2, width, bounds.height - 4);

        g.setColor(labelColor);
        //center align the label text.
        FontMetrics fontMetrics = g.getFontMetrics(UI.DEFAULT_FONT);
        Pair<Integer, Integer> textPos = Utils.calcCenterAlignForText(bounds, fontMetrics.getStringBounds(constLabel + label, g));
        g.drawString(constLabel + label, textPos.getKey() - 10, textPos.getValue());

    }
}
