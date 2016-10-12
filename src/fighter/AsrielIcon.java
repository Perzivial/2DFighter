package fighter;

import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class AsrielIcon extends CharacterIcon {

	public AsrielIcon(int row, ArrayList<Character> chars, Game gameinstance) {
		super(row,chars, gameinstance);
		img = new Image("img/icons/asriel.png").getScaledInstance(new Image("img/icons/asriel.png").img, 200, 200,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR, false);

	}

	public void addCharacter() {
		if (!isAiController) {
			if (!isChoosingKeyBoard) {
				Character Controller = new Asriel(300, 450, "Xbox 360 Wired Controller", "x", "y", "rx", "ry", "z",
						"rz", .5, .2, "1", "2", "3", "5", mygame);
				characters.add(Controller);
			} else {
				Character KeyBoard = new Asriel(500, 400, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT,
						KeyEvent.VK_RIGHT, KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE, KeyEvent.VK_Q, KeyEvent.VK_W,
						KeyEvent.VK_E, KeyEvent.VK_R, mygame);
				characters.add(KeyBoard);
			}
		} else {
			Character Ai = new Asriel(500, 400, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
					KeyEvent.VK_RIGHT, KeyEvent.VK_SPACE, KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R,
					mygame);
			mygame.aiList.add(new AiController(Ai, mygame));
			Ai.disableInput();
			characters.add(Ai);
		}
	}
}
