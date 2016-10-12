package fighter;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CharacterIcon {
	Character keyBoard;
	Character controller;
	Rectangle interactRect;
	BufferedImage img;
	boolean isChoosingKeyBoard = true;
	boolean isAiController = false;
	ArrayList<Character> characters;
	Game mygame;
	int x;
	int y;

	public CharacterIcon(int row, ArrayList<Character> chars, Game gameinstance) {
		// interactRect = new Rectangle(x,y,100,100);
		switch (row) {
		case (0):
			y = 100;
			break;
		case (1):
			y = 150;
			break;
		}
		characters = chars;
		mygame = gameinstance;
	}

	public void draw(Graphics g) {
		g.drawImage(img, x, y, 100, 100, null);
	}

	public void addCharacter() {

	}
}
