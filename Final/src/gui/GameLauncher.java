package gui;

import app.JApplication;

public class GameLauncher extends Viewer {
    public GameLauncher(final String[] args) {
        super(args);
    }

    public static void main(final String[] args) {
        JApplication app = new GameLauncher(args);
		invokeInEventDispatchThread(app);
    }

    
}
