package gui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import io.ResourceFinder;
import visual.dynamic.described.Stage;
import visual.statik.sampled.Content;
import visual.statik.sampled.ContentFactory;
import visual.statik.sampled.ImageFactory;
import visual.statik.sampled.TransformableContent;
import visual.VisualizationView;

import resources.Marker;


public class Tracks extends Stage{

	private HealthBar healthBar;

    public Tracks(String playerName) {
        super(50);
		int stageWidth = 800;
		int stageHeight = 450;

        // build visView
        VisualizationView stageView = getView();
        stageView.setBounds(0, 0, stageWidth, stageHeight);

        // resource finder to create player
        ResourceFinder finder = ResourceFinder.createInstance(Marker.class);
		ContentFactory tcFactory = new ContentFactory(finder);
		TransformableContent content = tcFactory.createContent(playerName, 4, false);

		Player player = new Player(content, (double)stageWidth, (double)stageHeight);
		add(player);
		addKeyListener(player);

		healthBar = new HealthBar(150, 20, HealthBar.createAggregateContent()); 
		healthBar.setLocation(20, 20); // top-left of screen
		add(healthBar);
          
		for(int i = 0; i < 2; ++i) {

			// create array of bernstein images and pick an initial one
			TransformableContent[] enemyContents = new TransformableContent[8];
			for (int j = 0; j < enemyContents.length; ++j) {
				enemyContents[j] = tcFactory.createContent("bernstein" + j + ".jpg", 4, false);
			}

			int initialIndex = (int)(Math.random() * enemyContents.length);
			Enemy gold = new Enemy(enemyContents, initialIndex, (double)stageWidth, (double)stageHeight, healthBar);
			if (initialIndex == 7) {
				gold.setScale(0.25);
			} else {
				gold.setScale(0.1);
			}

			gold.addAntagonist(player);
			add(gold);
		}

		ImageFactory imageFactory = new ImageFactory(finder);
		Content[] contents = new Content[3];
		BufferedImage[] images = imageFactory.createBufferedImages("heart.png", 3, 4);

		contents = new Content[3];

		for(int j = 0; j < contents.length; ++j) {
			contents[j] = tcFactory.createContent(images[j], false);
		}

		Boss heart = new Boss(contents, 40, 40);
		heart.setScale(0.25);
		heart.setLocation(stageWidth -90, 0);
		add(heart);

		setBackground(Color.gray);
    }
}
