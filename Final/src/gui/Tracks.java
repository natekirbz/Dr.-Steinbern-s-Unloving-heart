package gui;


import io.ResourceFinder;
import visual.dynamic.described.RuleBasedSprite;
import visual.dynamic.described.Stage;
import visual.statik.sampled.ContentFactory;
import visual.statik.sampled.TransformableContent;
import visual.VisualizationView;

import resources.Marker;


public class Tracks extends Stage{

    public Tracks() {
        super(50);
        ResourceFinder finder = ResourceFinder.createInstance(Marker.class);
		ContentFactory tcFactory = new ContentFactory(finder);
		int stageWidth = 800;
		int stageHeight = 450;
        VisualizationView stageView = getView();
        stageView.setBounds(0, 0, stageWidth, stageHeight);
		TransformableContent content = tcFactory.createContent("shark.png", 4, false);
		Player player = new Player(content, (double)stageWidth, (double)stageHeight);
		add(player);
		addKeyListener(player);
        
        for(int i = 0; i < 2; ++i) {
        	TransformableContent goldContent = tcFactory.createContent("balloon-gold.png", 4, false);
			goldContent.setScale(0.1, 0.1);
			Enemy gold = new Enemy(goldContent, (double)stageWidth, (double)stageHeight);
			gold.addAntagonist(player);
			add(gold);
        	// TransformableContent purpleContent = tcFactory.createContent("bern.png", 4, false);
			// Enemy purple = new Enemy(purpleContent, (double)stageWidth, (double)stageHeight);
			// purple.addAntagonist(cupola);
			// stage.add(purple);
		}

    }
    

}
