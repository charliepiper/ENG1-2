package uk.ac.york.student.audio.sound.elements;

import uk.ac.york.student.audio.sound.GameSound;
import uk.ac.york.student.settings.GamePreferences;
import uk.ac.york.student.settings.SoundPreferences;

/**
 * This class extends GameSound and represents the sound effect for a button click in the game.
 * It overrides the play method to check if sound is enabled in the game preferences before playing the sound.
 */
public class ButtonClickSound extends GameSound {
    /**
     * Default constructor for the ButtonClickSound class.
     * It initialises the object with the default path to the button click sound file.
     */
    public ButtonClickSound() {
        super("audio/sounds/mixkit-classic-click.mp3");
    }

    /**
     * Plays the button click sound.
     * It first checks if sound is enabled in the game preferences.
     * If sound is enabled, it calls the play method of the superclass (GameSound).
     * If sound is not enabled, it returns -1.
     * @return The sound id if sound is enabled, -1 otherwise
     */
    @Override
    public long play() {
        SoundPreferences soundPreferences = (SoundPreferences) GamePreferences.SOUND.getPreference();
        if (soundPreferences.isEnabled()) {
            return super.play();
        } else {
            return -1;
        }
    }

    /**
     * Stops the button click sound.
     * @param soundId The id of the sound to stop
     */
    @Override
    public void stop(long soundId) {
        sound.stop(soundId);
    }

    /**
     * Pauses the button click sound.
     * @param soundId The id of the sound to pause
     */
    @Override
    public void pause(long soundId) {
        sound.pause(soundId);
    }

    /**
     * Resumes the button click sound.
     * @param soundId The id of the sound to resume
     */
    @Override
    public void resume(long soundId) {
        sound.resume(soundId);
    }
}