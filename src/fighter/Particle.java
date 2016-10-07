package fighter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;

public class Particle {
	private int x;
	private int y;
	private int velX;
	private int velY;
	private Color color;
	Polygon myShape = new Polygon();
	private int lifetime = 30;

	public Particle(int posx, int posy, int velocityx, int velocityy, Color colour) {
		x = posx;
		y = posy;
		this.velX = velocityx;
		this.velY = velocityy;
		color = colour;
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		updateShapeLocation();
		g2.setColor(color);
		g2.fill(myShape);
		lifetime --;
	}

	public void setShapeToPreset(String preset) {
		switch (preset) {
		case ("squaresmall"):
			myShape.addPoint(x, y);
			myShape.addPoint(x + 5, y);
			myShape.addPoint(x + 5, y + 5);
			myShape.addPoint(x, y + 5);
			break;
		}
	}

	public void updateShapeLocation() {
		myShape.translate(velX, velY);
	}

	public int getLifetime() {
		return lifetime;
	}

	public void setLifetime(int lifetime) {
		this.lifetime = lifetime;
	}
}
