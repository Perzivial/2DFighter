package fighter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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
	double rotateamount;
	int lifetime = 600;
	int damage = 5;
	public Projectile(int posx, int posy, int mywidth, int myheight, double velx, double vely, BufferedImage image,
			Character chara) {
		x = posx;
		y = posy;
		w = mywidth;
		h = myheight;
		velX = velx;
		velY = vely;
		img = image;
		myHitbox = new AttackHitbox(chara, 0, 0, w, h, 0, 0, 5, 60, 5, 0, .05);
	}
	public Projectile(int posx, int posy, int mywidth, int myheight, double velx, double vely, BufferedImage image,
			Character chara, int damage) {
		x = posx;
		y = posy;
		w = mywidth;
		h = myheight;
		velX = velx;
		velY = vely;
		img = image;
		myHitbox = new AttackHitbox(chara, 0, 0, w, h, 0, 0, 5, 60, damage, 0, .05);
	}
	public Projectile(int posx, int posy, int mywidth, int myheight, double velx, double vely, BufferedImage image,
			Character chara, int damage, int knockbackx, int knockbacky) {
		x = posx;
		y = posy;
		w = mywidth;
		h = myheight;
		velX = velx;
		velY = vely;
		img = image;
		myHitbox = new AttackHitbox(chara, 0, 0, w, h, knockbackx, knockbacky, 5, 60, damage, 0, .05);
	}
	public void draw(Graphics g) {
		lifetime --;
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform old = g2.getTransform();

		// Rotation information
		
		if (rotateamount > 4.0){
			rotateamount = 0;
		}else
		rotateamount += 0.25;
		double rotationRequired = 90;
			System.out.println(rotateamount);
		if (rotateamount < 1)
			rotationRequired = Math.toRadians(90);
		else if (rotateamount < 2)
			rotationRequired = Math.toRadians(180);
		else if (rotateamount < 3)
			rotationRequired = Math.toRadians(270);
		else
			rotationRequired = Math.toRadians(360);
		
		double locationX = img.getWidth() / 2;
		double locationY = img.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		// Drawing the rotated image at the required drawing locations
		g2.drawImage(op.filter(img, null), x, y, (int)(w*1.3), (int)(h*1.3), null);

		myHitbox.updateLocation(x, y, w, h);
		x += velX;
		y += velY;
		// g.drawImage(img, x, y, w, h, null);

		g2.setTransform(old);
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
