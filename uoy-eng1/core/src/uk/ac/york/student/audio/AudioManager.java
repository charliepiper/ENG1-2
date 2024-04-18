package uk.ac.york.student.audio;

/**
 * Interface for managing audio in the game.
 * It provides methods to enable and disable audio.
 */
public interface AudioManager {
    /**
     * Called when the game is started.
     * Implementations should start playing audio here.
     */
    void onEnable();

    /**
     * Called when the game is stopped.
     * Implementations should stop playing audio and clean up resources here.
     */
    void onDisable();
}