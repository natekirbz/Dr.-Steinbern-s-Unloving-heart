package gui;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import visual.dynamic.described.RuleBasedSprite;
import visual.dynamic.described.Sprite;
import visual.statik.TransformableContent;

public class Enemy extends RuleBasedSprite {
   private double x;      // horizontal position
   private double y;      // vertical position (track)
   private double speed;
   private int stageWidth;
   private int stageHeight;
   private int hitCount;  //hits 4 times at first for some reason
   private static final Random rng = new Random();
   
   private static final int[] TRACKS = {50, 200, 350}; // all 3 tracks

   public Enemy(TransformableContent content, double stageWidth, double stageHeight) {
      super(content);

      Rectangle2D bounds = content.getBounds2D(false);
      this.stageWidth = (int) stageWidth;
      this.stageHeight = (int) stageHeight;

      //choose a track
      this.y = TRACKS[rng.nextInt(TRACKS.length)];
      this.x = stageWidth + bounds.getWidth();

      this.speed = 5;

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
         hitCount++;
         if (hitCount > 4) {
            System.out.println("hit" + hitCount);
         }
      }


      if (x < -50) {
            Rectangle2D bounds = getContent().getBounds2D(false);
            x = stageWidth + bounds.getWidth();
            y = TRACKS[rng.nextInt(TRACKS.length)];
            speed = 7;
      }
      setLocation(x, y);
   }
}
