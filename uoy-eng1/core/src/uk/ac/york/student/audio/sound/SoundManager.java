package uk.ac.york.student.audio.sound;

import lombok.Getter;
import uk.ac.york.student.audio.AudioManager;
import uk.ac.york.student.audio.sound.elements.ButtonClickSound;
import uk.ac.york.student.utils.EnumMapOfSuppliers;

import java.util.EnumMap;
import java.util.function.Supplier;

/**
 * Singleton class that manages the sound for the game.
 * It implements the AudioManager interface and controls the game sounds.
 */
public class SoundManager implements AudioManager {
    /**
     * The instance of the sound manager.
     * It is a static final instance of SoundManager.
     */
    @Getter
    private static final SoundManager instance = new SoundManager();

    /**
     * Supplier for the button click sound
     */
    private static final Supplier<GameSound> buttonClickSound = ButtonClickSound::new;

    /**
     * Map of game sounds.
     * It is a static final instance of EnumMap.
     */
    @Getter
    private static final EnumMap<Sounds, GameSound> sounds = new EnumMap<>(Sounds.class);

    /**
     * Map of suppliers for game sounds.
     * It is a static final instance of EnumMapOfSuppliers.
     */
    @Getter
    private static final EnumMapOfSuppliers<Sounds, GameSound> supplierSounds = new EnumMapOfSuppliers<>(Sounds.class);

    // Static block to initialize the sounds map and supplierSounds map
    static {
        sounds.put(Sounds.BUTTON_CLICK, buttonClickSound.get());
        supplierSounds.put(Sounds.BUTTON_CLICK, buttonClickSound);
    }

    /**
     * Private constructor to prevent instantiation.
     * As this is a singleton class, the constructor is private.
     */
    private SoundManager() {

    }

    /**
     * Called when the game is started.
     * This method is part of the AudioManager interface.
     */
    @Override
    public void onEnable() {

    }

    /**
     * Called when the game is stopped.
     * This method is part of the AudioManager interface.
     * It disposes all the game sounds.
     */
    @Override
    public void onDisable() {
        for (GameSound sound : sounds.values()) {
            sound.dispose();
        }
    }
}