package fighter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Projectile {
	int x;
	int y;
	int w;
	int h;
	double velX;
	double velY;
	BufferedImage img;
	AttackHitbox myHitbox;

	public Projectile(int posx, int posy, int mywidth, int myheight, double velx, double vely, BufferedImage image, Character chara) {
		x = posx;
		y = posy;
		w = mywidth;
		h = myheight;
		velX = velx;
		velY = vely;
		img = image;
		myHitbox = new AttackHitbox(chara, 0, 0, w, h, 0, 0, 5, 60, 5, 0, .05);
	}

	public void draw(Graphics g) {
		myHitbox.updateLocation(x, y, w, h);
		x += velX;
		y += velY;
		g.drawImage(img, x, y, w, h, null);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public double getVelX() {
		return velX;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public double getVelY() {
		return velY;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public AttackHitbox getMyHitbox() {
		return myHitbox;
	}

	public void setMyHitbox(AttackHitbox myHitbox) {
		this.myHitbox = myHitbox;
	}

}
