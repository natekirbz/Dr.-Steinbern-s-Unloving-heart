package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
 
import visual.dynamic.described.RuleBasedSprite;
import visual.statik.described.AggregateContent;
import visual.statik.described.Content;



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

    public HealthBar(double width, double height, AggregateContent ac) {
        super(ac);
        this.currentWidth = (float) width;
        // use the AggregateContent that was passed in (it's the content attached to the sprite)
        this.aggregateContent = ac;
        this.width = width;
        this.height = height;
        // create the initial bar content and keep a reference so we can update it on hits
        barContent = new Content(new Rectangle2D.Float(0, 0, currentWidth, barHeight), Color.BLACK, fillColor, new BasicStroke());
        barContent.setLocation(0, 0);
        aggregateContent.add(barContent);
    }

    public static AggregateContent createAggregateContent() {
        Color black = Color.BLACK;
        Color red = Color.red;
        BasicStroke stroke = new BasicStroke();

        Content bar = new Content(new Rectangle2D.Float(0, 0, 150, 20), black, red, stroke);

        AggregateContent ac = new AggregateContent();
        ac.add(bar);
        return ac;
    }

    public Shape getRedBarShape() {
        return new Rectangle2D.Float(0, 0, 150, 20);
    }

    // Shrink from the right by using scale
    public void shrink(double amount) {
        currentWidth -= amount;
        System.out.println(getAlive());
        // Create the remaining red health bar
        Content redBar = new Content(new Rectangle2D.Float(0, 0, currentWidth, barHeight), Color.BLACK, Color.RED, new BasicStroke());
        // Create the black (lost health) bar on the right
        Content blackBar = new Content(new Rectangle2D.Float(currentWidth, 0, (float) amount, barHeight), Color.BLACK, Color.BLACK, new BasicStroke());
        aggregateContent.add(redBar);
        aggregateContent.add(blackBar);
    }

    public boolean getAlive() {
        return currentWidth >= 0;
    }

    @Override
    public void handleTick(int arg0) {
        //do
    }
}
