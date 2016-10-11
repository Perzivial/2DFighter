package fighter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Cloud {
	BufferedImage img = new Image("img/misc/cloud.png").img;
	int x;
	int y;

	public Cloud(int posx, int posy) {
		x = posx;
		y = posy;

	}

	public void draw(Graphics g) {
		x += 1;

		g.drawImage(img, x, y, 100, (int) (100 / 1.4308943089), null);

	}
}
