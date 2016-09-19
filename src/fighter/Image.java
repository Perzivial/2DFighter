package fighter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Image{
	BufferedImage img;
	String path;

	public Image(String url) {
		img = loadImage(url);
		path = url;
	}

	public BufferedImage loadImage(String fileName) {
		BufferedImage buff = null;
		try {
			buff = ImageIO.read(getClass().getResourceAsStream(fileName));
		} catch (Exception e) {
			try {
				buff = ImageIO.read(new File(String.valueOf("src/fighter" + fileName)));

			} catch (Exception e2) {
				e.printStackTrace();
				return null;
			}
		}
		return buff;
	}

}
