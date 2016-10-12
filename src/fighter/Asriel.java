package fighter;

import java.awt.image.BufferedImage;

public class Asriel extends Character {

	public BufferedImage starimg = new Image("img/asriel/star.png").img;

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
		hitboxes.add(new AttackHitbox(this, -30 + bairOffset, 0, 20, h, 2, -1, 10, 10, 3, 20, .01));
		hitboxes.add(new AttackHitbox(this, -55 + bairOffset, 0, 20, h, 2, -1, 10, 10, 6, 20, .01));
		state = STATE_ATTACK_BAIR;
	}

	@Override
	public void dair() {
		super.dair();
		velY = 10;
	}

	@Override
	public void uair() {

		hitboxes.add(new AttackHitbox(this, -5, -10, w + 10, 15, 0, -5, 5, 20, 20, 10, .1));
		if(direction == DIRECTION_RIGHT)
		myGame.projectiles.add(new Projectile((int) x + w/2, (int) y - 20, 50, 50, 0,
				-2, starimg, this));
		else
			myGame.projectiles.add(new Projectile((int) x - w/2 - 10, (int) y - 20, 50, 50, 0,
					-2, starimg, this));
		
		
		state = STATE_ATTACK_UAIR;
	}
	@Override
	public void nair(){
		imageXTransform = 1.1299093656;
		
		hitboxes.add(new AttackHitbox(this, w - 20, 20, 25, 25, 0, -5, 5, 20, 20, 10, .1));
		hitboxes.add(new AttackHitbox(this, 0, 20, 25, 25, 0, -5, 5, 20, 20, 10, .1));
		state = STATE_ATTACK_NAIR;
	}
}
