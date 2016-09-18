package fighter;

public class AttackHitbox extends Hitbox {
	private double localX;
	private double localY;
	public int lifetime = 15;
	public boolean isActive = true;

	public AttackHitbox(Character linkedPlayer, double width, double height, double localposx,
			double localposy, int lifeTime) {
		super(linkedPlayer.getX()+localposx, linkedPlayer.getVelY()+localposy, width, height, Game.TYPE_ATTACK);
		localX = localposx;
		localY = localposy;
		lifetime = lifeTime;		
	}

	public double getlocalX() {
		return localX;
	}

	public double getlocalY() {
		return localY;
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
