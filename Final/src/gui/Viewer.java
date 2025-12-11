package gui;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import app.JApplication;
import io.ResourceFinder;
import resources.Marker;
import visual.VisualizationView;
import visual.dynamic.described.Stage;
import visual.statik.sampled.Content;
import visual.statik.sampled.ContentFactory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Viewer - main UI for the game.
 *
 * Cleaned up: removed magic numbers/strings, added named constants and
 * short student-style comments to explain intent. Functionality is preserved.
 */
public class Viewer extends JApplication implements ActionListener {

    /*
     * ---------------------------------------------------------------------
     * Window & layout constants (no magic numbers in method bodies)
     * ------------------------------------------------------------------
     */
    public static final int WIDTH = 800;
    public static final int HEIGHT = 450;

    private static final int TITLE_FONT_SIZE = 50;
    private static final int BUTTON_FONT_SIZE = 30;
    private static final int END_FONT_SIZE = 30;

    private static final int START_BTN_X = 90;
    private static final int START_BTN_Y = 300;
    private static final int START_BTN_W = 300;
    private static final int START_BTN_H = 100;

    private static final int CHAR_BTN_W = 150;
    private static final int CHAR_BTN_H = 60;

    private static final int CHARACTER_SELECT_BTN_X = 400;
    private static final int CHARACTER_SELECT_BTN_Y = 300;

    private static final int VIEW_X = 0;
    private static final int VIEW_Y = 0;

    /* audio / asset constants */
    private static final String AUDIO_DEMO = "demo-music.wav";
    private static final String AUDIO_THEME = "Castle-theme.wav";
    private static final String AUDIO_END = "The Emperor.wav";
    private static final String AUDIO_BUTTON = "Button-Press.wav";
    private static final String AUDIO_CHAR_SELECT = "character-select.wav";

    private static final String RESOURCE_PREFIX = "/resources/";
    private static final String DEFAULT_CHARACTER = "dolphin.png";

    /* end-screen images (named constants instead of literals scattered) */
    private static final String END_IMAGE_LOSE = "bernstein2.jpg";
    private static final String END_IMAGE_BOSS = "bernstein1.jpg";

    /* characters available on the character select screen */
    private static final String[] CHARACTERS = { "Dolphin", "Sheep", "Llama" };

    /* timers */
    private static final int HEALTH_CHECK_DELAY_MS = 50;

    /* ------------------------------------------------------------------ */

    // Main stage used by the game
    public Stage stage;

    // map of character selection buttons so we can replace a chosen button with
    // Confirm
    private final Map<String, JButton> characterBtns;

    // action command constants (avoid string literals in switch)
    protected static final String START = "Start";
    protected static final String CHARACTER = "Character";
    protected static final String CONFIRM = "Confirm";
    protected static final String EXIT = "Exit";

    // fonts (constructed once)
    private final Font titleFont = new Font("Arial", Font.BOLD | Font.ITALIC, TITLE_FONT_SIZE);
    private final Font buttonFont = new Font("Arial", Font.BOLD | Font.ITALIC, BUTTON_FONT_SIZE);
    private final Font endFont = new Font("Arial", Font.BOLD, END_FONT_SIZE);

    // simple audio clip store so multiple clips don't overlap unexpectedly
    private static final HashMap<Clip, Integer> audioClips = new HashMap<>();

    /**
     * Construct the Viewer. Use a default stage initialized with the default
     * character.
     */
    public Viewer(final String[] args) {
        super(WIDTH, HEIGHT);
        // initialize the stage with a character (kept identical to original behavior)
        this.stage = new Tracks(DEFAULT_CHARACTER);
        this.characterBtns = new HashMap<>();
    }

    /**
     * Central action handler for UI buttons.
     * 
     * @param evt the ActionEvent triggered by a button press
     */
    @Override
    public void actionPerformed(final ActionEvent evt) {
        String action = evt.getActionCommand();
        switch (action) {
            case START:
                handleStart();
                playButtonAudio(AUDIO_BUTTON);
                break;

            case CHARACTER:
                handleCharacter();
                playButtonAudio(AUDIO_BUTTON);
                break;

            // character selection actions (buttons labelled by character names)
            case "Dolphin":
            case "Sheep":
            case "Llama":
                setCharacter(action);
                playButtonAudio(AUDIO_BUTTON);
                break;

            case CONFIRM:
                startWindow();
                playButtonAudio(AUDIO_BUTTON);
                break;

            case EXIT:
                System.exit(0);
                break;

            default:
                // no-op for unrecognized commands
                break;
        }
    }

    /**
     * Replace the selected character button with a Confirm button and update the
     * stage.
     * 
     * @param action the character name selected
     */
    private void setCharacter(final String action) {
        String characterFile = action + ".png";
        // create a fresh stage for the selected character (same functionality as
        // before)
        this.stage = new Tracks(characterFile);

        // update the character selection UI to show "Confirm"
        handleCharacter();

        JButton tempButton = characterBtns.get(action);
        Rectangle bounds = tempButton.getBounds();

        // remove old button from the content pane
        getContentPane().remove(tempButton);

        // create a Confirm button in the same place and add it to the UI
        JButton confirmButton = createButton(CONFIRM, bounds.x, bounds.y, bounds.width,
            bounds.height, buttonFont);
        characterBtns.put(action, confirmButton);
        getContentPane().add(confirmButton);

        // show the chosen character image next to the controls
        viewCharacter(characterFile);

        // refresh the UI so changes appear immediately
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    /*
     * ------------------------------------------------------------------
     * Main Start Window (title + buttons)
     * ------------------------------------------------------------------
     */

    /**
     * Build and display the main start window.
     */
    public void startWindow() {
        JPanel contentPane = resetContentPane();
        contentPane.setLayout(null);

        JLabel title = new JLabel("Dr. Steinbern's Unloving Heart");
        title.setFont(titleFont);
        title.setBounds(50, 50, 1500, 200); // wide bounding box so long title fits
        contentPane.add(title);

        // Start Button
        JButton startBtn = createButton(START, START_BTN_X, START_BTN_Y, START_BTN_W,
            START_BTN_H, titleFont);
        contentPane.add(startBtn);

        // Character Select Button
        JButton characterBtn = createButton(CHARACTER, CHARACTER_SELECT_BTN_X,
            CHARACTER_SELECT_BTN_Y, START_BTN_W,
                START_BTN_H, titleFont);
        contentPane.add(characterBtn);

        // background audio for the start screen
        playAudio(AUDIO_DEMO);
    }

    /*
     * ------------------------------------------------------------------
     * Game start: show the stage inside the content pane
     * ------------------------------------------------------------------
     */

    /**
     * Replace the current UI with the stage view and start the game's stage loop.
     * This mirrors the original behavior (Tracks.resetTracks called first to ensure
     * a fresh start when returning from other screens).
     */
    public void handleStart() {
        JPanel cp = resetContentPane();

        // ensure tracks are reset (robust reset implementation lives in Tracks)
        if (stage instanceof Tracks) {
            ((Tracks) stage).resetTracks();
        }

        VisualizationView view = stage.getView();
        view.setBounds(VIEW_X, VIEW_Y, WIDTH, HEIGHT);
        cp.add(view);

        // Start the stage loop and play background music
        stage.start();
        playAudio(AUDIO_THEME);

        // Restart the health & death monitors for this play session.
        // The timers are stopped when a game ends, so we must recreate them here.
        startHealthMonitor();
        startDeathMonitor();
    }

    /*
     * ------------------------------------------------------------------
     * End / lose screens
     * ------------------------------------------------------------------
     */

    /**
     * Show the end-screen with a given image and two buttons.
     * This preserves previous functionality while making layout constants explicit.
     * 
     * @param imageName the image file to show on the end screen
     */
    public void endScreen(final String imageName) {
        if (stage != null) {
            stage.stop();
        }

        JPanel cp = resetContentPane();
        cp.setLayout(null);
        playAudio(AUDIO_END);

        // layered pane so stage + UI overlays can coexist
        JLayeredPane layers = new JLayeredPane();
        layers.setBounds(0, 0, WIDTH, HEIGHT);
        cp.add(layers);

        // Game Over label with local IP (useful for demos)
        JLabel gameOver = new JLabel();
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = localHost.getHostAddress();
            gameOver.setText("Game Over - " + ipAddress);
        } catch (UnknownHostException e) {
            gameOver.setText("Game Over");
        }
        gameOver.setFont(endFont);
        gameOver.setForeground(Color.WHITE);
        gameOver.setBounds(200, 20, 600, 50);

        // create a simple stage to show the end image
        Stage loseStage = new Stage(20);
        VisualizationView view = loseStage.getView();
        view.setBounds(0, 0, WIDTH, HEIGHT);

        // Buttons for replay / exit
        JButton startBtn = createButton(START, 20, 300, CHAR_BTN_W, CHAR_BTN_H, buttonFont);
        JButton exitBtn = createButton(EXIT, 20, 350, CHAR_BTN_W, CHAR_BTN_H, buttonFont);

        // add layers in order
        layers.add(view, JLayeredPane.DEFAULT_LAYER);
        layers.add(gameOver, JLayeredPane.PALETTE_LAYER);
        layers.add(startBtn, JLayeredPane.PALETTE_LAYER);
        layers.add(exitBtn, JLayeredPane.PALETTE_LAYER);

        // load the end image into the temporary stage
        ResourceFinder finder = ResourceFinder.createInstance(Marker.class);
        ContentFactory factory = new ContentFactory(finder);
        Content content = factory.createContent(imageName);

        loseStage.add(content);
        loseStage.start();
    }

    /*
     * ------------------------------------------------------------------
     * Character selection UI
     * ------------------------------------------------------------------
     */

    /**
     * Utility to show a character portrait next to the selection buttons.
     * 
     * @param characterName the image file name of the character to show
     */
    public void viewCharacter(final String characterName) {
        try (InputStream in = getClass().getResourceAsStream(RESOURCE_PREFIX + characterName)) {
            if (in == null) {
                // resource not found - fail gracefully
                return;
            }
            BufferedImage myPicture = ImageIO.read(in);
            // scaleHint is a hint only; we keep same size as before
            JLabel picLabel = new JLabel(new ImageIcon(myPicture));
            picLabel.setBounds(420, 50, 300, 300);
            getContentPane().add(picLabel);

            getContentPane().revalidate();
            getContentPane().repaint();

        } catch (IOException e) {
            // Print stack trace for student-level debugging; keep behavior otherwise
            // unchanged
            e.printStackTrace();
        }
    }

    /**
     * Build the character selection screen using the CHARACTERS constant array.
     */
    public void handleCharacter() {
        JPanel contentPane = resetContentPane();
        contentPane.setLayout(null);

        JLabel label = new JLabel("Choose your character");
        label.setFont(buttonFont);
        label.setBounds(50, 50, 800, 100);
        contentPane.add(label);

        // place character buttons with some spacing
        int startX = 20;
        int spacing = 180;
        for (int i = 0; i < CHARACTERS.length; i++) {
            String name = CHARACTERS[i];
            JButton btn = createButton(name, startX + i * spacing, 300, CHAR_BTN_W,
                CHAR_BTN_H, buttonFont);
            characterBtns.put(name, btn);
            contentPane.add(btn);
        }

        playAudio(AUDIO_CHAR_SELECT);
    }

    /*
     * ------------------------------------------------------------------
     * Utility methods
     * ------------------------------------------------------------------
     */

    /**
     * Remove everything from the content pane and return it so callers can
     * re-populate it.
     */
    private JPanel resetContentPane() {
        JPanel cp = (JPanel) getContentPane();
        cp.removeAll();
        cp.revalidate();
        cp.repaint();
        return cp;
    }

    /**
     * Create a JButton with standard properties and register this class as its
     * listener.
     * 
     * @param text the button label
     * @param x    the x position
     * @param y    the y position
     * @param w    the button width
     * @param h    the button height
     * @param font the button font
     * 
     * @return the constructed JButton
     */
    private JButton createButton(String text, int x, int y, int w, int h, Font font) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, w, h);
        btn.setFont(font);
        btn.addActionListener(this);
        return btn;
    }

    /**
     * Lifecycle init: show start window and begin monitors for health/boss states.
     */
    @Override
    public void init() {
        startWindow();
        startHealthMonitor();
        startDeathMonitor();
    }

    /**
     * Poll the player's health; when it reaches zero show the lose screen.
     * Timer frequency is defined by HEALTH_CHECK_DELAY_MS constant.
     */
    public void startHealthMonitor() {
        Timer timer = new Timer(HEALTH_CHECK_DELAY_MS, e -> {
            if (stage instanceof Tracks && !((Tracks) stage).isAlive()) {
                ((Timer) e.getSource()).stop();
                endScreen(END_IMAGE_LOSE);
            }
        });
        timer.start();
    }

    /**
     * Poll the boss's health; when boss dies show the boss-loss screen.
     */
    public void startDeathMonitor() {
        Timer timer = new Timer(HEALTH_CHECK_DELAY_MS, e -> {
            if (stage instanceof Tracks && !((Tracks) stage).bossAlive()) {
                ((Timer) e.getSource()).stop();
                endScreen(END_IMAGE_BOSS);
            }
        });
        timer.start();
    }

    /**
     * Play audio via ResourceFinder. Clips are stored so older clips are stopped
     * before new ones are stored (keeps audio behavior consistent with original).
     */
    public static void playAudio(final String audioFile) {
        ResourceFinder finder = ResourceFinder.createInstance(Marker.class);
        InputStream raw = finder.findInputStream(audioFile);
        try (BufferedInputStream buf = new BufferedInputStream(raw);
                AudioInputStream audio = AudioSystem.getAudioInputStream(buf)) {
            Clip clip = AudioSystem.getClip();
            storeClip(clip);
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Replace any currently playing clip(s) and remember the latest clip.
     * 
     * @param clip the Clip to store
     * 
     */
    private static void storeClip(final Clip clip) {
        for (Clip c : audioClips.keySet()) {
            c.stop();
        }
        audioClips.put(clip, 1);
    }

    /**
     * Play a short button sound without replacing the demo/looping music.
     * 
     * @param audioFile the audio file to play
     */
    public static void playButtonAudio(final String audioFile) {
        ResourceFinder finder = ResourceFinder.createInstance(Marker.class);
        InputStream raw = finder.findInputStream(audioFile);
        try (BufferedInputStream buf = new BufferedInputStream(raw);
                AudioInputStream audio = AudioSystem.getAudioInputStream(buf)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
