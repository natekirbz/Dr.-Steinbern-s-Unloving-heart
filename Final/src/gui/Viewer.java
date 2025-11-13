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
import gui.*;
import io.ResourceFinder;
import resources.Marker;

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
		int stageWidth = 800;
		int stageHeight = 450;
		Stage stage = new Stage(50);
		VisualizationView stageView = stage.getView();
		// Toolkit toolkit = stageView.getToolkit();
		// ImageFactory imageFactory = new ImageFactory(finder);
		// TransformableContent content = tcFactory.createContent("roof.png", 3, false);
		// stage.add(content);
		stageView.setBounds(0, 0, stageWidth, stageHeight);
		TransformableContent content = tcFactory.createContent("cupola.png", 4, false);
		Player player = new Player(content, (double)stageWidth, (double)stageHeight);
		stage.add(player);
		stage.addKeyListener(player);

      	for(int i = 0; i < 4; ++i) {
        	TransformableContent goldContent = tcFactory.createContent("balloon-gold.png", 4, false);
			Enemy gold = new Enemy(goldContent, (double)stageWidth, (double)stageHeight);
			gold.addAntagonist(player);
			stage.add(gold);
        	// TransformableContent purpleContent = tcFactory.createContent("bern.png", 4, false);
			// Enemy purple = new Enemy(purpleContent, (double)stageWidth, (double)stageHeight);
			// purple.addAntagonist(cupola);
			// stage.add(purple);
		}

		JPanel contentPane = (JPanel)this.getContentPane();
		contentPane.add(stageView);
		stage.start();
		
	}


}
