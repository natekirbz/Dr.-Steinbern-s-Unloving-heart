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

    public HealthBar(double width, double height, AggregateContent ac) {
        super(ac);
        this.currentWidth = (float) width;
        // use the AggregateContent that was passed in (it's the content attached to the sprite)
        this.aggregateContent = ac;
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
        // Update the bar's shape
        Content bar = new Content(new Rectangle2D.Float(0, 0, currentWidth, barHeight), Color.BLACK, Color.RED, new BasicStroke());
        aggregateContent.add(bar);
    }

    public boolean getAlive() {
        return currentWidth >= 0;
    }

    @Override
    public void handleTick(int arg0) {
        //do
    }
}
