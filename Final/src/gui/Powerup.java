package gui;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import visual.dynamic.described.RuleBasedSprite;
import visual.dynamic.described.Sprite;
import visual.statik.TransformableContent;

/**
 * A rarer variant of Enemy. Behavior mirrors `Enemy` but provides a
 * `shouldSpawn()` helper so spawners can decide to create this type less often.
 */
public class Powerup extends RuleBasedSprite {
   private double x;      // horizontal position
   private double y;      // vertical position (track)
   private double speed;
   private int stageWidth;
   private int stageHeight;
   private int hitCount;
   private static final Random rng = new Random();
   private HealthBar healthBar; // reference to the player's health

   private static final int[] TRACKS = {100, 225, 350}; // all 3 tracks

   private TransformableContent content;
   private int imageIndex;

   public Powerup(TransformableContent content, int initialIndex, double stageWidth, double stageHeight, HealthBar hb) {
      super(content);

      this.content = content;
      this.imageIndex = initialIndex;

      Rectangle2D bounds = content.getBounds2D(false);
      this.stageWidth = (int) stageWidth;
      this.stageHeight = (int) stageHeight;
      this.healthBar = hb;

      // choose a track
      this.y = TRACKS[rng.nextInt(TRACKS.length)];
      this.x = stageWidth + bounds.getWidth();

      // pretty slow
      this.speed = 3;

      this.setLocation(x, y);
   }

   @Override
   public void handleTick(int time) {
      x -= speed;
      Sprite player = null;

      if (this.antagonists.size() > 0) {
         player = (Sprite)this.antagonists.get(0);
      }

      if (player != null && this.intersects(player)) {
         //do damage to heart
         healthBar.shrink(10);
      //   System.out.print("baloon");
        Rectangle2D bounds = getContent().getBounds2D(false);
         x = stageWidth + bounds.getWidth();
         y = TRACKS[rng.nextInt(TRACKS.length)];
         speed += (int)(Math.random() * 2);
      }

      if (x < -50) {
         Rectangle2D bounds = getContent().getBounds2D(false);
         x = stageWidth + bounds.getWidth();
         y = TRACKS[rng.nextInt(TRACKS.length)];
         speed += (int)(Math.random() * 2);
         // pick a different image index each time we wrap
      }
      setLocation(x, y);
   }

   @Override
   public TransformableContent getContent() {
      return super.getContent();
   }
}
