package fighter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JComponent;

public class Game extends JComponent implements KeyListener {
	// types of hitboxes. affects how they are drawn, among other things
	public static final int TYPE_GROUND = 0;
	public static final int TYPE_ATTACK = 1;
	public static final int TYPE_SHIELD = 2;
	public static final int TYPE_HURTBOX = 3;
	// hitbox colours
	public static Color transparentred = new Color(255, 0, 0, 75);
	public static Color transparentgreen = new Color(0, 255, 0, 75);
	public static Color transparentblue = new Color(0, 0, 255, 75);
	public static Color transparentpink = new Color(0, 0, 255, 50);

	boolean shouldShowHitboxes = true;
	ArrayList<Hitbox> hitboxes = new ArrayList<Hitbox>();
	public static final Hitbox GROUND_HITBOX = new Hitbox(200, 600, 600, 100, TYPE_GROUND);

	ArrayList<Character> characters = new ArrayList<Character>();
	Character GOE = new Character(500, 400, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
			KeyEvent.VK_SPACE, KeyEvent.VK_Q);
	Character GOE2 = new Character(500, 400, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D,
			KeyEvent.VK_E, KeyEvent.VK_F);

	public Game() {
		hitboxes.add(GROUND_HITBOX);
		characters.add(GOE);
		characters.add(GOE2);
	}

	@Override
	public void paintComponent(Graphics g) {
		// TODO the paint method

		// basic background stuff to build scene
		drawBackground(g);
		drawGround(g);
		doPlayerPhysics();
		doPlayerDrawing(g);

		// draws hitboxes, should be after all other drawing code
		drawHitBoxes(g, hitboxes);
		drawPlayerHitboxes(g);
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
	}

	public void drawBackground(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, 1000, 800);
	}

	public void drawGround(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(200, 600, 600, 100);
	}

	public void drawPlayerHitboxes(Graphics g) {
		for (Character person : characters) {
			if (shouldShowHitboxes)
				for (int i = 0; i < person.hitboxes.size(); i++) {
					g.setColor(Game.transparentred);
					AttackHitbox tempbox = person.hitboxes.get(i);
					Graphics2D g2 = (Graphics2D) g;
					g2.fill(tempbox.getRect());
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
					g.setColor(transparentpink);
					break;
				}
				g.fillRect((int) myhitbox.getX(), (int) myhitbox.getY(), (int) myhitbox.getWidth(),
						(int) myhitbox.getHeight());
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
