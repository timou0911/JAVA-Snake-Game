import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    Clip clip;
    Clip clip2;
    URL soundURLs[] = new URL[5];

    public Sound() {
        soundURLs[0] = getClass().getResource("sound/BlueBoyAdventure.wav");
        soundURLs[1] = getClass().getResource("sound/Dungeon.wav");
        soundURLs[2] = getClass().getResource("sound/FinalBattle.wav");
        soundURLs[3] = getClass().getResource("sound/Merchant.wav");
        soundURLs[4] = getClass().getResource("sound/gameover.wav");
        
    }

    public void play() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURLs[0]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }

        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public void playGameOverSound() {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURLs[4]);
            clip2 = AudioSystem.getClip();
            clip2.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }

        clip2.start();
    }
}
