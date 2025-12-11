package gui;

import app.JApplication;

/**
 * GameLauncher to start the game.
 * 
 * This work complies with the JMU Honor Code.
 */
public class GameLauncher extends Viewer {
    /**
     * Constructor for GameLauncher.
     * 
     * @param args command line arguments
     */
    public GameLauncher(final String[] args) {
        super(args);
    }

    /**
     * Main method to launch the game.
     * 
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        JApplication app = new GameLauncher(args);
		invokeInEventDispatchThread(app);
    }

    
}
