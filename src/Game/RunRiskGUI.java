package Game;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

@SuppressWarnings("serial")
public class RunRiskGUI extends JFrame implements MouseListener,
		ActionListener, Observer {

	private static final String baseDir = System.getProperty("user.dir")
			+ "/Images/";
	private JMenuItem newGame = new JMenuItem("New Game");
	private JMenuItem saveGame = new JMenuItem("Save Game");
	private JMenuItem loadGame = new JMenuItem("Load Game");
	private JMenuItem exitGame = new JMenuItem("Exit Game");
	private JRadioButtonMenuItem defaultDiceOne = new JRadioButtonMenuItem("1");
	private JRadioButtonMenuItem defaultDiceMax = new JRadioButtonMenuItem(
			"Maximum");
	private GameBoard game;
	private Timer timer;
	private JFrame messageBox;
	private Container cp;
	private JPanel initScreen;
	private JPanel gameScreen;
	private JPanel splashScreen;
	private SliderFrame sliderFrame;
	private int screenHeight;
	private int screenWidth;
	private Dimension dim;
	private String fileName;
	private BufferedImage mapWorld;
	private float scale;
	private String message;
	private String chosenCountry = new String();
	private String attackCountry = new String();
	private String defendCountry = new String();
	private String fortifyCountry = new String();
	private boolean shown = false;
	private boolean clicked;
	private int gameState;
	private final static int INITIALIZING = 1;
	private final static int INITIALIZING2 = 13;
	private final static int DEPLOYING_ARMIES = 2;
	private final static int TURNIN = 3;
	private final static int TURN_DEPLOY = 4;
	private final static int PICK_ATTACK = 5;
	private final static int PICK_DEFEND = 6;
	private final static int ROLL_DICE = 7;
	private final static int PICK_ATTACK_NUM = 8;
	private final static int ASK_TURN_END = 9;
	private final static int FORTIFY_FROM = 10;
	private final static int FORTIFY_TO = 11;
	private final static int PICK_FORTIFY_NUM = 12;
	private final static int WINNER = 99;
	private int startingArmies;
	private int bonusArmies;
	private int attackDice;
	private int defendDice;
	private int numArmies;
	private String diceDefault;
	private ArrayList<Card> set;
	private boolean setTurnedIn = true;

	public static void main(String args[]) {

		// create container that will work with Window manager
		RunRiskGUI gui = new RunRiskGUI();
		gui.setVisible(true);
	}

	private void startGame() {

		timer.start();
		this.initializeGame();

	}

	private void initializeGame() {
		startingArmies = game.startingArmies();
	}

	public RunRiskGUI() {

		game = new GameBoard();
		GraphicsEnvironment graphicsEnviron = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice graphicsDev = graphicsEnviron.getDefaultScreenDevice();
		DisplayMode dispMode = graphicsDev.getDisplayMode();
		screenHeight = dispMode.getHeight();
		screenWidth = dispMode.getWidth();
		dim = new Dimension(screenWidth, screenHeight);
		this.setPreferredSize(dim);
		this.setSize(dim);
		this.addMouseListener(this);
		clicked = false;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Risk");
		this.setResizable(false);

		// Create MenuBar
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		JMenu gameMenu = new JMenu("File");
		JMenu defaultDiceMenu = new JMenu("Default Dice Number");
		menuBar.add(gameMenu);
		menuBar.add(defaultDiceMenu);

		newGame.addActionListener(new GameListener());
		saveGame.addActionListener(new GameListener());
		loadGame.addActionListener(new GameListener());
		exitGame.addActionListener(new GameListener());
		gameMenu.add(newGame);
		gameMenu.add(saveGame);
		gameMenu.add(loadGame);
		gameMenu.add(exitGame);

		defaultDiceOne.addActionListener(new DiceListener());
		defaultDiceMax.addActionListener(new DiceListener());
		ButtonGroup group = new ButtonGroup();
		group.add(defaultDiceOne);
		group.add(defaultDiceMax);
		defaultDiceMax.setSelected(true);
		diceDefault = "Maximum";
		defaultDiceMenu.add(defaultDiceOne);
		defaultDiceMenu.add(defaultDiceMax);

		cp = this.getContentPane();

		// Load map files
		fileName = baseDir + "RiskWorld.png";
		try {
			mapWorld = ImageIO.read(new File(fileName));
		} catch (IOException e) {
		}

		scale = ((float) this.getWidth()) / mapWorld.getWidth();

		// Create and display splash screen

		splashScreen = new SplashScreen();
		cp.add(splashScreen);
		pack();
		this.maximizeScreen();

		// Create Initialization Panel

		initScreen = new InitScreen();

		// Create Game Panel

		gameScreen = new GameScreen();

		// Create Slider Frame

		sliderFrame = new SliderFrame();

		// Initializes timer

		timer = new Timer(100, this);
		message = "Welcome to Risk!";
		set = new ArrayList<Card>();
		numArmies = -1;
	}

	private void maximizeScreen() {
		this.setSize(dim);
	}

	private class GameListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			Object source = event.getSource();
			JMenuItem selectedButton = (JMenuItem) source;
			String text = selectedButton.getText();

			if (text == "New Game") {
				cp.removeAll();
				cp.add(initScreen);
				game = new GameBoard();
				repaint();
				pack();
				maximizeScreen();
			}
			if (text == "Save Game") {
				// need to implement
			}
			if (text == "Load Game") {
				// need to implement
			}
			if (text == "Exit Game") {
				System.exit(0);
			}

		}
	}

	private class DiceListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			Object source = event.getSource();
			JMenuItem selectedButton = (JMenuItem) source;
			String text = selectedButton.getText();

			diceDefault = text;
		}
	}

	// Creates the Game Screen
	// Placed in its own class because of its size

	private class GameScreen extends JPanel {

		private BufferedImage infantryPic;
		private BufferedImage cavalryPic;
		private BufferedImage artilleryPic;
		private BufferedImage wildPic;

		GameScreen() {
			String fileName1 = baseDir + "infantry.gif";
			String fileName2 = baseDir + "cavalry.gif";
			String fileName3 = baseDir + "artillery.gif";
			String fileName4 = baseDir + "wildcard.gif";

			try {
				infantryPic = ImageIO.read(new File(fileName1));
				cavalryPic = ImageIO.read(new File(fileName2));
				artilleryPic = ImageIO.read(new File(fileName3));
				wildPic = ImageIO.read(new File(fileName4));
			} catch (IOException e) {
			}
		}

		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			super.paintComponent(g2);

			if ((gameState != TURNIN)
					|| (game.currentPlayer() instanceof ComputerPlayer)
					|| (game.currentPlayer().getHandSize() <= 2)) {
				g2.drawImage(mapWorld, 0, 0, this.getWidth(), (int) (mapWorld
						.getHeight() * scale), null);

				for (int i = 0; i < game.getMap().numCountries(); ++i) {
					for (int j = 0; j < game.getPlayerSize(); ++j) {
						if (game.getPlayer(j).equals(
								game.getMap().getCountry(i).getOwner())) {
							g2.setColor(game.getMap().getCountry(i).getOwner()
									.getColor());
						}
					}

					// draw circle for army
					Ellipse2D.Float circle = new Ellipse2D.Float(game.getMap()
							.getCountry(i).getCenterX()
							* scale - 10, game.getMap().getCountry(i)
							.getCenterY()
							* scale - 10, (float) 20.0, (float) 20.0);
					g2.draw(circle);
					g2.fill(circle);
					g2.setColor(Color.BLACK);
					String armies = Integer.toString(game.getMap()
							.getCountry(i).getArmy());

					Font font = new Font("Fixed", Font.PLAIN, 12);
					g2.setFont(font);
					g2.drawString(armies, game.getMap().getCountry(i)
							.getCenterX()
							* scale
							- (g2.getFontMetrics().stringWidth(armies) / 2),
							game.getMap().getCountry(i).getCenterY() * scale
									+ (g2.getFontMetrics().getHeight() / 4));

					font = new Font("Fixed", Font.BOLD, 12);
					g2.setFont(font);
					FontMetrics fm = g2.getFontMetrics();
					g2.setColor(Color.BLACK);
					// draw country name
					g2.drawString(game.getMap().getCountry(i).getName(), game
							.getMap().getCountry(i).getCenterX()
							* scale
							- (fm.stringWidth(game.getMap().getCountry(i)
									.getName()) / 2), game.getMap().getCountry(
							i).getCenterY()
							* scale - 12);
				}
				Font oldFont = g.getFont();
				Font font = new Font("Fixed", Font.BOLD, 16);
				g2.setFont(font);
				g2
						.drawString(message, 20,
								((mapWorld.getHeight() * scale) + (this
										.getHeight() - mapWorld.getHeight()
										* scale) / 2)
										+ g.getFontMetrics().getHeight() / 4);

				font = new Font("Fixed", Font.BOLD, 20);
				g2.setFont(font);
				g2.setColor(game.currentPlayer().getColor());
				g2.drawString(game.currentPlayer().getName() + "'s Turn", 20,
						mapWorld.getHeight() * scale * 3 / 5);

				g2.setFont(oldFont);
				g2.setColor(Color.BLACK);
			} else {
				ArrayList<Card> hand = game.currentPlayer().getHand();
				for (int i = 0; i < hand.size(); ++i) {
					for (int j = 0; j < set.size(); ++j) {
						if ((hand.get(i).getTerritory().equals(set.get(j)
								.getTerritory()))
								&& (i < 5)) {
							g2.setColor(Color.WHITE);
							g2.fillRect((i * 100 + 20), 70, 80, 120);
						}
						if ((hand.get(i).getTerritory().equals(set.get(j)
								.getTerritory()))
								&& (i >= 5)) {
							g2.setColor(Color.WHITE);
							g2.fillRect((i * 100 + 20), 210, 80, 120);
						}
					}
					g2.setColor(Color.BLACK);
					g2.drawRect((i * 100 + 20), 70, 80, 120);
				}

				if (hand.size() > 5) {
					for (int i = 0; i < hand.size() - 5; ++i) {
						g2.drawRect((i * 100 + 20), 210, 80, 120);
					}
				}

				for (int i = 0; i < hand.size(); ++i) {
					ArrayList<String> names = new ArrayList<String>();
					StringBuilder countryName = new StringBuilder(hand.get(i)
							.getTerritory());
					int last = 0;
					for (int j = 0; j < countryName.length() - 1; ++j) {
						if (countryName.charAt(j) == 32) {
							names.add(countryName.substring(last, j));
							last = j;
						}
					}
					names
							.add(countryName.substring(last, countryName
									.length()));

					for (int j = 0; j < names.size(); ++j) {
						g2.drawString(names.get(j), (i * 100 + 24),
								(86 + j * 15));

					}
				}

				for (int i = 0; i < hand.size(); ++i) {
					BufferedImage imageToDraw = infantryPic;
					if (hand.get(i).getTroopType() == 1) {
						imageToDraw = cavalryPic;
					}
					if (hand.get(i).getTroopType() == 2) {
						imageToDraw = artilleryPic;
					}
					if (hand.get(i).getTroopType() == 3) {
						imageToDraw = wildPic;
					}
					g2.drawImage(imageToDraw, (i * 100 + 30), (i / 5 + 125),
							60, 60, null);
				}
				Font font = new Font("Fixed", Font.BOLD, 16);
				g2.setFont(font);
				g2
						.drawString(message, 20,
								((mapWorld.getHeight() * scale) + (this
										.getHeight() - mapWorld.getHeight()
										* scale) / 2)
										+ g.getFontMetrics().getHeight() / 4);
			}
		}
	}

	private class PicturePanel extends JPanel {

		private String fileName;
		private String explosionFileName;
		private BufferedImage splashImage;
		private BufferedImage explosionImage;
		private BufferedImage[] explosion;
		private boolean exploded;
		private static final int NUM_MILLISECONDS = 25;
		private Timer myTimer;
		private int frameCount;

		PicturePanel() {
			fileName = baseDir + "risk.jpg";

			exploded = false;
			try {
				splashImage = ImageIO.read(new File(fileName));
			} catch (IOException e) {
			}

			explosion = new BufferedImage[30];
			try {
				for (int i = 0; i < 30; ++i) {
					explosionFileName = baseDir + "/ExplosionFrames/Frame_" + i
							+ ".gif";
					explosion[i] = ImageIO.read(new File(explosionFileName));
				}

			} catch (IOException e1) {
			}

			frameCount = 0;
			myTimer = new Timer(NUM_MILLISECONDS, new TimerListener());
			myTimer.start();
		}

		public void paintComponent(Graphics g) {

			Graphics2D g2 = (Graphics2D) g;
			super.paintComponent(g2);

			if (!exploded) {
				g2.drawImage(explosionImage, 0, 0, screenWidth, screenHeight,
						null);
			} else {
				myTimer.stop();
				g2.drawImage(splashImage, 0, 0, screenWidth, screenHeight - 50,
						null);
			}
		}

		private class TimerListener implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				explosionImage = explosion[frameCount];
				repaint();
				++frameCount;
				if (frameCount == explosion.length - 1)
					exploded = true;
			}
		}
	}

	// Creates the Splash Screen
	// Placed in its own class because of its size

	private class SplashScreen extends JPanel {

		private static final int NUM_MILLISECONDS = 15;
		private String[] scrolledText = {
				"The year is 1841.  Napoleon Bonaparte has failed in his",
				"conquest of Europe.  With the invention of the telegraph,",
				"it is possible to get news from all over the globe in a",
				"matter of days, not weeks as had been the case.  Technology",
				"has allowed you to finish what Napolean had started --",
				"the conquest of the world!  Unfortunately, you're not the",
				"only one hoping to become Napoleon's successor.  Five other",
				"serious contenders hope to succeed him as well.  Do you have",
				"what it takes to become the first world leader?" };
		private int[] placementOfText = new int[10];
		private Timer myTimer;

		SplashScreen() {

			this.setPreferredSize(new Dimension(screenWidth, screenHeight));
			this.setSize(screenWidth, screenHeight);
			this.setPlacement();

			myTimer = new Timer(NUM_MILLISECONDS, new TimerListener());
			myTimer.start();
		}

		private class TimerListener implements ActionListener {

			public void actionPerformed(ActionEvent e) {
				splashScreen.repaint();
			}
		};

		private void setPlacement() {

			placementOfText[0] = screenHeight;
			for (int i = 1; i < placementOfText.length; ++i) {
				placementOfText[i] = placementOfText[i - 1]
						+ (int) Math.floor(30 * (screenWidth / screenHeight))
						+ 3;
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			for (int i = 0; i < placementOfText.length - 1; ++i) {
				String text = scrolledText[i];
				Font font = new Font("Fixed", Font.BOLD, (int) Math
						.floor(30 * (screenWidth / screenHeight)));
				g.setFont(font);
				FontMetrics fontMet = g.getFontMetrics(font);
				int lengthOfText = fontMet.stringWidth(text);
				g.drawString(text, (screenWidth - lengthOfText) / 2,
						placementOfText[i]);
				placementOfText[i] -= 1;
			}
			if (placementOfText[8] < -10) {
				myTimer.stop();
				cp.removeAll();
				cp.add(new PicturePanel());
				pack();
				maximizeScreen();
			}
		}
	}

	// Creates the Initialization Screen
	// Placed in its own class because of its size

	private class InitScreen extends JPanel {

		private ArrayList<JComboBox> playerList;
		private ArrayList<JComboBox> strategyList;
		private JComboBox playerList1;
		private JComboBox strategyList1;
		private JComboBox playerList2;
		private JComboBox strategyList2;
		private JComboBox playerList3;
		private JComboBox strategyList3;
		private JComboBox playerList4;
		private JComboBox strategyList4;
		private JComboBox playerList5;
		private JComboBox strategyList5;
		private JComboBox playerList6;
		private JComboBox strategyList6;
		private JComboBox initialize;

		InitScreen() {

			JPanel playerSelect = new JPanel(new GridLayout(7, 3));

			String[] playerType = { "None", "Human", "Computer" };
			String[] strategyType = { "Beginner", "Intermediate" };
			String[] initializeType = { "Random", "Choose" };

			this.add(playerSelect, BorderLayout.CENTER);

			JButton startGameButton = new JButton("Start Game");
			startGameButton.addActionListener(new StartGameListener());
			this.add(startGameButton, BorderLayout.SOUTH);

			playerList = new ArrayList<JComboBox>();
			strategyList = new ArrayList<JComboBox>();
			playerList1 = new JComboBox(playerType);
			playerList1.addActionListener(new PlayerSelectListener());
			strategyList1 = new JComboBox(strategyType);
			playerList2 = new JComboBox(playerType);
			playerList2.addActionListener(new PlayerSelectListener());
			strategyList2 = new JComboBox(strategyType);
			playerList3 = new JComboBox(playerType);
			playerList3.addActionListener(new PlayerSelectListener());
			strategyList3 = new JComboBox(strategyType);
			playerList4 = new JComboBox(playerType);
			playerList4.addActionListener(new PlayerSelectListener());
			strategyList4 = new JComboBox(strategyType);
			playerList5 = new JComboBox(playerType);
			playerList5.addActionListener(new PlayerSelectListener());
			strategyList5 = new JComboBox(strategyType);
			playerList6 = new JComboBox(playerType);
			playerList6.addActionListener(new PlayerSelectListener());
			strategyList6 = new JComboBox(strategyType);
			playerList.add(playerList1);
			playerList.add(playerList2);
			playerList.add(playerList3);
			playerList.add(playerList4);
			playerList.add(playerList5);
			playerList.add(playerList6);
			strategyList.add(strategyList1);
			strategyList.add(strategyList2);
			strategyList.add(strategyList3);
			strategyList.add(strategyList4);
			strategyList.add(strategyList5);
			strategyList.add(strategyList6);
			strategyList1.setEnabled(false);
			strategyList2.setEnabled(false);
			playerList3.setEnabled(false);
			strategyList3.setEnabled(false);
			playerList4.setEnabled(false);
			strategyList4.setEnabled(false);
			playerList5.setEnabled(false);
			strategyList5.setEnabled(false);
			playerList6.setEnabled(false);
			strategyList6.setEnabled(false);

			playerSelect.add(new JLabel("Player 1:"));
			playerSelect.add(playerList1);
			playerSelect.add(strategyList1);
			playerSelect.add(new JLabel("Player 2:"));
			playerSelect.add(playerList2);
			playerSelect.add(strategyList2);
			playerSelect.add(new JLabel("Player 3:"));
			playerSelect.add(playerList3);
			playerSelect.add(strategyList3);
			playerSelect.add(new JLabel("Player 4:"));
			playerSelect.add(playerList4);
			playerSelect.add(strategyList4);
			playerSelect.add(new JLabel("Player 5:"));
			playerSelect.add(playerList5);
			playerSelect.add(strategyList5);
			playerSelect.add(new JLabel("Player 6:"));
			playerSelect.add(playerList6);
			playerSelect.add(strategyList6);

			initialize = new JComboBox(initializeType);
			playerSelect.add(new JLabel("Country Selection Method:"));
			playerSelect.add(initialize);
		}

		private class PlayerSelectListener implements ActionListener {

			public void actionPerformed(ActionEvent event) {

				Object source = event.getSource();
				JComboBox selectedPlayer = (JComboBox) source;

				int playerIndex = 0;
				for (int i = 0; i < playerList.size(); ++i) {
					if (selectedPlayer.equals(playerList.get(i)))
						playerIndex = i;
				}

				String text = (String) selectedPlayer.getSelectedItem();

				if (text.equals("None")) {
					for (int i = playerIndex; i < 6; ++i) {
						playerList.get(i).setSelectedIndex(0);
						strategyList.get(i).setSelectedIndex(0);
						strategyList.get(i).setEnabled(false);
						if (i > 1)
							playerList.get(i).setEnabled(false);
					}
				}
				if (text.equals("Human")) {
					strategyList.get(playerIndex).setEnabled(false);
				}
				if (text.equals("Computer")) {
					strategyList.get(playerIndex).setEnabled(true);
					strategyList.get(playerIndex).setSelectedIndex(0);
				}

				for (int i = 0; i < playerList.size() - 1; ++i) {
					if (playerList.get(i).getSelectedIndex() != 0)
						playerList.get(i + 1).setEnabled(true);
				}
			}
		}

		private class StartGameListener implements ActionListener {

			public void actionPerformed(ActionEvent event) {

				if ((playerList.get(0).getSelectedIndex() != 0)
						&& (playerList.get(1).getSelectedIndex() != 0)) {
					for (int i = 0; i < playerList.size(); ++i) {
						if (playerList.get(i).getSelectedIndex() != 0) {

							if (playerList.get(i).getSelectedIndex() == 1) {
								game.addPlayer(new HumanPlayer("Player "
										+ (i + 1)));
							}

							if (playerList.get(i).getSelectedIndex() == 2) {
								if (strategyList.get(i).getSelectedIndex() == 0)
									game.addPlayer(new ComputerPlayer("Player "
											+ (i + 1), new BeginnerStrategy()));
								if (strategyList.get(i).getSelectedIndex() == 1)
									game.addPlayer(new ComputerPlayer("Player "
											+ (i + 1),
											new IntermediateStrategy()));
							}
						}
					}

					if (initialize.getSelectedIndex() == 0) {
						gameState = INITIALIZING2;
					} else {
						gameState = INITIALIZING;
					}
					if (timer.isRunning()) {
						timer.stop();
					}
					cp.removeAll();
					cp.add(gameScreen);
					gameScreen.repaint();
					pack();
					maximizeScreen();
					startGame();

				} else {
					JOptionPane
							.showMessageDialog(messageBox,
									"Player 1 and Player 2 must be selected to start a game!");
				}
			}
		}
	}

	private class SliderFrame extends JFrame implements ChangeListener,
			ActionListener {

		private JSlider slider;
		private JLabel number;
		private JLabel message;
		private JButton button;
		private Container content;

		private SliderFrame() {
			this.setLocation(screenWidth / 4, screenHeight / 4);
			slider = new JSlider(0, 0, 0);
			slider.addChangeListener(this);
			number = new JLabel("");
			message = new JLabel("");
			button = new JButton("Move 'em");
			button.addActionListener(this);
			content = this.getContentPane();
			content.add(message, BorderLayout.NORTH);
			content.add(slider, BorderLayout.CENTER);
			content.add(number, BorderLayout.EAST);
			content.add(button, BorderLayout.SOUTH);
		}

		public void selection(int max, boolean attacked) {
			slider.setMinimum(0);
			slider.setMaximum(max);
			slider.setValue(max / 2);
			number.setText(Integer.toString(slider.getValue()));
			if (attacked) {
				message
						.setText("You conquered the territory.  How many armies do you want to move?");
			} else {
				message.setText("How many armies do you want to fortify with?");
			}

			this.setVisible(true);
			pack();
		}

		public void stateChanged(ChangeEvent arg0) {
			number.setText(Integer.toString(slider.getValue()));
		}

		public void actionPerformed(ActionEvent arg0) {
			numArmies = slider.getValue();
			timer.start();
			timer.setDelay(100);
			this.setVisible(false);
		}
	}

	// RunRiskGUI's method
	public void mouseClicked(MouseEvent me) {

		int x = me.getX();
		int y = me.getY();

		if (gameScreen.isShowing()) {
			if ((gameState != TURNIN) && (me.getButton() == MouseEvent.BUTTON1)) {
				this.getCountryAt(x, y);
				if (game.currentPlayer() instanceof HumanPlayer) {
					clicked = true;
					if (gameState != WINNER)
						shown = false;
				}
			}

			if ((me.getButton() != MouseEvent.BUTTON1)
					&& ((gameState == FORTIFY_FROM) || (gameState == FORTIFY_TO))) {
				gameState = TURNIN;
				clicked = false;
				game.nextPlayer();
			}

			if (gameState == TURNIN) {
				if (me.getButton() == MouseEvent.BUTTON1) {
					int cardXSelected = 0;
					int cardSelected = 0;
					if ((x > 20) && (x < 100)) {
						cardXSelected = 0;
					}
					if ((x > 120) && (x < 200)) {
						cardXSelected = 1;
					}
					if ((x > 220) && (x < 300)) {
						cardXSelected = 2;
					}
					if ((x > 320) && (x < 400)) {
						cardXSelected = 3;
					}
					if ((x > 420) && (x < 500)) {
						cardXSelected = 4;
					} else {
						// if y > 210, selected card is in 2nd row
						if (y > 210) {
							cardXSelected = cardSelected + 5;
						}
					}
					cardSelected = cardXSelected;
					try {
						set.add(game.currentPlayer().getHand()
								.get(cardSelected));
					} catch (Exception e) {
					}
				} else {
					if (game.currentPlayer().getHandSize() < 5) {
						setTurnedIn = false;
					} else {
						JOptionPane
								.showMessageDialog(messageBox,
										"You have 5 or more cards and must make a turn-in.");
					}

				}
				gameScreen.repaint();
			}

			if (((gameState == PICK_ATTACK) || (gameState == PICK_DEFEND))
					&& (me.getButton() != MouseEvent.BUTTON1)) {
				gameState = FORTIFY_FROM;
			}
		}
	}

	private void getCountryAt(int x, int y) {

		float scaledX = x / scale;

		// the 50 pixels in scaledY is the height of the menu bar and title bar
		// put together
		float scaledY = (y - 50) / scale;

		if (gameScreen.isShowing()) {

			int colorOfPixel = mapWorld.getRGB(Math.round(scaledX), Math
					.round(scaledY));
			Color color = new Color(colorOfPixel);

			for (int i = 0; i < game.getMap().getSize(); ++i) {
				for (int j = 0; j < game.getMap().getContinent(i).getSize(); j++) {
					Color countryColor = game.getMap().getContinent(i)
							.getCountry(j).getColor();
					if (color.equals(countryColor)) {
						chosenCountry = game.getMap().getContinent(i)
								.getCountry(j).getName();
						break;
					}
				}
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {

	}

	public void mouseReleased(MouseEvent arg0) {

	}

	public void update(Observable arg0, Object arg1) {
		gameScreen.repaint();
	}

	public void actionPerformed(ActionEvent arg0) {

		Random ourGenerator = new Random();

		if (game.isWinner(game.currentPlayer()) == true) {
			gameState = WINNER;
		}
		
		if (gameState == INITIALIZING2) {
			for (int i = 0; i < game.getMap().numCountries(); ++i) {
				int num = ourGenerator.nextInt(game.getMap().numCountries());
				while (game.getMap().getCountry(num).getOwner() != null) {
					num = ourGenerator.nextInt(game.getMap().numCountries());
				}
				chosenCountry = game.getMap().getCountry(num).getName();
				game.getMap().getCountry(num).setOwner(game.currentPlayer());
				game.placeArmies(chosenCountry, 1);
				--startingArmies;
				message = "" + game.currentPlayer().getName() + " was given "
						+ chosenCountry + ".";
				game.nextPlayer();
				gameScreen.repaint();
			}
			while (!(game.currentPlayer().equals(game.getPlayer(0)))) {
				game.nextPlayer();
			}
			gameState = DEPLOYING_ARMIES;
		}

		if (gameState == INITIALIZING) {

			if (game.currentPlayer() instanceof HumanPlayer) {
				message = "" + game.currentPlayer().getName()
						+ ", click on the country you want to own.";
				gameScreen.repaint();
				if (clicked == false) {
					return;
				} else {
					if (game.getMap().getCountryOwner(chosenCountry) == null) {
						clicked = false;
						game.setCountryOwner(chosenCountry, game
								.currentPlayer());
					} else {
						clicked = false;
						JOptionPane
								.showMessageDialog(messageBox,
										"That country is already owned.  Please make another selection.");
						return;
					}
				}
			} else {
				chosenCountry = ((ComputerPlayer) game.currentPlayer())
						.chooseCountry(game.getMap());
				game.setCountryOwner(chosenCountry, game.currentPlayer());

			}

			if (game.isInitialized()) {
				gameState = DEPLOYING_ARMIES;
				while (!(game.currentPlayer().equals(game.getPlayer(0)))) {
					game.nextPlayer();
				}
			}
			game.placeArmies(chosenCountry, 1);
			--startingArmies;
			message = "" + game.currentPlayer().getName() + " selected "
					+ chosenCountry + ".";
			game.nextPlayer();
			gameScreen.repaint();
		}

		if (gameState == DEPLOYING_ARMIES) {

			if (game.currentPlayer() instanceof HumanPlayer) {
				message = ""
						+ game.currentPlayer().getName()
						+ ", click on the country you where you want to place an army.";
				gameScreen.repaint();
				if (clicked == false) {
					return;
				} else {
					if (game.getMap().getCountryOwner(chosenCountry) == game
							.currentPlayer()) {
						clicked = false;
						game.placeArmies(chosenCountry, 1);
						--startingArmies;
						message = "" + game.currentPlayer().getName()
								+ " placed an army in " + chosenCountry + ".";
						game.nextPlayer();
					} else {
						clicked = false;
						JOptionPane
								.showMessageDialog(messageBox,
										"That country is not yours.  Please make another selection.");
						return;
					}
				}
			} else {
				chosenCountry = ((ComputerPlayer) game.currentPlayer())
						.placeArmy(game.getMap());
				game.placeArmies(chosenCountry, 1);
				--startingArmies;
				message = "" + game.currentPlayer().getName()
						+ " placed an army in " + chosenCountry + ".";
				game.nextPlayer();
			}

			if (startingArmies == 0) {
				gameState = TURNIN;
				while (!(game.currentPlayer().equals(game.getPlayer(0)))) {
					game.nextPlayer();
				}
			}
			gameScreen.repaint();
		}

		if (gameState == TURNIN) {
			bonusArmies = 0;
			bonusArmies = game.calculateArmies(game.currentPlayer());

			if (game.currentPlayer() instanceof HumanPlayer) {
				if (game.currentPlayer().getHandSize() < 3) {
					JOptionPane.showMessageDialog(messageBox,
							"You don't have enough cards for a set.");
					gameState = TURN_DEPLOY;
				} else {
					if (game.currentPlayer().checkForSet()) {
						message = "Click on the cards you want to turn in.  Right click to cancel turn-in.";
						gameScreen.repaint();
						if (setTurnedIn == false) {
							setTurnedIn = true;
						}
						if ((set.size() == 3) && (setTurnedIn)) {
							boolean setOkay = this.checkSet();
							if (setOkay) {
								bonusArmies += game.armiesForTurnIn();
								for (int i = 0; i < set.size(); ++i) {
									String setCountry = set.get(i)
											.getTerritory();
									for (int j = 0; j < game.currentPlayer()
											.getHandSize(); ++j) {
										String handCountry = game
												.currentPlayer().getHand().get(
														j).getTerritory();
										if (setCountry.equals(handCountry)) {
											game.currentPlayer().getHand()
													.remove(j);
										}
									}
								}
								game.turnInSet(set);
								set.removeAll(set);
								if (game.currentPlayer().getHandSize() < 5) {
									gameState = TURN_DEPLOY;
								}
								setTurnedIn = false;
							} else {
								message = "That is not a valid set.  Please choose again.";
								set.removeAll(set);
							}
						} else {
							return;
						}
					} else {
						message = "You don't have a set to turn in.";
						gameState = TURN_DEPLOY;
					}
				}
			} else {
				if (((ComputerPlayer) game.currentPlayer()).checkForSet() == true) {
					game.turnInSet(((ComputerPlayer) game.currentPlayer())
							.turnInCards());
					bonusArmies += game.armiesForTurnIn();
				}
				gameState = TURN_DEPLOY;
			}
			gameScreen.repaint();
		}

		if (gameState == TURN_DEPLOY) {
			if (game.currentPlayer() instanceof HumanPlayer) {
				message = ""
						+ game.currentPlayer().getName()
						+ ", you have "
						+ bonusArmies
						+ " armies to place.  Click on the country you where you want to place an army.";
				gameScreen.repaint();
				if (clicked == false) {
					return;
				} else {
					if (game.getMap().getCountryOwner(chosenCountry) == game
							.currentPlayer()) {
						clicked = false;
						game.placeArmies(chosenCountry, 1);
						--bonusArmies;
						message = "" + game.currentPlayer().getName()
								+ " placed an army in " + chosenCountry + ".";
					} else {
						clicked = false;
						JOptionPane
								.showMessageDialog(messageBox,
										"That country is not yours.  Please make another selection.");
						return;
					}
				}
			} else {
				chosenCountry = ((ComputerPlayer) game.currentPlayer())
						.placeArmy(game.getMap());
				game.placeArmies(chosenCountry, 1);
				--bonusArmies;
				message = "" + game.currentPlayer().getName()
						+ " placed an army in " + chosenCountry + ".";
			}

			if (bonusArmies == 0) {
				gameState = PICK_ATTACK;
			}
			gameScreen.repaint();
		}

		if (gameState == PICK_ATTACK) {
			boolean playerAttack = false;
			for (int i = 0; i < game.getMap().numCountries(); ++i) {
				if ((game.getMap().getCountry(i).getOwner().equals(game
						.currentPlayer()))
						&& (game.getMap().getCountry(i).getArmy() > 1)) {
					ArrayList<String> borders = game.getMap().getCountry(i)
							.getBorders();
					for (int j = 0; j < borders.size(); ++j) {
						if (!(game.getMap().getCountryOwner(borders.get(j))
								.equals(game.currentPlayer()))) {
							playerAttack = true;
						}
					}
				}
			}

			if (playerAttack == false) {
				if (game.currentPlayer() instanceof HumanPlayer) {
					gameState = FORTIFY_FROM;
					JOptionPane.showMessageDialog(messageBox, game
							.currentPlayer().getName()
							+ " can't attack.");
				}
			}
		}

		if (gameState == PICK_ATTACK) {
			attackCountry = new String();

			if (game.currentPlayer() instanceof HumanPlayer) {
				message = ""
						+ game.currentPlayer().getName()
						+ ", click on the country you want to attack FROM.  Right click to skip the attack phase.";
				gameScreen.repaint();
				if (clicked == false) {
					return;
				} else {
					boolean canAttack = false;
					ArrayList<String> borders = game.getMap()
							.getCountryBorders(chosenCountry);
					for (int i = 0; i < borders.size(); ++i) {
						if (!(game.getMap().getCountryOwner(borders.get(i))
								.equals(game.currentPlayer()))) {
							canAttack = true;
						}
					}
					if (game.getMap().getCountryArmy(chosenCountry) == 1) {
						canAttack = false;
					}

					if ((game.getMap().getCountryOwner(chosenCountry) == game
							.currentPlayer())
							&& (canAttack)) {
						attackCountry = chosenCountry;
						gameState = PICK_DEFEND;
						clicked = false;
					} else {
						clicked = false;
						JOptionPane
								.showMessageDialog(
										messageBox,
										"That country is not yours or you cannot attack anyone from it.  Please make another selection.");
						return;
					}
				}
			} else {
				if (((ComputerPlayer) game.currentPlayer()).playAgain(game
						.currentPlayer(), game.getMap())) {
					attackCountry = ((ComputerPlayer) game.currentPlayer())
							.selectOwnedCountry(game.getMap());

					if (attackCountry != null) {
						gameState = PICK_DEFEND;
					}

				} else {
					gameState = FORTIFY_FROM;
				}
			}
		}

		if (gameState == PICK_DEFEND) {
			defendCountry = new String();

			if (game.currentPlayer() instanceof HumanPlayer) {
				message = game.currentPlayer().getName()
						+ ", you are attacking from "
						+ attackCountry
						+ ".  Click on the country you want to ATTACK or right click to skip to fortification phase.";
				gameScreen.repaint();
				if (clicked == false) {
					return;
				} else {
					boolean isBorder = false;
					ArrayList<String> borders = game.getMap()
							.getCountryBorders(attackCountry);
					for (int i = 0; i < borders.size(); ++i) {
						if (borders.get(i).equals(chosenCountry)) {
							isBorder = true;
						}
					}
					if ((!(game.getMap().getCountryOwner(chosenCountry) == game
							.currentPlayer()))
							&& (isBorder)) {
						defendCountry = chosenCountry;
						gameState = ROLL_DICE;
						clicked = false;
					} else {
						clicked = false;
						JOptionPane
								.showMessageDialog(
										messageBox,
										"That country cannot be attacked from the country you chose to attack from.  Please make another selection.");
						return;
					}
				}
			} else {
				defendCountry = ((ComputerPlayer) game.currentPlayer()).attack(
						attackCountry, game.getMap());

				if (defendCountry != null) {
					gameState = ROLL_DICE;
				}
			}
			message = "" + game.currentPlayer().getName() + " chose to attack "
					+ defendCountry + " from " + attackCountry + ".";
		}

		if (gameState == ROLL_DICE) {
			if (game.currentPlayer() instanceof HumanPlayer) {
				if (diceDefault == "1") {
					attackDice = 1;
				} else {
					attackDice = game.getMap().getCountryArmy(attackCountry) - 1;
					if (attackDice > 3) {
						attackDice = 3;
					}
				}
				Player defendingPlayer = game.getMap().getCountryOwner(
						defendCountry);
				if (defendingPlayer instanceof HumanPlayer) {
					if (diceDefault == "1") {
						defendDice = 1;
					} else {
						defendDice = game.getMap()
								.getCountryArmy(defendCountry);
						if (defendDice > 2) {
							defendDice = 2;
						}
					}
				} else {
					defendDice = ((ComputerPlayer) defendingPlayer)
							.chooseDefendDice(defendCountry, game.getMap());
				}
			} else {
				attackDice = ((ComputerPlayer) game.currentPlayer())
						.chooseAttackDice(attackCountry, game.getMap());
				Player defendingPlayer = game.getMap().getCountryOwner(
						defendCountry);
				if (defendingPlayer instanceof HumanPlayer) {
					if (diceDefault == "1") {
						defendDice = 1;
					} else {
						defendDice = game.getMap()
								.getCountryArmy(defendCountry);
						if (defendDice > 2) {
							defendDice = 2;
						}
					}
				} else {
					defendDice = ((ComputerPlayer) defendingPlayer)
							.chooseDefendDice(defendCountry, game.getMap());
				}
			}
			int[] losses = game.rollDice(attackCountry, attackDice,
					defendCountry, defendDice);
			message = "Attacker lost " + losses[0]
					+ " armies and Defender lost " + losses[1] + " armies.";
			gameScreen.repaint();
			if (losses[2] == 1) {
				gameState = PICK_ATTACK_NUM;
				if (game.getMap().getCountriesOwned(
						game.getMap().getCountryOwner(defendCountry)) == 0) {
					ArrayList<Card> defeatedHand = game.getMap()
							.getCountryOwner(defendCountry).getHand();
					ArrayList<Card> winnerHand = game.getMap().getCountryOwner(
							attackCountry).getHand();

					// gives winner all cards in loser's hand if he has been
					// defeated
					for (int i = 0; i < defeatedHand.size(); ++i) {
						winnerHand.add(defeatedHand.remove(i));
					}
				}
			} else {
				gameState = PICK_ATTACK;
			}
		}

		if (gameState == PICK_ATTACK_NUM) {
			if (game.currentPlayer() instanceof HumanPlayer) {
				if ((!shown) && (clicked == false)) {
					timer.stop(); // restarted in SliderFrame once input
					// received
					sliderFrame.selection((game.getMap().getCountryArmy(
							attackCountry) - 1), true);
					shown = true;
				} else {
					if (numArmies == -1) {
						return;
					} else {
						game.getMap().removeArmies(attackCountry, numArmies);
						game.getMap().addArmies(defendCountry, numArmies);
						gameState = PICK_ATTACK;
						numArmies = -1;
					}
				}
			} else {
				numArmies = ((ComputerPlayer) game.currentPlayer()).moveArmies(
						attackCountry, game.getMap());
				game.getMap().removeArmies(attackCountry, numArmies);
				game.getMap().addArmies(defendCountry, numArmies);
				if (((ComputerPlayer) game.currentPlayer()).playAgain(game
						.currentPlayer(), game.getMap())) {
					gameState = PICK_ATTACK;
				} else {
					gameState = FORTIFY_FROM;
				}
			}
			gameScreen.repaint();
		}

		// if (gameState == ASK_TURN_END) {
		// if (game.currentPlayer() instanceof HumanPlayer) {
		//
		// boolean allOwned = true;
		// for (int i = 0; i < game.getMap().getSize(); ++i) {
		// if (!(game.getMap().getCountry(i).getOwner().equals(game
		// .currentPlayer()))) {
		// allOwned = false;
		// }
		// }
		//
		// if (allOwned == false) {
		// timer.stop(); // wait till after input to restart timer
		// int choice = JOptionPane.showConfirmDialog(messageBox,
		// "Would you like to end your turn?", "End Turn",
		// JOptionPane.YES_NO_OPTION);
		// if (choice == 0) { // YES
		// if (game.hasDefeatedCountry() == true) {
		// game.dealCard(game.currentPlayer());
		// }
		// if (game.isWinner(game.currentPlayer()) == true) {
		// gameState = WINNER;
		// }
		// gameState = FORTIFY_FROM;
		// } else { // NO
		// gameState = PICK_ATTACK;
		// clicked = false;
		// shown = false;
		// }
		// } else {
		// gameState = WINNER;
		// }
		// timer.start();
		// timer.setDelay(100);
		// } else {
		// if (!((ComputerPlayer) game.currentPlayer()).playAgain(game
		// .currentPlayer(), game.getMap())) {
		//
		// if (game.hasDefeatedCountry() == true) {
		// game.dealCard(game.currentPlayer());
		// }
		// if (game.isWinner(game.currentPlayer()) == true) {
		// gameState = WINNER;
		// } else {
		// gameState = FORTIFY_FROM;
		// }
		// } else {
		// gameState = PICK_ATTACK;
		// }
		// }
		// }

		if (gameState == FORTIFY_FROM) {
			attackCountry = new String();

			// check for winner
			if (game.currentPlayer() instanceof HumanPlayer) {

				if (game.isWinner(game.currentPlayer()) == true) {
					gameState = WINNER;
				} else {
					if (game.hasDefeatedCountry() == true) {
						game.dealCard(game.currentPlayer());
						game.setDefeatedCountry(false);
					}
				}
			}

			if (game.currentPlayer() instanceof HumanPlayer) {
				message = ""
						+ game.currentPlayer().getName()
						+ ", click on the country you want to move armies FROM or right click to skip fortification.";
				gameScreen.repaint();
				if (clicked == false) {
					return;
				} else {
					boolean canFortify = false;
					ArrayList<String> borders = game.getMap()
							.getCountryBorders(chosenCountry);
					for (int i = 0; i < borders.size(); ++i) {
						if (game.getMap().getCountryOwner(borders.get(i))
								.equals(game.currentPlayer())) {
							canFortify = true;
						}
					}
					if (game.getMap().getCountryArmy(chosenCountry) == 1) {
						canFortify = false;
					}

					if ((game.getMap().getCountryOwner(chosenCountry) == game
							.currentPlayer())
							&& (canFortify)) {
						attackCountry = chosenCountry;
						gameState = FORTIFY_TO;
						clicked = false;
					} else {
						clicked = false;
						JOptionPane
								.showMessageDialog(
										messageBox,
										"That country is not yours or you cannot fortify your territory from it.  Please make another selection or right click to skip fortification.");
						return;
					}
				}
			} else {
				chosenCountry = ((ComputerPlayer) game.currentPlayer())
						.selectOwnedCountry(game.getMap());

				if (chosenCountry != null) {
					fortifyCountry = ((ComputerPlayer) game.currentPlayer())
							.fortify(chosenCountry, game.getMap());

					if (fortifyCountry != null) {
						game.fortifyCountry(chosenCountry, fortifyCountry,
								((ComputerPlayer) game.currentPlayer())
										.moveArmies(chosenCountry, game
												.getMap()));
						message = "" + game.currentPlayer().getName()
								+ " moves armies from " + chosenCountry
								+ " to " + fortifyCountry + ".";
					}
				}
				gameState = TURNIN;
				game.nextPlayer();
			}
			gameScreen.repaint();
		}

		if (gameState == FORTIFY_TO) {
			defendCountry = new String();

			message = "" + game.currentPlayer().getName()
					+ ", click on the country you want to FORTIFY from "
					+ attackCountry
					+ " or right click to cancel fortification.";
			gameScreen.repaint();
			if (clicked == false) {
				return;
			} else {
				boolean isBorder = false;
				ArrayList<String> borders = game.getMap().getCountryBorders(
						attackCountry);
				for (int i = 0; i < borders.size(); ++i) {
					if (borders.get(i).equals(chosenCountry)) {
						isBorder = true;
					}
				}
				if ((game.getMap().getCountryOwner(chosenCountry) == game
						.currentPlayer())
						&& (isBorder)) {
					defendCountry = chosenCountry;
					gameState = PICK_FORTIFY_NUM;
					clicked = false;
				} else {
					clicked = false;
					JOptionPane
							.showMessageDialog(
									messageBox,
									"That country is not yours or can't be fortified from the chosen country.  Please make another selection or right click to skip fortification.");
					return;
				}
			}
			message = "" + game.currentPlayer().getName() + " chose to attack "
					+ defendCountry + " from " + attackCountry + ".";
			gameScreen.repaint();
		}

		if (gameState == PICK_FORTIFY_NUM) {
			if ((!shown) && (clicked == false)) {
				timer.stop(); // restarted in SliderFrame once input received
				sliderFrame.selection((game.getMap().getCountryArmy(
						attackCountry) - 1), false);
				shown = true;
			} else {
				if (numArmies == -1) {
					return;
				} else {
					game.getMap().removeArmies(attackCountry, numArmies);
					game.getMap().addArmies(defendCountry, numArmies);
					game.nextPlayer();
					gameState = TURNIN;
					numArmies = -1;
				}
			}

			gameScreen.repaint();
		}

		if (gameState == WINNER) {
			message = game.currentPlayer().getName() + " WINS!";
			gameScreen.repaint();
		}
	}

	private boolean checkSet() {
		boolean setOkay = false;
		int cannonCards = 0;
		int infantryCards = 0;
		int cavalryCards = 0;

		for (int i = 0; i < set.size(); ++i) {
			int troopType = set.get(i).getTroopType();
			if (troopType == 0)
				++infantryCards;
			if (troopType == 1)
				++cavalryCards;
			if (troopType == 2)
				++cannonCards;
			if (troopType == 3) {
				++infantryCards;
				++cavalryCards;
				++cannonCards;
			}
		}
		if ((cannonCards >= 3)
				|| (infantryCards >= 3)
				|| (cavalryCards >= 3)
				|| ((cannonCards >= 1) && (infantryCards >= 1) && (cavalryCards >= 1))) {
			setOkay = true;
		}
		return setOkay;
	}
}
