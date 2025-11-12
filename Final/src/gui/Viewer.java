package gui;

import java.awt.Color;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JPanel;

import java.awt.Image;

import app.JApplication;
import visual.VisualizationView;
import visual.dynamic.described.Stage;
import visual.statik.sampled.ContentFactory;
import visual.statik.sampled.ImageFactory;
import gui.*;
import io.ResourceFinder;
import resources.Marker;

import java.awt.Point;
import visual.statik.sampled.TransformableContent;

public class Viewer extends JApplication
{
	public static final int HEIGHT = 450; //divisible by 3 150px tracks
	public static final int WIDTH = 800;

	public Viewer(final String[] args) {
		super(WIDTH, HEIGHT);
	}
	
	public static void main(final String[] args) throws IOException
	  {
	    JApplication app = new Viewer(args);
	    invokeInEventDispatchThread(app);
	  }

	@Override
	public void init() {
		ResourceFinder finder = ResourceFinder.createInstance(Marker.class);
      ContentFactory tcFactory = new ContentFactory(finder);
      int stageWidth = 640;
      int stageHeight = 480;
      Stage stage = new Stage(50);
      VisualizationView stageView = stage.getView();
      Toolkit toolkit = stageView.getToolkit();
      ImageFactory imageFactory = new ImageFactory(finder);
      Image cursor = imageFactory.createBufferedImage("blankcursor.png", 4);
      TransformableContent content = tcFactory.createContent("roof.png", 3, false);
      stage.add(content);
      stageView.setBounds(0, 0, stageWidth, stageHeight);
      stageView.setCursor(toolkit.createCustomCursor(cursor, new Point(0, 0), "Blank"));
      content = tcFactory.createContent("cupola.png", 4, false);
      Cupola cupola = new Cupola(content, (double)stageWidth, (double)stageHeight);
      stage.add(cupola);
      stage.addMouseMotionListener(cupola);

      for(int i = 0; i < 4; ++i) {
         TransformableContent goldContent = tcFactory.createContent("balloon-gold.png", 4, false);
         TransformableContent purpleContent = tcFactory.createContent("balloon-purple.png", 4, false);
         Balloon gold = new Balloon(goldContent, (double)stageWidth, (double)stageHeight);
         gold.addAntagonist(cupola);
         stage.add(gold);
         Balloon purple = new Balloon(purpleContent, (double)stageWidth, (double)stageHeight);
         purple.addAntagonist(cupola);
         stage.add(purple);
      }

      JPanel contentPane = (JPanel)this.getContentPane();
      contentPane.add(stageView);
      stage.start();
		
	}


}
