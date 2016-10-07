package fighter;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class CharacterIcon {
	Character keyBoard;
	Character controller;
	Rectangle interactRect;
	int x;
	int y;
	BufferedImage img;
	boolean isChoosingKeyBoard = true;
	boolean isAiController = false;
	ArrayList<Character> characters;
	Game mygame;
	public CharacterIcon(int posx, int posy, ArrayList<Character> chars, Game gameinstance) {
		x = posx;
		y = posy;
		interactRect = new Rectangle(x,y,100,100);
		characters = chars;
		mygame = gameinstance;
	}

	public void draw(Graphics g) {
		g.drawImage(img, x, y, 100, 100, null);
	}
	public void addCharacter(){
		
	}
}
