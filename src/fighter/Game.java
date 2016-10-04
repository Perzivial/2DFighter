package fighter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JComponent;
import net.java.games.input.*;
import net.java.games.input.Controller.Type;

public class Game extends JComponent implements KeyListener {
	// screen related variables
	public static final int DEFAULT_SCREEN_SIZE_X = 1200;
	public static final int DEFAULT_SCREEN_SIZE_Y = 675;
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static double screenWidth = screenSize.getWidth();
	static double screenHeight = screenSize.getHeight();
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

	boolean shouldShowHitboxes = true;
	ArrayList<Hitbox> hitboxes = new ArrayList<Hitbox>();
	public static final Hitbox GROUND_HITBOX = new Hitbox(200, 500, 720, 175, TYPE_GROUND);

	ArrayList<Character> characters = new ArrayList<Character>();
	Character GOE = new KidGoku(500, 400, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
			KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE, KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R, this);
	private int screenState = 0;
	private final static int SCREEN_STATE_INGAME = 0;
	private final static int SCREEN_STATE_ADDCHARACTER = 1;
	private final static int SCREEN_STATE_CHARACTER_SELECT = 2;

	private int characterSlideNum = 0;
	private int characterSlideNum2 = 1;
	private boolean isEditing = false;
	Controller[] ca;
	static ArrayList<Controller> controllers = new ArrayList<Controller>();
	static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	//images
	BufferedImage sky = new Image("img/misc/sky.png").img;
	BufferedImage ground = new Image("img/misc/ground.png").img;
	public Game() {
		hitboxes.add(GROUND_HITBOX);
		doControllerThings();
		characters.add(new Character(300, 450, "Xbox 360 Wired Controller", "x", "y", "rx", "ry", "z", "rz", .5, .2,
				"1", "2", "3", "5", this));
		characters.add(GOE);
		for (Controller control : ca) {
			System.out.println(control.getName());
		}

	}

	@Override
	public void paintComponent(Graphics g) {
		// TODO the paint method
		getControllerInput();
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform oldTransform = g2.getTransform();
		graphicsSettings(g2);
		if (!isnormalscreen) {
			g2.translate(0, (screenWidth / (screenHeight / DEFAULT_SCREEN_SIZE_X) - DEFAULT_SCREEN_SIZE_Y) / 16);
			g2.scale(screenWidth / DEFAULT_SCREEN_SIZE_X, (screenHeight / DEFAULT_SCREEN_SIZE_X) / 0.625);

		}
		g2.setColor(Color.white);
		g2.fillRect(0,0,WIDTH,HEIGHT);
		switch (screenState) {
		case (SCREEN_STATE_INGAME):
			// basic background stuff to build scene
			drawBackground(g);
			drawGround(g);
			doPlayerPhysics(g);
			doPlayerDrawing(g);
			doProjectileThings(g);
			// draws hitboxes, should be after all other drawing code
			drawHitBoxes(g, hitboxes);
			drawPlayerHitboxes(g);
			drawPlayerPercentage(g);
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
		case (SCREEN_STATE_CHARACTER_SELECT):

			break;
		}

		g2.setTransform(oldTransform);
		

	}

	public void graphicsSettings(Graphics2D g2) {

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		//g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g2.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		//g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		//g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);

	}

	public void doPlayerDrawing(Graphics g) {
		for (Character person : characters) {
			person.draw(g);

		}
	}

	public void doPlayerPhysics(Graphics g) {
		for (Character person : characters) {
			Rectangle groundChecker = new Rectangle((int) person.getX(), (int) person.getY() + 1,
					(int) person.getWidth(), (int) person.getHeight());
			if (checkCollision(groundChecker, hitboxes.get(0).getRect()) && person.getY() + person.getHeight() - 1 < hitboxes.get(0).getY())
				person.isGrounded = true;
			else
				person.isGrounded = false;

		}
		checkForAndExecutePlayerHitDectection((Graphics2D) g);
	}

	public void drawPlayerPercentage(Graphics g) {
		int getSpaceBetweenNumbers = (int) (screenHeight / characters.size());
		for (int i = 0; i < characters.size(); i++) {
			Character person = characters.get(i);
			g.setColor(Color.red);
			g.drawString(Double.toString(person.getpercentage()), getSpaceBetweenNumbers/ 2 + (getSpaceBetweenNumbers * i + 1), 600);
		}
	}

	public void doProjectileThings(Graphics g) {
		for (Projectile proj : projectiles) {
			proj.draw(g);
			for (Character player : characters) {
				if (proj.getMyHitbox().getLinkedCharacter() != player)
					if (proj.getMyHitbox().getRect().intersects(player.getHurtbox().getRect())) {
						if (!proj.getMyHitbox().playerHitList.contains(player)) {
							if (!player.getShield().intersects(proj.getMyHitbox().getRect())) {
								player.applyDamage(proj.getMyHitbox().getDamage());
								player.applyHitstun(proj.getMyHitbox().getHitstunLength());
							} else {
								player.setShieldWidth(player.getShieldWidth() - proj.getMyHitbox().getShieldDamage());
							}
							proj.getMyHitbox().playerHitList.add(player);
						}
					}
			}
		}
	}

	public void drawBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, 1200, 675);
		g.drawImage(sky, 0, 0, 1200,675,null);
	}

	public void drawGround(Graphics g) {
		//g.setColor(Color.black);
		//Graphics2D g2 = (Graphics2D) g;
		//g2.fill(GROUND_HITBOX.getRect());
		g.drawImage(ground,(int)GROUND_HITBOX.getX(),(int) GROUND_HITBOX.getY() - 43, (int)GROUND_HITBOX.getWidth(), (int)GROUND_HITBOX.getHeight() + 43, null);
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
			}
		}
	}

	public void drawHitBoxes(Graphics g, ArrayList<Hitbox> hitboxList) {
		if (shouldShowHitboxes)
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
							if (person1.getState() != Character.STATE_DODGE && person2.getGrabBox() == null) {

								boolean shouldapplydamage = false;
								// new approach, divides the hitbox into 6, each
								// of which check first if they are intersecting
								// with the hitbox
								// then check if it is not colliding with the
								// shield
								for (int i = 1; i < 6; i++) {
									for (int o = 1; o < 6; o++) {

										Rectangle2D miniRect = new Rectangle2D.Double(
												(hitbox.getX() + (hitbox.getWidth() * i) / 6) - hitbox.getWidth() / 6,
												(hitbox.getY() + (hitbox.getHeight() * o) / 6) - hitbox.getHeight() / 6,
												hitbox.getWidth() / 6, hitbox.getHeight() / 6);

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
						if (person.getControllerName().equals(currentController.getName()) && person.getPortNum() == currentController.getPortNumber()) {
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
							person.changeController(currentController);
							isEditing = false;
						}
						break;
					case (2):
						if (comp.getPollData() > .5) {
							person.setAxisNameX(comp.getName());
							isEditing = false;
						}
						break;
					case (3):
						if (comp.getPollData() > .5) {
							person.setAxisNameY(comp.getName());
							isEditing = false;
						}
						break;
					case (4):
						if (comp.getPollData() > 0) {
							person.setNewDeadZone(comp.getDeadZone());
							isEditing = false;
						}
						break;
					case (5):
						if (comp.getPollData() > 0) {
							person.setAxisMidpoint(comp.getPollData());
						}
						break;
					case (6):
						if (comp.getPollData() > 0 && !comp.getName().equals(person.getAxisNameX())
								&& !comp.getName().equals(person.getAxisNameY()))
							person.setJumpButton(comp.getName());
						break;
					case (7):
						if (comp.getPollData() > 0 && !comp.getName().equals(person.getAxisNameX())
								&& !comp.getName().equals(person.getAxisNameY()))
							person.setAttackButton(comp.getName());
						break;
					case (8):
						if (comp.getPollData() > 0)
							person.setMoveAxisNameRX(comp.getName());
						break;
					case (9):
						if (comp.getPollData() > 0)
							person.setMoveAxisNameRY(comp.getName());
						break;
					case (10):
						if (comp.getPollData() > 0)
							person.setMoveAxisNameZ(comp.getName());
						break;
					case (11):
						if (comp.getPollData() > 0)
							person.setMoveAxisNameRZ(comp.getName());
						break;
					case (12):
						if (comp.getPollData() > 0)
							person.setButtonGrab(comp.getName());
						break;
					case (13):
						if (comp.getPollData() > 0)
							person.setButtonSpecial(comp.getName());
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
			break;

		case (SCREEN_STATE_ADDCHARACTER):

			if (e.getKeyCode() == KeyEvent.VK_1) {
				characters.add(new Character(500, 400, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
						KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT, KeyEvent.VK_SPACE, KeyEvent.VK_Q, KeyEvent.VK_W,
						KeyEvent.VK_R, KeyEvent.VK_E, this));

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
							characters.get(characterSlideNum).changecontrols(e.getKeyCode(), -1, -1, -1, -1, -1, -1,-1,-1,-1);
						if (characterSlideNum2 == 2)
							characters.get(characterSlideNum).changecontrols(-1, -e.getKeyCode(), -1, -1, -1, -1, -1,-1,-1,-1);
						if (characterSlideNum2 == 3)
							characters.get(characterSlideNum).changecontrols(-1, -1, e.getKeyCode(), -1, -1, -1, -1,-1,-1,-1);
						if (characterSlideNum2 == 4)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, e.getKeyCode(), -1, -1, -1,-1,-1,-1);
						if (characterSlideNum2 == 5)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, e.getKeyCode(), -1, -1,-1,-1,-1);
						if (characterSlideNum2 == 6)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, -1, e.getKeyCode(), -1 ,-1,-1,-1);
						if (characterSlideNum2 == 7)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, -1, -1, e.getKeyCode(),-1,-1,-1);
						if (characterSlideNum2 == 8)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, -1, -1, -1, e.getKeyCode(),-1,-1);
						if (characterSlideNum2 == 9)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, -1, -1, -1,-1, e.getKeyCode(),-1);
						if (characterSlideNum2 == 10)
							characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, -1, -1,-1,-1,-1 , e.getKeyCode());
						isEditing = false;
					}
				}
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
		case (SCREEN_STATE_ADDCHARACTER):

			break;
		}

	}
}
