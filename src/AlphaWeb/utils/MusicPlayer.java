package AlphaWeb.utils;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;

public class MusicPlayer {
    private static MusicPlayer instance;
    private Clip clip;
    private boolean isPlaying = false;
    private boolean isPaused = false;

    private MusicPlayer() {
        // Private constructor for singleton
    }

    public static synchronized MusicPlayer getInstance() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }

    public void play(String filepath) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }

            java.net.URL resource = getClass().getResource(filepath);
            if (resource == null) {
                System.err.println("Audio file not found: " + filepath);
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(resource.openStream()));
            if (clip != null) {
                clip.close();
            }
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            if (!isPaused) {
                clip.start();
                isPlaying = true;
            }
            System.out.println("Music started for: " + filepath + ", clip open: " + (clip != null && clip.isOpen()));
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error playing music: " + e.getMessage());
            isPlaying = false;
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
            isPlaying = false;
            System.out.println("Music stopped, clip closed.");
        } else if (clip != null) {
            System.out.println("Clip exists but not running, state: " + clip.isOpen());
        } else {
            System.out.println("No clip to stop.");
        }
    }

    public void toggle() {
        if (clip == null) {
            play("/AlphaWeb/utils/music.wav");
            return;
        }
        if (isPlaying) {
            clip.stop();
            isPlaying = false;
        } else {
            clip.start();
            isPlaying = true;
        }
        System.out.println("Music toggled, isPlaying: " + isPlaying);
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
        if (clip != null) {
            if (paused) {
                if (clip.isRunning()) {
                    clip.stop();
                    isPlaying = false;
                }
            } else if (!isPlaying) {
                clip.start();
                isPlaying = true;
            }
            System.out.println("Paused set to: " + paused + ", clip state: " + (clip.isOpen() ? "open" : "closed"));
        } else {
            System.out.println("No clip to pause, attempting to play default.");
            play("/AlphaWeb/utils/music.wav");
        }
    }

    public boolean isPlaying() {
        return isPlaying && (clip != null && clip.isRunning());
    }
}
