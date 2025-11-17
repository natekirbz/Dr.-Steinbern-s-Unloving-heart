package gui;

import java.io.IOException;
import javax.swing.JPanel;

import app.JApplication;
 
import visual.VisualizationView;
import visual.dynamic.described.Stage;

public class Viewer extends JApplication
{
	public static final int HEIGHT = 450; //divisible by 3 150px tracks
	public static final int WIDTH = 800;
	public Stage stage;

	public Viewer(final String[] args) {
		super(WIDTH, HEIGHT);
		
		this.stage = new Tracks();

	}
	
	public static void main(final String[] args) throws IOException
	{
		JApplication app = new Viewer(args);
		invokeInEventDispatchThread(app);
	}

	@Override
	public void init() {
		VisualizationView view = stage.getView();
    	JPanel cp = (JPanel) getContentPane();
    	cp.add(view);

    	stage.start();
	}


}
