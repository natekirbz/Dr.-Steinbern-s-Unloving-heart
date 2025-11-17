package gui;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import app.JApplication;
import io.ResourceFinder;
import resources.Marker;
import visual.VisualizationView;
import visual.dynamic.described.Stage;
import visual.statik.sampled.Content;
import visual.statik.sampled.ContentFactory;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Viewer extends JApplication implements ActionListener {

	public static final int HEIGHT = 450;
	public static final int WIDTH = 800;

	public Stage stage;

	public Player player;

	// Commands
	protected static final String START = "Start";
	protected static final String CHARACTER = "Character";

	// Fonts
	private final Font titleFont = new Font("Arial", Font.BOLD | Font.ITALIC, 50);
	private final Font buttonFont = new Font("Arial", Font.BOLD | Font.ITALIC, 30);

	public Viewer(final String[] args) {
		super(WIDTH, HEIGHT);
		this.stage = new Tracks("dolphin.png");
	}

	public static void main(final String[] args) throws IOException {
		JApplication app = new Viewer(args);
		invokeInEventDispatchThread(app);
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		String action = evt.getActionCommand();
		String character = "";
		switch (action) {
			case START:
				handleStart();
				break;

			case CHARACTER:
				handleCharacter();
				break;

			case "Dolphin":
				character = "dolphin.png";
				this.stage = new Tracks(character);
				handleCharacter();
				viewCharacter(character);
				break;

			case "Sheep":
				character = "sheep.png";
				this.stage = new Tracks(character);
				handleCharacter();
				viewCharacter(character);
				break;

			case "Llama":
				character = "llama.png";
				this.stage = new Tracks(character);
				handleCharacter();
				viewCharacter(character);
				break;

			case "Save":
				startWindow();
				break;

			default:
				break;
		}
	}

	// --------------------------------------------------------
	// Main Start Button Window
	// --------------------------------------------------------
	public void startWindow() {
		JPanel contentPane = resetContentPane();
		contentPane.setLayout(null);

		JLabel title = new JLabel("Dr. Steinbern's Unloving Heart");
		title.setFont(titleFont);
		title.setBounds(50, 50, 1500, 200);
		contentPane.add(title);

		// Start Button
		JButton startBtn = createButton(START, 90, 300, 300, 100, titleFont);
		contentPane.add(startBtn);

		// Character Select Button
		JButton characterBtn = createButton(CHARACTER, 400, 300, 300, 100, titleFont);
		contentPane.add(characterBtn);
	}

	// --------------------------------------------------------
	// Start → Show the Stage
	// --------------------------------------------------------
	public void handleStart() {
		JPanel cp = resetContentPane();
		VisualizationView view = stage.getView();
		cp.add(view);
		stage.start();
	}

	public void lostScreen() {
		stage.stop();
		JPanel cp = resetContentPane();
		cp.setLayout(null);

		// Create a new Stage for the losing animation
		Stage loseStage = new Stage(20);
		VisualizationView view = loseStage.getView();
		view.setBounds(0, 0, WIDTH, HEIGHT);

		// Add the view to your window
		cp.add(view);

		// Load image
		ResourceFinder finder = ResourceFinder.createInstance(Marker.class);
		ContentFactory factory = new ContentFactory(finder);
		Content content = factory.createContent("bernstein2.jpg");

		// Add the content to the stage
		loseStage.add(content);

		loseStage.start();
	}

	public void viewCharacter(String characterName) {
		try {
			BufferedImage myPicture = ImageIO.read(
					getClass().getResourceAsStream("/resources/" + characterName));
			myPicture.getScaledInstance(350, 300, 0);
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			picLabel.setBounds(420, 50, 300, 300);

			getContentPane().add(picLabel);

			getContentPane().revalidate();
			getContentPane().repaint();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// --------------------------------------------------------
	// Character Selection Screen
	// --------------------------------------------------------
	public void handleCharacter() {
		JPanel contentPane = resetContentPane();
		contentPane.setLayout(null);

		JLabel label = new JLabel("Choose your character");
		label.setFont(buttonFont);
		label.setBounds(50, 50, 800, 100);
		contentPane.add(label);

		// Character buttons
		contentPane.add(createButton("Dolphin", 20, 300, 150, 60, buttonFont));
		contentPane.add(createButton("Sheep", 200, 300, 150, 60, buttonFont));
		contentPane.add(createButton("Llama", 380, 300, 150, 60, buttonFont));
		contentPane.add(createButton("Save", 10, 10, 105, 60, buttonFont));
	}

	// --------------------------------------------------------
	// Utility Methods
	// --------------------------------------------------------
	private JPanel resetContentPane() {
		JPanel cp = (JPanel) getContentPane();
		cp.removeAll();
		cp.revalidate();
		cp.repaint();
		return cp;
	}

	private JButton createButton(String text, int x, int y, int w, int h, Font font) {
		JButton btn = new JButton(text);
		btn.setBounds(x, y, w, h);
		btn.setFont(font);
		btn.addActionListener(this);
		return btn;
	}

	@Override
	public void init() {
		startWindow();

		startHealthMonitor();
	}

	public void startHealthMonitor() {
		HealthBar hb = ((Tracks) stage).getHealthbar();

		Timer timer = new Timer(50, e -> { // check every 50 ms
			if (!hb.getAlive()) {
				((Timer) e.getSource()).stop(); // stop checking
				lostScreen(); // show losing screen
			}
		});

		timer.start();
	}
}
