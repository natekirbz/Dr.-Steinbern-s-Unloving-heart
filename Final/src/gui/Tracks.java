package gui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import io.ResourceFinder;

import visual.dynamic.described.Stage;
import visual.statik.described.AggregateContent;
import visual.statik.sampled.Content;
import visual.statik.sampled.ContentFactory;
import visual.statik.sampled.ImageFactory;
import visual.statik.sampled.TransformableContent;
import visual.VisualizationView;

import resources.Marker;

public class Tracks extends Stage{

	private HealthBar healthBar;
	private HealthBar bossBar;

    public Tracks(String playerName) {
        super(50);
		int stageWidth = 800;
		int stageHeight = 450;

        // build visView
        VisualizationView stageView = getView();
        stageView.setBounds(0, 0, stageWidth, stageHeight);

        // resource finder and create player
        ResourceFinder finder = ResourceFinder.createInstance(Marker.class);
		ContentFactory tcFactory = new ContentFactory(finder);
		TransformableContent content = tcFactory.createContent(playerName, 4, false);
		Player player = new Player(content, (double)stageWidth, (double)stageHeight);
		add(player);
		addKeyListener(player);

		// player health bar
		healthBar = new HealthBar(150, 20, HealthBar.createAggregateContent()); 
		healthBar.setLocation(20, 20); // top-left of screen
		add(healthBar);

		//create boss health bar
        AggregateContent ac = new AggregateContent();
		bossBar = new HealthBar(100, 20, ac); 
		bossBar.setLocation(640, 50);
		bossBar.setRotation(3.14159/2);
		add(bossBar);
          
		//create 2 bernstein enemies for the screen
		for(int i = 0; i < 2; ++i) {
			// create array of bernstein images and pick an initial one
			TransformableContent[] enemyContents = new TransformableContent[7];
			for (int j = 0; j < enemyContents.length; ++j) {
				enemyContents[j] = tcFactory.createContent("bernstein" + j + ".jpg", 4, false);
			} 
			int initialIndex = (int)(Math.random() * enemyContents.length);
			Enemy gold = new Enemy(enemyContents, initialIndex, (double)stageWidth, (double)stageHeight, healthBar);
			gold.setScale(0.1);
			gold.addAntagonist(player);
			add(gold);
		}

		// spawn a powerup occasionally
		TransformableContent goldContent = tcFactory.createContent("monster.jpeg", 4, false);
		Powerup energy = new Powerup(goldContent, (int)(Math.random() * 3), (double)stageWidth, (double)stageHeight, bossBar); //random enemy sprite implement later
		energy.setScale(0.3);
		energy.addAntagonist(player);
		add(energy);

		//create images for boss heart
		ImageFactory imageFactory = new ImageFactory(finder);
		Content[] contents = new Content[3];
		BufferedImage[] images = imageFactory.createBufferedImages("heart.png", 3, 4);
		contents = new Content[3];
		for(int j = 0; j < contents.length; ++j) {
			contents[j] = tcFactory.createContent(images[j], false);
		}

		//create boss heart in corner of screen
		Boss heart = new Boss(contents, 40, 40, 10);
		heart.setScale(0.25);
		heart.setLocation(stageWidth -90, 0);
		add(heart);
		
		setBackground(Color.gray);
    }

	public boolean isAlive(){
		return this.healthBar.getAlive();
	}
}
