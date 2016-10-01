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
	public void nair() {
		hitboxes.add(new AttackHitbox(this, -5, -10, w + 10, 15, 0, -5, 5, 20, 20, 10, .3));
	}
}
