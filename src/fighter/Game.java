package fighter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
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
	public static final Hitbox GROUND_HITBOX = new Hitbox(200, 600, 720, 100, TYPE_GROUND);

	ArrayList<Character> characters = new ArrayList<Character>();
	Character GOE = new Character(500, 400, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
			KeyEvent.VK_SPACE, KeyEvent.VK_Q);
	Character GOE2 = new Character(500, 400, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_E,
			KeyEvent.VK_F);
	private int screenState = 0;
	private final static int SCREEN_STATE_INGAME = 0;
	private final static int SCREEN_STATE_ADDCHARACTER = 1;
	private final static int SCREEN_STATE_CONTROLLERCALIBRATION = 2;
	private int characterSlideNum = 0;
	private int characterSlideNum2 = 1;
	private boolean isEditing = false;
	Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
	ArrayList<Controller> controllers = new ArrayList<Controller>();

	public Game() {
		hitboxes.add(GROUND_HITBOX);
		doControllerThings();
		characters.add(new Character(300, 575, "Xbox 360 Wired Controller", "x", "y", .5, .2, "1", "2", characters));
		characters.add(GOE);
		characters.add(GOE2);
		for (Controller control : controllers) {
			System.out.println(control.getName());
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		// TODO the paint method
		getControllerInput();
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform oldTransform = g2.getTransform();
		if (!isnormalscreen)
			g2.scale(((screenWidth / DEFAULT_SCREEN_SIZE_Y) / 16) * 9, screenHeight / DEFAULT_SCREEN_SIZE_Y);
		switch (screenState) {
		case (SCREEN_STATE_INGAME):
			// basic background stuff to build scene
			drawBackground(g);
			drawGround(g);
			doPlayerPhysics();
			doPlayerDrawing(g);

			// draws hitboxes, should be after all other drawing code
			drawHitBoxes(g, hitboxes);
			drawPlayerHitboxes(g);
			break;
		case (SCREEN_STATE_ADDCHARACTER):
			g.setColor(Color.WHITE);
			g.drawString("Press 1 to add new character(keyboard)", 20, 20);
			g.drawString("Press 2 to add new control method (controller *not inplemented yet*)", 20, 40);
			g.drawString("Up and down to choose which character to edit", 20, 60);
			g.drawString("Left and right to change which attribute to edit, then press 3 to edit it", 20, 80);
			g.drawString("Current amount of characters: " + characters.size()
					+ ", you are viewing the inputs for number: " + characterSlideNum, 20, 100);
			if (characters.size() > 0) {
				g.drawString(String.valueOf(characters.get(characterSlideNum).getUpKey()), 60, 200);
				g.drawString(String.valueOf(characters.get(characterSlideNum).getDownKey()), 120, 200);
				g.drawString(String.valueOf(characters.get(characterSlideNum).getLeftKey()), 180, 200);
				g.drawString(String.valueOf(characters.get(characterSlideNum).getRightKey()), 240, 200);
				g.drawString(String.valueOf(characters.get(characterSlideNum).getJumpKey()), 300, 200);
				g.drawString(String.valueOf(characters.get(characterSlideNum).getAttackKey()), 360, 200);

				g.drawString("^", 60 * characterSlideNum2, 220);
			}

			break;
		}
		g2.setTransform(oldTransform);
	}

	public void doPlayerDrawing(Graphics g) {
		for (Character person : characters) {
			person.draw(g);

		}
	}

	public void doPlayerPhysics() {
		for (Character person : characters) {
			Rectangle groundChecker = new Rectangle((int) person.getX(), (int) person.getY() + 1,
					(int) person.getWidth(), (int) person.getHeight());
			if (checkCollision(groundChecker, hitboxes.get(0).getRect()))
				person.isGrounded = true;
			else
				person.isGrounded = false;
		}
		checkForAndExecutePlayerHitDectection();
	}

	public void drawBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, 1200, 675);
	}

	public void drawGround(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(200, 600, 720, 100);
	}

	public void drawPlayerHitboxes(Graphics g) {
		for (Character person : characters) {
			if (shouldShowHitboxes) {

				for (int i = 0; i < person.hitboxes.size(); i++) {
					g.setColor(Game.transparentred);
					AttackHitbox tempbox = person.hitboxes.get(i);
					Graphics2D g2 = (Graphics2D) g;
					g2.fill(tempbox.getRect());
				}
				g.setColor(Game.transparentpurple);
				Graphics2D g2 = (Graphics2D) g;
				g2.fill(person.getHurtbox().getRect());

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
	public void checkForAndExecutePlayerHitDectection() {
		/*
		 * for (int j = 0; j < characters.size(); j++) { for (int i = 0; i <
		 * characters.size(); i++) { for (int o = 0; o <
		 * characters.get(i).hitboxes.size(); o++) { if
		 * (checkCollision(characters.get(j).getHurtbox().getRect(),
		 * characters.get(i).hitboxes.get(o).getRect())) { if
		 * (!characters.get(i).hitboxes.get(o).getLinkedCharacter()
		 * .equals(characters.get(j).getHurtbox().getLinkedCharacter())) {
		 * System.out.println("A collision was detected");
		 * characters.get(j).applyKnockback(
		 * (characters.get(i).hitboxes.get(o).getKnockbackX())
		 * characters.get(i).getDirection(),
		 * (characters.get(i).hitboxes.get(o).getKnockbackY()));
		 * characters.get(j).applyHitstun(characters.get(i).hitboxes.get(o).
		 * getHitstunLength());
		 * characters.get(j).setDirection(characters.get(i).getDirection()); } }
		 * } } }
		 */
		for (Character person1 : characters) {
			for (Character person2 : characters) {
				for (AttackHitbox hitbox : person2.hitboxes) {

					if (checkCollision(person1.getHurtbox().getRect(), hitbox.getRect())) {
						if (!person1.equals(hitbox.getLinkedCharacter())) {
							// in the scope or this part of the code, person1 is
							// the person getting hit, and hitbox is the
							// reference o the hitbox hitting the person
							person1.applyKnockback(hitbox.getKnockbackX() * person2.getDirection(),
									hitbox.getKnockbackY());
							person1.applyHitstun(hitbox.getHitstunLength());
							person1.setDirection(hitbox.getLinkedCharacter().getDirection());

						}
					}
				}
			}
		}
	}

	public static boolean checkCollision(Rectangle rect1, Rectangle rect2) {
		if (rect1.intersects(rect2))
			return true;
		return false;
	}

	public void doControllerThings() {
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
						if (person.getAxisNameX().equals(comp.getName())) {
							// code for the horizontal movement stick
							if ((Math.abs(comp.getPollData()) > person.getAxisDeadZone())) {
								if ((Math.abs(comp.getPollData()) < (float) person.getAxisMidpoint()))
									person.setAxisHalfway(true);
								else
									person.setAxisHalfway(false);

								if (comp.getPollData() > 0)
									person.setAxisRight(true);

								if (comp.getPollData() < 0)
									person.setAxisLeft(true);
							} else {
								person.setAxisRight(false);
								person.setAxisLeft(false);
							}
						}
						if (person.getAxisNameY().equals(comp.getName())) {
							// code for the vertical movement stick
							if ((Math.abs(comp.getPollData()) > person.getAxisDeadZone())) {
								if ((Math.abs(comp.getPollData()) > .1)) {

									if (comp.getPollData() > 0)
										person.setAxisUp(true);

									if (comp.getPollData() < 0)
										person.setAxisDown(true);
								} else {
									person.setAxisUp(false);
									person.setAxisDown(false);
								}
							}
						}
						if (person.getJumpButton().equals(comp.getName())) {
							boolean hasPressedThisFrame = false;
							if (comp.getPollData() == 1.0) {
								person.jump();
								System.out.println("true");
								hasPressedThisFrame = true;
								person.wasJumpKeyDownLastFrame = true;
								person.cycleArray(true, person.jumpKeyDownHistory);
							}
						}
						if (person.getAttackButton().equals(comp.getName())) {
							if (comp.getPollData() == 1.0) {
								if (!person.getIsAttackButtonDown()) {
									person.chooseAttack();
									person.setIsAttackButtonDown(true);
									// System.out.println(person.getIsAttackButtonDown());
								}
							}
							if (comp.getPollData() != 1.0)
								person.setIsAttackButtonDown(false);
						}
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
						KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE, KeyEvent.VK_Q));
				characterSlideNum = 0;
				characterSlideNum2 = 1;
			}

			if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				screenState = SCREEN_STATE_INGAME;
			if (characters.size() > 0) {
				if (!isEditing) {
					if (e.getKeyCode() == KeyEvent.VK_UP && characterSlideNum > 0) {
						characterSlideNum--;
					}
					if (e.getKeyCode() == KeyEvent.VK_DOWN && characterSlideNum < characters.size() - 1) {
						characterSlideNum++;
					}
					if (e.getKeyCode() == KeyEvent.VK_LEFT && characterSlideNum2 > 1) {
						characterSlideNum2--;
					}
					if (e.getKeyCode() == KeyEvent.VK_RIGHT && characterSlideNum2 < 6) {
						characterSlideNum2++;
					}
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
						isEditing = true;
				} else {
					if (characterSlideNum2 == 1)
						characters.get(characterSlideNum).changecontrols(e.getKeyCode(), -1, -1, -1, -1, -1);
					if (characterSlideNum2 == 2)
						characters.get(characterSlideNum).changecontrols(-1, -e.getKeyCode(), -1, -1, -1, -1);
					if (characterSlideNum2 == 3)
						characters.get(characterSlideNum).changecontrols(-1, -1, e.getKeyCode(), -1, -1, -1);
					if (characterSlideNum2 == 4)
						characters.get(characterSlideNum).changecontrols(-1, -1, -1, e.getKeyCode(), -1, -1);
					if (characterSlideNum2 == 5)
						characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, e.getKeyCode(), -1);
					if (characterSlideNum2 == 6)
						characters.get(characterSlideNum).changecontrols(-1, -1, -1, -1, -1, e.getKeyCode());
					isEditing = false;
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
