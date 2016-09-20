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

	public Game() {
		hitboxes.add(GROUND_HITBOX);
		characters.add(GOE);
		characters.add(GOE2);

		ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
		Controller[] cs = ce.getControllers();
		for (int i = 0; i < cs.length; i++) {
			System.out.println(i + ". " + cs[i].getName() + ", " + cs[i].getType());
		}

	}

	@Override
	public void paintComponent(Graphics g) {
		// TODO the paint method
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform oldTransform = g2.getTransform();
		if (!isnormalscreen)
			g2.scale(((screenWidth / DEFAULT_SCREEN_SIZE_Y) / 16)
					* 9/* screenWidth / DEFAULT_SCREEN_SIZE_X */, screenHeight / DEFAULT_SCREEN_SIZE_Y);
		// basic background stuff to build scene
		drawBackground(g);
		drawGround(g);
		doPlayerPhysics();
		doPlayerDrawing(g);

		// draws hitboxes, should be after all other drawing code
		drawHitBoxes(g, hitboxes);
		drawPlayerHitboxes(g);

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
							person1.applyKnockback(hitbox.getKnockbackX() * person2.getDirection(),
									hitbox.getKnockbackY());
							person1.applyHitstun(hitbox.getHitstunLength());
						}
					}
				}
			}
		}
	}

	public boolean checkCollision(Rectangle rect1, Rectangle rect2) {
		if (rect1.intersects(rect2))
			return true;
		return false;
	}

	// TODO key pressing code, should be in it's own area, unbroken
	@Override
	public void keyTyped(KeyEvent e) {
		// relates to text input in text input fields so not in use
	}

	@Override
	public void keyPressed(KeyEvent e) {
		for (Character person : characters) {
			person.keysPressed.add(e.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println(e.getKeyCode());
		for (Character person : characters) {
			if (person.keysPressed.contains(e.getKeyCode()))
				person.keysPressed.remove(e.getKeyCode());
		}
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			shouldShowHitboxes = !shouldShowHitboxes;
		}
	}
}
