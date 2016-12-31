package graphics;

import graphics.events.types.button.ButtonRefreshEvent;
import graphics.events.types.button.ButtonSetEvent;
import graphics.events.types.keyboard.KeyPressedEvent;
import graphics.events.types.keyboard.KeyReleasedEvent;
import graphics.events.types.mouse.MouseMovedEvent;
import graphics.events.types.mouse.MousePressedEvent;
import graphics.events.types.mouse.MouseReleasedEvent;
import graphics.ui.UI;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.NumberFormatter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Andrew on 12/28/2016.
 */
public class Window extends JFrame {


    public Window(final int width, final int height, final GraphicApp app) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setTitle(GraphicApp.TITLE);

        //set the window icon.
        List<Image> icons = loadIconImages("icon/slayer_");
        this.setIconImages(icons);

        //add listeners to app...

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                MousePressedEvent event = new MousePressedEvent(e.getButton(), e.getX(), e.getY());
                app.onEvent(event);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                MouseReleasedEvent event = new MouseReleasedEvent(e.getButton(), e.getX(), e.getY());
                app.onEvent(event);
            }


            @Override
            public void mouseMoved(MouseEvent e) {
                MouseMovedEvent event = new MouseMovedEvent(e.getX(), e.getY(), false);
                app.onEvent(event);
            }
        };

        KeyAdapter keyboard = new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                KeyPressedEvent event = new KeyPressedEvent(e.getKeyCode());
                app.onEvent(event);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                KeyReleasedEvent event = new KeyReleasedEvent(e.getKeyCode());
                app.onEvent(event);
            }
        };


        app.addMouseMotionListener(mouse);
        app.addMouseListener(mouse);

        this.add(app, BorderLayout.CENTER);

        //setup side panel
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weighty = 0;
        constraints.weightx = 1;
//        Box box = new Box(BoxLayout.);
        JTextField npcSearch = new JTextField("", 1);
        npcSearch.setFont(UI.DEFAULT_FONT);
        FontMetrics fontMetrics = npcSearch.getFontMetrics(UI.DEFAULT_FONT);
        Rectangle2D maxFontBounds = fontMetrics.getMaxCharBounds(npcSearch.getGraphics());
        npcSearch.setPreferredSize(new Dimension(100, (int) Math.floor(maxFontBounds.getHeight() + maxFontBounds.getCenterY())));
        npcSearch.setMinimumSize(npcSearch.getPreferredSize());
        npcSearch.setToolTipText("Type the name of an NPC.");

        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = npcSearch.getPreferredSize().height;
        constraints.gridx = 0;
        constraints.gridy = 0;

        panel.add(npcSearch, constraints);

        NumberFormat killFormat = NumberFormat.getInstance();
        NumberFormatter killFormatter = new NumberFormatter(killFormat);
        killFormatter.setValueClass(Integer.class);
        killFormatter.setMinimum(0);
        killFormatter.setMaximum(100_000_000);
        killFormatter.setAllowsInvalid(true);
        // If you want the value to be committed on each keystroke instead of focus lost
        killFormatter.setCommitsOnValidEdit(true);
        JFormattedTextField killsField = new JFormattedTextField(killFormatter);
        killsField.setToolTipText("Amount of times to kill the NPC.");

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;

        panel.add(killsField, constraints);


        JButton setBtn = new JButton("Set Loot");
        setBtn.setToolTipText("Change the loot to the NPC\n from the above fields.");

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;

        panel.add(setBtn, constraints);

        JButton refresh = new JButton("Refresh Loot");
        refresh.setToolTipText("Rerolls every drop for the currently set NPC.");
        refresh.setFont(UI.DEFAULT_FONT);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.ipady = 1;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weighty = 1;

        panel.add(refresh, constraints);

        Set<KeyStroke> traversalKeySet = new HashSet<>();
        traversalKeySet.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        traversalKeySet.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
        npcSearch.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, traversalKeySet);
        killsField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, traversalKeySet);

        setBtn.addActionListener((ActionEvent e) -> {
            //called when button is clicked.
            NumberFormat format = (NumberFormat) ((NumberFormatter) killsField.getFormatter()).getFormat();
            int kills;
            try {
                kills = format.parse(killsField.getText()).intValue();
            } catch (ParseException ex) {
                kills = 1;
            }
            ButtonSetEvent event = new ButtonSetEvent(npcSearch.getText(), kills);
            app.onEvent(event);
        });

        refresh.addActionListener((ActionEvent e) -> {
            //called when the button is clicked.
            ButtonRefreshEvent event = new ButtonRefreshEvent();
            app.onEvent(event);
        });

        this.add(panel, BorderLayout.EAST);
        this.pack();
        this.setAutoRequestFocus(true);
        this.setVisible(true);

        app.start();
    }

    private List<Image> loadIconImages(String path) {
        List<Image> images = new ArrayList<>();
        for(int i = 16; i < 512; i *= 2) {
            try {
                images.add(ImageIO.read(getClass().getClassLoader().getResourceAsStream(path + i + ".png")));
            } catch (IOException e) {
                continue;
            }
        }
        return images;
    }
}
