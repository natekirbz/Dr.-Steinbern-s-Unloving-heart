package gui;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import visual.dynamic.described.RuleBasedSprite;
import visual.dynamic.described.Sprite;
import visual.statik.TransformableContent;

/**
 * Enemy character that moves towards the player and affects the player's health
 * on collision.
 * 
 * This work complies with the JMU Honor Code.
 */
public class Enemy extends RuleBasedSprite {
   private double x;      // horizontal position
   private double y;      // vertical position (track)
   private double speed;
   private int stageWidth;
   private int stageHeight;
   private int hitCount;  //hits 4 times at first for some reason
   private static final Random rng = new Random();
   private HealthBar healthBar; // reference to the player's health

   
   private static final int[] TRACKS = {100, 225, 350}; // all 3 tracks
   
   private TransformableContent[] contents;
   private int imageIndex;

   /**
    * Constructor for Enemy.
    * 
    * @param contents     the visual contents of the enemy
    * @param initialIndex the initial image index for the enemy
    * @param stageWidth   the width of the stage
    * @param stageHeight  the height of the stage
    * @param hb           the health bar to affect on collision
    */
   public Enemy(final TransformableContent[] contents, final int initialIndex, final double stageWidth, final double stageHeight, final HealthBar hb) {
      super(contents[initialIndex]);

      this.contents = contents;
      this.imageIndex = initialIndex;

      Rectangle2D bounds = contents[initialIndex].getBounds2D(false);
      this.stageWidth = (int) stageWidth;
      this.stageHeight = (int) stageHeight;
      this.healthBar = hb;

      //choose a track
      this.y = TRACKS[rng.nextInt(TRACKS.length)];
      this.x = stageWidth + bounds.getWidth();
      this.speed = 5;
      this.setLocation(x, y);
   }

   /**
    * Handle tick events to update enemy position and check for collisions.
    * 
    * @param time the current time tick
    */
   @Override
   public void handleTick(final int time) {
      x -= speed;
      Sprite player = null;

      if (this.antagonists.size() > 0) {
         player = (Sprite)this.antagonists.get(0);
      }

      if (player != null && this.intersects(player)) {
         hitCount++;
         if (hitCount > 4) {
            // System.out.println("hit" + hitCount + " " + this.imageIndex);
            healthBar.shrink(4);
         }
      }

      if (x < -50) {
            Rectangle2D bounds = getContent().getBounds2D(false);
            x = stageWidth + bounds.getWidth();
            y = TRACKS[rng.nextInt(TRACKS.length)];
            speed += (int)(Math.random() * 7);
            // pick a different image index each time we wrap
            if (this.contents != null && this.contents.length > 1) {
               int next = this.imageIndex;
               while (next == this.imageIndex) {
                  next = rng.nextInt(this.contents.length);
               }
               this.imageIndex = next;
            }
      }
      setLocation(x, y);
   }

   /**
    * Get the content of the enemy.
    * 
    * @return the TransformableContent of the enemy
    */
   @Override
   public TransformableContent getContent() {
      if (this.contents != null && this.contents.length > 0) {
         return this.contents[this.imageIndex];
      }
      return super.getContent();
   }
}
