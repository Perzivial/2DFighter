package fighter;

import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class KidGokuIcon extends CharacterIcon {

	public KidGokuIcon(int posx, int posy, ArrayList<Character> chars, Game gameinstance) {
		super(posx, posy, chars, gameinstance);
		img = new Image("img/icons/kidgoku.png").getScaledInstance(new Image("img/icons/kidgoku.png").img, 200, 200,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);

	}

	public void addCharacter() {
		if(!isAiController){
		if (!isChoosingKeyBoard) {
			Character KidGokuController = new KidGoku(300, 450, "Xbox 360 Wired Controller", "x", "y", "rx", "ry", "z",
					"rz", .5, .2, "1", "2", "3", "5", mygame);
			characters.add(KidGokuController);
		} else {
			Character KidGokuKeyBoard = new KidGoku(500, 400, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
					KeyEvent.VK_RIGHT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE, KeyEvent.VK_Q, KeyEvent.VK_W,
					KeyEvent.VK_E, KeyEvent.VK_R, mygame);
			characters.add(KidGokuKeyBoard);
		}
	}
	else{
		Character KidGokuAi = new KidGoku(500, 400, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
				KeyEvent.VK_RIGHT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE, KeyEvent.VK_Q, KeyEvent.VK_W,
				KeyEvent.VK_E, KeyEvent.VK_R, mygame);
		mygame.aiList.add(new AiController(KidGokuAi, mygame));
		KidGokuAi.disableInput();
		characters.add(KidGokuAi);
	}
	}
}
