package gui;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import visual.dynamic.described.RuleBasedSprite;
import visual.statik.TransformableContent;

public class Enemy extends RuleBasedSprite {
   private double x;      // horizontal position
   private double y;      // vertical position (track)
   private double speed;
   private int stageWidth;
   private int stageHeight;
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

      if (x < -50) {
            Rectangle2D bounds = getContent().getBounds2D(false);
            x = stageWidth + bounds.getWidth();
            y = TRACKS[rng.nextInt(TRACKS.length)];
            speed = 7;
      }
      setLocation(x, y);
   }
}
