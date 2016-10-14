package fighter;

import java.awt.Color;
import java.util.Random;

public class Helper {

	public Helper() {
		
	}
	
	public int randInt(int min, int max) {
		Random random = new Random();
		int randomNum = random.nextInt(max - min + 1) + min;
		return randomNum;
	}
	
	public Color randomrainbowcolor(float lightness) {

		Random rand = new Random();
		/*
		 * 
		 * float r = rand.nextFloat() / 2f + 0.5f; float g = rand.nextFloat() /
		 * 2f + 0.5f; float b = rand.nextFloat() / 2f + 0.5f; Color randomColor
		 * = new Color(r, g, b);
		 */
		final float hue = rand.nextFloat();
		// Saturation between 0.1 and 0.3
		final float saturation = (rand.nextInt(2000) + 1000) / 10000f;
		final Color randomColor = Color.getHSBColor(hue, saturation, lightness);
		return randomColor;
	}
	
}
