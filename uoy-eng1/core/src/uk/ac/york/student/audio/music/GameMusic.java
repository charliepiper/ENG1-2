package uk.ac.york.student.audio.music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import lombok.Getter;
import org.jetbrains.annotations.Range;

/**
 * This abstract class implements the Music interface and serves as a base class for game music.
 * It provides common functionality for managing game music, such as play, pause, stop, and loop controls.
 */
@Getter
public abstract class GameMusic implements Music {
    /**
     * Music object that this GameMusic wraps around
     */
    protected final Music music;

    /**
     * Constructor for the GameMusic class.
     * It initialises the object with the given path to the music file.
     * @param path The internal path to the music file (in assets folder)
     */
    protected GameMusic(final String path)  {
        music = Gdx.audio.newMusic(Gdx.files.internal(path));
    }

    /**
     * Starts playing the music.
     */
    @Override
    public void play() {
        music.play();
    }

    /**
     * Pauses the music.
     */
    @Override
    public void pause() {
        music.pause();
    }

    /**
     * Stops the music.
     */
    @Override
    public void stop() {
        music.stop();
    }

    /**
     * Checks if the music is currently playing.
     * @return True if the music is playing, false otherwise
     */
    @Override
    public boolean isPlaying() {
        return music.isPlaying();
    }

    /**
     * Sets whether the music should loop.
     * @param isLooping whether to loop the stream
     */
    @Override
    public void setLooping(final boolean isLooping) {
        music.setLooping(isLooping);
    }

    /**
     * Checks if the music is currently looping.
     * @return True if the music is looping, false otherwise
     */
    @Override
    public boolean isLooping() {
        return music.isLooping();
    }

    /**
     * Sets the volume of the music.
     * @param volume The volume in range [0.0, 1.0]
     */
    @Override
    public void setVolume(@Range(from=0, to=1) final float volume) {
        music.setVolume(volume);
    }

    /**
     * Gets the volume of the music.
     * @return The volume in range [0.0, 1.0]
     */
    @Override
    public @Range(from=0, to=1) float getVolume() {
        return music.getVolume();
    }

    /**
     * Sets the panning and volume of the music.
     * @param pan panning in the range -1 (full left) to 1 (full right). 0 is center position.
     * @param volume the volume in the range [0,1].
     */
    @Override
    public void setPan(@Range(from=-1, to=1) final float pan, @Range(from=0,to=1) final float volume) {
        music.setPan(pan, volume);
    }

    /**
     * Sets the playback position in seconds.
     * @param position the position in seconds
     */
    @Override
    public void setPosition(final float position) {
        music.setPosition(position);
    }

    /**
     * Gets the playback position in seconds.
     * @return the position in seconds
     */
    @Override
    public float getPosition() {
        return music.getPosition();
    }

    /**
     * Disposes of the music.
     * Needs to be called when the Music is no longer needed.
     */
    @Override
    public void dispose() {
        music.dispose();
    }

    /**
     * Registers a callback to be invoked when the end of a music stream has been reached during playback.
     * @param listener the callback that will be run.
     */
    @Override
    public void setOnCompletionListener(final OnCompletionListener listener) {
        music.setOnCompletionListener(listener);
    }
}