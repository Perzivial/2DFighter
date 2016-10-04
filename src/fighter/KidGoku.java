package fighter;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class KidGoku extends Character {
	BufferedImage fsmashblast = new Image("img/misc/fsmashkidgoku.png").img;
	BufferedImage downspecialki = new Image("img/misc/downspecialki.png").img;

	public KidGoku(int posx, int posy, int upKey, int downKey, int leftKey, int rightKey, int modifierKey, int jumpKey,
			int attackKey, int specialKey, int shieldKey, int grabKey, Game gameinstance) {
		super(posx, posy, upKey, downKey, leftKey, rightKey, modifierKey, jumpKey, attackKey, specialKey, shieldKey,
				grabKey, gameinstance);
		setName("kidgoku");

		setW((int) (getW() * 1.0133333333));
		setH((int) (getW() * 1.5263157895));
		setW(getW() * 2);
		setH(getH() * 2);

	}

	public KidGoku(int posx, int posy, String nameOfController, String axisName, String axisName2, String axisName3,
			String axisName4, String leftTrigger, String rightTrigger, double axisMidpoint, double deadZone,
			String jumpButton, String attackButton, String specialButton, String grabButton, Game gameinstance) {
		super(posx, posy, nameOfController, axisName, axisName2, axisName3, axisName4, leftTrigger, rightTrigger,
				axisMidpoint, deadZone, jumpButton, attackButton, specialButton, grabButton, gameinstance);
		setName("kidgoku");

		setW((int) (getW() * 1.0133333333));
		setH((int) (getW() * 1.5263157895));
		setW(getW() * 2);
		setH(getH() * 2);

	}

	@Override
	public void drawSpecialSprites(Graphics g) {
		if (state == STATE_SMASH_ATTACK_FORWARD && hitboxes.get(0).isActive) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(fsmashblast, (int) x + w - 10, (int) y, 50, h, null);
			if (direction == DIRECTION_LEFT)
				g.drawImage(fsmashblast, (int) x + 10, (int) y, -50, h, null);

		}
		if (hitboxes.size() > 0)
			if (state == STATE_NEUTRALSPECIAL && hitboxes.get(0).isActive) {
				if (direction == DIRECTION_RIGHT)
					g.drawImage(chargeBeamBlueImage, (int) x + w + 8, (int) y, 1000, h, null);
				else
					g.drawImage(chargeBeamBlueImage, (int) x - 1000 - 8, (int) y, 1000, h, null);
			}
		
		if (state == STATE_DOWNSPECIAL && hitboxes.get(0).isActive) {
			if (direction == DIRECTION_RIGHT)
				g.drawImage(downspecialki, (int) x - 15, (int) y - 10, 30 + w, h + 10, null);
			else
				g.drawImage(downspecialki, (int) x - 15, (int) y- 10, 30 + w, h + 10, null);
		}
		if(state == STATE_HELPLESS){
			imageYTransform = 1;
			imageXTransform = 1;
		}
	}

	@Override
	public void jab() {
		imageXTransform = 1.32;
		hitboxes.add(new AttackHitbox(this, w - 5, 25, 25, 25, .5, -1, 10, 5, 3, 3, .1));
		state = STATE_ATTACK;
		velX += direction * 2;
	}

	@Override
	public void fTilt() {
		imageXTransform = 1.348;
		imageYTransform = 1.0263157895;

		hitboxes.add(new AttackHitbox(this, w, 25, 25, 25, .5, -1, 10, 5, 3, 3, .1));

		state = STATE_ATTACKSIDE;
		velX += direction * 5;
	}

	@Override
	public void uTilt() {
		imageXTransform = 1;
		hitboxes.add(new AttackHitbox(this, w - 10, 5, 20, 25, .5, -10, 10, 5, 3, 12, .1));

		state = STATE_ATTACKUP;
	}

	@Override
	public void dTilt() {
		imageXTransform = 1.296;

		hitboxes.add(new AttackHitbox(this, w - 10, 45, 25, 15, .5, -1, 10, 5, 3, 3, .1));

		state = STATE_ATTACKDOWN;
		velX += direction * 5;
	}

	@Override
	public void nair() {
		imageXTransform = 1.276;

		hitboxes.add(new AttackHitbox(this, w - 10, 35, 25, 15, .5, -1, 10, 60, 3, 6, .1));

		state = STATE_ATTACK_NAIR;
	}

	@Override
	public void fair() {
		imageXTransform = 1.276;

		hitboxes.add(new AttackHitbox(this, w - 10, 35, 25, 15, .5, -1, 10, 30, 3, 6, .1));
		hitboxes.add(new AttackHitbox(this, w - 10, 45, 25, 15, .5, -1, 10, 30, 3, 6, .1));
		state = STATE_ATTACK_FAIR;
		velX += direction * 10;
	}

	@Override
	public void bair() {
		imageXTransform = 1.196;

		hitboxes.add(new AttackHitbox(this, -20, 35, 25, 15, .5, -1, 10, 60, 3, 6, .1));
		state = STATE_ATTACK_BAIR;
	}

	@Override
	public void uair() {
		imageXTransform = 1;
		hitboxes.add(new AttackHitbox(this, -5, 0, w + 10, 15, 0, -5, 5, 20, 20, 10, .1));
		state = STATE_ATTACK_UAIR;
	}

	@Override
	public void dair() {
		super.dair();
		imageXTransform = 1;
	}

	@Override
	public void forwardSmash() {
		imageXTransform = 1.2;
		hitboxes.add(new AttackHitbox(this, w, 0, 30, h, 10 * smashAttackChargePercent, -5, 10, 50, 50, 20, .1));
		state = STATE_SMASH_ATTACK_FORWARD;
	}

	@Override
	public void downSmash() {

		hitboxes.add(new AttackHitbox(this, w / 2, h - 15, w, 15, 1 * smashAttackChargePercent,
				8 * smashAttackChargePercent, 10, 10, 2 * smashAttackChargePercent, 20, .6));

		state = STATE_SMASH_ATTACK_DOWN;
		smashAttackChargePercent = 1.0;
	}

	@Override
	public void upSmash() {
		hitboxes.add(new AttackHitbox(this, 0, 0, w, 30, 1 * smashAttackChargePercent, -10 * smashAttackChargePercent,
				30, 10, 15 * smashAttackChargePercent, 20, .5));
		state = STATE_SMASH_ATTACK_UP;
		smashAttackChargePercent = 1.0;
	}

	@Override
	public void placeGrabBox() {

		if (state == STATE_GRAB && grabbedPlayer == null) {
			if (direction == DIRECTION_RIGHT)
				grabBox = new Rectangle((int) x + w - 10, (int) y + 10, 20, 30);
			else
				grabBox = new Rectangle((int) x - 10, (int) y + 10, 20, 30);
			imageXTransform = 1.172;
		} else {
			grabBox = null;
		}
	}

	@Override
	public void forceGrabPlayerLocation() {
		if (direction == DIRECTION_RIGHT) {
			grabbedPlayer.x = x + w - 5;
			grabbedPlayer.y = y - 5;
			grabbedPlayer.direction = DIRECTION_LEFT;
		} else {
			grabbedPlayer.x = x - grabbedPlayer.getW() + 5;
			grabbedPlayer.y = y - 5;
			grabbedPlayer.direction = DIRECTION_RIGHT;
		}
	}

	@Override
	public void fThrow() {
		imageXTransform = 1.088;
		grabbedPlayer.velX = (2 + grabbedPlayer.percent / 8) * direction;
		grabbedPlayer.velY = -8 + percent / 10;
		grabbedPlayer.applyHitstun(5);
		grabbedPlayer.applyDamage(7);
	}

	@Override
	public void bThrow() {
		imageXTransform = 1.088;
		grabbedPlayer.velX = (5 + grabbedPlayer.percent / 10) * -direction;
		grabbedPlayer.velY = -5 + percent / 10;
		grabbedPlayer.applyDamage(3);
	}

	@Override
	public void uThrow() {
		imageXTransform = 1.088;
		grabbedPlayer.velY = (-8 - (grabbedPlayer.percent / 20)) - 5;
		grabbedPlayer.applyDamage(5);
	}

	@Override
	public void dThrow() {
		grabbedPlayer.velY = 2 + percent / 20;
		grabbedPlayer.applyHitstun((int) (10 + grabbedPlayer.percent / 5));
		grabbedPlayer.applyDamage(2);
	}

	@Override
	public void forwardSpecial() {
		imageXTransform = 1.104;
		if (direction == DIRECTION_RIGHT)
			myGame.projectiles.add(new Projectile((int) x + w + 5, (int) (y + h / 3) + 7, 15, (int) (15 / 1.5638138138),
					15.0, 0.0, kiBlastBlueImage, this));
		else
			myGame.projectiles.add(new Projectile((int) x - 5 - 15, (int) (y + h / 3) + 7, 15,
					(int) (15 / 1.5638138138), -15.0, 0.0, kiBlastBlueImage, this));
	}

	@Override
	public void downSpecial() {
		imageXTransform = 1;
		hitboxes.add(new AttackHitbox(this, -10, -10, w + 20, 40, 0, 10, 5, 20, 20, 10, .8));
	}
	@Override
	public void upSpecial(){
		super.upSpecial();
		imageXTransform = 1;
		imageYTransform = 1;
	}
}
