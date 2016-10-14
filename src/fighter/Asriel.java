package fighter;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Asriel extends Character {

	public BufferedImage starimg = new Image("img/asriel/star.png").img;
	public BufferedImage sparkimg = new Image("img/asriel/spark.png").img;
	public BufferedImage spark2img = new Image("img/asriel/spark2.png").img;
	public BufferedImage spark3img = new Image("img/asriel/spark3.png").img;
	public BufferedImage spark4img = new Image("img/asriel/spark4.png").img;
	public BufferedImage trail1img = new Image("img/asriel/asrieltrail1.png").img;
	public BufferedImage trail2img = new Image("img/asriel/asrieltrail2.png").img;
	public BufferedImage trail3img = new Image("img/asriel/asrieltrail3.png").img;

	public Asriel(int posx, int posy, int upKey, int downKey, int leftKey, int rightKey, int modifierKey, int jumpKey,
			int attackKey, int specialKey, int shieldKey, int grabKey, Game gameinstance) {
		super(posx, posy, upKey, downKey, leftKey, rightKey, modifierKey, jumpKey, attackKey, specialKey, shieldKey,
				grabKey, gameinstance);
		setName("asriel");
		setW((int) (getW() * 1.324));
		setH((int) (getH() * 1.0757314974));
		setW(getW() * 2);
		setH(getH() * 2);

		jumpHeight = 15;
		lowJumpHeight = 7;
		bairOffset = 60;
		neutralSpecialChargeIncrement = 0;
	}

	public Asriel(int posx, int posy, String nameOfController, String axisName, String axisName2, String axisName3,
			String axisName4, String leftTrigger, String rightTrigger, double axisMidpoint, double deadZone,
			String jumpButton, String attackButton, String specialButton, String grabButton, Game gameinstance) {
		super(posx, posy, nameOfController, axisName, axisName2, axisName3, axisName4, leftTrigger, rightTrigger,
				axisMidpoint, deadZone, jumpButton, attackButton, specialButton, grabButton, gameinstance);
		setName("asriel");
		setW((int) (getW() * 1.324));
		setH((int) (getH() * 1.0757314974));
		setW(getW() * 2);
		setH(getH() * 2);
		jumpHeight = 15;
		lowJumpHeight = 7;
		bairOffset = 60;
		neutralSpecialChargeIncrement = 0;
	}

	@Override
	public void drawSpecialSprites(Graphics g) {
		if (state == STATE_FORWARDSPECIAL) {
			if (state == STATE_FORWARDSPECIAL) {
				if (direction == DIRECTION_RIGHT) {
					g.drawImage(trail1img, (int) x - 10, (int) y, (int) (w * imageXTransform), h, null);
					g.drawImage(trail2img, (int) x - 20, (int) y, (int) (w * imageXTransform), h, null);
					g.drawImage(trail3img, (int) x - 30, (int) y, (int) (w * imageXTransform), h, null);
				}
				if (direction == DIRECTION_LEFT) {
					g.drawImage(trail1img, (int) x + w + 10, (int) y, (int) (-w * imageXTransform), h, null);
					g.drawImage(trail2img, (int) x + w + 20, (int) y, (int) (-w * imageXTransform), h, null);
					g.drawImage(trail3img, (int) x + w + 30, (int) y, (int) (-w * imageXTransform), h, null);
				}
			}

		} else {
			shouldSlide = false;
		}

		if (state == STATE_NEUTRALSPECIAL) {
			bringPlayersCloser(200);
			if (isAxisDown || isPressing(getDownKey()))
				velY = 0;
			else
				velY -= .5;
		}
	}

	public void bringPlayersCloser(int range) {
		for (Character person : myGame.characters) {
			if (person != this) {
				int distanceToX = (int) Math.abs(x - person.x);
				int distanceToY = (int) Math.abs(y - person.y);
				if (distanceToX < range && distanceToY < range) {
					if (person.x < x)
						person.velX++;
					else
						person.velX--;
					if (person.y < y)
						person.velY++;
					else{
						person.velY-=fallSpeed;
						person.velY--;
					}
				}
			}
		}
	}

	@Override
	public void jab() {
		imageXTransform = 1.2205438066;
		hitboxes.add(new AttackHitbox(this, w - 20, 25, 25, 25, 2, -1, 10, 5, 3, 5, .1));

		myGame.projectiles.add(
				new Projectile((int) x + w / 2, (int) (y + h / 4), 20, (int) (20), 5 * direction, 0.0, starimg, this));

		state = STATE_ATTACK;
		velX += direction * 2;
	}

	@Override
	public void dTilt() {
		imageXTransform = 1.2084592145;
		hitboxes.add(new AttackHitbox(this, w - 20, h - 25, 60, 25, 2, -1, 10, 5, 3, 5, .1));
		state = STATE_ATTACKDOWN;

	}

	@Override
	public void uTilt() {
		imageXTransform = 1;
		imageYTransform = 1.2048192771;
		hitboxes.add(new AttackHitbox(this, w - 25, 5, 25, 25, 2, -1, 10, 5, 3, 5, .1));
		state = STATE_ATTACKUP;

		if (direction == DIRECTION_RIGHT)
			myGame.projectiles.add(new Projectile((int) x + (5 + w / 2), (int) (y + h / 4) - 20, 20, (int) (20), 0, -5,
					starimg, this));
		else
			myGame.projectiles.add(new Projectile((int) x + (5) * direction, (int) (y + h / 4) - 20, 20, (int) (20), 0,

			-5, starimg, this));
	}

	@Override
	public void fTilt() {
		imageXTransform = 1.836858006;
		imageYTransform = 1.1445783133;
		hitboxes.add(new AttackHitbox(this, w, 0, 20, h, 2, -1, 10, 10, 3, 10, .01));
		hitboxes.add(new AttackHitbox(this, w + 15, 0, 20, h, 2, -1, 10, 10, 6, 10, .01));
		state = STATE_ATTACKSIDE;
	}

	@Override
	public void fair() {
		imageXTransform = 1.836858006;
		imageYTransform = 1.1445783133;
		hitboxes.add(new AttackHitbox(this, w, 0, 20, h, 2, -1, 10, 10, 3, 10, .01));
		hitboxes.add(new AttackHitbox(this, w + 25, 0, 20, h, 2, -1, 10, 10, 6, 10, .01));
		state = STATE_ATTACK_FAIR;
	}

	@Override
	public void bair() {
		imageXTransform = 1.836858006;
		imageYTransform = 1.1445783133;
		hitboxes.add(new AttackHitbox(this, -30 + bairOffset, 0, 20, h, -2, -1, 10, 10, 3, 20, .01));
		hitboxes.add(new AttackHitbox(this, -55 + bairOffset, 0, 20, h, -2, -1, 10, 10, 6, 20, .01));
		state = STATE_ATTACK_BAIR;
	}

	@Override
	public void dair() {
		super.dair();
		velY = 10;
	}

	@Override
	public void uair() {
		imageXTransform = 1;
		hitboxes.add(new AttackHitbox(this, -5, -10, w + 10, 15, 0, -5, 5, 20, 20, 10, .1));
		if (direction == DIRECTION_RIGHT)
			myGame.projectiles.add(new Projectile((int) x + w / 2, (int) y - 20, 50, 50, 0, -2, starimg, this, 20));
		else
			myGame.projectiles
					.add(new Projectile((int) x - w / 2 - 10, (int) y - 20, 50, 50, 0, -2, starimg, this, 20));

		state = STATE_ATTACK_UAIR;
	}

	@Override
	public void nair() {
		imageXTransform = 1.1299093656;

		hitboxes.add(new AttackHitbox(this, w - 20, 20, 25, 25, 0, -5, 5, 20, 20, 10, .1));
		hitboxes.add(new AttackHitbox(this, 0, 20, 25, 25, 0, -5, 5, 20, 20, 10, .1));

		for (int i = 0; i < 2; i++) {
			myGame.projectiles.add(new Projectile((int) (x + w / 2) + Game.help.randInt(-20, 20),
					(int) y + 20 + Game.help.randInt(-20, 20), 10, 10, Game.help.randInt(-20, 20),
					Game.help.randInt(-20, 20), sparkimg, this));

			myGame.projectiles.add(new Projectile((int) (x + w / 2) + Game.help.randInt(-20, 20),
					(int) y + 20 + Game.help.randInt(-20, 20), 10, 10, Game.help.randInt(-20, 20),
					Game.help.randInt(-20, 20), spark2img, this));

			myGame.projectiles.add(new Projectile((int) (x + w / 2) + Game.help.randInt(-20, 20),
					(int) y + 20 + Game.help.randInt(-20, 20), 10, 10, Game.help.randInt(-20, 20),
					Game.help.randInt(-20, 20), spark3img, this));

			myGame.projectiles.add(new Projectile((int) (x + w / 2) + Game.help.randInt(-20, 20),
					(int) y + 20 + Game.help.randInt(-20, 20), 10, 10, Game.help.randInt(-20, 20),
					Game.help.randInt(-20, 20), spark4img, this));
		}

		state = STATE_ATTACK_NAIR;
	}

	@Override
	public void downSmash() {
		imageXTransform = 1;
		hitboxes.add(new AttackHitbox(this, -w / 2 + 50, h - 15, w * .5, 15, -1 * smashAttackChargePercent,
				-10 * smashAttackChargePercent, 10, 10, 2 * smashAttackChargePercent, 20, .6));

		hitboxes.add(new AttackHitbox(this, w / 2, h - 15, w * .5, 15, 1 * smashAttackChargePercent,
				-10 * smashAttackChargePercent, 10, 10, 2 * smashAttackChargePercent, 20, .6));

		state = STATE_SMASH_ATTACK_DOWN;
		smashAttackChargePercent = 1.0;

	}

	@Override
	public void forwardSmash() {
		imageXTransform = 1;
		if (hitboxes.size() == 0) {
			if (direction == DIRECTION_RIGHT)
				myGame.projectiles.add(new Projectile((int) x + 30, (int) y + 10, 50, 50, 2, 0, starimg, this, 10,
						(int) (2 * smashAttackChargePercent), 0));
			else
				myGame.projectiles.add(new Projectile((int) x - 30, (int) y + 10, 50, 50, -2, 0, starimg, this, 10,
						(int) (-2 * smashAttackChargePercent), 0));
		}
		super.forwardSmash();
		imageXTransform = 1;
	}

	@Override
	public void upSmash() {
		imageXTransform = 1;
		hitboxes.add(new AttackHitbox(this, 0, 0, w, h, 1 * smashAttackChargePercent, -10 * smashAttackChargePercent,
				30, 10, 15 * smashAttackChargePercent, 20, .5));
		state = STATE_SMASH_ATTACK_UP;
		smashAttackChargePercent = 1.0;
		velY = -20;
	}

	@Override
	public void forwardSpecial() {
		imageXTransform = 1;
		if (hitboxes.size() == 0) {
			hitboxes.add(new AttackHitbox(this, 0, 0, w, h, 1, 10, 30, 10, 15, 20, .5));
			state = STATE_FORWARDSPECIAL;
			velX = 30 * direction;

			shouldSlide = true;
		}
	}

	@Override
	public void upSpecial() {
		imageXTransform = 1;
		if (hitboxes.size() == 0) {
			velY = -20;
			hitboxes.add(new AttackHitbox(this, 0, 0, w, h, 1 * smashAttackChargePercent,
					-10 * smashAttackChargePercent, 30, 10, 15 * smashAttackChargePercent, 1, .5));
			state = STATE_UPSPECIAL;
		}
	}

	@Override
	public void downSpecial() {
		if(hitboxes.size() == 0){
		super.downSpecial();
		imageXTransform = 1;
		hitboxes.add(new AttackHitbox(this, -10, -10, w + 20, h + 20, 1, 1, 5, 20, 20, 30, .8));
		hitboxes.add(new AttackHitbox(this, -10, -10, w + 20, 20, 0, 1, 1, 20, 20, 30, .8));
		}
	}

	@Override
	public void placeGrabBox() {

		if (state == STATE_GRAB && grabbedPlayer == null) {
			if (direction == DIRECTION_RIGHT)
				grabBox = new Rectangle((int) x + w - 20, (int) y + 5, 40, h - 5);
			else
				grabBox = new Rectangle((int) (x - 20), (int) y + 5, 40, h - 5);
			imageXTransform = 1.0906344411;
		} else {
			grabBox = null;
		}
	}

	@Override
	public void uThrow() {
		imageXTransform = 1;
		imageYTransform = 1;
		grabbedPlayer.velY = (-5 + (grabbedPlayer.percent / 15)) + 10;
		grabbedPlayer.applyDamage(3);
	}

	@Override
	public void fThrow() {
		imageXTransform = 1.1299093656;
		grabbedPlayer.velX = (5 + grabbedPlayer.percent / 8) * direction;
		grabbedPlayer.velY = -8 + percent / 10;
		grabbedPlayer.applyDamage(4);
	}

	@Override
	public void bThrow() {
		imageXTransform = 1.2114803625;
		grabbedPlayer.velX = (5 + grabbedPlayer.percent / 10) * -direction;
		grabbedPlayer.velY = -10 + percent / 10;
		grabbedPlayer.applyDamage(2);
	}

	@Override
	public void dThrow() {
		imageXTransform = 1.2114803625;
		grabbedPlayer.velX = (5) * -direction;
		grabbedPlayer.velY = 2 + percent / 20;
		grabbedPlayer.applyDamage(1);
	}

}
