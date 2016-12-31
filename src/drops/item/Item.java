package drops.item;

import java.awt.image.BufferedImage;

/**
 * Created by Andrew on 12/27/2016.
 */
public class Item {

    public final String name;
    private final BufferedImage image;

    public int individualPrice;

    public Item(final String name, final BufferedImage image) {
        this.name = name;
        this.image = image;
    }

    public final BufferedImage getImage() {
        return image;
    }

    @Override
    public String toString() {
        return name;
    }
}
