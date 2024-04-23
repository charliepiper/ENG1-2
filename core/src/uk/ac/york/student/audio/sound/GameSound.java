package uk.ac.york.student.audio.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import org.jetbrains.annotations.Range;
import uk.ac.york.student.settings.GamePreferences;
import uk.ac.york.student.settings.SoundPreferences;

/**
 * This abstract class implements the Sound interface and serves as a base class for game sounds.
 * It provides common functionality for managing game sounds, such as play, pause, stop, loop controls, and sound settings.
 */
public abstract class GameSound implements Sound {
    /**
     * Sound object that this GameSound wraps around
     */
    protected final Sound sound;

    /**
     * Constructor for the GameSound class.
     * It initialises the object with the given path to the sound file.
     * @param path The internal path to the sound file (in assets folder)
     */
    protected GameSound(final String path) {
        sound = Gdx.audio.newSound(Gdx.files.internal(path));
    }

    /**
     * Plays the sound with the volume set in the game preferences.
     * @return The sound id
     */
    @Override
    public long play() {
        return sound.play(((SoundPreferences) GamePreferences.SOUND.getPreference()).getVolume());
    }

    /**
     * Plays the sound with the given volume.
     * @param volume The volume to play the sound at [0, 1]
     * @return The sound id
     */
    @Override
    public long play(@Range(from=0, to=1) final float volume) {
        return sound.play(volume);
    }

    /** Plays the sound. If the sound is already playing, it will be played again, concurrently.
     * @param volume the volume in the range [0,1]
     * @param pitch the pitch multiplier, 1 == default, >1 == faster, <1 == slower, the value has to be between 0.5 and 2.0
     * @param pan panning in the range -1 (full left) to 1 (full right). 0 is center position.
     * @return the id of the sound instance if successful, or -1 on failure.
     */
    @Override
    public long play(@Range(from=0, to=1) final float volume, final float pitch, @Range(from=-1, to=1) final float pan) {
        return sound.play(volume, pitch, pan);
    }

    /**
     * Loops the sound with the volume set in the game preferences.
     * @return The sound id
     */
    @Override
    public long loop() {
        return sound.loop(((SoundPreferences) GamePreferences.SOUND.getPreference()).getVolume());
    }

    /**
     * Loops the sound with the given volume.
     * @param volume The volume to play the sound at [0, 1]
     * @return The sound id
     */
    @Override
    public long loop(@Range(from=0, to=1) final float volume) {
        return sound.loop(volume);
    }

    /**
     * Loops the sound with the given volume, pitch and pan.
     * @param volume The volume to play the sound at
     * @param pitch The pitch to play the sound at
     * @param pan The pan to play the sound at
     * @return The sound id
     */
    @Override
    public long loop(@Range(from=0, to=1) final float volume, final float pitch, @Range(from=-1, to=1) final float pan) {
        return sound.loop(volume, pitch, pan);
    }

    /**
     * Stops the sound.
     */
    @Override
    public void stop() {
        sound.stop();
    }

    /**
     * Pauses the sound.
     */
    @Override
    public void pause() {
        sound.pause();
    }

    /**
     * Resumes the sound.
     */
    @Override
    public void resume() {
        sound.resume();
    }

    /**
     * Disposes of the sound.
     * Needs to be called when the Sound is no longer needed.
     */
    @Override
    public void dispose() {
        sound.dispose();
    }

    /**
     * Sets whether the sound should loop.
     * @param soundId The sound id
     * @param looping Whether the sound should loop
     */
    @Override
    public void setLooping(final long soundId, final boolean looping) {
        sound.setLooping(soundId, looping);
    }

    /**
     * Sets the pitch of the sound.
     * @param soundId The sound id
     * @param pitch The pitch to set the sound to
     */
    @Override
    public void setPitch(final long soundId, final float pitch) {
        sound.setPitch(soundId, pitch);
    }

    /**
     * Sets the volume of the sound.
     * @param soundId The sound id
     * @param volume The volume to set the sound to [0, 1]
     */
    @Override
    public void setVolume(final long soundId, @Range(from=0, to=1) final float volume) {
        sound.setVolume(soundId, volume);
    }

    /**
     * Sets the pan and volume of the sound.
     * @param soundId The sound id
     * @param pan The pan to set the sound to
     * @param volume The volume to set the sound to
     */
    @Override
    public void setPan(final long soundId, @Range(from=-1, to=1) final float pan, @Range(from=0, to=1) final float volume) {
        sound.setPan(soundId, pan, volume);
    }
}