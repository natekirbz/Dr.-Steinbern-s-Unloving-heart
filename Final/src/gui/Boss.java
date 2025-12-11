package gui;

import java.util.Iterator;
import java.util.Random;

import visual.dynamic.described.RuleBasedSprite;
import visual.dynamic.described.Sprite;
import visual.statik.TransformableContent;

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

    public TransformableContent getContent() {
      return this.contents[this.state];
    }

    public void damage() {
        this.health--;

        // reduce saturation by 10% per hit
        grayFactor -= 0.1f;
        if (grayFactor < 0f) grayFactor = 0f;

        applyGrayTransform();
    }

    private void applyGrayTransform() {
        
    }



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
