package gui;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JPanel;

import app.JApplication;
import visual.VisualizationView;
import visual.dynamic.described.Stage;
import gui.*;

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
		// add a stage to the jApp
		Stage stage = new Tracks();
		stage.setBackground(Color.gray);
		VisualizationView stageView = stage.getView();
		stageView.setBounds(0,0,WIDTH,HEIGHT);
		
		JPanel contentPane = (JPanel)getContentPane();
		contentPane.add(stageView);
		stageView.setFocusable(true);
		stageView.requestFocusInWindow();

		
		stage.start();
	}


}
