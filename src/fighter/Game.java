package fighter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;
import net.java.games.input.*;
import net.java.games.input.Controller.Type;

public class Game extends JComponent implements KeyListener {
	public static Helper help = new Helper();
	// screen related variables
	public static final int DEFAULT_SCREEN_SIZE_X = 1200;
	// public static final int DEFAULT_SCREEN_SIZE_Y = 675;
	public static final int DEFAULT_SCREEN_SIZE_Y = (int) (1200 / 1.6);
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static double screenWidth = screenSize.getWidth();
	public static double screenHeight = screenSize.getHeight();
	boolean isnormalscreen = true;

	// types of hitboxes. affects how they are drawn, among other things
	public static final int TYPE_GROUND = 0;
	public static final int TYPE_ATTACK = 1;
	public static final int TYPE_SHIELD = 2;
	public static final int TYPE_HURTBOX = 3;
	// hitbox colours
	public static Color transparentred = new Color(255, 0, 0, 75);
	public static Color transparentgreen = new Color(0, 255, 0, 75);
	public static Color transparentblue = new Color(0, 0, 255, 75);
	public static Color transparentpurple = new Color(100, 0, 100, 50);

	boolean shouldShowHitboxes = false;
	ArrayList<Hitbox> hitboxes = new ArrayList<Hitbox>();
	public static final Hitbox GROUND_HITBOX = new Hitbox(200, 600, 720, 175, TYPE_GROUND);
	public static ArrayList<Hitbox> PLATFORMS = new ArrayList<Hitbox>();

	ArrayList<Character> characters = new ArrayList<Character>();
	ArrayList<AiController> aiList = new ArrayList<AiController>();
	// this controls the screen state
	private int screenState = 2;
	private final static int SCREEN_STATE_INGAME = 0;
	private final static int SCREEN_STATE_ADDCHARACTER = 1;
	private final static int SCREEN_STATE_CHARACTER_SELECT = 2;
	private final static int SCREEN_STATE_CHOOSE_CONTROL_SCHEME = 3;
	private final static int SCREEN_STATE_WIN_SCREEN = 4;
	// character select screen specific variables
	private int charSelectX = DEFAULT_SCREEN_SIZE_X / 2;
	private int charSelectY = DEFAULT_SCREEN_SIZE_Y / 2;
	private HashSet<Integer> keysPressed = new HashSet<Integer>();
	BufferedImage selectorHandImage = initializeImage("img/misc/selectorhand.png", 75 * 2,
			(int) (75 * 0.6818181818) * 2);
	BufferedImage selectorHand2Image = initializeImage("img/misc/selectorhand2.png", 75 * 2,
			(int) (75 * 0.6818181818) * 2);
	BufferedImage controlChoiceImage = initializeImage("img/misc/controlchoice.png", DEFAULT_SCREEN_SIZE_X * 2,
			DEFAULT_SCREEN_SIZE_Y * 2);
	BufferedImage pressenterImage = initializeImage("img/misc/pressenter.png", DEFAULT_SCREEN_SIZE_X * 2,
			DEFAULT_SCREEN_SIZE_Y * 2);
	BufferedImage sunimg = initializeImage("img/misc/sun.png", 900, 900);
	BufferedImage sun2img = initializeImage("img/misc/sun2.png", 900, 900);
	boolean sunimage1 = true;
	int sunimgcounter = 30;
	Rectangle selectionRect;
	boolean isSelecting = false;
	CharacterIcon selectedIcon = null;

	private int characterSlideNum = 0;
	private int characterSlideNum2 = 1;
	private boolean isEditing = false;
	Controller[] ca;
	static ArrayList<Controller> controllers = new ArrayList<Controller>();
	static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	// images
	BufferedImage mountains = new Image("img/misc/mountains.png").img;
	BufferedImage sky = new Image("img/misc/sky.png").img;
	BufferedImage ground = new Image("img/misc/ground.png").img;
	BufferedImage platform = new Image("img/misc/platform.png").img;
	ArrayList<CharacterIcon> charIcons = new ArrayList<CharacterIcon>();
	ArrayList<Cloud> clouds = new ArrayList<Cloud>();

	// music
	Sound gameStart = new Sound("sound/music/gamestart.wav");
	Sound menuMusic = new Sound("sound/music/menutheme.wav");
	Sound headchalapiano = new Sound("sound/music/headchalapiano.wav");
	Sound headchalaremix = new Sound("sound/music/headchalaremix.wav");
	SoundArray music = new SoundArray(headchalapiano, headchalaremix);

	// misc sounds
	Sound hitsound1 = new Sound("sound/misc/hitsound1.wav");
	Sound hitsound2 = new Sound("sound/misc/hitsound2.wav");
	Sound hitsound3 = new Sound("sound/misc/hitsound3.wav");
	Sound hitsound4 = new Sound("sound/misc/hitsound4.wav");
	Sound hitsound5 = new Sound("sound/misc/hitsound5.wav");
	Sound hitsound6 = new Sound("sound/misc/hitsound6.wav");
	SoundArray hitSounds = new SoundArray(hitsound2, hitsound2, hitsound3, hitsound4, hitsound5, hitsound6);

	boolean canPlayMusic = false;
	boolean musicEnabled = true;
	Character winningPlayer;
	int winScreenCounter = 120;
	double dayTime = 2.3;
	int shakeScreenCounter = 0;

	Character GOE = new Asriel(500, 400, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
			KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE, KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R, this);

	Character GOEAI = new KidGoku(500, 400, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
			KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE, KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R, this);
	AiController GOEAIController = new AiController(GOEAI, this);

	double cameraLocationX = 0;
	double cameraLocationY = 0;

	double cameraLocationVelX = 0;
	double cameraLocationVelY = 0;

	int personAverageX = 0;
	int personAverageY = 0;

	// characters are all kept in an arraylist, some of them will in fact be an
	// extension of the character class
	public Game() throws IOException {

		hitboxes.add(GROUND_HITBOX);
		doControllerThings();

		// GOEAI.disableInput();

		// characters.add(GOE);
		// characters.add(GOEAI);
		// aiList.add(GOEAIController);

		for (Controller control : ca) {
			System.out.println(control.getName());
		}
		addCharIcons();
		initializeConfig();

		PLATFORMS.add(new Hitbox(300, 530, 100, 20, TYPE_GROUND));
		PLATFORMS.add(new Hitbox(720, 530, 100, 20, TYPE_GROUND));
		PLATFORMS.add(new Hitbox(380, 450, 360, 20, TYPE_GROUND));

		initializeEnvironment();
	}

	public void initializeEnvironment() {
		for (int i = 0; i < 3; i++)
			clouds.add(new Cloud(help.randInt(-50, DEFAULT_SCREEN_SIZE_X + 50),
					help.randInt(-50, DEFAULT_SCREEN_SIZE_Y + 50)));

	}

	public void initializeConfig() throws IOException {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get("config/game.txt"));
		} catch (IOException e) {
			System.out.println("Did not find any game.txt file in /config, creating one");
			File dir = new File("config");
			@SuppressWarnings("unused")
			boolean successful = dir.mkdir();
			PrintWriter writer;
			writer = new PrintWriter("config/game.txt");
			lines = Files.readAllLines(Paths.get("config/game.txt"));
		}
		if (lines.size() > 0)
			if (lines.get(0).equals("false")) {
				musicEnabled = false;
			}
	}

	public void addCharIcons() {
		// kid goku
		charIcons.add(new KidGokuIcon(0, characters, this));

		charIcons.add(new AsrielIcon(0, characters, this));

		for (int i = 0; i < charIcons.size(); i++) {
			CharacterIcon icon = charIcons.get(i);
			icon.x = 100 + (150 * i);
			icon.interactRect = icon.interactRect = new Rectangle(icon.x, icon.y, 100, 100);
		}
	}

	public void drawIcons(Graphics g) {
		switch (screenState) {
		case (SCREEN_STATE_CHARACTER_SELECT):

			for (CharacterIcon icon : charIcons) {
				icon.draw(g);

			}
			if (isSelecting)
				for (CharacterIcon icon : charIcons) {
					if (icon.interactRect.intersects(selectionRect)) {
						selectedIcon = icon;
						screenState = SCREEN_STATE_CHOOSE_CONTROL_SCHEME;
						keysPressed.clear();
						break;
					}
				}
			break;
		}
	}

	// the drawing is state dependent, meaning that depending on how the state
	// is set, the rendering will run certain code
	@Override
	public void paintComponent(Graphics g) {
		// TODO the paint method
		if (screenState == SCREEN_STATE_INGAME) {
			if (!music.isAnySoundPlaying()) {
				if (canPlayMusic && musicEnabled)
					music.getRandomSound().play();
				else
					canPlayMusic = true;
			}
		} else {
			music.stopAllSounds();
		}
		if (screenState == SCREEN_STATE_CHARACTER_SELECT || screenState == SCREEN_STATE_CHOOSE_CONTROL_SCHEME) {
			if (!menuMusic.isrunning() && musicEnabled) {
				menuMusic.play();
			}
		} else {
			menuMusic.stop();
		}
		getControllerInput();
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform oldTransform = g2.getTransform();
		graphicsSettings(g2);
		AffineTransform newTransformnoShake = g2.getTransform();
		AffineTransform oldTransform2 = g2.getTransform();
		if (screenState == SCREEN_STATE_INGAME) {
			g2.scale(1.2, 1.2);
			g2.translate(DEFAULT_SCREEN_SIZE_X * -0.08333333333 * 0.625, DEFAULT_SCREEN_SIZE_Y * -0.08333333333);
			g2.translate(0, -20);
			g2.translate(cameraLocationX, cameraLocationY);
		}

		if (!isnormalscreen) {
			// g2.translate(0, (screenWidth / (screenHeight /
			// DEFAULT_SCREEN_SIZE_X) - DEFAULT_SCREEN_SIZE_Y) / 16);
			g2.scale(screenWidth / DEFAULT_SCREEN_SIZE_X, (screenHeight / DEFAULT_SCREEN_SIZE_X) / 0.625);

		}

		g2.setColor(Color.white);
		g2.fillRect(0, 0, WIDTH, HEIGHT);
		switch (screenState) {
		case (SCREEN_STATE_INGAME):
			// basic background stuff to build scene

			applyShakeScreen(g2);
			AffineTransform newTransform = g2.getTransform();
			// creates a new camera transform to set to after it draws the
			// background
			shouldPlayerWin();

			g2.setTransform(oldTransform2);
			drawBackground(g);
			g2.setTransform(oldTransform2);
			// g2.setTransform(newTransformnoShake);
			// g2.translate(-cameraLocationX, -cameraLocationY);
			drawSun(g2);
			// g2.translate(cameraLocationX, cameraLocationY);

			drawMountain(g);

			g2.setTransform(newTransform);

			drawClouds(g2);

			drawGround(g);
			doPlayerPhysics(g);
			doPlayerDrawing(g);
			doAIThings();
			doProjectileThings(g);
			// draws hitboxes, should be after all other drawing code
			drawHitBoxes(g, hitboxes);
			drawPlayerHitboxes(g);

			doDayCycle();
			break;
		case (SCREEN_STATE_ADDCHARACTER):
			g.setColor(Color.WHITE);
			g.drawString("Press 1 to add new character(keyboard)", 20, 20);
			g.drawString("Press 2 to add new control method (controller)", 20, 40);
			g.drawString("Up and down to choose which character to edit", 20, 60);
			g.drawString("Left and right to change which attribute to edit, then press 3 to edit it", 20, 80);
			g.drawString("Current amount of characters: " + characters.size()
					+ ", you are viewing the inputs for number: " + characterSlideNum, 20, 100);
			g.drawString("Press 0 to erase a character", 20, 120);
			if (characters.size() > 0) {
				if (!characters.get(characterSlideNum).getIsUsingController()) {
					g.drawString(String.valueOf(characters.get(characterSlideNum).getUpKey()), 60, 200);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getDownKey()), 120, 220);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getLeftKey()), 180, 240);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getRightKey()), 240, 260);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getJumpKey()), 300, 280);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getAttackKey()), 360, 300);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getModifierKey()), 420, 320);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getShieldKey()), 480, 340);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getKeyGrab()), 541, 360);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getKeySpecial()), 600, 380);

					g.drawString("^", 60 * characterSlideNum2, 220 + (20 * characterSlideNum2));
				} else {
					g.drawString(String.valueOf(characters.get(characterSlideNum).getControllerName()), 60, 200);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getAxisNameX()), 120, 220);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getAxisNameY()), 180, 240);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getAxisDeadZone()), 240, 260);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getAxisMidpoint()), 300, 280);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getJumpButton()), 360, 300);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getAttackButton()), 420, 320);

					g.drawString(String.valueOf(characters.get(characterSlideNum).getMoveAxisNameRX()), 480, 340);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getMoveAxisNameRY()), 540, 360);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getMoveAxisNameZ()), 600, 380);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getMoveAxisNameRZ()), 660, 400);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getButtonGrab()), 720, 420);
					g.drawString(String.valueOf(characters.get(characterSlideNum).getButtonSpecial()), 780, 440);
					g.drawString("^", 60 * characterSlideNum2, 220 + (20 * characterSlideNum2));
					controllerConfigChange(characters.get(characterSlideNum));
				}
			}

			break;
		// TODO the character select screen
		case (SCREEN_STATE_CHARACTER_SELECT):
			
			//g.setColor(new Color(149, 214, 223));
			g.setColor(new Color(150, 150, 150));
		g.fillRect(0, 0, DEFAULT_SCREEN_SIZE_X, DEFAULT_SCREEN_SIZE_Y);
			if (keysPressed.contains(KeyEvent.VK_UP))
				charSelectY -= 15;
			if (keysPressed.contains(KeyEvent.VK_DOWN))
				charSelectY += 15;
			if (keysPressed.contains(KeyEvent.VK_LEFT))
				charSelectX -= 15;
			if (keysPressed.contains(KeyEvent.VK_RIGHT))
				charSelectX += 15;

			if (keysPressed.contains(KeyEvent.VK_SPACE))
				isSelecting = true;
			else
				isSelecting = false;
			
			drawPlayersSelection(g);
			
			drawIcons(g);
			
			if (!isSelecting) {
				g.drawImage(selectorHandImage, charSelectX, charSelectY, 75, (int) (75 * 0.6818181818), this);
				selectionRect = new Rectangle(charSelectX - 10, charSelectY - 10, 35, 35);
			} else {
				g.drawImage(selectorHand2Image, charSelectX, charSelectY, 75, (int) (75 * 0.6818181818), this);
				selectionRect = new Rectangle(charSelectX - 10, charSelectY - 10, 35, 35);
			}

			if (characters.size() > 0) {
				g.drawImage(pressenterImage, 0, 0, DEFAULT_SCREEN_SIZE_X, DEFAULT_SCREEN_SIZE_Y, null);
			}

			break;
		case (SCREEN_STATE_CHOOSE_CONTROL_SCHEME):
			g.setColor(Color.BLACK);

			g.drawImage(controlChoiceImage, 0, 0, DEFAULT_SCREEN_SIZE_X, DEFAULT_SCREEN_SIZE_Y, null);

			if (selectedIcon.isChoosingKeyBoard) {
				g.fillOval(100, (DEFAULT_SCREEN_SIZE_Y / 2) - 50, 50, 50);
			} else {
				g.fillOval(100 + DEFAULT_SCREEN_SIZE_X / 2, (DEFAULT_SCREEN_SIZE_Y / 2) - 50, 50, 50);
			}
			break;
		case (SCREEN_STATE_WIN_SCREEN):
			g.setColor(Color.white);
			g.drawString("The winner is: " + winningPlayer.playerName, 20, 40);
			winScreenCounter--;
			if (winScreenCounter <= 0) {
				screenState = SCREEN_STATE_CHARACTER_SELECT;
				characters.clear();
				aiList.clear();
				winScreenCounter = 120;
				cameraLocationX = 0;
				cameraLocationY = 0;
			}
			break;
		}

		g2.setTransform(oldTransform);
		if (!isnormalscreen) {
			// g2.translate(0, (screenWidth / (screenHeight /
			// DEFAULT_SCREEN_SIZE_X) - DEFAULT_SCREEN_SIZE_Y) / 16);
			g2.scale(screenWidth / DEFAULT_SCREEN_SIZE_X, (screenHeight / DEFAULT_SCREEN_SIZE_X) / 0.625);

		}

		if (screenState == SCREEN_STATE_INGAME) {
			overlayColour(g);
			drawPlayerPercentage(g);

		}

		drawBlackBars(g);

		g2.setTransform(oldTransform);

	}

	public void doDayCycle() {
		dayTime += .001;
	}

	public void drawBlackBars(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(-100, -100, DEFAULT_SCREEN_SIZE_X + 100, 100);
		g.fillRect(-100, DEFAULT_SCREEN_SIZE_Y, DEFAULT_SCREEN_SIZE_X + 100, 100);
		g.fillRect(-100, -100, 100, 100);
		g.fillRect(DEFAULT_SCREEN_SIZE_X, -100, 100, 100);
	}

	public void graphicsSettings(Graphics2D g2) {

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);

	}

	public void doPlayerDrawing(Graphics g) {
		int totalX = 0;
		int totalY = 0;
		int averageVelX = 0;
		int totalVelX = 0;
		int averageVelY = 0;
		int totalVelY = 0;
		for (Character person : characters) {
			if (person.getMyGame() != this)
				person.setMyGame(this);
			person.draw(g);
			if (person.getLives() > 0) {
				totalX += person.getX();
				totalY += person.getY();
				totalVelX += person.getVelX();
				totalVelY += person.getVelY();
			}
		}

		personAverageX = totalX / characters.size();
		personAverageY = totalY / characters.size();
		averageVelX = totalVelX / characters.size();
		averageVelY = totalVelY / characters.size();
		/*
		 * int tempCameraLocationX = (int)(personAverageX -
		 * DEFAULT_SCREEN_SIZE_X / 2.3); int tempCameraLocationY =
		 * personAverageY - DEFAULT_SCREEN_SIZE_Y / 2;
		 */
		int tempCameraLocationX = ((int) ((personAverageX / 2) - (DEFAULT_SCREEN_SIZE_X / 4.3)));
		int tempCameraLocationY = personAverageY - DEFAULT_SCREEN_SIZE_Y / 2;
		/*
		 * if (personAverageX < 444 && cameraLocationVelX > .5)
		 * cameraLocationVelX = 1; if (personAverageX > 644 &&
		 * cameraLocationVelX < -.5) cameraLocationVelX = -1;
		 */
		// decided to put stage movement code in here cause it is relient on the
		// players location
		// if the players are on the left side of the stage, then give them more
		// space on the opposite side
		if (personAverageX > 744) {
			if (cameraLocationX < 75)
				cameraLocationVelX += .05;
			else
				cameraLocationVelX /= 1.1;

		} else if (personAverageX < 344) {
			if (cameraLocationX > -75)
				cameraLocationVelX -= .05;
			else
				cameraLocationVelX /= 1.1;
		} else {

			if (cameraLocationX < -5)
				cameraLocationVelX += .03;
			else if (cameraLocationX > 5)
				cameraLocationVelX -= .03;
			else
				cameraLocationVelX /= 1.2;
			if (cameraLocationX < -75)
				cameraLocationVelX = .5;
			if (cameraLocationX > 75)
				cameraLocationVelX = -.5;
		}
		// decided not to alter to y location. it just didnt work
		/*
		 * if (cameraLocationY > -15) cameraLocationVelY -= .01; else if
		 * (cameraLocationY < 15) cameraLocationVelY += .01;
		 * 
		 * if (cameraLocationY < -15 || cameraLocationY > 15) if
		 * (Math.abs(cameraLocationVelY) > 0) { cameraLocationVelY /= 1.05; }
		 */
		cameraLocationX += cameraLocationVelX;
		// cameraLocationY += cameraLocationVelY;
		g.setColor(Color.red);
		if (shouldShowHitboxes) {
			g.drawRect(((int) cameraLocationX + DEFAULT_SCREEN_SIZE_X / 2) - 50,
					((int) cameraLocationY + DEFAULT_SCREEN_SIZE_Y / 2) - 20, 40, 40);
		}
	}

	public void doPlayerPhysics(Graphics g) {
		for (Character person : characters) {

			Rectangle groundChecker = new Rectangle((int) person.getX(), (int) person.getY() + 1,
					(int) person.getWidth(), (int) person.getHeight());

			boolean isOnGround = false;
			boolean isOnPlatform = false;
			if (checkCollision(groundChecker, hitboxes.get(0).getRect())
					&& person.getY() + person.getHeight() - 1 < hitboxes.get(0).getY()) {
				isOnGround = true;
			}

			for (Hitbox platform : PLATFORMS) {
				if (groundChecker.intersects(platform.getRect())) {
					if (person.getY() + person.getH() - 10 < platform.getY()) {
						if (person.getVelY() >= 0 && person.getState() != Character.STATE_CROUCH) {
							isOnPlatform = true;
							person.isOnPlatform = true;
						} else
							person.isOnPlatform = false;
						person.setY(platform.getY() - person.getH());
					}
				}

			}

			if (!isOnGround && !isOnPlatform)
				person.isGrounded = false;
			else {
				person.isGrounded = true;
			}

		}
		checkForAndExecutePlayerHitDectection((Graphics2D) g);
	}

	public void doAIThings() {
		for (AiController ai : aiList) {
			ai.update();
		}
	}

	public BufferedImage initializeImage(String url, int scaleX, int scaleY) {
		try {
			BufferedImage buff = getScaledInstance(new Image(url).img, scaleX, scaleY,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);
			return buff;
		} catch (NullPointerException e) {
			System.out.println("error");

			return null;
		}

	}

	// not my code. only have some idea of how this works
	public BufferedImage getScaledInstance(BufferedImage img, int targetWidth, int targetHeight, Object hint,
			boolean higherQuality) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
			System.out.println("1");
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}
		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}
			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}
			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();
			ret = tmp;
			System.out.println("2");
		} while (w != targetWidth || h != targetHeight);
		return ret;
	}

	public void drawPlayerPercentage(Graphics g) {
		// this is not just for percentage.it is the player ui
		Graphics2D g2 = (Graphics2D) g;

		int getSpaceBetweenNumbers = (int) (screenHeight / characters.size());
		for (int i = 0; i < characters.size(); i++) {
			int placementX = getSpaceBetweenNumbers / 2 + (getSpaceBetweenNumbers * i + 1);

			RoundRectangle2D rounded = new RoundRectangle2D.Double(placementX - 50, 650, 120, 60, 30, 30);
			RoundRectangle2D rounded2 = new RoundRectangle2D.Double(placementX - 60, 640, 140, 80, 30, 30);
			g.setColor(new Color(255, 255, 255, 150));
			g2.fill(rounded2);
			g.setColor(new Color(149, 214, 223, 150));
			g2.fill(rounded);

			Character person = characters.get(i);
			g.setColor(new Color(0, 0, 0, 200));
			g.setFont(new Font("Futura", Font.PLAIN, 15));
			g.drawString(String.valueOf((int) person.getpercentage()) + " %", placementX + 25, 675);

			String output = person.name.substring(0, 1).toUpperCase() + person.name.substring(1);
			g.drawString(output, placementX - 40, 700);

			g.drawString("P" + (i + 1), placementX + 25, 700);

			for (int o = 0; o < person.getLives(); o++) {
				g.drawImage(person.livesIconImage, placementX + (o * 22) - 45, 660, 20, 20, null);
			}
		}
	}
	
	public void drawPlayersSelection(Graphics g){
		//int space = (int) (screenWidth / characters.size());
		for(int i = 0; i < characters.size();i++){
			g.setColor(Color.red);
				g.fillRect(((DEFAULT_SCREEN_SIZE_X / 5 ))/2 - 20 + (((DEFAULT_SCREEN_SIZE_X / 5) + 10) * i), (int) (DEFAULT_SCREEN_SIZE_Y * .70), (int) (DEFAULT_SCREEN_SIZE_X / 5), (int) (DEFAULT_SCREEN_SIZE_Y * .25));

			/*
			if(i == 0){
				g.fillRect(50, (int) (DEFAULT_SCREEN_SIZE_Y * .70), (int) (DEFAULT_SCREEN_SIZE_Y / 5), (int) (DEFAULT_SCREEN_SIZE_Y * .25));
			}
			*/
		}
	}
	
	
	public void shouldPlayerWin() {
		int howManyAtZero = 0;
		for (Character person : characters) {
			if (person.getLives() <= 0)
				howManyAtZero++;
		}
		if (characters.size() > 1)
			if (howManyAtZero == characters.size() - 1) {
				screenState = SCREEN_STATE_WIN_SCREEN;
				getWinningPlayer();
			}
		if (characters.size() == 1) {
			if (howManyAtZero == 1) {
				screenState = SCREEN_STATE_WIN_SCREEN;
				getWinningPlayer();
			}
		}
	}

	public void getWinningPlayer() {
		if (characters.size() > 0)
			for (Character person : characters) {
				if (person.getLives() > 0) {
					winningPlayer = person;
				}
			}
		if (characters.size() == 1)
			winningPlayer = characters.get(0);
	}

	public void doProjectileThings(Graphics g) {
		for (Projectile proj : projectiles) {
			proj.draw(g);
			for (Character player : characters) {
				if (proj.getMyHitbox().getLinkedCharacter() != player)
					if (proj.getMyHitbox().getRect().intersects(player.getHurtbox().getRect())) {

						// if
						// (!proj.getMyHitbox().playerHitList.contains(player))
						// {
						if (player.state != Character.STATE_HITSTUN && player.state != Character.STATE_GRABBED) {
							if (!player.getShield().intersects(proj.getMyHitbox().getRect())) {
								player.applyDamage(proj.getMyHitbox().getDamage());

									player.applyHitstun(proj.getMyHitbox().getHitstunLength());
									player.applyKnockback(proj.getMyHitbox().getKnockbackX(),
											proj.getMyHitbox().getKnockbackX(), 1);

							} else {
								player.setShieldWidth(player.getShieldWidth() - proj.getMyHitbox().getShieldDamage());
							}
							proj.getMyHitbox().playerHitList.add(player);
						}
					}
			}

		}
		for (Projectile proj : projectiles) {
			if (proj.x < -200 || proj.y < -200 || proj.lifetime <= 0) {
				projectiles.remove(proj);
				break;
			}
			if (proj.x > DEFAULT_SCREEN_SIZE_X + 200 || proj.y > DEFAULT_SCREEN_SIZE_Y + 200) {
				projectiles.remove(proj);
				break;
			}
		}
	}

	public void drawBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, 1200, (int) (1200 / 1.6));
		if (isnormalscreen)
			g.drawImage(sky, 0, 0, DEFAULT_SCREEN_SIZE_X, (int) (DEFAULT_SCREEN_SIZE_X / 1.6), null);
		else
			g.drawImage(sky, 0, 0, (int) screenWidth, (int) screenHeight, null);
	}

	public void drawSun(Graphics2D g2) {
		AffineTransform old = g2.getTransform();
		if (!isnormalscreen) {
			// g2.translate(0, (screenWidth / (screenHeight /
			// DEFAULT_SCREEN_SIZE_X) - DEFAULT_SCREEN_SIZE_Y) / 16);
			g2.scale(screenWidth / DEFAULT_SCREEN_SIZE_X, (screenHeight / DEFAULT_SCREEN_SIZE_X) / 0.625);

		}

		sunimgcounter--;
		if (sunimgcounter <= 0) {
			sunimage1 = !sunimage1;
			sunimgcounter = 30;
		}

		if (sunimage1)
			g2.drawImage(sunimg, (int) (dayTime * 300) - 1100, (int) (Math.sin(dayTime) * 400) + 500, 400, 400, this);

		else
			g2.drawImage(sun2img, (int) (dayTime * 300) - 1100, (int) (Math.sin(dayTime) * 400) + 500, 400, 400, this);

		if (dayTime > 5.8)
			dayTime = 2.3;

		g2.setTransform(old);
	}

	public void overlayColour(Graphics g) {
		g.setColor(new Color(0, 0, 180, 80 - (int) (Math.sin(dayTime - 2.5) * 30)));

		if (isnormalscreen)
			g.fillRect(0, 0, DEFAULT_SCREEN_SIZE_X, DEFAULT_SCREEN_SIZE_Y);
		else
			g.fillRect(0, 0, (int) screenWidth, (int) screenHeight);
	}

	public void drawMountain(Graphics g) {
		if (isnormalscreen)
			g.drawImage(mountains, 0, 0, DEFAULT_SCREEN_SIZE_X, (int) (DEFAULT_SCREEN_SIZE_X / 1.6), null);
		else
			g.drawImage(mountains, 0, 0, (int) screenWidth, (int) screenHeight, null);
	}

	public void drawClouds(Graphics2D g2) {
		g2.translate(-cameraLocationX, -cameraLocationY);
		Random rand = new Random();
		int randInt = rand.nextInt(300);
		if (randInt == 27) {
			clouds.add(new Cloud(-300, help.randInt(-50, DEFAULT_SCREEN_SIZE_Y + 50)));
		}
		for (Cloud current : clouds) {
			current.draw(g2);

		}
		// second loop for deleting
		for (Cloud current : clouds) {

			if (current.x > DEFAULT_SCREEN_SIZE_X + 300) {
				clouds.remove(current);
				break;
			}
		}
		g2.translate(-cameraLocationX, -cameraLocationY);
	}

	public void drawGround(Graphics g) {
		g.drawImage(ground, (int) GROUND_HITBOX.getX(), ((int) GROUND_HITBOX.getY() - 43),
				(int) GROUND_HITBOX.getWidth(), (int) GROUND_HITBOX.getHeight() + 43, null);
		for (Hitbox platform : PLATFORMS) {
			g.drawImage(this.platform, (int) platform.getX(), (int) platform.getY() - 3, (int) platform.getWidth(),
					(int) platform.getHeight(), null);
		}
	}

	public void drawPlayerHitboxes(Graphics g) {
		for (Character person : characters) {
			Graphics2D g2 = (Graphics2D) g;
			if (shouldShowHitboxes) {
				for (int i = 0; i < person.hitboxes.size(); i++) {
					g.setColor(Game.transparentred);
					AttackHitbox tempbox = person.hitboxes.get(i);

					if (tempbox.isActive)
						g2.fill(tempbox.getRect());

				}
				g.setColor(Game.transparentpurple);
				g2.fill(person.getHurtbox().getRect());
				g2.setColor(new Color(255, 0, 0, 30));
				if (person.getGrabBox() != null)
					g2.fill(person.getGrabBox());

				Point2D centre = new Point2D.Double(DEFAULT_SCREEN_SIZE_X / 2, DEFAULT_SCREEN_SIZE_Y / 2);
				g.setColor(Color.red);
				g.drawLine((int) (centre.getX() - 20), (int) (centre.getY()), (int) (centre.getX() + 20),
						(int) (centre.getY()));
				g.drawLine((int) (centre.getX()), (int) (centre.getY() - 20), (int) (centre.getX()),
						(int) (centre.getY() + 20));
			}
		}
	}

	public void drawHitBoxes(Graphics g, ArrayList<Hitbox> hitboxList) {
		if (shouldShowHitboxes) {
			for (Hitbox myhitbox : hitboxList) {
				switch ((int) myhitbox.getType()) {
				case TYPE_GROUND:
					g.setColor(transparentgreen);
					break;
				case TYPE_ATTACK:
					g.setColor(transparentred);
					break;
				case TYPE_SHIELD:
					g.setColor(transparentblue);
					break;
				case TYPE_HURTBOX:
					g.setColor(transparentpurple);
					break;
				}
				g.fillRect((int) myhitbox.getX(), (int) myhitbox.getY(), (int) myhitbox.getWidth(),
						(int) myhitbox.getHeight());
			}
			for (Hitbox box : PLATFORMS) {
				Graphics2D g2 = (Graphics2D) g;
				g2.fill(box.getRect());
			}
		}
	}

	// self explanatory naming FTW
	public void checkForAndExecutePlayerHitDectection(Graphics2D g2) {

		// person1 is the person getting hit and hitbox is the hitbox doing the
		// hitting

		for (Character person1 : characters)
			for (Character person2 : characters)
				for (AttackHitbox hitbox : person2.hitboxes)
					if (checkCollision(person1.getHurtbox().getRect(), hitbox.getRect()))
						if (!person1.equals(hitbox.getLinkedCharacter()) && !hitbox.playerHitList.contains(person1)) {
							if (person1.getState() != Character.STATE_DODGE && person2.getGrabBox() == null
									&& person1.getRespawnTimer() <= 0) {

								boolean shouldapplydamage = false;
								// new approach, divides the hitbox into 6, each
								// of which check first if they are intersecting
								// with the hitbox
								// then check if it is not colliding with the
								// shield
								for (int i = 1; i < 3; i++) {
									for (int o = 1; o < 3; o++) {

										Rectangle2D miniRect = new Rectangle2D.Double(
												(hitbox.getX() + (hitbox.getWidth() * i) / 3) - hitbox.getWidth() / 3,
												(hitbox.getY() + (hitbox.getHeight() * o) / 3) - hitbox.getHeight() / 3,
												hitbox.getWidth() / 3, hitbox.getHeight() / 3);

										if (miniRect.intersects(hitbox.getRect())) {

											if (!person1.getShield().intersects(miniRect)) {
												shouldapplydamage = true;
											} else {
												person1.setShieldWidth(
														(person1.getShieldWidth() - hitbox.getShieldDamage() / 100));
											}
										}
									}
								}

								if (shouldapplydamage) {
									person1.applyKnockback(hitbox.getKnockbackX() * person2.getDirection(),
											hitbox.getKnockbackY(), hitbox.getLinkedCharacter().getDirection());

									person1.applyHitstun(hitbox.getHitstunLength());
									person1.applyDamage(hitbox.getDamage());
									hitbox.playerHitList.add(person1);
									hitSounds.getRandomSound().play();
									person1.hurtsounds.stopAllSounds();
									person1.hurtsounds.getRandomSound().play();
									shakeScreen(1 + (int) (person1.percent / 10) + (int) (hitbox.getDamage() / 10));
								}
							}
						}
	}

	public static boolean checkCollision(Rectangle rect1, Rectangle rect2) {
		if (rect1.intersects(rect2))
			return true;
		return false;
	}

	public ArrayList<Character> getCharacters() {
		return characters;
	}

	// below here is all the controlling code keep it that way
	public void doControllerThings() {
		ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
		for (int i = 0; i < ca.length; i++) {
			if (ca[i].getType() == Type.GAMEPAD || ca[i].getType() == Type.STICK) {
				controllers.add(ca[i]);
				/* Get the name of the controller */
				System.out.println(ca[i].getName());
				System.out.println("Type: " + ca[i].getType().toString());

				/* Get this controllers components (buttons and axis) */
				Component[] components = ca[i].getComponents();
				System.out.println("Component Count: " + components.length);
				for (int j = 0; j < components.length; j++) {

					/* Get the components name */
					System.out.println("Component " + j + ": " + components[j].getName());
					System.out.println("    Identifier: " + components[j].getIdentifier().getName());
					System.out.print("    ComponentType: ");
					if (components[j].isRelative()) {
						System.out.print("Relative");
					} else {
						System.out.print("Absolute");
					}
					if (components[j].isAnalog()) {
						System.out.print(" Analog");
					} else {
						System.out.print(" Digital");
					}
				}
			}
			System.out.println("");
			System.out.println("----");
		}
	}

	public void shakeScreen(int time) {
		shakeScreenCounter = time;
	}

	public void applyShakeScreen(Graphics2D g2) {
		if (shakeScreenCounter > 0) {
			shakeScreenCounter--;
			Random rand = new Random();
			g2.translate(-1, -1);
			g2.scale(1.005, 1.005);

			g2.translate(rand.nextInt(4) - 2, rand.nextInt(4) - 2);
		}
	}

	public void getControllerInput() {
		double axisXinput = 0;
		for (Controller currentController : controllers) {
			Component[] components = currentController.getComponents();

			boolean shoulddelete = currentController.poll();
			if (!shoulddelete) {
				controllers.remove(currentController);
				break;
			}
			for (Component comp : components) {
				// comp.getPollData() returns an float between 0 and 1 that says
				// how far a button or axis has been pushed
				for (Character person : characters) {
					if (person.getIsUsingController()) {
						if (person.getControllerName().equals(currentController.getName())
								&& person.getPortNum() == currentController.getPortNumber()) {
							if (person.getAxisNameX().equals(comp.getName())) {
								// code for the horizontal movement stick
								axisXinput = comp.getPollData();
								if ((Math.abs(comp.getPollData()) > person.getAxisDeadZone())) {
									if ((Math.abs(comp.getPollData()) < (float) person.getAxisMidpoint()))
										person.setAxisHalfway(true);
									else
										person.setAxisHalfway(false);

									if (comp.getPollData() > 0) {
										person.setAxisRight(true);
										person.setAxisLeft(false);

									}

									if (comp.getPollData() < 0) {
										person.setAxisLeft(true);
										person.setAxisRight(false);
									}
									if (Math.abs(comp.getPollData()) > person.getAxisMidpoint())
										person.setAxisDown(false);
								} else {
									person.setAxisRight(false);
									person.setAxisLeft(false);
								}
							}
							if (person.getAxisNameY().equals(comp.getName())) {
								// code for the vertical movement stick
								if ((Math.abs(comp.getPollData()) > person.getAxisDeadZone())) {

									if (comp.getPollData() > 0 && Math.abs(axisXinput) < person.getAxisMidpoint())
										person.setAxisDown(true);

									if (comp.getPollData() < 0)
										person.setAxisUp(true);
								} else {
									person.setAxisUp(false);
									person.setAxisDown(false);
								}

							}
							// jump button
							if (person.getJumpButton().equals(comp.getName())) {
								boolean hasPressedThisFrame = false;
								if (comp.getPollData() == 1.0) {
									person.jump();
									person.cycleArray(true, person.jumpKeyDownHistory);
								}
							}
							// attack button
							if (person.getAttackButton().equals(comp.getName())) {
								if (comp.getPollData() == 1.0) {
									person.chooseAttack(currentController);
									person.cycleArray(true, person.attackKeyDownHistory);
								}

								if (comp.getPollData() != 1.0)
									person.setIsAttackButtonDown(false);
							}
							if (person.getButtonGrab().equals(comp.getName())) {
								if (comp.getPollData() == 1 && person.isGrounded) {
									if (person.getState() == Character.STATE_NEUTRAL)
										person.grab();
								}
							}
							if (comp.getName() == person.getButtonSpecial()) {
								if (Math.abs(comp.getPollData()) > person.getAxisDeadZone())
									person.cycleArray(true, person.specialKeyDownHistory);
							}
						}
					}
				}
			}
		}
	}

	public void controllerConfigChange(Character person) {
		if (isEditing) {
			for (Controller currentController : controllers) {
				currentController.poll();
				for (Component comp : currentController.getComponents()) {
					switch (characterSlideNum2) {
					case (1):
						if (comp.getPollData() > .5) {
							boolean canChange = true;
							for (Character person2 : characters) {
								if (person.getMyController() == currentController)
									canChange = false;
							}
							person.changeController(currentController);
							isEditing = false;
						}
						break;
					case (2):
						if (comp.getPollData() > comp.getDeadZone()) {
							person.setAxisNameX(comp.getName());
							isEditing = false;
						}
						break;
					case (3):
						if (comp.getPollData() > comp.getDeadZone()) {
							person.setAxisNameY(comp.getName());
							isEditing = false;
						}
						break;
					case (4):
						if (comp.getPollData() > comp.getDeadZone()) {
							person.setNewDeadZone(comp.getDeadZone());
							isEditing = false;
						}
						break;
					case (5):
						if (comp.getPollData() > comp.getDeadZone()) {
							person.setAxisMidpoint(comp.getPollData());
							isEditing = false;
						}
						break;
					case (6):
						if (comp.getPollData() > comp.getDeadZone() && !comp.getName().equals(person.getAxisNameX())
								&& !comp.getName().equals(person.getAxisNameY())) {
							person.setJumpButton(comp.getName());
							isEditing = false;
						}
						break;
					case (7):
						if (comp.getPollData() > comp.getDeadZone() && !comp.getName().equals(person.getAxisNameX())
								&& !comp.getName().equals(person.getAxisNameY())) {
							person.setAttackButton(comp.getName());
							isEditing = false;
						}
						break;
					case (8):
						if (comp.getPollData() > comp.getDeadZone()) {
							person.setMoveAxisNameRX(comp.getName());
							isEditing = false;
						}
						break;
					case (9):
						if (comp.getPollData() > comp.getDeadZone()) {
							person.setMoveAxisNameRY(comp.getName());
							isEditing = false;
						}
						break;
					case (10):
						if (comp.getPollData() > comp.getDeadZone()) {
							person.setMoveAxisNameZ(comp.getName());
							isEditing = false;
						}
						break;
					case (11):
						if (comp.getPollData() > comp.getDeadZone()) {
							person.setMoveAxisNameRZ(comp.getName());
							isEditing = false;
						}
						break;
					case (12):
						if (comp.getPollData() > comp.getDeadZone()) {
							person.setButtonGrab(comp.getName());
							isEditing = false;
						}
						break;
					case (13):
						if (comp.getPollData() > comp.getDeadZone()) {
							person.setButtonSpecial(comp.getName());

							isEditing = false;
						}
						break;
					}

				}
			}
		}

	}

	// TODO key pressing code, should be in it's own area, unbroken
	@Override
	public void keyTyped(KeyEvent e) {
		// relates to text input in text input fields so not in use
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// depending on the state, different keys are checked for
		switch (screenState) {

		case (SCREEN_STATE_INGAME):
			for (Character person : characters) {
				person.keysPressed.add(e.getKeyCode());
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				screenState = SCREEN_STATE_ADDCHARACTER;
			// in game play code above here
			if (e.getKeyCode() == KeyEvent.VK_L) {
				System.out.println(cameraLocationX);
				System.out.println(cameraLocationY);
			}
			break;

		case (SCREEN_STATE_ADDCHARACTER):

			if (e.getKeyCode() == KeyEvent.VK_1) {
				characters.add(new Character(500, 400, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
						KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT, KeyEvent.VK_SPACE, KeyEvent.VK_Q, KeyEvent.VK_W,
						KeyEvent.VK_E, KeyEvent.VK_R, this));

				characterSlideNum = 0;
				characterSlideNum2 = 1;
			}
			if (e.getKeyCode() == KeyEvent.VK_2) {
				characters.add(new Character(300, 275, "Xbox 360 Wired Controller", "x", "y", "rx", "ry", "z", "rz", .8,
						.2, "1", "2", "3", "5", this));
				characterSlideNum = 0;
				characterSlideNum2 = 1;
			}
			if (e.getKeyCode() == KeyEvent.VK_0) {
				characters.remove(characterSlideNum);
				characterSlideNum = 0;
				characterSlideNum2 = 1;
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				screenState = SCREEN_STATE_INGAME;
			if (characters.size() > 0) {
				if (!isEditing) {
					if (e.getKeyCode() == KeyEvent.VK_UP && characterSlideNum > 0) {
						characterSlideNum--;
						if (!characters.get(characterSlideNum).getIsUsingController() && characterSlideNum2 > 7)
							characterSlideNum2 = 7;
					}
					if (e.getKeyCode() == KeyEvent.VK_DOWN && characterSlideNum < characters.size() - 1) {
						characterSlideNum++;
						if (!characters.get(characterSlideNum).getIsUsingController() && characterSlideNum2 > 7)
							characterSlideNum2 = 7;
					}
					if (e.getKeyCode() == KeyEvent.VK_LEFT && characterSlideNum2 > 1) {
						characterSlideNum2--;
					}
					if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

						if (!characters.get(characterSlideNum).getIsUsingController() && characterSlideNum2 < 10)
							characterSlideNum2++;
						if (characters.get(characterSlideNum).getIsUsingController() && characterSlideNum2 < 13)
							characterSlideNum2++;
					}

					if (e.getKeyCode() == KeyEvent.VK_ENTER)
						isEditing = true;
				} else {
					if (!characters.get(characterSlideNum).getIsUsingController()) {
						if (characterSlideNum2 == 1)
							characters.get(characterSlideNum).changecontrols(e.getKeyCode(), -1, -1, -1, -1, -1, -1, -1,
									-1, -1);
						if (characterSlideNum2 == 2)
							characters.get(characterSlideNum).changecontrols(-1, -e.getKeyCode(), -1, -1, -1, -1, -1,
									-1, -1, -1);
						if (characterSlideNum2 == 3)
							characters.get(characterSlideNum).changecontrols(-1, -1, e.getKeyCode(), -1, -1, -1, -1, -1,
									-1, -1);
						if (characterSlideNum2 == 4)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, e.getKeyCode(), -1, -1, -1, -1,
									-1, -1);
						if (characterSlideNum2 == 5)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, e.getKeyCode(), -1, -1, -1,
									-1, -1);
						if (characterSlideNum2 == 6)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, -1, e.getKeyCode(), -1, -1,
									-1, -1);
						if (characterSlideNum2 == 7)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, -1, -1, e.getKeyCode(), -1,
									-1, -1);
						if (characterSlideNum2 == 8)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, -1, -1, -1, e.getKeyCode(),
									-1, -1);
						if (characterSlideNum2 == 9)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, -1, -1, -1, -1,
									e.getKeyCode(), -1);
						if (characterSlideNum2 == 10)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, -1, -1, -1, -1, -1,
									e.getKeyCode());
						isEditing = false;
					} else {

					}
				}
			}
			break;
		case SCREEN_STATE_CHARACTER_SELECT:
			keysPressed.add(e.getKeyCode());
			if (e.getKeyCode() == KeyEvent.VK_ENTER && characters.size() > 0) {
				screenState = SCREEN_STATE_INGAME;
				keysPressed.clear();
				menuMusic.stop();
				gameStart.play();
			}
			break;
		case (SCREEN_STATE_CHOOSE_CONTROL_SCHEME):
			if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)
				selectedIcon.isChoosingKeyBoard = !selectedIcon.isChoosingKeyBoard;
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (characters.size() < 4)
					selectedIcon.addCharacter();
				screenState = SCREEN_STATE_CHARACTER_SELECT;
				keysPressed.clear();
				selectedIcon = null;
			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				screenState = SCREEN_STATE_CHARACTER_SELECT;
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (characters.size() < 4) {
					selectedIcon.isAiController = true;
					selectedIcon.addCharacter();
					selectedIcon.isAiController = false;
				}
				screenState = SCREEN_STATE_CHARACTER_SELECT;
				keysPressed.clear();
				selectedIcon = null;
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (screenState) {
		case (SCREEN_STATE_INGAME):
			for (Character person : characters) {
				if (person.keysPressed.contains(e.getKeyCode()))
					person.keysPressed.remove(e.getKeyCode());
			}
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				shouldShowHitboxes = !shouldShowHitboxes;
			}
			break;

		case SCREEN_STATE_CHARACTER_SELECT:
			keysPressed.remove(e.getKeyCode());
			break;
		}
	}

}
