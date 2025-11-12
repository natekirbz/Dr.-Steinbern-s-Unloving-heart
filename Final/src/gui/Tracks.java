package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;

import io.ResourceFinder;
import visual.dynamic.described.RuleBasedSprite;
import visual.dynamic.described.SampledSprite;
import visual.dynamic.described.Stage;
import visual.statik.described.AggregateContent;
import visual.statik.sampled.Content;
import visual.statik.sampled.ContentFactory;
import visual.dynamic.described.*;
import resources.Marker;
import java.awt.Shape;


public class Tracks extends Stage implements KeyListener{
	private RuleBasedSprite player;
    private int lane = 1; // 0 = left, 1 = middle, 2 = right
    private int[] laneX = {0, 150, 300}; // x-coordinates for each lane
    private ResourceFinder jarFinder;

    public Tracks() {
        super(60);
        addKeyListener(this);

        jarFinder = ResourceFinder.createInstance(new Marker());
        ContentFactory contentFactory = new ContentFactory(jarFinder);
        
        // Player setup
        
        Content logo = contentFactory.createContent("bern.png", 3);
        player = new RuleBasedSprite(logo);
        
        player.addKeyTime(1, new Point2D.Double(0.0, 0.0), 0.0, 1.0, logo);
        player.addKeyTime(10, new Point2D.Double(0.0, 0.0), 0.0, 1.0, null);


        player.setLocation(laneX[lane], 300);
        add(player);
        
        start();
        setRestartTime(500);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && lane > 0) {
            lane--;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && lane < 2) {
            lane++;
        }
        player.setLocation(laneX[lane], 500);
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

}
