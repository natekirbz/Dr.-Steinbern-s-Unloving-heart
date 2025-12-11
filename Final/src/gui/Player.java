package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import visual.dynamic.described.RuleBasedSprite;
import visual.statik.TransformableContent;

/**
 * This work complies with the JMU Honor Code.
 * 
 * Player character that can move between tracks based on keyboard input.
 */
public class Player extends RuleBasedSprite implements KeyListener {
    private double left;
    private double top;
    private static final int[] TRACKS = { 100, 225, 350 }; // all 3 tracks
    private int current;

    /**
     * Constructor for Player.
     * 
     * @param content     the visual content of the player
     * @param stageWidth  the width of the stage
     * @param stageHeight the height of the stage
     */
    public Player(final TransformableContent content, final double stageWidth, final double stageHeight) {
        super(content);
        this.top = TRACKS[1];
        this.left = 100;
        this.setLocation(this.left, this.top);
        current = 1;
    }

    /**
     * Handle tick events to update player position.
     * 
     * @param time the current time tick
     */
    public void handleTick(int time) {
        this.setLocation(this.left, this.top);
    }

    /**
     * Handle key press events to move the player between tracks.
     * 
     * @param e the key event
     */
    @Override
    public void keyPressed(final KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            if (current == 1) {
                top = TRACKS[2];
                current = 2;
            } else if (current == 0) {
                top = TRACKS[1];
                current = 1;
            }
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            if (current == 1) {
                top = TRACKS[0];
                current = 0;
            } else if (current == 2) {
                top = TRACKS[1];
                current = 1;
            }

        }
    }

    /**
     * Handle key typed events (not used).
     * 
     * @param e the key event
     */
    @Override
    public void keyTyped(KeyEvent e) {
    
    }

    /**
     * Handle key released events (not used).
     * 
     * @param e the key event
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
