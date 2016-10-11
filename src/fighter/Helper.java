package fighter;

import java.util.Random;

public class Helper {

	public Helper() {
		
	}
	
	public int randInt(int min, int max) {
		Random random = new Random();
		int randomNum = random.nextInt(max - min + 1) + min;
		return randomNum;
	}
	
}
