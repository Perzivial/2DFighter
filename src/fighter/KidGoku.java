package fighter;

public class KidGoku extends Character {

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
	public void jab() {
		imageXTransform = 1.32;
		hitboxes.add(new AttackHitbox(this, w -5, 25, 25, 25, .5, -1, 10, 5, 3, 3, .1));
		state = STATE_ATTACK;
		velX += direction * 2;
	}
	@Override
	public void fTilt(){
		imageXTransform = 1.348;
		imageYTransform = 1.0263157895;
		hitboxes.add(new AttackHitbox(this, w -5, 25, 25, 25, .5, -1, 10, 5, 3, 3, .1));
		state = STATE_ATTACKSIDE;
		velX += direction * 2;
	}
}
