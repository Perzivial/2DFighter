package fighter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Character {
	private double fallSpeed = 1;
	private double velX;
	private double velY;
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
	// State variables
	private static int STATE_NEUTRAL = 0;
	private static int STATE_ATTACK = 1;
	private static int STATE_ATTACKUP = 2;
	private static int STATE_ATTACKDOWN = 3;
	private static int STATE_ATTACKLEFT = 4;
	private static int STATE_ATTACKRIGHT = 5;
	private static int STATE_HITSTUN = 6;

	private int direction = 1;
	public final static int DIRECTION_LEFT = -1;
	public final static int DIRECTION_RIGHT = 1;
	private int state = 0;
	private int hitstunCounter = 0;

	HashSet<Integer> keysPressed = new HashSet<Integer>();
	BufferedImage neutralImage = new Image("img/stickman_neutral.png").img;
	BufferedImage jabImage = new Image("img/stickman_attack1.png").img;
	BufferedImage hitstunImage = new Image("img/stickman_hitstun.png").img;
	private final double startx;
	private final double starty;
	private boolean isController = false;
	private String controllerName;
	private String moveAxisName;
	private double moveAxisMidpoint;
	private String buttonJump;
	private String buttonAttack;
	private int portNum;

	public Character(int posx, int posy, int upKey, int downKey, int leftKey, int rightKey, int jumpKey,
			int attackKey) {
		x = posx;
		y = posy;
		startx = x;
		starty = y;
		keyUp = upKey;
		keyDown = downKey;
		keyLeft = leftKey;
		keyRight = rightKey;
		keyJump = jumpKey;
		keyAttack = attackKey;
		hurtbox = new Hitbox(this, x, y, w, h, Game.TYPE_HURTBOX);
	}

	public Character(int posx, int posy, String nameOfController, String axisName, double axisMidpoint,
			String jumpButton, String attackButton, ArrayList<Character> characters) {

		keyUp = KeyEvent.VK_UP;
		keyDown = KeyEvent.VK_DOWN;
		keyLeft = KeyEvent.VK_LEFT;
		keyRight = KeyEvent.VK_RIGHT;
		keyJump = KeyEvent.VK_SPACE;
		keyAttack = KeyEvent.VK_Q;

		x = posx;
		y = posy;
		startx = x;
		starty = y;

		hurtbox = new Hitbox(this, x, y, w, h, Game.TYPE_HURTBOX);

		Controller[] ca = ControllerEnvironment.getDefaultEnvironment().getControllers();
		for (Controller currentController : ca) {
			boolean isvalid = true;
			if (currentController.getName().equals(nameOfController)) {
				for (Character person : characters) {
					if (person.getIsUsingController())
						if (person.portNum == currentController.getPortNumber())
							isvalid = false;
					if (isvalid) {
						this.portNum = currentController.getPortNumber();
						break;
					}
				}
			}

		}
		moveAxisName = axisName;
		moveAxisMidpoint = axisMidpoint;
		buttonJump = jumpButton;
		buttonAttack = attackButton;
		isController = true;

	}

	public void draw(Graphics g) {
		g.setColor(Color.black);
		if (isController)
			g.fillRect((int) x, (int) y, w, h);
		updateStates();
		fall();
		handleInput();
		move();
		translateHitboxes();
		blastZone();

		drawCorrectSprite(g);

	}

	public void drawCorrectSprite(Graphics g) {
		// draws the correct sprite given current state, direction and other
		// factors
		if (state == STATE_NEUTRAL) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(neutralImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(neutralImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_ATTACK) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(jabImage, (int) x, (int) y, (int) (w * 1.6), h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(jabImage, (int) x + w, (int) y, (int) (-w * 1.6), h, null);
		}
		if (state == STATE_HITSTUN) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(hitstunImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(hitstunImage, (int) x + w, (int) y, -w, h, null);
		}
	}

	public void move() {
		x += velX;

		if (new Rectangle((int) x, (int) (y + velY), (int) w, (int) h).intersects(Game.GROUND_HITBOX.getRect())) {

			y = (int) Game.GROUND_HITBOX.getRect().getY() - h;

		} else {

			y += velY;
		}
		if (state != STATE_HITSTUN)
			applyFriction();
	}

	public void applyHitstun(int length) {
		hitstunCounter = length;
		state = STATE_HITSTUN;
	}

	private void blastZone() {
		if (x < 0 || x + w > 1200 || y < 0 || y + h > 675) {
			x = startx;
			y = starty;
		}
	}

	public void handleInput() {
		if (!isController) {
			if (state != STATE_HITSTUN) {
				if (state == STATE_NEUTRAL) {
					// Jumping code
					if (isPressing(keyJump)) {
						if (isGrounded) {
							velY -= jumpHeight;
							keysPressed.remove(keyJump);
						} else if (hasDoubleJump) {
							velY -= jumpHeight * 2;
							hasDoubleJump = false;
						}
					}
					// Horizontal Movement code
					if (isPressing(keyLeft)) {
						runLeft();
					}
					if (isPressing(keyRight)) {
						if (isGrounded) {
							velX = runSpeed;
							direction = DIRECTION_RIGHT;
						} else if (Math.abs(velX) < maxAirSpeed)
							velX += horizontalInAirDistance;
					}
				}
				chooseAttack();
			}
		}
	}

	public void runLeft() {
		if (isGrounded) {
			velX = -runSpeed;
			direction = DIRECTION_LEFT;
		} else if (Math.abs(velX) < maxAirSpeed)
			velX -= horizontalInAirDistance;
	}

	public void fall() {
		if (!isGrounded) {
			velY += fallSpeed;
		} else
			velY = 0;
	}

	public void updateStates() {
		if (isGrounded && !hasDoubleJump)
			hasDoubleJump = true;
		if (hitstunCounter > -1)
			hitstunCounter--;
		if (hitstunCounter == 0)
			state = STATE_NEUTRAL;
	}

	public void applyFriction() {
		if (isGrounded)
			velX /= horizontalSlowdownFactor;
	}

	// TODO attack locations. syntax is : character, local position x, local
	// position y,width, height, hitstun time, knockbackx, knockbacky, time it
	// will be out for
	private void chooseAttack() {
		if (isPressing(keyAttack)) {
			if (isPressing(keyUp)) {

			} else if (isPressing(keyDown)) {

			} else if (isPressing(keyLeft)) {

			} else if (isPressing(keyRight)) {

			} else {
				hitboxes.add(new AttackHitbox(this, 20, 15, 15, 15, .5, -1, 10, 5));
				state = STATE_ATTACK;
				velX += direction * 2;
			}
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
			if (direction == DIRECTION_RIGHT)
				box.updateLocation(x + box.getlocalX(), y + box.getlocalY(), box.getWidth(), box.getHeight());
			if (direction == DIRECTION_LEFT)
				box.updateLocation(x + box.getlocalX() - (w * 1.75), y + box.getlocalY(), box.getWidth(),
						box.getHeight());
		}
		for (AttackHitbox box : hitboxes) {

			if (!box.isActive) {
				hitboxes.remove(box);
				state = STATE_NEUTRAL;
				break;
			}
		}
	}

	public Hitbox getHurtbox() {
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
		return velY;
	}

	public double getVelY() {
		return velX;
	}

	public double getWidth() {
		return w;
	}

	public double getHeight() {
		return h;
	}

	public int getDownKey() {
		return keyDown;
	}

	public int getUpKey() {
		return keyUp;
	}

	public int getLeftKey() {
		return keyLeft;
	}

	public int getRightKey() {
		return keyRight;
	}

	public int getJumpKey() {
		return keyJump;
	}

	public int getAttackKey() {
		return keyAttack;
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

	public int getDirection() {
		return direction;
	}

	public void setDirection(int newDirection) {
		direction = newDirection;
	}

	public void setVelX(double newVelX) {
		velX = newVelX;
		x += velX;
	}

	public void setVelY(double newVelY) {
		velY = newVelY;
		y += velY;
	}

	public String getAxisName() {
		return moveAxisName;
	}

	public double getAxisMidpoint() {
		return moveAxisMidpoint;
	}

	public void setAxisName(String newAxisName) {
		moveAxisName = newAxisName;
	}

	public void setAxisMidpoint(double newAxisMidpoint) {
		moveAxisMidpoint = newAxisMidpoint;
	}

	public void applyKnockback(double newVelX, double newVelY) {
		setVelX(newVelX);
		if (newVelY < 0)
			y--;
		setVelY(newVelY);
	}

	// changes the controls at runtime
	public void changecontrols(int newKeyUp, int newKeyDown, int newKeyLeft, int newKeyRight, int newKeyJump,
			int newKeyAttack) {
		if (newKeyUp != -1)
			keyUp = newKeyUp;
		if (newKeyDown != -1)
			keyDown = newKeyDown;
		if (newKeyLeft != -1)
			keyLeft = newKeyLeft;
		if (newKeyRight != -1)
			keyRight = newKeyRight;
		if (newKeyJump != -1)
			keyJump = newKeyJump;
		if (newKeyAttack != -1)
			keyAttack = newKeyAttack;
	}

	public boolean getIsUsingController() {
		return isController;
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
