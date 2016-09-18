package fighter;

import java.awt.*;

public class Hitbox {

	private double x;
	private double y;
	private double w;
	private double h;
	private int type;
	private Rectangle rect;
	public Hitbox(double posx, double posy, double width, double height, int typeOfHitbox) {
		x = posx;
		y = posy;
		w = width;
		h = height;
		type = typeOfHitbox;
		rect = new Rectangle((int)x,(int)y,(int)w,(int)h);
	}
	//stock return methods. nothing special here
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public double getWidth(){
		return w;
	}
	public double getHeight(){
		return h;
	}
	public double getType(){
		return type;
	}
	public Rectangle getRect(){
		return rect;
	}
	public void updateLocation(double posx,double posy, double width, double height){
		x = posx;
		y = posy;
		w = width;
		h = height;
		rect = new Rectangle((int)x,(int)y,(int)w,(int)h);
	}
	
}
