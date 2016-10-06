package fighter;

import java.util.ArrayList;
import java.util.Random;

public class SoundArray {
	private ArrayList<Sound> sounds = new ArrayList<Sound>();

	public SoundArray(Sound... sounds) {
		for (Sound current : sounds) {
			this.sounds.add(current);
		}
	}

	public Sound getRandomSound() {
		for (Sound current : sounds) {
			current.stop();
		}
		Random rand = new Random();
		return sounds.get(rand.nextInt(sounds.size()));
	}

	public boolean isAnySoundPlaying() {
		for (Sound current : sounds) {
			if (current.isrunning())
				return true;
		}
		return false;
	}
	public void stopAllSounds(){
		for (Sound current : sounds) {
			current.stop();
		}
	}

}
