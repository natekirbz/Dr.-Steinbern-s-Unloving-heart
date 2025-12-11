package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import visual.dynamic.described.RuleBasedSprite;
import visual.statik.described.AggregateContent;
import visual.statik.described.Content;
/**
 * This work complies with the JMU Honor Code.
 * 
 * Health bar representation for the player.
 */
public class HealthBar extends RuleBasedSprite {

    private float currentWidth;
    private final float maxWidth = 150f;
    private final float barHeight = 20f;
    private AggregateContent aggregateContent;
    // hit tracking and color state
    private int hitCount = 0;
    private final int maxHits = 10;
    private Color fillColor = Color.RED;
    private Content barContent;
    private double width;
    private double height;

    /**
     * Constructor for HealthBar.
     * 
     * @param width
     * @param height
     * @param ac
     */
    public HealthBar(final double width, final double height, final AggregateContent ac) {
        super(ac);
        this.currentWidth = (float) width;
        // use the AggregateContent that was passed in (it's the content attached to the
        // sprite)
        this.aggregateContent = ac;
        this.width = width;
        this.height = height;
        // create the initial bar content and keep a reference so we can update it on
        // hits
        barContent = new Content(new Rectangle2D.Float(0, 0, currentWidth, barHeight), Color.BLACK, fillColor,
                new BasicStroke());
        barContent.setLocation(0, 0);
        aggregateContent.add(barContent);
    }

    /**
     * Create the AggregateContent for the HealthBar.
     * 
     * @return the AggregateContent representing the health bar
     */
    public static AggregateContent createAggregateContent() {
        Color black = Color.BLACK;
        Color red = Color.red;
        BasicStroke stroke = new BasicStroke();

        Content bar = new Content(new Rectangle2D.Float(0, 0, 150, 20), black, red, stroke);

        AggregateContent ac = new AggregateContent();
        ac.add(bar);
        return ac;
    }

    /**
     * Get the shape of the red health bar.
     * 
     * @return the Shape of the red health bar
     */
    public Shape getRedBarShape() {
        return new Rectangle2D.Float(0, 0, 150, 20);
    }

    /**
     * Shrink the health bar by the specified amount.
     * 
     * @param amount the amount to shrink the health bar
     */
    public void shrink(final double amount) {
        // clamp amount so we don't go below zero
        float amt = (float) amount;
        if (amt < 0)
            return;
        float prevWidth = currentWidth;
        currentWidth = Math.max(0f, currentWidth - amt);

        // remove previous visuals and recreate so we don't accumulate many Content
        // objects
        try {
            aggregateContent.remove(barContent);
        } catch (Exception e) {
            // some AggregateContent implementations may not support clear(); fall back to
            // adding
        }

        // Create the remaining red health bar
        Content redBar = new Content(new Rectangle2D.Float(0, 0, currentWidth, barHeight), Color.BLACK, Color.RED,
                new BasicStroke());
        // Create the black (lost health) bar on the right (only if some was lost)
        if (currentWidth < prevWidth) {
            Content blackBar = new Content(new Rectangle2D.Float(currentWidth, 0, prevWidth - currentWidth, barHeight),
                    Color.BLACK, Color.BLACK, new BasicStroke());
            aggregateContent.add(blackBar);
        }
        aggregateContent.add(redBar);
    }

    public boolean getAlive() {
        return currentWidth > 0;
    }

    // New method: restore bar to full width/initial visuals
    public void reset() {
        // restore width & state
        this.currentWidth = (float) this.width;
        this.hitCount = 0;
        this.fillColor = Color.RED;

        // clear old visuals and recreate the initial bar content
        try {
            aggregateContent.remove(barContent);
        } catch (Exception e) {
            // ignore if not supported
        }

        barContent = new Content(new Rectangle2D.Float(0, 0, currentWidth, barHeight), Color.BLACK, fillColor,
                new BasicStroke());
        barContent.setLocation(0, 0);
        aggregateContent.add(barContent);
    }

    @Override
    public void handleTick(final int arg0) {
        // do
    }
}
