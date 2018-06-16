package graphics;

import drops.item.Item;
import graphics.events.Event;
import graphics.events.layers.ButtonLayer;
import graphics.events.layers.ExceptionLayer;
import graphics.events.layers.Layer;
import graphics.events.layers.TextLayer;
import graphics.events.types.button.ButtonSetEvent;
import graphics.events.types.exception.ExceptionFileNotFoundEvent;
import graphics.events.types.mouse.MouseMovedEvent;
import graphics.ui.UIItem;
import graphics.ui.UILabel;
import graphics.ui.UILoaderLabel;
import page.WikiConstants;
import utils.LootUtils;
import utils.MutableLong;
import utils.Utils;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Andrew on 12/28/2016.
 */
public class GraphicApp extends Canvas implements Runnable {

    public static int WIDTH = 628, HEIGHT = 562;
    public static String TITLE = "OSRS Drop Simulator";
    public static Color bgColor;
    private static final boolean showFps = false;
    private Thread thread;
    private boolean running;

    private List<Layer> layerStack;

    public UILabel npcLabel;
    public UILoaderLabel loaderLabel;
    public String npcName;
    public int killAmt;
    private Map<Item, MutableLong> loot;
    private List<UIItem> uiLootItems;
    private ExecutorService lootLoader;

    private volatile boolean currentlyRefreshing;

    private Comparator<Map.Entry<Item, MutableLong>> lootSorter = (d1, d2) -> {
        if(d1.getKey().individualPrice == WikiConstants.notSoldPrice) return -1;
        else if(d2.getKey().individualPrice == WikiConstants.notSoldPrice) return 1;
        long d1Total = d1.getKey().individualPrice * d1.getValue().get();
        long d2Total = d2.getKey().individualPrice * d2.getValue().get();
        if(Long.compare(d1Total, d2Total) >= 0) return -1;
        else if(Long.compare(d2Total, d1Total) >= 0) return 1;
        return 0;
    };

    private GraphicApp() {
        thread = new Thread(this, TITLE);
        this.setSize(WIDTH, HEIGHT);
        bgColor = new Color(0x473D32);
        layerStack = new ArrayList<>();

        //setup ui
        //setup the Npc Label
        npcName = "General Graardor";
        killAmt = 1_000;
        npcLabel = new UILabel(0, 0, WIDTH, 15, npcName, "NPC: ", "; ");
        npcLabel.bgColor = null;
        //setup the loader label
        int loaderWidth = 250, loaderHeight = 35;
        int x = WIDTH / 2 - loaderWidth / 2;
        int y = HEIGHT / 2 - loaderHeight / 2;
        loaderLabel = new UILoaderLabel(x, y, loaderWidth, loaderHeight, "Loading...");
        loaderLabel.visible = false;

        //we want the loaderLabel to appear over the items, just in case the items don't disappear when loading.
        layerStack.add(loaderLabel);
        layerStack.add(npcLabel);

        //setup loot service
        lootLoader = Executors.newSingleThreadExecutor();
    }

    /**
     * loads things that need the the window to be initialized.
     */
    public void init() {
        //setup EventHandlers
        layerStack.add(new TextLayer(this));
        layerStack.add(new ButtonLayer(this));
        layerStack.add(new ExceptionLayer(this));


        //setup loot
        ButtonSetEvent btnSetEvent = new ButtonSetEvent(npcName, killAmt);
        onEvent(btnSetEvent);

        refreshLoot();
    }

    public void refreshLoot() {
        if(!lootLoader.isTerminated() && !currentlyRefreshing) {
            //clear the current uiItems from the screen, preparing for the loaderLabel.
            clearUILoot();
            loaderLabel.visible = true;
            lootLoader.execute(() -> {
                currentlyRefreshing = true;
                try {
                    loot = LootUtils.getLoot(npcName, killAmt);
                } catch (FileNotFoundException e) {
                    ExceptionFileNotFoundEvent event = new ExceptionFileNotFoundEvent(e);
                    this.onEvent(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(loot != null) {
                    sortLoot();
                    uiLootItems = createUILoot();
                }
                currentlyRefreshing = false;
            });
        }
    }

    public List<UIItem> createUILoot() {
        clearUILoot();
        List<UIItem> uiItemList = new ArrayList<>();
        final int itemSize = 64;
        final int itemLabelOffsets = 8;
        final Color itemLabelColor = Color.YELLOW, outlineColor = Color.WHITE,
                hoverColor = new Color(255, 255, 255, 25);
        int padding = 2;
        int x = padding, y = itemSize / 8 + 10;

        for(Map.Entry entry : loot.entrySet()) {
            Item item = (Item) entry.getKey();
            long amt = ((MutableLong) entry.getValue()).get();
            UIItem uiItem = new UIItem(x, y, itemSize, (int) (itemSize / 1.1))
                    .setImage(item.getImage())
                    .setLabel(NumberFormat.getInstance(Locale.US).format(amt), null);
            if(Long.compare(amt, 10_000_000_000L) >= 0) {
                uiItem.labelColor = Color.CYAN;
                if(Long.compare(amt, 100_000_000_000L) >= 0)
                    uiItem.setLabel(Utils.getDigitsFromNum(amt, 0, 3) + "B", null);
                else
                    uiItem.setLabel(Utils.getDigitsFromNum(amt, 0, 2) + "B", null);
            } else if(amt > 10_000_000) {
                uiItem.labelColor = Color.GREEN;
                if(amt >= 1_000_000_000)
                    uiItem.setLabel(Utils.getDigitsFromNum(amt, 0, 4) + "M", null);
                else if(amt >= 100_000_000)
                    uiItem.setLabel(Utils.getDigitsFromNum(amt, 0, 3) + "M", null);
                else
                    uiItem.setLabel(Utils.getDigitsFromNum(amt, 0, 2) + "M", null);

            } else if(amt > 100_000) {
                uiItem.labelColor = Color.WHITE;
                if(amt >= 1_000_000)
                    uiItem.setLabel(Utils.getDigitsFromNum(amt, 0, 4) + "K", null);
                else
                    uiItem.setLabel(Utils.getDigitsFromNum(amt, 0, 3) + "K", null);
            } else
                uiItem.labelColor = itemLabelColor;
//            uiItem.outlineColor = outlineColor;
            uiItem.hoverColor = hoverColor;
            uiItem.setOnHover((MouseMovedEvent e) -> {
                boolean inBounds = uiItem.getBounds().contains(e.getX(), e.getY());
                if(!uiItem.isHovering && inBounds) {
                    uiItem.isHovering = true;
                }
                else if(uiItem.isHovering && !inBounds){
                    uiItem.isHovering = false;
                }
                return false;
            });
            uiItem.setHoverLabel(
                    item.name + " x " + NumberFormat.getInstance().format(amt) + "; " + NumberFormat.getInstance().format(item.individualPrice * amt) + " GP",
                    null
            );
            uiItem.setLabelOffsets(-itemLabelOffsets, -itemLabelOffsets / 2);
            uiItemList.add(uiItem);
            x += itemSize + padding;
            if(x + itemSize >= getWidth()) {
                y += itemSize / 1.1 + padding;
                x = padding;
            }
        }
        layerStack.addAll(uiItemList);
        return uiItemList;
    }

    private void sortLoot() {
        //sort loot by GE Value most expensive at beginning, along with non-tradeables.
        List<Map.Entry<Item, MutableLong>> unsortedLootList = new LinkedList<>(loot.entrySet());
        Collections.sort(unsortedLootList, lootSorter);
        Map<Item, MutableLong> sortedLootMap = new LinkedHashMap<>();
        for(Map.Entry<Item, MutableLong> entry : unsortedLootList) {
            sortedLootMap.put(entry.getKey(), entry.getValue());
        }
        loot = sortedLootMap;
    }

    private void clearUILoot() {
        if(uiLootItems != null) {
            uiLootItems.forEach(item -> layerStack.remove(item));
        }
    }

    public void update() {
        for(int i = 0; i < layerStack.size(); i++) {
            layerStack.get(i).onUpdate();
        }
    }

    public void draw() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        //enable smooth font rendering
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        //drawing to the screen..
        {
            g.setColor(bgColor);
            g.fillRect(0, 0, getWidth(), getHeight());
            for (int i = 0; i < layerStack.size(); i++) {
                layerStack.get(i).onDraw(g);
            }
            if(showFps)
                g.drawString("FPS: " + fps, getWidth() - 50, 15);
        }
        g.dispose();
        bs.show();
    }

    public void onEvent(Event event) {
        for(int i = layerStack.size() - 1; i > -1; i--) {
            layerStack.get(i).onEvent(event);
        }
    }

    int fps, frameCount;

    @Override
    public void run() {
        new Thread(() -> init(), "Osrs Drop Sim Loader").start();

        final double GAME_HERTZ = 15.0;
        final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
        //At the very most we will update the game this many times before a new render.
        final int MAX_UPDATES_BEFORE_RENDER = 10;
        double lastUpdateTime = System.nanoTime();
        double lastRenderTime = lastUpdateTime;

        //If we are able to get as high as this FPS, don't render again.
        final double TARGET_FPS = 60;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

        int lastSecondTime = (int) (lastUpdateTime / 1000000000);

        while (running)
        {
            double now = System.nanoTime();
            int updateCount = 0;

            //Do as many game updates as we need to, potentially playing catchup.
            while( now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER )
            {
                update();
                lastUpdateTime += TIME_BETWEEN_UPDATES;
                updateCount++;
            }

            //If for some reason an update takes forever, we don't want to do an insane number of catchups.
            if ( now - lastUpdateTime > TIME_BETWEEN_UPDATES)
            {
                lastUpdateTime = now - TIME_BETWEEN_UPDATES;
            }

            draw();
            frameCount++;
            lastRenderTime = now;


            //counting the fps..
            int thisSecond = (int) (lastUpdateTime / 1000000000);
            if (thisSecond > lastSecondTime)
            {
                fps = frameCount;
                frameCount = 0;
                lastSecondTime = thisSecond;
            }

            //Yield until it has been at least the target time between renders. This saves us from hogging the CPU.
            while ( now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                Thread.yield();

                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                }

                now = System.nanoTime();
            }
        }
    }

    public synchronized void start() {
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            running = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(() -> {
            try {
//                UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                UIManager.put("ToolTip.border", BorderFactory.createEmptyBorder());
            } catch (Exception e) {
                System.out.println("failed to initialize the given look and feel.");
            }
            new Window(WIDTH, HEIGHT, new GraphicApp());
        });
    }
}
