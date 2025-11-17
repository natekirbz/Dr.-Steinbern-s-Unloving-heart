package gui;

import javax.swing.JButton;
import io.ResourceFinder;
import resources.Marker;
import visual.VisualizationView;
import visual.dynamic.sampled.Screen;
import visual.statik.sampled.ContentFactory;

public class MainWindow extends Screen {
    public MainWindow() {
        super(50);
        int stageWidth = 800;
        int stageHeight = 450;

        // build visView
        VisualizationView stageView = getView();
        stageView.setBounds(20, 20, stageWidth, stageHeight);

        // resource finder to create player
        ResourceFinder finder = ResourceFinder.createInstance(Marker.class);
        ContentFactory tcFactory = new ContentFactory(finder);
        
        JButton start = new JButton("start");
        start.setBounds(10, 10, 10, 10);
        stageView.add(start);

    }
}
