package fighter;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class CharacterIcon {
	Character keyBoard;
	Character controller;
	Rectangle interactRect;
	int x;
	int y;
	BufferedImage img;

	public CharacterIcon(String url, int posx, int posy, Character keyBoardCharacter, Character ControllerCharacter) {
		keyBoard = keyBoardCharacter;
		controller = ControllerCharacter;
		x = posx;
		y = posy;
		img = new Image(url).img;
	}

	public void draw(Graphics g) {
		g.drawImage(img, x, y, 100, 100, null);
	}
}
