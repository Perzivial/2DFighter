package fighter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Character {
	private String name = "stickman";
	private Ellipse2D shield;
	private double shieldWidth = 1.0;
	boolean isShielding = false;
	private double fallSpeed = .8;
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
	private int keyModifier;
	private int keyJump;
	private int keyAttack;
	private int keyShield;
	private int keyGrab;
	private int keySpecial;
	private double jumpHeight = 9;
	private double lowJumpHeight = 6;
	private double runSpeed = 6;
	private double horizontalSlowdownFactor = 1.3;
	private double horizontalInAirSpeed = 6;
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
	public static int STATE_CROUCH = 9;
	public static int STATE_SMASH_ATTACK_CHARGE = 10;
	public static int STATE_SMASH_ATTACK_UP = 11;
	public static int STATE_SMASH_ATTACK_DOWN = 12;
	public static int STATE_SMASH_ATTACK_FORWARD = 13;
	public static int STATE_ATTACK_NAIR = 14;
	public static int STATE_ATTACK_FAIR = 15;
	public static int STATE_ATTACK_UAIR = 16;
	public static int STATE_ATTACK_DAIR = 17;
	public static int STATE_ATTACK_BAIR = 18;
	public static int STATE_SHIELD = 19;
	public static int STATE_DODGE = 20;
	public static int STATE_LAG = 21;
	public static int STATE_AIRDODGE = 22;
	public static int STATE_LANDFALLSPECIAL = 23;
	public static int STATE_GRAB = 24;
	public static int STATE_GRABBED = 25;
	public static int STATE_UTHROW = 26;
	public static int STATE_DTHROW = 27;
	public static int STATE_FTHROW = 28;
	public static int STATE_BTHROW = 29;
	public static int STATE_UPSPECIAL = 30;
	public static int STATE_DOWNSPECIAL = 30;
	public static int STATE_FORWARDSPECIAL = 30;
	public static int STATE_NEUTRALSPECIAL = 30;
	private int direction = 1;
	private boolean isChargingSmashAttack = false;
	private double smashAttackChargePercent = 1.0;
	private int smashAttackDirection = 1;

	public final static int DIRECTION_LEFT = -1;
	public final static int DIRECTION_RIGHT = 1;
	public final static int DIRECTION_DOWN = -2;
	public final static int DIRECTION_UP = 2;
	private int state = 0;
	private int hitstunCounter = 0;

	HashSet<Integer> keysPressed = new HashSet<Integer>();
	BufferedImage neutralImage = new Image("img/" + name + "/neutral.png").img;
	// jab and tilts
	BufferedImage jabImage = new Image("img/" + name + "/attack1.png").img;
	BufferedImage fTiltImage = new Image("img/" + name + "/tilt_f.png").img;
	BufferedImage uTiltImage = new Image("img/" + name + "/tilt_u.png").img;
	BufferedImage dTiltImage = new Image("img/" + name + "/tilt_d.png").img;
	// aerials
	BufferedImage nairImage = new Image("img/" + name + "/air_n.png").img;
	BufferedImage fairImage = new Image("img/" + name + "/air_f.png").img;
	BufferedImage bairImage = new Image("img/" + name + "/air_b.png").img;
	BufferedImage dairImage = new Image("img/" + name + "/air_d.png").img;
	BufferedImage uairImage = new Image("img/" + name + "/air_u.png").img;
	// smashes
	BufferedImage uSmashChargeImage = new Image("img/" + name + "/smash_charge_u.png").img;
	BufferedImage uSmashImage = new Image("img/" + name + "/smash_u.png").img;
	BufferedImage dSmashChargeImage = new Image("img/" + name + "/smash_charge_d.png").img;
	BufferedImage dSmashImage = new Image("img/" + name + "/smash_d.png").img;
	BufferedImage fSmashChargeImage = new Image("img/" + name + "/smash_charge_f.png").img;
	BufferedImage fSmashImage = new Image("img/" + name + "/smash_f.png").img;
	// other
	BufferedImage hitstunImage = new Image("img/" + name + "/hitstun.png").img;
	BufferedImage jumpSquatImage = new Image("img/" + name + "/jumpsquat.png").img;
	BufferedImage jumpImage = new Image("img/" + name + "/jump.png").img;
	// shielding/dodging
	BufferedImage shieldImage = new Image("img/" + name + "/shield.png").img;
	BufferedImage dodgeImage = new Image("img/" + name + "/dodge.png").img;
	// lag
	BufferedImage lagImage = new Image("img/" + name + "/endlag.png").img;

	// grab
	BufferedImage grabImage = new Image("img/" + name + "/grab.png").img;
	BufferedImage grabbedImage = new Image("img/" + name + "/grabbed.png").img;
	BufferedImage uThrowImage = new Image("img/" + name + "/throw_u.png").img;
	BufferedImage dThrowImage = new Image("img/" + name + "/throw_d.png").img;
	BufferedImage fThrowImage = new Image("img/" + name + "/throw_f.png").img;
	BufferedImage bThrowImage = new Image("img/" + name + "/throw_b.png").img;

	private final double startx;
	private final double starty;
	private boolean isController = false;

	private String controllerName;
	private String moveAxisNameX;
	private String moveAxisNameY;
	private String moveAxisNameRX;
	private String moveAxisNameRY;
	private String moveAxisNameZ;
	private String moveAxisNameRZ;
	private String axisShield1;
	private String axisShield2;

	private double moveAxisMidpoint;
	private boolean isAxisRight = false;
	private boolean isAxisLeft = false;
	private boolean isAxisUp = false;
	private boolean isAxisDown = false;
	private boolean isAxisHalfway = false;
	private double axisDeadZone;
	private String buttonJump;
	private String buttonAttack;
	private String buttonGrab;
	private String buttonSpecial;
	public boolean isAttackButtonDown;
	public boolean isJumpButtonDown;
	private int portNum;
	private int jumpTimeBuffer = 0;
	public boolean wasJumpKeyDownLastFrame = false;

	public boolean[] jumpKeyDownHistory = new boolean[] { false, false, false };
	public boolean[] attackKeyDownHistory = new boolean[] { false, false, false };
	public boolean[] specialKeyDownHistory = new boolean[] { false, false, false };
	private double lastFrameX;
	private double lastFrameY;
	private double[] moveAxisHistoryX = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
	private double[] moveAxisHistoryY = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0 };
	private boolean lastFrameIsGrounded;
	private int landingLagCounter = 0;

	private int rollDistance = 10;
	private int rollCounter = 0;
	private int lagCounter = 0;
	private int rollLag = 10;

	private double moveAxisDeadZone;
	private double percent = 0;
	private int jumpSquatFrames = 8;
	private int jumpSquatBuffer = 0;
	private boolean canAirDodge = true;
	private int grabCounter = 0;
	private int grabLength = 20;
	private int grabbedTime = 0;
	private final int grabbedTimeDefault = 60;
	private Rectangle grabBox;
	private Game myGame;
	private Controller myController;
	private double neutralSpecialCharge = 0;
	private double neutralSpecialChargeIncrement = 0.006666666667;
	private Character grabbedPlayer = null;

	public Character(int posx, int posy, int upKey, int downKey, int leftKey, int rightKey, int modifierKey,
			int jumpKey, int attackKey, int specialKey, int shieldKey, int grabKey, Game gameinstance) {
		myGame = gameinstance;
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
		keyShield = shieldKey;
		keyGrab = grabKey;
		keySpecial = specialKey;
		hurtbox = new Hitbox(this, x, y, w, h, Game.TYPE_HURTBOX);
		placeShield();
	}

	public Character(int posx, int posy, String nameOfController, String axisName, String axisName2, String axisName3,
			String axisName4, String leftTrigger, String rightTrigger, double axisMidpoint, double deadZone,
			String jumpButton, String attackButton, String specialButton, String grabButton, Game gameinstance) {
		myGame = gameinstance;
		x = posx;
		y = posy;
		startx = x;
		starty = y;
		hurtbox = new Hitbox(this, x, y, w, h, Game.TYPE_HURTBOX);

		ArrayList<Character> characters = gameinstance.characters;

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
		moveAxisNameRX = axisName3;
		moveAxisNameRY = axisName4;
		moveAxisNameZ = leftTrigger;
		moveAxisNameRZ = rightTrigger;
		moveAxisMidpoint = axisMidpoint;
		buttonJump = jumpButton;
		buttonAttack = attackButton;
		buttonGrab = grabButton;
		buttonSpecial = specialButton;
		isController = true;
		controllerName = nameOfController;
		placeShield();
	}

	public void draw(Graphics g) {

		getController();
		updateStates();
		chargeNeutralSpecial();
		placeGrabBox();
		testForGrabAndDoGrabbedPlayerThings();
		fall();
		shouldBounceOffGround();
		checkifShouldApplyLandingLag();
		handleInput();
		tryGrab();
		doSpecialAttacks();
		move();
		blastZone();
		translateHitboxes();
		drawCorrectSprite(g);
		recordLastFrameX();
		recordLastFrameY();
		recordLastFrameIsGrounded();
		getControllerAxisInformation();
		attemptToShield();
		drawShield(g);
		placeShield();
	}

	public void placeShield() {
		shield = new Ellipse2D.Double();
		if (!isJumpButtonDownController() && state != STATE_JUMPSQUAT && state != STATE_JUMP
				&& state != STATE_LANDFALLSPECIAL) {
			if (isShielding && state != STATE_DODGE && state != STATE_LAG && !isJumpButtonDownController()) {
				shield.setFrame((x - w) + ((1 - shieldWidth) * w * 1.5), (y - 5) + ((1 - shieldWidth) * w),
						(h + 5) * shieldWidth, (h + 5) * shieldWidth);

				state = STATE_SHIELD;
				if (isAxisLeft) {
					velX = -5;
					dodge();
				}
				if (isAxisRight) {
					velX = 5;
					dodge();
				}
			} else if (state == STATE_SHIELD) {
				state = STATE_NEUTRAL;
			} else if (shieldWidth <= 1.0) {
				shieldWidth += 0.002083333333;
			}
			isShielding = false;
		}
	}

	public void setShield(Ellipse2D shield) {
		this.shield = shield;
	}

	public void breakShield() {
		if (shieldWidth <= .4) {
			isShielding = false;
			putIntoLag(4 * 60);
		}
	}

	public void attemptToShield() {
		if (state != STATE_GRABBED && state != STATE_GRAB) {
			if (isController) {
				if (myController != null)
					for (Component comp : myController.getComponents()) {
						if (comp.getName().equals(getLeftTrigger()) || comp.getName().equals(getRightTrigger())) {
							if (comp.getPollData() > getAxisMidpoint()) {
								if (isGrounded) {
									if (isGrounded && state != STATE_HITSTUN && state != STATE_DODGE) {
										isShielding = true;
										shieldWidth -= 0.002666666667;
										breakShield();
									}
								} else if (state != STATE_HITSTUN && state != STATE_DODGE && canAirDodge) {
									// velX = 0;
									// velY = 0;
									if (state != STATE_JUMPSQUAT) {
										dodge();
									}
								}
							}

						}
					}
			} else {
				if (isPressing(keyShield) && state != STATE_HITSTUN && state != STATE_DODGE && state != STATE_CROUCH) {
					if (isGrounded && state != STATE_JUMPSQUAT) {
						if (isPressing(keyLeft)) {
							velX = -5;
							dodge();
						} else if (isPressing(keyRight)) {
							velX = 5;
							dodge();
						} else {
							isShielding = true;
							shieldWidth -= 0.002666666667;
							breakShield();
						}

					} else {
						if (state != STATE_JUMPSQUAT) {
							/*
							 * if (isPressing(keyLeft)) velX -= waveDashSpeed;
							 * if (isPressing(keyRight)) velX += waveDashSpeed;
							 * if (isPressing(keyUp)) velY -= waveDashSpeed; if
							 * (isPressing(keyDown)) velY += waveDashSpeed;
							 */
							dodge();
						}
					}
				}
			}
		}
	}

	public Ellipse2D getShield() {
		return shield;
	}

	public void dodge() {

		if (isGrounded) {
			if (state == STATE_SHIELD) {
				rollCounter = rollDistance;
				state = STATE_DODGE;
			}

		} else if (canAirDodge) {
			rollCounter = rollDistance;
			state = STATE_DODGE;
			canAirDodge = false;
		}
	}

	public void putIntoLag(int lagAmount) {
		lagCounter = lagAmount;
		state = STATE_LAG;
	}

	public void applyDamage(double damageApplied) {
		percent += damageApplied;
	}

	public void getController() {
		for (Controller currentController : myGame.controllers) {
			if (currentController.getPortNumber() == portNum) {
				myController = currentController;
			}
		}
	}

	public void removeJumpButtonHistory() {
		for (boolean mybool : jumpKeyDownHistory) {
			mybool = false;
		}
	}

	public void drawShield(Graphics g) {
		if (isShielding) {
			Graphics2D g2 = (Graphics2D) g;
			g.setColor(Game.transparentblue);
			g2.fill(getShield());
		}
	}

	public void drawCorrectSprite(Graphics g) {
		// TODO draws the correct sprite given current state, direction and
		// other
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
		if (state == STATE_ATTACKUP) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(uTiltImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(uTiltImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_ATTACKDOWN) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(dTiltImage, (int) x, (int) y, (int) (w * 1.876), h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(dTiltImage, (int) x + w, (int) y, (int) (-w * 1.876), h, null);
		}
		if (state == STATE_ATTACK_NAIR) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(nairImage, (int) x, (int) y, (int) (w / 0.704735376), h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(nairImage, (int) x + w, (int) y, (int) (-w / 0.704735376), h, null);
		}
		if (state == STATE_ATTACK_FAIR) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(fairImage, (int) x, (int) y, (int) (w / 0.6203473945), h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(fairImage, (int) x + w, (int) y, (int) (-w / 0.6203473945), h, null);
		}
		if (state == STATE_ATTACK_BAIR) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(bairImage, (int) x, (int) y, (int) (w / 0.6203473945), h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(bairImage, (int) x + w, (int) y, (int) (-w / 0.6203473945), h, null);
		}
		if (state == STATE_ATTACK_DAIR) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(dairImage, (int) x, (int) y, (int) (w / 0.8445945946), h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(dairImage, (int) x + w, (int) y, (int) (-w / 0.8445945946), h, null);
		}
		if (state == STATE_ATTACK_UAIR) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(uairImage, (int) x, (int) y, (int) (w / 0.8445945946), h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(uairImage, (int) x + w, (int) y, (int) (-w / 0.8445945946), h, null);
		}
		// smash chargin has a bit of nuances in it so it needs it's own area
		if (state == STATE_SMASH_ATTACK_CHARGE && isGrounded) {
			if (smashAttackDirection == DIRECTION_UP) {
				if (direction == DIRECTION_RIGHT)
					g.drawImage(uSmashChargeImage, (int) x, (int) y, w, h, null);
				if (direction == DIRECTION_LEFT)
					g.drawImage(uSmashChargeImage, (int) x + w, (int) y, -w, h, null);
			}
			if (smashAttackDirection == DIRECTION_DOWN) {
				if (direction == DIRECTION_RIGHT)
					g.drawImage(dSmashChargeImage, (int) x, (int) y, w, h, null);
				if (direction == DIRECTION_LEFT)
					g.drawImage(dSmashChargeImage, (int) x + w, (int) y, -w, h, null);
			}
			if (smashAttackDirection == DIRECTION_LEFT || smashAttackDirection == DIRECTION_RIGHT) {
				if (direction == DIRECTION_RIGHT)
					g.drawImage(fSmashChargeImage, (int) x, (int) y, w, h, null);
				if (direction == DIRECTION_LEFT)
					g.drawImage(fSmashChargeImage, (int) x + w, (int) y, -w, h, null);
			}
		}
		if (state == STATE_SMASH_ATTACK_UP) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(uSmashImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(uSmashImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_SMASH_ATTACK_DOWN) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(dSmashImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(dSmashImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_SMASH_ATTACK_FORWARD) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(fSmashImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(fSmashImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_HITSTUN) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(hitstunImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(hitstunImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_JUMPSQUAT) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(jumpSquatImage, (int) x, (int) y + h / 2, w, h / 2, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(jumpSquatImage, (int) x + w, (int) y + h / 2, -w, h / 2, null);
		}
		if (state == STATE_JUMP) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(jumpImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(jumpImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_LANDINGLAG) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(jumpSquatImage, (int) x, (int) y, w, (int) (h), null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(jumpSquatImage, (int) x + w, (int) y, -w, (int) (h), null);
		}
		if (state == STATE_CROUCH) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(jumpSquatImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(jumpSquatImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_SHIELD) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(shieldImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(shieldImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_DODGE) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(dodgeImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(dodgeImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_LAG) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(lagImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(lagImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_GRAB) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(grabImage, (int) x, (int) y, (int) (w * 1.66), h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(grabImage, (int) x + w, (int) y, (int) (-w * 1.66), h, null);
		}
		if (state == STATE_GRABBED) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(grabbedImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(grabbedImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_UTHROW) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(uThrowImage, (int) x, (int) y, (int) (w * 1.068), h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(uThrowImage, (int) x + w, (int) y, (int) (-w * 1.068), h, null);
		}
		if (state == STATE_DTHROW) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(dThrowImage, (int) x, (int) y, w, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(dThrowImage, (int) x + w, (int) y, -w, h, null);
		}
		if (state == STATE_FTHROW) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(fThrowImage, (int) x, (int) y, (int) (w * 1.376), h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(fThrowImage, (int) x + w, (int) y, (int) (-w * 1.376), h, null);
		}
		if (state == STATE_BTHROW) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(bThrowImage, (int) x, (int) y - (int) (h * 1.1456) + h, (int) (w * 1.232),
						(int) (h * 1.1456), null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(bThrowImage, (int) x + w, (int) y - (int) (h * 1.1456) + h, (int) (-w * 1.232),
						(int) (h * 1.1456), null);
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
		if (length > 0) {
			hitstunCounter = length;
			state = STATE_HITSTUN;
			System.out.println(length);
		}
	}

	private void blastZone() {
		if (x < (0 - 50) || x + w > (1200 + 50) || y < (0 - 50) || y + h > (675 + 50)) {
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
				if (state == STATE_NEUTRAL || !isGrounded) {
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
				if (isGrounded) {
					if (state == STATE_NEUTRAL && isPressing(keyDown)) {
						state = STATE_CROUCH;
					}
					if (state == STATE_CROUCH && !isPressing(keyDown)) {
						state = STATE_NEUTRAL;
					}
				}
			}
			if (!isGrounded && !isPressing(keyAttack) && state == STATE_NEUTRAL) {
				if (Math.abs(velY) < 3) {
					if (isPressing(keyDown))
						velY = fallSpeed * 10;
				}
			}
		} else {
			if (state != STATE_HITSTUN) {
				if (state == STATE_NEUTRAL || !isGrounded) {
					if (isAxisRight)
						runRight();
					if (isAxisLeft)
						runLeft();
				}
			}
			if (isGrounded) {
				if (state == STATE_NEUTRAL && isAxisDown) {
					state = STATE_CROUCH;
				}
				if (state == STATE_CROUCH && !isAxisDown) {
					state = STATE_NEUTRAL;

				}
			}
			if (!isGrounded && !isAttackButtonDownController() && state == STATE_NEUTRAL) {
				if (Math.abs(velY) < 3) {
					if (isAxisDown)
						velY = fallSpeed * 10;
				}
			}
		}
		if (!isController)
			chooseAttack();

	}

	public void runLeft() {

		if (isGrounded) {
			if (state == STATE_NEUTRAL) {
				if (Math.abs(moveAxisHistoryX[0]) < moveAxisMidpoint || isPressing(keyModifier))
					velX = -runSpeed / 3;
				else
					velX = -runSpeed;
				direction = DIRECTION_LEFT;
			}
		} else if (velX > -maxAirSpeed && state != STATE_AIRDODGE && canAirDodge && state != STATE_GRABBED)
			velX -= horizontalInAirSpeed;

	}

	public void runRight() {

		if (isGrounded) {
			if (state == STATE_NEUTRAL) {
				if (Math.abs(moveAxisHistoryX[0]) < moveAxisMidpoint || isPressing(keyModifier))
					velX = runSpeed / 3;
				else
					velX = runSpeed;
				direction = DIRECTION_RIGHT;
			}
		} else if (velX < maxAirSpeed && state != STATE_AIRDODGE && canAirDodge && state != STATE_GRABBED)
			velX += horizontalInAirSpeed;

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

	public boolean isSpecialButtonDownController() {
		for (boolean bool : specialKeyDownHistory) {
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
			if (state == STATE_NEUTRAL || state == STATE_CROUCH) {
				if (!isController) {
					if (isGrounded) {
						/*
						 * velY -= jumpHeight; keysPressed.remove(keyJump);
						 * state = STATE_JUMP; jumpTimeBuffer = 5;
						 * jumpKeyDownHistory[0] = true;
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
				} else if (state != STATE_SMASH_ATTACK_CHARGE) {
					if (isGrounded) {
						enterJumpSquat();
					} else if (hasDoubleJump) {
						velX = 0;
						velY = 0;
						velY -= jumpHeight * 1.2;
						hasDoubleJump = false;
						state = STATE_JUMP;
						jumpTimeBuffer = 2;
						keysPressed.remove(keyJump);
					}

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
					velY -= lowJumpHeight;
					state = STATE_JUMP;
					jumpTimeBuffer = 2;
				} else {
					velY--;
					velY -= jumpHeight;
					state = STATE_JUMP;
					jumpTimeBuffer = 5;
				}
			} else {
				if (!isPressing(keyJump)) {
					velY -= lowJumpHeight;
					keysPressed.remove(keyJump);
					state = STATE_JUMP;
					jumpTimeBuffer = 2;
					jumpKeyDownHistory[0] = true;
				} else {
					velY -= jumpHeight;
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

	public void cycleArray(double newDouble, double[] array) {
		array[4] = array[3];
		array[3] = array[2];
		array[2] = array[1];
		array[1] = array[0];
		array[0] = newDouble;
	}

	public void fall() {
		if (state != STATE_GRABBED) {
			if (!isGrounded) {

				velY += fallSpeed;

			} else {
				if (state == STATE_NEUTRAL || state == STATE_CROUCH)
					velY = 0;
			}
		}
	}

	public void checkifShouldApplyLandingLag() {
		if (isGrounded) {
			if (!lastFrameIsGrounded) {
				if (state == STATE_NEUTRAL)
					placeInLandingLag(3);
				else if (state == STATE_ATTACK_NAIR) {
					placeInLandingLag(6);
					hitboxes.clear();
				} else if (state == STATE_ATTACK_FAIR) {
					placeInLandingLag(4);
					hitboxes.clear();
				} else if (state == STATE_ATTACK_BAIR) {
					placeInLandingLag(10);
					hitboxes.clear();
				} else if (state == STATE_ATTACK_UAIR) {
					placeInLandingLag(4);
					hitboxes.clear();
				} else if (state == STATE_ATTACK_DAIR) {
					placeInLandingLag(15);
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
		if (state != STATE_GRABBED) {
			applyJumpWhenRequired();
			chargeSmashAttack();

			if (grabCounter > 0) {
				if (grabbedPlayer == null)
					grabCounter--;
			} else if (state == STATE_GRAB || state == STATE_BTHROW || state == STATE_UTHROW || state == STATE_FTHROW
					|| state == STATE_DTHROW) {
				if (grabbedPlayer == null) {
					state = STATE_NEUTRAL;
					putIntoLag(10);
				}
			}

			if (!canAirDodge && isGrounded) {
				canAirDodge = true;
			}
			if (state == STATE_LAG) {
				if (lagCounter > 0) {
					lagCounter--;
				} else {
					state = STATE_NEUTRAL;
				}
			}
			if (state == STATE_DODGE) {
				if (rollCounter > 0) {
					rollCounter--;
				} else {

					if (isGrounded) {
						putIntoLag(rollLag);
						velX = 0;
					} else {
						putIntoLag(rollLag * 3);
					}
				}
			}
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
			cycleArray(false, specialKeyDownHistory);
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
			getKeyboardTiltInfo();
			checkForCStickSmashAttacks();
		}
	}

	// TODO change
	public void chargeSmashAttack() {
		if (state == STATE_SMASH_ATTACK_CHARGE) {
			if (!isController) {
				if (smashAttackDirection == DIRECTION_UP) {

					if (isPressing(keyAttack) && smashAttackChargePercent < 1.4) {
						smashAttackChargePercent += 0.006666666667;
					} else {
						upSmash();
						keysPressed.remove(keyAttack);
					}
				} else if (smashAttackDirection == DIRECTION_DOWN) {

					if (isPressing(keyAttack) && smashAttackChargePercent < 1.4) {
						smashAttackChargePercent += 0.006666666667;
					} else {
						downSmash();
						keysPressed.remove(keyAttack);
					}
				} else if (smashAttackDirection == DIRECTION_LEFT || smashAttackDirection == DIRECTION_RIGHT) {

					if (isPressing(keyAttack) && smashAttackChargePercent < 1.4) {
						smashAttackChargePercent += 0.006666666667;
					} else {
						forwardSmash();
						keysPressed.remove(keyAttack);
					}
				}

			} else {
				// for controllers
				if (smashAttackDirection == DIRECTION_UP) {
					if (isAttackButtonDownController() && smashAttackChargePercent < 1.4) {
						smashAttackChargePercent += 0.006666666667;
					} else {
						upSmash();
					}
				} else if (smashAttackDirection == DIRECTION_DOWN) {
					if (isAttackButtonDownController() && smashAttackChargePercent < 1.4) {
						smashAttackChargePercent += 0.006666666667;
					} else {
						downSmash();
					}
				} else if (smashAttackDirection == DIRECTION_LEFT || smashAttackDirection == DIRECTION_RIGHT) {
					if (isAttackButtonDownController() && smashAttackChargePercent < 1.4) {
						smashAttackChargePercent += 0.006666666667;
					} else {
						forwardSmash();
					}
				}
			}
		}
	}

	public void getKeyboardTiltInfo() {
		if (!isController) {
			if (isPressing(keyUp))
				cycleArray(1.0, moveAxisHistoryY);
			else if (isPressing(keyDown))
				cycleArray(-1.0, moveAxisHistoryY);
			else
				cycleArray(0.0, moveAxisHistoryY);

			if (isPressing(keyLeft))
				cycleArray(-1.0, moveAxisHistoryX);
			else if (isPressing(keyRight))
				cycleArray(1.0, moveAxisHistoryX);
			else
				cycleArray(0.0, moveAxisHistoryX);
		}
	}

	public void getControllerAxisInformation() {
		for (Controller controller : myGame.controllers) {
			if (controller.getPortNumber() == portNum) {
				for (Component comp : controller.getComponents()) {
					if (comp.getName() == moveAxisNameX) {
						if (Math.abs(comp.getPollData()) > getAxisDeadZone()) {
							cycleArray(comp.getPollData(), moveAxisHistoryX);

						}
					}
					if (comp.getName() == moveAxisNameY) {
						if (Math.abs(comp.getPollData()) > getAxisDeadZone())
							cycleArray(comp.getPollData(), moveAxisHistoryY);
					}

				}
			}
		}
	}

	public void applyFriction() {
		if (isGrounded && state != STATE_DODGE && state != STATE_JUMPSQUAT)
			velX /= horizontalSlowdownFactor;
		if (state == STATE_LANDFALLSPECIAL) {
			velX /= horizontalSlowdownFactor / 2;
		}
	}

	// TODO attack locations. syntax is : character, local position x, local
	// position y,width, height, hitstun time, knockbackx, knockbacky, time it
	// will be out for
	public void chooseAttack() {
		if (state == STATE_NEUTRAL || state == STATE_CROUCH) {
			if (state != STATE_LANDINGLAG) {
				if (!isController) {

					boolean shouldTiltX = true;
					boolean shouldTiltLeft = true;
					for (double currentAxisX : moveAxisHistoryX) {
						if (Math.abs(currentAxisX) == 0 || state == STATE_SMASH_ATTACK_CHARGE) {
							shouldTiltX = false;
							break;
						}
					}
					boolean shouldTiltY = true;
					boolean shouldTiltUp = true;
					for (double currentAxisY : moveAxisHistoryY) {
						if (Math.abs(currentAxisY) == 0 || state == STATE_SMASH_ATTACK_CHARGE) {
							shouldTiltY = false;
							break;
						}
					}

					if (isPressing(keyAttack)) {
						if (isPressing(keyUp)) {
							if (isGrounded) {
								if (shouldTiltY && moveAxisHistoryY[0] == 1.0) {
									uTilt();
								} else {
									state = STATE_SMASH_ATTACK_CHARGE;
									smashAttackDirection = DIRECTION_UP;
								}
							} else {
								uair();
							}
						} else if (isPressing(keyDown)) {
							if (isGrounded) {
								if (shouldTiltY && moveAxisHistoryY[0] == -1.0) {
									dTilt();
								} else {
									state = STATE_SMASH_ATTACK_CHARGE;
									smashAttackDirection = DIRECTION_DOWN;
								}
							} else {
								dair();
							}
						} else if (isPressing(keyLeft)) {
							if (isGrounded) {
								if (shouldTiltX)
									fTilt();
								else {
									state = STATE_SMASH_ATTACK_CHARGE;
									smashAttackDirection = DIRECTION_LEFT;
									direction = DIRECTION_LEFT;
								}
							} else {
								if (direction == DIRECTION_LEFT)
									fair();
								else
									bair();
							}
						} else if (isPressing(keyRight)) {
							if (isGrounded) {
								if (shouldTiltX)
									fTilt();
								else {
									state = STATE_SMASH_ATTACK_CHARGE;
									smashAttackDirection = DIRECTION_RIGHT;
									direction = DIRECTION_RIGHT;
								}
							} else {
								if (direction == DIRECTION_RIGHT)
									fair();
								else
									bair();
							}
						} else {
							if (isGrounded) {
								if (Math.abs(velX) < .1 && Math.abs(velY) < .1 && state != STATE_SMASH_ATTACK_CHARGE)
									jab();
							} else if (state == STATE_NEUTRAL) {
								nair();
							}
						}
						if (state != STATE_SMASH_ATTACK_CHARGE) {
							keysPressed.remove(keyAttack);
						}
					}
				}

			}
		}
	}

	public void chooseAttack(Controller myController) {
		if (canAttack()) {
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
			boolean shouldbetilt = true;

			for (Component comp : myController.getComponents()) {

				if (comp.getName().equals(moveAxisNameX)) {
					boolean shouldTilt = true;
					// boolean shouldTilt = true;
					for (double currentAxisX : moveAxisHistoryX) {
						if (Math.abs(currentAxisX) <= moveAxisMidpoint) {
							shouldTilt = false;
							break;
						}
					}
					if (Math.abs(comp.getPollData()) < moveAxisMidpoint
							&& Math.abs(comp.getPollData()) > axisDeadZone) {
						if (comp.getPollData() < 0) {
							isAxisLeftLocal = true;
						} else {
							isAxisRightLocal = true;
						}

					}
					if (Math.abs(comp.getPollData()) > moveAxisMidpoint) {
						if (comp.getPollData() < 0) {
							if (isGrounded) {
								if (shouldTilt) {
									isAxisLeftLocal = true;

								} else {
									state = STATE_SMASH_ATTACK_CHARGE;
									smashAttackDirection = DIRECTION_LEFT;
									direction = DIRECTION_LEFT;
								}
							} else {
								if (direction == DIRECTION_LEFT)
									fair();
								else
									bair();
								break;
							}
						} else {
							if (isGrounded) {
								if (shouldTilt) {
									isAxisRightLocal = true;
								} else {
									state = STATE_SMASH_ATTACK_CHARGE;
									smashAttackDirection = DIRECTION_RIGHT;
									direction = DIRECTION_RIGHT;
								}
							} else {
								if (direction == DIRECTION_RIGHT)
									fair();
								else
									bair();
								break;
							}
						}
					}

				}

				if (comp.getName().equals(moveAxisNameY)) {
					boolean shouldTilt = true;
					if (Math.abs(comp.getPollData()) < moveAxisMidpoint
							&& Math.abs(comp.getPollData()) > axisDeadZone) {
						if (comp.getPollData() < 0) {
							isAxisUpLocal = true;
						} else {
							isAxisDownLocal = true;
						}

					}
					for (double currentAxisY : moveAxisHistoryY) {
						if (Math.abs(currentAxisY) <= moveAxisMidpoint) {
							shouldTilt = false;
							break;
						}
					}
					if (Math.abs(comp.getPollData()) > moveAxisMidpoint) {
						if (comp.getPollData() < 0) {
							if (isGrounded) {
								if (shouldTilt) {
									isAxisUpLocal = true;
								} else {
									state = STATE_SMASH_ATTACK_CHARGE;
									smashAttackDirection = DIRECTION_UP;
								}
							} else {
								uair();
								break;
							}
						} else {
							if (isGrounded) {
								if (shouldTilt) {
									isAxisDownLocal = true;

								} else {
									state = STATE_SMASH_ATTACK_CHARGE;
									smashAttackDirection = DIRECTION_DOWN;
								}
							} else {
								dair();
								break;
							}
						}
					}

					// actual code starts here
					if (canAttack && state != STATE_SMASH_ATTACK_CHARGE && isGrounded) {
						if (isAxisUpLocal) {
							uTilt();

						} else if (isAxisDownLocal) {
							dTilt();
						}

						else if (isAxisRightLocal) {
							fTilt();

						} else if (isAxisLeftLocal) {
							fTilt();
						}

						else {

							if (Math.abs(velX) < .1 && Math.abs(velY) < .1)
								jab();
						}

					}
					if (state == STATE_NEUTRAL && !isGrounded) {
						nair();
					}
				}
			}
		}
	}

	public void checkForCStickSmashAttacks() {
		if (state == STATE_NEUTRAL || state == STATE_CROUCH) {
			boolean canAttack = true;

			if (myController != null)
				for (Component comp : myController.getComponents()) {
					if (comp.getName().equals(moveAxisNameRX)) {
						if (Math.abs(comp.getPollData()) > moveAxisMidpoint) {
							if (comp.getPollData() > 0) {
								if (isGrounded) {
									state = STATE_SMASH_ATTACK_CHARGE;
									smashAttackDirection = DIRECTION_RIGHT;
									direction = DIRECTION_RIGHT;
									break;
								} else if (canAttack()) {
									if (direction == DIRECTION_RIGHT)
										fair();
									else
										bair();
									break;
								}
							} else {
								if (isGrounded) {
									state = STATE_SMASH_ATTACK_CHARGE;
									smashAttackDirection = DIRECTION_LEFT;
									direction = DIRECTION_LEFT;
									break;
								} else if (canAttack()) {
									if (direction == DIRECTION_LEFT)
										fair();
									else
										bair();
									break;
								}
							}

						}
					}
					if (comp.getName().equals(moveAxisNameRY)) {
						if (Math.abs(comp.getPollData()) > moveAxisMidpoint) {
							if (comp.getPollData() > 0) {
								if (isGrounded) {
									state = STATE_SMASH_ATTACK_CHARGE;
									smashAttackDirection = DIRECTION_DOWN;
									break;
								} else if (canAttack) {
									dair();
									break;
								}

							} else {
								if (isGrounded) {
									state = STATE_SMASH_ATTACK_CHARGE;
									smashAttackDirection = DIRECTION_UP;
									break;
								} else if (canAttack) {
									uair();
									break;
								}
							}

						}
					}
				}
		}
	}

	public void applyKnockback(double newVelX, double newVelY, int direction2) {
		double dampenedpercent = percent / 100;
		velX = (newVelX * (dampenedpercent + 1));
		velY = newVelY * (dampenedpercent + 1);
		System.out.println(direction2);
	}

	public void shouldBounceOffGround() {
		if (isGrounded && velY > 0 && state == STATE_HITSTUN) {
			velY = -velY;
		}
	}

	public void tryGrab() {
		if (isGrounded) {
			if (!isController) {
				if (isPressing(keyGrab)) {
					if (state == STATE_NEUTRAL)
						grab();
				}
			}
		}
	}

	public boolean canAttack() {
		if (hitboxes.isEmpty()) {
			if (state == STATE_CROUCH || state == STATE_NEUTRAL) {
				return true;
			}
		}
		return false;
	}

	public void grab() {
		if (state == STATE_NEUTRAL) {
			state = STATE_GRAB;
			grabCounter = grabLength;
		}
	}

	public void placeGrabBox() {

		if (state == STATE_GRAB && grabbedPlayer == null) {
			if (direction == DIRECTION_RIGHT)
				grabBox = new Rectangle((int) x + w, (int) y + 5, 20, 30);
			else
				grabBox = new Rectangle((int) (x + w) - (w) - w, (int) y + 5, 20, 30);
		} else {
			grabBox = null;
		}
	}

	// self explanatory naming FTW
	public void testForGrabAndDoGrabbedPlayerThings() {

		for (Character person : myGame.characters) {
			if (grabBox != null)
				if (person.getHurtbox().getRect().intersects(grabBox) && person.getState() != STATE_GRAB
						&& person.getState() != STATE_DODGE) {
					velX = 0;
					velY = 0;
					grabbedPlayer = person;
					grabbedPlayer.rollCounter = -1;
					grabbedPlayer.state = STATE_GRABBED;
					grabbedPlayer.velX = 0;
					grabbedPlayer.velY = 0;
					grabbedTime = (int) (grabbedTimeDefault + (grabbedPlayer.percent));
					if (direction == DIRECTION_RIGHT) {
						grabbedPlayer.x = x + w + 5;
						grabbedPlayer.y = y - 5;
						grabbedPlayer.direction = DIRECTION_LEFT;
					} else {
						grabbedPlayer.x = x - w - 5;
						grabbedPlayer.y = y - 5;
						grabbedPlayer.direction = DIRECTION_RIGHT;
					}
					break;
				}
		}
		if (grabbedPlayer != null) {
			if (grabbedPlayer.state != STATE_GRABBED)
				grabbedPlayer.state = STATE_GRABBED;

			if (grabbedTime <= 0) {
				grabbedPlayer.state = STATE_NEUTRAL;
				grabbedPlayer = null;
				grabCounter = 0;
			} else {
				grabbedTime--;
				if (isController) {

					// each throw should in future set the player who is
					// throwing into a specific state to show a new image
					if (isAxisUp && grabbedPlayer != null) {
						uThrow();
						grabbedPlayer.applyHitstun((int) (10 + grabbedPlayer.percent / 10));
						grabbedPlayer = null;
						state = STATE_UTHROW;
					}
					if (isAxisDown && grabbedPlayer != null) {
						dThrow();
						grabbedPlayer.applyHitstun((int) (10 + grabbedPlayer.percent / 10));
						grabbedPlayer = null;
						state = STATE_DTHROW;
					}
					if (isAxisLeft && grabbedPlayer != null) {
						if (direction == DIRECTION_LEFT) {
							fThrow();
							grabbedPlayer.applyHitstun((int) (10 + grabbedPlayer.percent / 10));
							grabbedPlayer = null;
							state = STATE_FTHROW;
						} else {
							bThrow();
							grabbedPlayer.applyHitstun((int) (10 + grabbedPlayer.percent / 10));
							grabbedPlayer = null;
							state = STATE_BTHROW;
						}
					}
					if (isAxisRight && grabbedPlayer != null) {
						if (direction == DIRECTION_RIGHT) {
							fThrow();
							grabbedPlayer.applyHitstun((int) (10 + grabbedPlayer.percent / 10));
							grabbedPlayer = null;
							state = STATE_FTHROW;
						} else {
							bThrow();
							grabbedPlayer.applyHitstun((int) (10 + grabbedPlayer.percent / 10));
							grabbedPlayer = null;
							state = STATE_BTHROW;
						}
					}
				} else {
					// each throw should in future set the player who is
					// throwing into a specific state to show a new image
					if (isPressing(keyUp) && grabbedPlayer != null) {
						uThrow();
						grabbedPlayer.applyHitstun((int) (10 + grabbedPlayer.percent / 10));
						grabbedPlayer = null;
						state = STATE_UTHROW;
					}
					if (isPressing(keyDown) && grabbedPlayer != null) {
						dThrow();
						grabbedPlayer.applyHitstun((int) (10 + grabbedPlayer.percent / 10));
						grabbedPlayer = null;
						state = STATE_DTHROW;
					}
					if (isPressing(keyLeft) && grabbedPlayer != null) {
						if (direction == DIRECTION_LEFT) {
							fThrow();
							grabbedPlayer.applyHitstun((int) (10 + grabbedPlayer.percent / 10));
							grabbedPlayer = null;
							state = STATE_FTHROW;
						} else {
							bThrow();
							grabbedPlayer.applyHitstun((int) (10 + grabbedPlayer.percent / 10));
							grabbedPlayer = null;
							state = STATE_BTHROW;
						}
					}
					if (isPressing(keyRight) && grabbedPlayer != null) {
						if (direction == DIRECTION_RIGHT) {
							fThrow();
							grabbedPlayer.applyHitstun((int) (10 + grabbedPlayer.percent / 10));
							grabbedPlayer = null;
							state = STATE_FTHROW;
						} else {
							bThrow();
							grabbedPlayer.applyHitstun((int) (10 + grabbedPlayer.percent / 10));
							grabbedPlayer = null;
							state = STATE_BTHROW;
						}
					}
				}
			}
		}
	}

	// TODO attack below here
	// AttackHitbox creation syntax , character, localx,
	// localy,width,height,xknockback,yknockback,hitstunlen,lifetime, damage
	public void jab() {
		hitboxes.add(new AttackHitbox(this, 20, 15, 15, 15, .5, -1, 10, 5, 3, 3));
		state = STATE_ATTACK;
		velX += direction * 2;
	}

	public void fTilt() {
		hitboxes.add(new AttackHitbox(this, 20, 20, 20, 15, 2, -1, 10, 5, 6, 5));
		hitboxes.add(new AttackHitbox(this, 20, 25, 10, 15, 1, -3, 10, 5, 8, 5));
		state = STATE_ATTACKSIDE;
		velX += direction * 3;
	}

	public void uTilt() {
		hitboxes.add(new AttackHitbox(this, 10, -20, 15, 25, 1, -6, 10, 10, 8, 5));
		state = STATE_ATTACKUP;
	}

	public void dTilt() {
		hitboxes.add(new AttackHitbox(this, 20, 35, 25, 15, 3, -4, 10, 6, 4, 2));
		state = STATE_ATTACKDOWN;
	}

	public void nair() {
		hitboxes.add(new AttackHitbox(this, 20, 15, 15, 15, .5, -1, 10, 20, 5, 3));
		state = STATE_ATTACK_NAIR;
		velX += direction * 2;
	}

	public void fair() {
		hitboxes.add(new AttackHitbox(this, 20 + 2, 0, 15, 15, .5, -1, 10, 10, 5, 1));
		hitboxes.add(new AttackHitbox(this, 25 + 2, 6, 15, 15, .5, -1, 10, 10, 5, 1));
		hitboxes.add(new AttackHitbox(this, 30 + 2, 12, 15, 15, .5, -1, 10, 10, 5, 1));
		hitboxes.add(new AttackHitbox(this, 25 + 2, 18, 15, 15, .5, -1, 10, 10, 5, 1));
		hitboxes.add(new AttackHitbox(this, 20 + 2, 24, 15, 15, .5, -1, 10, 10, 5, 1));
		state = STATE_ATTACK_FAIR;
	}

	public void bair() {
		hitboxes.add(new AttackHitbox(this, -20, 15, 30, 15, -10, -1, 10, 20, 5, 3));
		state = STATE_ATTACK_BAIR;
	}

	public void uair() {
		hitboxes.add(new AttackHitbox(this, -5, -10, w + 10, 15, 0, -5, 5, 20, 20, 10));
		state = STATE_ATTACK_UAIR;
	}

	public void dair() {
		hitboxes.add(new AttackHitbox(this, 0, h, w, 15, .5, 5, 5, 20, 5, 10));
		state = STATE_ATTACK_DAIR;
	}

	public void upSmash() {
		hitboxes.add(new AttackHitbox(this, 10, -20, 20, 30, 1 * smashAttackChargePercent,
				-10 * smashAttackChargePercent, 30, 10, 15 * smashAttackChargePercent, 20));
		state = STATE_SMASH_ATTACK_UP;
		smashAttackChargePercent = 1.0;
	}

	public void downSmash() {
		hitboxes.add(new AttackHitbox(this, -w, h - 15, w * 1.5, 15, -10 * smashAttackChargePercent,
				1 * smashAttackChargePercent, 30, 10, 2 * smashAttackChargePercent, 20));

		hitboxes.add(new AttackHitbox(this, w / 2, h - 15, w * 1.5, 15, 10 * smashAttackChargePercent,
				1 * smashAttackChargePercent, 10, 10, 2 * smashAttackChargePercent, 20));

		state = STATE_SMASH_ATTACK_DOWN;
		smashAttackChargePercent = 1.0;
	}

	public void forwardSmash() {
		hitboxes.add(
				new AttackHitbox(this, w, 0, 10, h, 5 * (smashAttackChargePercent * 2), 0 * smashAttackChargePercent,
						(int) (10 * smashAttackChargePercent), 10, 5 * (smashAttackChargePercent * 2), 20));
		state = STATE_SMASH_ATTACK_FORWARD;
		smashAttackChargePercent = 1.0;
	}

	public void fThrow() {
		grabbedPlayer.velX = (5 + grabbedPlayer.percent / 8) * direction;
		grabbedPlayer.velY = -8 + percent / 10;
		grabbedPlayer.applyDamage(7);
	}

	public void bThrow() {
		grabbedPlayer.velX = (5 + grabbedPlayer.percent / 10) * -direction;
		grabbedPlayer.velY = -10 + percent / 10;
		grabbedPlayer.applyDamage(3);
	}

	public void uThrow() {
		grabbedPlayer.velY = (-5 + (grabbedPlayer.percent / 20)) + 10;
		grabbedPlayer.applyDamage(5);
	}

	public void dThrow() {
		grabbedPlayer.velX = (5) * -direction;
		grabbedPlayer.velY = 2 + percent / 20;
		grabbedPlayer.applyDamage(2);
	}

	public void upSpecial() {
		hitboxes.add(new AttackHitbox(this, -5, -10, w + 10, 15, 0, -5, 5, 20, 20, 10));
	}

	public void downSpecial() {
		hitboxes.add(new AttackHitbox(this, -5, -10, w + 10, 15, 0, -5, 5, 20, 20, 10));
	}

	public void forwardSpecial() {
		hitboxes.add(new AttackHitbox(this, -5, -10, w + 10, 15, 0, -5, 5, 20, 20, 10));
	}

	public void neutralSpecial() {
		hitboxes.add(new AttackHitbox(this, -2, h / 3, 1000, 15, 10 + percent / 10, -5, 20, 60, 40, 60));
	}

	public void chargeNeutralSpecial() {
		if (state == STATE_NEUTRALSPECIAL) {
			if (isController) {
				if (isSpecialButtonDownController() && neutralSpecialCharge < 1) {
					neutralSpecialCharge += neutralSpecialChargeIncrement;
				} else {
					if (neutralSpecialCharge >= 1) {
						neutralSpecial();
						neutralSpecialCharge = 0;
					} else if (hitboxes.size() == 0)
						state = STATE_NEUTRAL;
				}
			} else {
				if (isPressing(keySpecial) && neutralSpecialCharge < 1) {
					neutralSpecialCharge += neutralSpecialChargeIncrement;
				} else {
					if (neutralSpecialCharge >= 1) {
						neutralSpecial();
						neutralSpecialCharge = 0;
					} else if (hitboxes.size() == 0)
						state = STATE_NEUTRAL;
				}
			}
		} else if (neutralSpecialCharge != 0) {
			neutralSpecialCharge = 0;
		}
	}
	// end of attack codes

	public void doSpecialAttacks() {
		if (state == STATE_NEUTRAL) {
			if (isController) {
				if (isSpecialButtonDownController()) {
					if (isAxisUp) {
						upSpecial();
						state = STATE_UPSPECIAL;
					} else if (isAxisDown) {
						downSpecial();
						state = STATE_DOWNSPECIAL;
					} else if (isAxisLeft || isAxisRight) {
						forwardSpecial();
						state = STATE_FORWARDSPECIAL;
					} else {
						state = STATE_NEUTRALSPECIAL;
					}

				}
			} else if (isPressing(keySpecial)) {
				if (isPressing(keyUp)) {
					upSpecial();
					state = STATE_UPSPECIAL;
				} else if (isPressing(keyDown)) {
					downSpecial();
					state = STATE_DOWNSPECIAL;
				} else if (isPressing(keyLeft)) {
					forwardSpecial();
					state = STATE_FORWARDSPECIAL;
				} else if (isPressing(keyRight)) {
					forwardSpecial();
					state = STATE_FORWARDSPECIAL;
				} else {
					state = STATE_NEUTRALSPECIAL;
				}
			}
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

	// hitboxes and hurtboxes are updated here
	public void translateHitboxes() {
		if (state == STATE_ATTACKDOWN || state == STATE_CROUCH || state == STATE_LANDINGLAG)
			hurtbox.updateLocation(x, y + h * .2, w, h * .8);
		else if (state == STATE_ATTACK_BAIR) {
			if (direction == DIRECTION_RIGHT)
				hurtbox.updateLocation(x + w - 5, y, w, h);
			if (direction == DIRECTION_LEFT)
				hurtbox.updateLocation(x - w + 5, y, w, h);
		} else
			hurtbox.updateLocation(x, y, w, h);
		for (AttackHitbox box : hitboxes) {
			if (direction == DIRECTION_RIGHT)
				box.updateLocation(x + box.getlocalX(), y + box.getlocalY(), box.getWidth(), box.getHeight());
			if (direction == DIRECTION_LEFT)
				box.updateLocation((x + w) - box.getlocalX() - box.getWidth(), y + box.getlocalY(), box.getWidth(),
						box.getHeight());
		}
		for (AttackHitbox box : hitboxes) {

			if (!(box.getEndLag() > 0)) {
				hitboxes.remove(box);
				if (state != STATE_LANDINGLAG)
					state = STATE_NEUTRAL;
				break;
			}
		}
		if (state == STATE_HITSTUN)
			hitboxes.clear();
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

	public int getModifierKey() {
		return keyModifier;
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

	public String getAxisNameRX() {
		return moveAxisNameRX;
	}

	public String getAxisNameRY() {
		return moveAxisNameRY;
	}

	public void setAxisNameX(String newName) {
		moveAxisNameX = newName;
	}

	public void setAxisNameY(String newName) {
		moveAxisNameY = newName;
	}

	public void setAxisNameRX(String newName) {
		moveAxisNameRX = newName;
	}

	public void setAxisNameRY(String newName) {
		moveAxisNameRY = newName;
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

	public void setAxisMidpoint(double newAxisMidpoint) {
		moveAxisMidpoint = newAxisMidpoint;
	}

	// changes the controls at runtime
	public void changecontrols(int newKeyUp, int newKeyDown, int newKeyLeft, int newKeyRight, int newKeyJump,
			int newKeyAttack, int newKeyModifier) {
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
		if (newKeyModifier != -1)
			keyModifier = newKeyModifier;
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
		return isAxisUp;
	}

	public boolean getIsAxisDown() {
		return isAxisDown;
	}

	public boolean getIsAxisLeft() {
		return isAxisLeft;
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

	public String getLeftTrigger() {
		return moveAxisNameZ;
	}

	public String getRightTrigger() {
		return moveAxisNameRZ;
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

	public void setRollDistance(int newRollDistance) {
		rollDistance = newRollDistance;
	}

	public int getShieldKey() {
		return keyShield;
	}

	public void setShieldKey(int newShieldKey) {
		keyShield = newShieldKey;
	}

	public int getKeyGrab() {
		return keyGrab;
	}

	public void setKeyGrab(int keyGrab) {
		this.keyGrab = keyGrab;
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

	public String getButtonGrab() {
		return buttonGrab;
	}

	public void setButtonGrab(String buttonGrab) {
		this.buttonGrab = buttonGrab;
	}

	public Rectangle getGrabBox() {
		return grabBox;
	}

	public void setGrabBox(Rectangle grabBox) {
		this.grabBox = grabBox;
	}

	public Character getGrabbedPlayer() {
		return grabbedPlayer;
	}

	public void setGrabbedPlayer(Character grabbedPlayer) {
		this.grabbedPlayer = grabbedPlayer;
	}

	public int getKeySpecial() {
		return keySpecial;
	}

	public void setKeySpecial(int keySpecial) {
		this.keySpecial = keySpecial;
	}

	public double getNeutralSpecialChargeIncrement() {
		return neutralSpecialChargeIncrement;
	}

	public void setNeutralSpecialChargeIncrement(double neutralSpecialChargeIncrement) {
		this.neutralSpecialChargeIncrement = neutralSpecialChargeIncrement;
	}

	public String getButtonSpecial() {
		return buttonSpecial;
	}

	public void setButtonSpecial(String buttonSpecial) {
		this.buttonSpecial = buttonSpecial;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
