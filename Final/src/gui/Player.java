package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import visual.dynamic.described.RuleBasedSprite;
import visual.statik.TransformableContent;

public class Player extends RuleBasedSprite implements KeyListener{
    private double left;
    private double top;
    private static final int[] TRACKS = {0, 150, 300}; // all 3 tracks
    private int current;


    public Player(TransformableContent content, double stageWidth, double stageHeight) {
        super(content);
        Rectangle2D bounds = content.getBounds2D(false);
        this.top = TRACKS[1];
        this.left = 100;
        this.setLocation(this.left, this.top);
        current = 1;
    }

    public void handleTick(int time) {
        this.setLocation(this.left, this.top);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
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

    @Override
    public void keyReleased(KeyEvent e) {
        //do nothing
    }
}
