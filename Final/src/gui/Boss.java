package gui;

import java.util.Iterator;
import java.util.Random;

import visual.dynamic.described.RuleBasedSprite;
import visual.dynamic.described.Sprite;
import visual.statik.TransformableContent;

/**
 * Boss character that has multiple states and can take damage.
 * 
 * This work complies with the JMU Honor Code.
 */
public class Boss extends RuleBasedSprite{
    protected double maxX;
    protected double maxY;
    protected double speed;
    protected double x;
    protected double y;
    protected int lastTime;
    protected int millisPerState;
    protected int state;
    protected int stateChange;
    protected int timeInState;
    protected TransformableContent[] contents;
    private static final Random rng = new Random();
    private int health;
    private float grayFactor = 1.0f; 


    /**
     * Constructor for Boss.
     * 
     * @param contents the visual contents of the boss
     * @param width    the width of the stage
     * @param height   the height of the stage
     * @param health   the initial health of the boss
     */
    public Boss(final TransformableContent[] contents, final double width, final double height, final int health) {
        super(contents[0]);
        this.contents = contents;
        this.maxX = width;
        this.maxY = height;
        this.x = rng.nextDouble() * this.maxX;
        this.y = (double)rng.nextInt() * this.maxY;
        this.state = 0;
        this.lastTime = 0;
        this.timeInState = 0;
        this.stateChange = 1;
        this.health = health;
    }

    /**
     * Get the content of the boss.
     * 
     * @return the TransformableContent of the boss
     */
    @Override
    public TransformableContent getContent() {
      return this.contents[this.state];
    }

    /**
     * Inflict damage to the boss, reducing its health and updating its appearance.
     */
    public void damage() {
        this.health--;
    }



    /**
     * Handle tick events to update boss state and check for collisions.
     * 
     * @param time the current time tick
     */
    @Override
    public void handleTick(final int time) {
        Iterator<Sprite> i = this.antagonists.iterator();

        while(i.hasNext()) {
            Sprite shark = (Sprite)i.next();
            if (this.intersects(shark)) {
                this.speed = 20.0;
            }
        }

        this.millisPerState = 500 - (int)(this.speed * 20.0);
        this.timeInState += time - this.lastTime;
        if (this.timeInState > this.millisPerState) {
            this.timeInState = 0;
            this.state += this.stateChange;
            if (this.state == 2) {
                this.stateChange = -1;
            } else if (this.state == 0) {
                this.stateChange = 1;
            }
        }

        this.lastTime = time;
    }

}
