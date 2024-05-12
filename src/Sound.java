import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.lang.Math;

public class Sound {
    Clip clipBackgroundMusic;
    Clip clipGameOverSound;
    URL soundURLs[] = new URL[5];

    public Sound() {
        soundURLs[0] = getClass().getResource("sound/BlueBoyAdventure.wav");
        soundURLs[1] = getClass().getResource("sound/Dungeon.wav");
        soundURLs[2] = getClass().getResource("sound/FinalBattle.wav");
        soundURLs[3] = getClass().getResource("sound/Merchant.wav");
        soundURLs[4] = getClass().getResource("sound/gameover.wav");
    }

    public void playAndLoopBackgroundMusic() {
        int random = (int) (Math.random() * 3);

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURLs[random]);
            clipBackgroundMusic = AudioSystem.getClip();
            clipBackgroundMusic.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }

        clipBackgroundMusic.start();
        clipBackgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stopBackgroundMusic() {
        clipBackgroundMusic.stop();
    }

    public void playGameOverSound() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURLs[4]);
            clipGameOverSound = AudioSystem.getClip();
            clipGameOverSound.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }

        clipGameOverSound.start();
    }

    public void gameOverSOundStop() {
        clipGameOverSound.stop();
    }
}
