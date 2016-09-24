package fighter;

public class AttackHitbox extends Hitbox {
	private double localX;
	private double localY;
	public int lifetime = 15;
	public boolean isActive = true;
	private double knockbackX;
	private double knockbackY;
	private int hitstunLength;
	private double damage;
	private double startupTime;

	public AttackHitbox(Character boundPlayer, double localposx, double localposy, double width, double height,
			double xknockback, double yknockback, int hitstunLen, int lifeTime, double myDamage, double myStartupTime) {
		super(boundPlayer, boundPlayer.getX() + localposx, boundPlayer.getVelY() + localposy, width, height,
				Game.TYPE_ATTACK);
		localX = localposx;
		localY = localposy;
		knockbackX = xknockback;
		knockbackY = yknockback;
		hitstunLength = hitstunLen;
		lifetime = lifeTime;
		damage = myDamage;
		startupTime = myStartupTime;
	}

	public double getlocalX() {
		return localX;
	}

	public double getDamage() {
		return damage;
	}

	public double getlocalY() {
		return localY;
	}

	public double getKnockbackX() {
		return knockbackX;
	}

	public double getKnockbackY() {
		return knockbackY;
	}

	public int getHitstunLength() {
		return hitstunLength;
	}
	public int getLifeTime(){
		return lifetime;
	}
	@Override
	public void updateLocation(double posx, double posy, double width, double height) {
		super.updateLocation(posx, posy, width, height);

		if (lifetime > 0 && startupTime == 0)
			lifetime--;
		else
			isActive = false;
		if (startupTime > 0)
			startupTime--;
		else if (lifetime > 0)
			isActive = true;

	}

}
