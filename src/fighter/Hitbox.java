package fighter;

import java.awt.*;

public class Hitbox {

	private int x;
	private int y;
	private int w;
	private int h;
	private int type;
	private Rectangle rect;
	public Hitbox(int posx, int posy, int width, int height, int typeOfHitbox) {
		x = posx;
		y = posy;
		w = width;
		h = height;
		type = typeOfHitbox;
		rect = new Rectangle(x,y,w,h);
	}
	//stock return methods. nothing special here
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getWidth(){
		return w;
	}
	public int getHeight(){
		return h;
	}
	public int getType(){
		return type;
	}
	public Rectangle getRect(){
		return rect;
	}
	public void updateLocation(int posx,int posy, int width, int height){
		x = posx;
		y = posy;
		w = width;
		h = height;
		rect = new Rectangle(x,y,w,h);
	}
	
}
