package fighter;

public class AttackHitbox extends Hitbox {
	private double localX;
	private double localY;
	public int lifetime = 15;
	public boolean isActive = true;
	private double knockbackX;
	private double knockbackY;

	public AttackHitbox(Character boundPlayer, double localposx, double localposy, double width, double height,
			double xknockback, double yknockback, int lifeTime) {
		super(boundPlayer, boundPlayer.getX() + localposx, boundPlayer.getVelY() + localposy, width, height,
				Game.TYPE_ATTACK);
		localX = localposx;
		localY = localposy;
		knockbackX = xknockback;
		knockbackY = yknockback;
		lifetime = lifeTime;
	}

	public double getlocalX() {
		return localX;
	}

	public double getlocalY() {
		return localY;
	}

	public double getknockbackX() {
		return knockbackX;
	}

	public double getKnockbackY() {
		return knockbackY;
	}

	@Override
	public void updateLocation(double posx, double posy, double width, double height) {
		super.updateLocation(posx, posy, width, height);
		if (lifetime > 0)
			lifetime--;
		else
			isActive = false;

	}

}
