package fighter;

public class AttackHitbox extends Hitbox {
	private int localx;
	private int localy;
	public AttackHitbox(int posx, int posy, int width, int height, int typeOfHitbox, int localposx, int localposy) {
		super(posx, posy, width, height, typeOfHitbox);
		localx = localposx;
		localy = localposy;
		// TODO Auto-generated constructor stub
	}
	public int getlocalX(){
		return localx;
	}
	public int getlocalY(){
		return localx;
	}
}
