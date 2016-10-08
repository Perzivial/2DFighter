package fighter;

public class Asriel extends Character {

	public Asriel(int posx, int posy, int upKey, int downKey, int leftKey, int rightKey, int modifierKey, int jumpKey,
			int attackKey, int specialKey, int shieldKey, int grabKey, Game gameinstance) {
		super(posx, posy, upKey, downKey, leftKey, rightKey, modifierKey, jumpKey, attackKey, specialKey, shieldKey,
				grabKey, gameinstance);
		setName("asriel");
		setW((int)(getW() * 1.324));
		setH((int)(getH() * 1.0757314974));
		setW(getW() * 2);
		setH(getH() * 2);
	}

	public Asriel(int posx, int posy, String nameOfController, String axisName, String axisName2, String axisName3,
			String axisName4, String leftTrigger, String rightTrigger, double axisMidpoint, double deadZone,
			String jumpButton, String attackButton, String specialButton, String grabButton, Game gameinstance) {
		super(posx, posy, nameOfController, axisName, axisName2, axisName3, axisName4, leftTrigger, rightTrigger,
				axisMidpoint, deadZone, jumpButton, attackButton, specialButton, grabButton, gameinstance);
		setName("asriel");
		setW((int)(getW() * 1.324));
		setH((int)(getH() * 1.0757314974));
		setW(getW() * 2);
		setH(getH() * 2);
	}
	
	@Override
	public void jab(){
		imageXTransform = 1.2205438066;
		hitboxes.add(new AttackHitbox(this, w - 5, 25, 25, 25, 2, -1, 10, 5, 3, 20, .1));
		
		myGame.projectiles.add(new Projectile((int) x + w + 5, (int) (y + h / 3) + 7, 15, (int) (15 / 1.5638138138),
				15.0, 0.0, kiBlastBlueImage, this));
		
		state = STATE_ATTACK;
		velX += direction * 2;
	}
}
