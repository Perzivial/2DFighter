package fighter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;

import net.java.games.input.Component;
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
	private double runSpeed = 5;
	private double horizontalSlowdownFactor = 10;
	private double horizontalInAirDistance = 1;
	private boolean hasDoubleJump = false;
	private double maxAirSpeed = 5;
	// State variables
	public static int STATE_NEUTRAL = 0;
	public static int STATE_ATTACK = 1;
	public static int STATE_ATTACKUP = 2;
	public static int STATE_ATTACKDOWN = 3;
	public static int STATE_ATTACKSIDE = 4;
	public static int STATE_HITSTUN = 5;
	public static int STATE_JUMP = 6;
	public static int STATE_JUMPSQUAT = 7;
	public static int STATE_LANDINGLAG = 8;
	private int direction = 1;
	public final static int DIRECTION_LEFT = -1;
	public final static int DIRECTION_RIGHT = 1;
	private int state = 0;
	private int hitstunCounter = 0;

	HashSet<Integer> keysPressed = new HashSet<Integer>();
	BufferedImage neutralImage = new Image("img/stickman_neutral.png").img;
	BufferedImage jabImage = new Image("img/stickman_attack1.png").img;
	BufferedImage fTiltImage = new Image("img/stickman_tilt_f.png").img;
	BufferedImage hitstunImage = new Image("img/stickman_hitstun.png").img;
	BufferedImage jumpSquatImage = new Image("img/stickman_jumpsquat.png").img;
	BufferedImage jumpImage = new Image("img/stickman_jump.png").img;
	private final double startx;
	private final double starty;
	private boolean isController = false;
	private String controllerName;
	private String moveAxisNameX;
	private String moveAxisNameY;
	private double moveAxisMidpoint;
	private boolean isAxisRight = false;
	private boolean isAxisLeft = false;
	private boolean isAxisUp = false;
	private boolean isAxisDown = false;
	private boolean isAxisHalfway = false;
	private double axisDeadZone;
	private String buttonJump;
	private String buttonAttack;
	public boolean isAttackButtonDown;
	public boolean isJumpButtonDown;
	private int portNum;
	private int jumpTimeBuffer = 0;
	public boolean wasJumpKeyDownLastFrame = false;
	public boolean[] jumpKeyDownHistory = new boolean[] { false, false, false };
	public boolean[] attackKeyDownHistory = new boolean[] { false, false, false };
	private double lastFrameX;
	private double lastFrameY;
	private boolean lastFrameIsGrounded;
	private int landingLagCounter = 0;
	private double moveAxisDeadZone;
	private double percent = 0;
	private int jumpSquatFrames = 6;
	private int jumpSquatBuffer = 0;

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

	public Character(int posx, int posy, String nameOfController, String axisName, String axisName2,
			double axisMidpoint, double deadZone, String jumpButton, String attackButton,
			ArrayList<Character> characters) {

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
		axisDeadZone = deadZone;
		moveAxisNameX = axisName;
		moveAxisNameY = axisName2;
		moveAxisMidpoint = axisMidpoint;
		buttonJump = jumpButton;
		buttonAttack = attackButton;
		isController = true;
		controllerName = nameOfController;
	}

	public void applyDamage(double damageApplied) {
		percent += damageApplied / 20;
	}

	public void draw(Graphics g) {

		updateStates();
		fall();
		checkifShouldApplyLandingLag();
		handleInput();
		move();

		blastZone();
		translateHitboxes();

		drawCorrectSprite(g);
		recordLastFrameX();
		recordLastFrameY();
		recordLastFrameIsGrounded();
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
		if (state == STATE_ATTACKSIDE) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(fTiltImage, (int) x, (int) y, (int) (w * 1.6), h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(fTiltImage, (int) x + w, (int) y, (int) (-w * 1.6), h, null);
		}
		if (state == STATE_HITSTUN) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(hitstunImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(hitstunImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_JUMPSQUAT) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(jumpSquatImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(jumpSquatImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_JUMP) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(jumpImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(jumpImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_LANDINGLAG) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(neutralImage, (int) x, (int) y + (int) (h / 2), w, (int) (h / 2), null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(neutralImage, (int) x + w, (int) y + (int) (h / 2), -w, (int) (h / 2), null);
		}
	}

	public void move() {
		Rectangle sideGroundChecker = new Rectangle((int) x - 1, (int) y, (int) w + 2, (int) h);
		if (!Game.checkCollision(Game.GROUND_HITBOX.getRect(), sideGroundChecker))
			x += velX;

		if (new Rectangle((int) x, (int) (y + velY), (int) w, (int) h).intersects(Game.GROUND_HITBOX.getRect())
				&& y < Game.GROUND_HITBOX.getY()) {

			y = (int) Game.GROUND_HITBOX.getRect().getY() - h;

		} else {

			y += velY;
		}
		if (Game.checkCollision(Game.GROUND_HITBOX.getRect(), sideGroundChecker) && y > Game.GROUND_HITBOX.getY()) {
			velY += fallSpeed;
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
			velX = 0;
			velY = 0;
			percent = 0;
		}
	}

	public void handleInput() {
		if (!isController) {
			if (state != STATE_HITSTUN) {
				if (state == STATE_NEUTRAL) {
					// Jumping code
					if (isPressing(keyJump)) {
						jump();
					}
					// Horizontal Movement code
					if (isPressing(keyLeft)) {
						runLeft();
					}
					if (isPressing(keyRight)) {
						runRight();
					}
				}

			}
		} else {
			if (isAxisRight)
				runRight();
			if (isAxisLeft)
				runLeft();
		}
		if (!isController)
			chooseAttack();
	}

	public void runLeft() {

		if (isGrounded) {

			if (isAxisHalfway)
				velX = -runSpeed / 3;
			else
				velX = -runSpeed;
			direction = DIRECTION_LEFT;

		} else if (Math.abs(velX) < maxAirSpeed)
			velX -= horizontalInAirDistance;
	}

	public void runRight() {
		if (isGrounded) {

			if (isAxisHalfway)
				velX = runSpeed / 3;
			else
				velX = runSpeed;
			direction = DIRECTION_RIGHT;
		} else if (Math.abs(velX) < maxAirSpeed)
			velX += horizontalInAirDistance;
	}

	public boolean isJumpButtonDownController() {
		for (boolean bool : jumpKeyDownHistory) {
			if (bool == true) {
				return true;
			}
		}
		return false;
	}

	public boolean isAttackButtonDownController() {
		for (boolean bool : attackKeyDownHistory) {
			if (bool == true) {
				return true;
			}
		}
		return false;
	}

	public void jump() {
		boolean canJump = true;
		for (boolean bool : jumpKeyDownHistory) {
			if (bool == true) {
				canJump = false;
			}
		}
		if (canJump) {
			if (!isController) {
				if (isGrounded) {
					/*
					 * velY -= jumpHeight; keysPressed.remove(keyJump); state =
					 * STATE_JUMP; jumpTimeBuffer = 5; jumpKeyDownHistory[0] =
					 * true;
					 */
					enterJumpSquat();
				} else if (hasDoubleJump) {
					velX = 0;
					velY = 0;
					velY -= jumpHeight * 1.2;
					hasDoubleJump = false;
					state = STATE_JUMP;
					jumpTimeBuffer = 2;
					jumpKeyDownHistory[0] = true;
					keysPressed.remove(keyJump);
				}
			} else {
				if (isGrounded) {
					/*
					 * velY -= jumpHeight; state = STATE_JUMP; jumpTimeBuffer =
					 * 5;
					 */
					enterJumpSquat();
				} else if (hasDoubleJump) {
					velX = 0;
					velY = 0;
					velY -= jumpHeight * 1.2;
					hasDoubleJump = false;
					state = STATE_JUMP;
					jumpTimeBuffer = 2;
				}

			}
		}

	}

	public void enterJumpSquat() {
		jumpSquatBuffer = jumpSquatFrames + 1;
		state = STATE_JUMPSQUAT;
	}

	public void applyJumpWhenRequired() {
		if (jumpSquatBuffer == 1) {
			if (isController) {
				if (!isJumpButtonDownController()) {
					velY--;
					velY -= jumpHeight;
					state = STATE_JUMP;
					jumpTimeBuffer = 2;
				} else {
					velY--;
					velY -= jumpHeight * 1.5;
					state = STATE_JUMP;
					jumpTimeBuffer = 5;
				}
			} else {
				if (!isPressing(keyJump)) {
					velY -= jumpHeight;
					keysPressed.remove(keyJump);
					state = STATE_JUMP;
					jumpTimeBuffer = 2;
					jumpKeyDownHistory[0] = true;
				} else {
					velY -= jumpHeight * 1.5;
					keysPressed.remove(keyJump);
					state = STATE_JUMP;
					jumpTimeBuffer = 5;
					jumpKeyDownHistory[0] = true;
				}	
			}
		}
		if (jumpSquatBuffer > 0) {
			jumpSquatBuffer--;
		}
	}

	public void recordLastFrameX() {
		lastFrameX = x;
	}

	public void recordLastFrameY() {
		lastFrameY = y;
	}

	public void recordLastFrameIsGrounded() {
		lastFrameIsGrounded = isGrounded;
	}

	public void cycleArray(boolean newBoolean, boolean[] array) {
		array[2] = array[1];
		array[1] = array[0];
		array[0] = newBoolean;
	}

	public void fall() {

		if (!isGrounded) {

			velY += fallSpeed;

		} else {
			if (state != STATE_JUMP)
				velY = 0;
		}
	}

	public void checkifShouldApplyLandingLag() {
		if (isGrounded) {
			if (!lastFrameIsGrounded) {
				if (state == STATE_NEUTRAL)
					placeInLandingLag(3);
				else if (state == STATE_ATTACK) {
					placeInLandingLag(6);
					hitboxes.clear();
				}
			}
		}
	}

	public void placeInLandingLag(int length) {
		setVelX(0);
		setVelY(0);
		landingLagCounter = length;
		state = STATE_LANDINGLAG;
	}

	public void updateStates() {
		applyJumpWhenRequired();
		if (landingLagCounter > 0) {
			landingLagCounter--;
		} else if (state == STATE_LANDINGLAG) {
			state = STATE_NEUTRAL;
		}
		if (isGrounded && state != STATE_JUMP) {
			isJumpButtonDown = false;
			hasDoubleJump = true;
		}
		cycleArray(false, jumpKeyDownHistory);
		cycleArray(false, attackKeyDownHistory);
		if (jumpTimeBuffer > 0)
			jumpTimeBuffer--;
		if (state == STATE_JUMP && jumpTimeBuffer == 0) {
			state = STATE_NEUTRAL;
		}
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
	public void chooseAttack() {

		if (state != STATE_LANDINGLAG) {
			if (!isController) {
				if (isPressing(keyAttack)) {
					if (isPressing(keyUp)) {

					} else if (isPressing(keyDown)) {

					} else if (isPressing(keyLeft)) {
						fTilt();
					} else if (isPressing(keyRight)) {
						fTilt();
					} else {
						if (Math.abs(velX) < .1 && Math.abs(velY) < .1)
							jab();
					}
					keysPressed.remove(keyAttack);
				}
			}
		}
	}

	public void chooseAttack(Controller myController) {

		boolean canAttack = true;
		for (boolean bool : attackKeyDownHistory) {
			if (bool == true) {
				canAttack = false;
			}
		}
		boolean isAxisLeftLocal = false;
		boolean isAxisRightLocal = false;
		boolean isAxisUpLocal = false;
		boolean isAxisDownLocal = false;
		for (Component comp : myController.getComponents()) {
			if (comp.getName().equals(moveAxisNameX)) {
				if (Math.abs(comp.getPollData()) < moveAxisMidpoint && Math.abs(comp.getPollData()) > axisDeadZone) {
					if (comp.getPollData() < 0) {
						isAxisLeftLocal = true;
					} else {
						isAxisRightLocal = true;
					}

				}

			}
			if (comp.getName().equals(moveAxisNameY)) {
				if (Math.abs(comp.getPollData()) < moveAxisMidpoint && Math.abs(comp.getPollData()) > axisDeadZone) {
					if (comp.getPollData() < 0) {
						isAxisDownLocal = true;
					} else {
						isAxisUpLocal = true;
					}

				}
			}
		}
		if (canAttack) {
			boolean hasChosenATilt = false;
			if (isAxisUpLocal) {

				hasChosenATilt = true;
			} else if (isAxisDownLocal) {

				hasChosenATilt = true;
			}

			else if (isAxisRightLocal && !hasChosenATilt) {
				fTilt();
			} else if (isAxisLeftLocal && !hasChosenATilt) {
				fTilt();
			}

			else {
				if (Math.abs(velX) < .1 && Math.abs(velY) < .1)
					jab();
			}

		}
	}

	public void getControllerInput(Controller myController) {
		for (Component comp : myController.getComponents()) {
			if (comp.getName().equals(moveAxisNameX)) {

			}
		}
	}

	// TODO attack below here
	// AttackHitbox creation syntax , character, localx,
	// localy,width,height,xknockback,yknockback,hitstunlen,lifetime, damage
	public void jab() {
		hitboxes.add(new AttackHitbox(this, 20, 15, 15, 15, .5, -1, 10, 5, 10));
		state = STATE_ATTACK;
		velX += direction * 2;
	}

	public void fTilt() {
		hitboxes.add(new AttackHitbox(this, 20, 20, 20, 15, 3, -3, 10, 5, 20));
		hitboxes.add(new AttackHitbox(this, 20, 25, 10, 15, 1, -10, 10, 5, 20));
		state = STATE_ATTACKSIDE;
		velX += direction * 3;
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
				if (state != STATE_LANDINGLAG)
					state = STATE_NEUTRAL;
				break;
			}
		}
	}

	public int getState() {
		return state;
	}

	public Hitbox getHurtbox() {
		return hurtbox;
	}

	// some stock getter and setter methods
	public double getX() {
		return x;
	}

	public void setX(double newX) {
		x = newX;
	}

	public void setY(double newY) {
		y = newY;
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

	public String getAxisNameX() {
		return moveAxisNameX;
	}

	public String getAxisNameY() {
		return moveAxisNameY;
	}

	public void setAxisNameX(String newName) {
		moveAxisNameX = newName;
	}

	public void setAxisNameY(String newName) {
		moveAxisNameY = newName;
	}

	public void setNewMidpoint(double newMidPoint) {
		moveAxisMidpoint = newMidPoint;
	}

	public void setNewDeadZone(double newDeadZone) {
		moveAxisDeadZone = newDeadZone;
	}

	public double getAxisMidpoint() {
		return moveAxisMidpoint;
	}

	public void setAxisName(String newAxisName) {
		moveAxisNameX = newAxisName;
	}

	public void setAxisMidpoint(double newAxisMidpoint) {
		moveAxisMidpoint = newAxisMidpoint;
	}

	public void applyKnockback(double newVelX, double newVelY, int direction) {
		double dampenedpercent = percent / 100;
		setVelX(newVelX + dampenedpercent * direction);
		if (newVelY < 0)
			y--;
		if (newVelY > 0)
			setVelY(newVelY + dampenedpercent);
		else
			setVelY(newVelY - dampenedpercent);
		System.out.println(percent);
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

	public String getControllerName() {
		return controllerName;
	}

	public int getPortNum() {
		return portNum;
	}

	// controller change code in debug menu
	public void changeController(Controller newController) {
		controllerName = newController.getName();
		portNum = newController.getPortNumber();
	}

	public boolean getIsAxisUp() {
		return isAxisRight;
	}

	public boolean getIsAxisDown() {
		return isAxisRight;
	}

	public boolean getIsAxisLeft() {
		return isAxisRight;
	}

	public boolean getIsAxisRight() {
		return isAxisRight;
	}

	public void setAxisUp(boolean newAxisState) {
		isAxisUp = newAxisState;
	}

	public void setAxisDown(boolean newAxisState) {
		isAxisDown = newAxisState;
	}

	public void setAxisLeft(boolean newAxisState) {
		isAxisLeft = newAxisState;
	}

	public void setAxisRight(boolean newAxisState) {
		isAxisRight = newAxisState;
	}

	public boolean getIsAxisHalfway() {
		return isAxisHalfway;
	}

	public void setAxisHalfway(boolean newAxisConfig) {
		isAxisHalfway = newAxisConfig;
	}

	public double getAxisDeadZone() {
		return axisDeadZone;
	}

	public String getJumpButton() {
		return buttonJump;
	}

	public String getAttackButton() {
		return buttonAttack;
	}

	public void setAttackButton(String newAttackButton) {
		buttonAttack = newAttackButton;
	}

	public void setJumpButton(String newJumpButton) {
		buttonJump = newJumpButton;
	}

	public boolean getIsAttackButtonDown() {
		return isAttackButtonDown;
	}

	public void setIsAttackButtonDown(boolean newAttackButtonConfig) {
		isAttackButtonDown = newAttackButtonConfig;
	}

	public boolean getIsJumpButtonDown() {
		return isJumpButtonDown;
	}

	public void setIsJumpButtonDown(boolean newJumpButtonConfig) {
		isJumpButtonDown = newJumpButtonConfig;
	}

	public double getpercentage() {
		return percent;
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
