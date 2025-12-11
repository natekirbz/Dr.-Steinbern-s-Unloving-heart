package gui;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import app.JApplication;
import io.ResourceFinder;
import resources.Marker;
import visual.VisualizationView;
import visual.dynamic.described.Stage;
import visual.statik.sampled.Content;
import visual.statik.sampled.ContentFactory;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Viewer extends JApplication implements ActionListener {

	public static final int HEIGHT = 450;
	public static final int WIDTH = 800;

	// Main stage for game
	public Stage stage;

	// Character selection buttons map
	Map<String, JButton> characterBtns;
	// Commands
	protected static final String START = "Start";
	protected static final String CHARACTER = "Character";

	// Fonts
	private final Font titleFont = new Font("Arial", Font.BOLD | Font.ITALIC, 50);
	private final Font buttonFont = new Font("Arial", Font.BOLD | Font.ITALIC, 30);
	private static HashMap<Clip, Integer> audioClips = new HashMap<>();

	public Viewer(final String[] args) {
		super(WIDTH, HEIGHT);
		this.stage = new Tracks("dolphin.png");
		characterBtns = new HashMap<>();
	}

	public static void main(final String[] args) throws IOException {
		JApplication app = new Viewer(args);
		invokeInEventDispatchThread(app);
	}

	@Override
	public void actionPerformed(final ActionEvent evt) {
		String action = evt.getActionCommand();
		switch (action) {
			case START:
				handleStart();
				playButtonAudio("Button-Press.wav");
				break;

			case CHARACTER:
				handleCharacter();
				playButtonAudio("Button-Press.wav");
				break;

			case "Dolphin":
				setCharacter(action);
				playButtonAudio("Button-Press.wav");
				break;

			case "Sheep":
				setCharacter(action);
				playButtonAudio("Button-Press.wav");
				break;

			case "Llama":
				setCharacter(action);
				playButtonAudio("Button-Press.wav");
				break;

			case "Confirm":
				startWindow();
				playButtonAudio("Button-Press.wav");
				break;

			case "Exit":
				System.exit(0);
				break;

			default:
				break;
		}
	}

	private void setCharacter(String action) {
		String character;
		character = action + ".png";
		// Change to dolphin track
		this.stage = new Tracks(character);
		handleCharacter();
		// Get the button being replaced
		JButton tempButton = characterBtns.get(action);
		Rectangle r = tempButton.getBounds(); // correct way to get bounds

		// Remove old button from UI
		getContentPane().remove(tempButton);

		// Create the confirm button
		JButton newButton = createButton(
				"Confirm",
				r.x, r.y, r.width, r.height,
				buttonFont);

		// Put into map
		characterBtns.put(action, newButton);

		// Add new button TO SCREEN
		getContentPane().add(newButton);

		// Show the character image (AFTER button added)
		viewCharacter(character);

		// Refresh UI
		getContentPane().revalidate();
		getContentPane().repaint();
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
		playAudio("demo-music.wav");
	}

	// --------------------------------------------------------
	// Start → Show the Stage
	// --------------------------------------------------------
	public void handleStart() {
		JPanel cp = resetContentPane();
		((Tracks) stage).resetTracks();
		VisualizationView view = stage.getView();
		cp.add(view);
		stage.start();
		playAudio("Castle-theme.wav");
	}

	public void endScreen(String imageName) {
		if (stage != null)
			stage.stop();

		JPanel cp = resetContentPane();
		cp.setLayout(null);
		playAudio("The Emperor.wav");

		// ---- Layered Pane ----
		JLayeredPane layers = new JLayeredPane();
		layers.setBounds(0, 0, WIDTH, HEIGHT);
		cp.add(layers);

		// -----------------------
		// Game Over + IP
		// -----------------------
		JLabel gameOver = new JLabel();
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			String ipAddress = localHost.getHostAddress();
			gameOver.setText("Game Over - " + ipAddress);
		} catch (UnknownHostException e) {
			gameOver.setText("Game Over");
		}

		gameOver.setFont(new Font("Arial", Font.BOLD, 30));
		gameOver.setForeground(Color.WHITE);
		gameOver.setBounds(200, 20, 600, 50);

		// -----------------------
		// Stage View
		// -----------------------
		Stage loseStage = new Stage(20);
		VisualizationView view = loseStage.getView();
		view.setBounds(0, 0, WIDTH, HEIGHT);

		// -----------------------
		// Buttons
		// -----------------------
		JButton startBtn = createButton("Start", 20, 300, 150, 60, buttonFont);
		JButton exitBtn = createButton("Exit", 20, 350, 150, 60, buttonFont);

		startBtn.setBounds(20, 300, 150, 60);
		exitBtn.setBounds(20, 350, 150, 60);

		// -----------------------
		// Add to layers
		// -----------------------
		layers.add(view, JLayeredPane.DEFAULT_LAYER);
		layers.add(gameOver, JLayeredPane.PALETTE_LAYER);
		layers.add(startBtn, JLayeredPane.PALETTE_LAYER);
		layers.add(exitBtn, JLayeredPane.PALETTE_LAYER);

		// -----------------------
		// Load image into stage
		// -----------------------
		ResourceFinder finder = ResourceFinder.createInstance(Marker.class);
		ContentFactory factory = new ContentFactory(finder);
		Content content = factory.createContent(imageName);

		loseStage.add(content);
		loseStage.start();
	}

	public void viewCharacter(String characterName) {
		try {
			BufferedImage myPicture = ImageIO.read(
					getClass().getResourceAsStream("/resources/" + characterName));
			myPicture.getScaledInstance(10, 10, 0);
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
		characterBtns.put("Dolphin", createButton("Dolphin", 20, 300, 150, 60, buttonFont));
		contentPane.add(characterBtns.get("Dolphin"));
		characterBtns.put("Sheep", createButton("Sheep", 200, 300, 150, 60, buttonFont));
		contentPane.add(characterBtns.get("Sheep"));
		characterBtns.put("Llama", createButton("Llama", 380, 300, 150, 60, buttonFont));
		contentPane.add(characterBtns.get("Llama"));
		playAudio("character-select.wav");

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
		startDeathMonitor();
	}

	public void startHealthMonitor() {
		Timer timer = new Timer(50, e -> { // check every 50 ms
			if (!((Tracks) stage).isAlive()) {
				((Timer) e.getSource()).stop(); // stop checking
				endScreen("bernstein2.jpg"); // show losing screen
			}
		});

		timer.start();
	}

	public void startDeathMonitor() {
		Timer timer = new Timer(50, e -> { // check every 50 ms
			if (!((Tracks) stage).bossAlive()) {
				((Timer) e.getSource()).stop(); // stop checking
				endScreen("bernstein1.jpg"); // show losing screen
			}
		});

		timer.start();
	}

	public static void playAudio(String audioFile) {
		ResourceFinder finder = ResourceFinder.createInstance(Marker.class);
		InputStream raw = finder.findInputStream(audioFile);
		try (BufferedInputStream buf = new BufferedInputStream(raw);
				AudioInputStream audio = AudioSystem.getAudioInputStream(buf)) {
			Clip clip = AudioSystem.getClip();
			storeClip(clip);
			clip.open(audio);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void storeClip (Clip clip) {
		for (Clip c : audioClips.keySet()) {
			c.stop();
		}
		audioClips.put(clip, 1);
	}

	public static void playButtonAudio(String audioFile) {
		ResourceFinder finder = ResourceFinder.createInstance(Marker.class);
		InputStream raw = finder.findInputStream(audioFile);
		try (BufferedInputStream buf = new BufferedInputStream(raw);
				AudioInputStream audio = AudioSystem.getAudioInputStream(buf)) {
			Clip clip = AudioSystem.getClip();
			clip.open(audio);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
