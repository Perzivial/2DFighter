package fighter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Handles playing, stoping, and looping of sounds for the game.
 * 
 * @author Tyler Thomas
 *
 */
public class Sound {
	private Clip clip;
	float volume;
	boolean ismuted;
	Mixer.Info[] infos = AudioSystem.getMixerInfo();
	Line[] lines;
	boolean isdisabled = true;

	public Sound(String fileName) {
		// specify the sound to play
		// (assuming the sound can be played by the audio system)
		// from a wave File
		try {
			// fileName = "file:" + fileName;
			URL url = new URL(fileName);
			AudioInputStream sound = AudioSystem.getAudioInputStream(url);
			// load the sound into memory (a Clip)
			clip = AudioSystem.getClip();
			clip.open(sound);

		} catch (Exception e2) {

			try {

				URL url = getClass().getResource(fileName);
				// System.out.println(url);
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(url);// check
																					// the
																					// URL!
				// AudioInputStream audioInputStream =
				// AudioSystem.getAudioInputStream(url);
				clip = AudioSystem.getClip();
				clip.open(inputStream);

			}

			catch (MalformedURLException e) {
				e.printStackTrace();
				throw new RuntimeException("Sound: Malformed URL: " + e);
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
				throw new RuntimeException("Sound: Unsupported Audio File: " + e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Sound: Input/Output Error: " + e);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
				throw new RuntimeException("Sound: Line Unavailable Exception Error: " + e);
			}
		}
		// play, stop, loop the sound clip
		for (Mixer.Info info : infos) {
			Mixer mixer = AudioSystem.getMixer(info);
			lines = mixer.getSourceLines();
		}
		
	}

	public void play() {
		if (!isdisabled) {
			clip.setFramePosition(0); // Must always rewind!
			clip.start();
		}
	}

	public void continuesound() {
		if (!isdisabled) {
			clip.start();
			reducesound();
		}
	}

	public void loop() {
		if (!isdisabled) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	public void stop() {
		clip.stop();
	}

	public boolean isrunning() {
		if (clip.isRunning())
			return true;
		return false;
	}
public void reducesound(){
	FloatControl gainControl = 
		    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(-10.0f);
}

}