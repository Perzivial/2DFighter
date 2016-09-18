package fighter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.*;

public class Character {
	private double fallSpeed = 1;
	private double velx;
	private double vely;
	private double x;
	private double y;
	private int w = 20;
	private int h = 50;
	private Hitbox hurtbox;
	public ArrayList<AttackHitbox> hitboxes = new ArrayList<AttackHitbox>();
	public boolean isGrounded = true;
	private int keyUp;
	private int keyDown;
	private int keyLeft;
	private int keyRight;
	private int keyJump;
	private int keyAttack;
	private double jumpHeight = 7;
	private double runSpeed = 7;
	private double horizontalSlowdownFactor = 10;
	private double horizontalInAirDistance = 2;
	private boolean hasDoubleJump = false;
	private double maxAirSpeed = 10;
	HashSet<Integer> keysPressed = new HashSet<Integer>();

	public Character(int posx, int posy, int upKey, int downKey, int leftKey, int rightKey, int jumpKey,
			int attackKey) {
		x = posx;
		y = posy;
		keyUp = upKey;
		keyDown = downKey;
		keyLeft = leftKey;
		keyRight = rightKey;
		keyJump = jumpKey;
		keyAttack = attackKey;
		hurtbox = new Hitbox(x, y, w, h, Game.TYPE_HURTBOX);
	}

	public void draw(Graphics g) {
		updateStates();
		fall();
		handleInput();
		move();
		translateHitboxes();
		g.fillRect((int) x, (int) y, (int) w, (int) h);
		for (int i = 0; i < hitboxes.size(); i++) {
			g.setColor(Game.transparentred);
			AttackHitbox tempbox = hitboxes.get(i);
			Graphics2D g2 = (Graphics2D) g;
			g2.fill(tempbox.getRect());
		}
	}

	public void move() {
		x += velx;
		if (new Rectangle((int) x, (int) (y + vely), (int) w, (int) h).intersects(Game.GROUND_HITBOX.getRect()))
			y = (int) Game.GROUND_HITBOX.getRect().getY() - h;
		else
			y += vely;
		applyFriction();
	}

	public void handleInput() {
		// Jumping code
		if (isPressing(keyJump)) {
			if (isGrounded) {
				vely -= jumpHeight;
				keysPressed.remove(keyJump);
			} else if (hasDoubleJump) {
				vely -= jumpHeight * 2;
				hasDoubleJump = false;
			}
		}
		// Horizontal Movement code
		if (isPressing(keyLeft)) {
			if (isGrounded)
				velx = -runSpeed;
			else if (Math.abs(velx) < maxAirSpeed)
				velx -= horizontalInAirDistance;
		}
		if (isPressing(keyRight)) {
			if (isGrounded)
				velx = runSpeed;
			else if (Math.abs(velx) < maxAirSpeed)
				velx += horizontalInAirDistance;
		}
		chooseAttack();
	}

	public void fall() {
		if (!isGrounded) {
			vely += fallSpeed;
		} else
			vely = 0;
	}

	public void updateStates() {
		if (isGrounded && !hasDoubleJump)
			hasDoubleJump = true;
	}

	public void applyFriction() {
		if (isGrounded)
			velx /= horizontalSlowdownFactor;
	}

	private void chooseAttack() {
		if (isPressing(keyAttack)) {
			hitboxes.add(new AttackHitbox(this, 20, 20, 5, 5, 100));
			keysPressed.remove(keyAttack);
		}

	}

	public boolean checkIfInSpecificHitBox(Hitbox box) {
		if (hurtbox.getRect().intersects(box.getRect()))
			return true;
		return false;
	}

	public boolean isPressing(int key) {
		if (keysPressed.contains(key)) {
			return true;
		}
		return false;
	}

	public void translateHitboxes() {
		hurtbox.updateLocation(x, y, w, h);
		for (AttackHitbox box : hitboxes) {

			box.updateLocation(x + box.getlocalX(), y + box.getlocalY(), box.getWidth(), box.getHeight());

		}
		for (AttackHitbox box : hitboxes) {

			if (!box.isActive) {
				hitboxes.remove(box);
				break;
			}
		}
	}

	public Hitbox getHitbox() {
		return hurtbox;
	}

	// some stock getter and setter methods
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getVelX() {
		return vely;
	}

	public double getVelY() {
		return velx;
	}

	public double getWidth() {
		return w;
	}

	public double getHeight() {
		return h;
	}

	public double getJumpHeight() {
		return jumpHeight;
	}

	public double getRunSpeed() {
		return runSpeed;
	}

	public double getMaxAirSpeed() {
		return maxAirSpeed;
	}

	private void setJumpHeight(double newJumpHeight) {
		jumpHeight = newJumpHeight;
	}

	private void setRunSpeed(double newRunSpeed) {
		runSpeed = newRunSpeed;
	}

	private void setMaxAirSpeed(double newMaxAirSpeed) {
		maxAirSpeed = newMaxAirSpeed;
	}
}
