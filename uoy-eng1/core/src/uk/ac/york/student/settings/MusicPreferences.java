package uk.ac.york.student.settings;

import lombok.Getter;

/**
 * The {@link MusicPreferences} class implements the {@link Preference} interface and manages the music settings of the game.
 * It includes preferences for whether the music is enabled and the volume of the music.
 * The class uses the {@link GamePreferences} class to store and retrieve the preferences from the game's preferences file.
 * The keys for the preferences are generated using the {@link MusicPreferences#getKey(String)} method with the specific part of the key related to a preference as the argument.
 * The default values for the preferences are set in the constructor of the class.
 * The constant string {@link MusicPreferences#ENABLED} is the key for the 'enabled' preference of the music settings.
 * The constant boolean {@link MusicPreferences#DEFAULT_ENABLED} is the default value for the 'enabled' preference of the music settings.
 * The constant string {@link MusicPreferences#VOLUME} is the key for the 'volume' preference of the music settings.
 * The constant float {@link MusicPreferences#DEFAULT_VOLUME} is the default value for the 'volume' preference of the music settings.
 */
@Getter
public class MusicPreferences implements Preference {
    private static final String ENABLED = "enabled";
    private static final boolean DEFAULT_ENABLED = true;
    private static final String VOLUME = "volume";
    private static final float DEFAULT_VOLUME = 0.5f;

    /**
     * The constructor for the {@link MusicPreferences} class.
     * It sets the default values for the {@link MusicPreferences#ENABLED} and {@link MusicPreferences#VOLUME} preferences of the music settings.
     * The default values are stored in the game's preferences file using the {@link GamePreferences} class.
     * The keys for the preferences are obtained using the {@link MusicPreferences#getKey(String)} method with {@link MusicPreferences#ENABLED} and {@link MusicPreferences#VOLUME} as the arguments.
     * After setting the values, the preferences are flushed to ensure they are saved.
     */
    public MusicPreferences() {
        GamePreferences.getPreferences().putBoolean(getKey(ENABLED), DEFAULT_ENABLED);
        GamePreferences.getPreferences().putFloat(getKey(VOLUME), DEFAULT_VOLUME);
        GamePreferences.getPreferences().flush();
    }

    /**
     * This method checks if the music is enabled in the game's settings.
     * It retrieves the value of the {@link MusicPreferences#ENABLED} preference from the game's preferences file using the {@link GamePreferences} class.
     * The key for the 'enabled' preference is obtained using the {@link MusicPreferences#getKey(String)} method with {@link MusicPreferences#ENABLED} as the argument.
     * @return boolean - returns true if the music is enabled, false otherwise.
     */
    public boolean isEnabled() {
        return GamePreferences.getPreferences().getBoolean(getKey(ENABLED));
    }

    /**
     * This method sets the {@link MusicPreferences#ENABLED} preference of the music settings to the provided value.
     * It stores the value in the game's preferences file using the {@link GamePreferences} class.
     * The key for the {@link MusicPreferences#ENABLED} preference is obtained using the {@link MusicPreferences#getKey(String)} method with {@link MusicPreferences#ENABLED} as the argument.
     * After setting the value, the preferences are flushed to ensure the change is saved.
     * @param b - the new value for the {@link MusicPreferences#ENABLED} preference. If true, the music is enabled; if false, the music is disabled.
     */
    public void setEnabled(boolean b) {
        GamePreferences.getPreferences().putBoolean(getKey(ENABLED), b);
        GamePreferences.getPreferences().flush();
    }

    /**
     * This method retrieves the volume level of the music settings from the game's preferences.
     * It retrieves the value of the {@link MusicPreferences#VOLUME} preference from the game's preferences file using the {@link GamePreferences} class.
     * The key for the {@link MusicPreferences#VOLUME} preference is obtained using the {@link MusicPreferences#getKey(String)} method with {@link MusicPreferences#VOLUME} as the argument.
     * @return float - returns the current volume level of the music settings.
     */
    public float getVolume() {
        return GamePreferences.getPreferences().getFloat(getKey(VOLUME));
    }

    /**
     * This method sets the {@link MusicPreferences#VOLUME} preference of the music settings to the provided value.
     * It stores the value in the game's preferences file using the {@link GamePreferences} class.
     * The key for the {@link MusicPreferences#VOLUME} preference is obtained using the {@link MusicPreferences#getKey(String)} method with {@link MusicPreferences#VOLUME} as the argument.
     * After setting the value, the preferences are flushed to ensure the change is saved.
     * @param vol - the new value for the {@link MusicPreferences#VOLUME} preference. It represents the volume level of the music settings.
     */
    public void setVolume(float vol) {
        GamePreferences.getPreferences().putFloat(getKey(VOLUME), vol);
        GamePreferences.getPreferences().flush();
    }

    /**
     * This method generates the key for a specific music preference.
     * It appends the provided key to the string "music." to create a unique key for each music preference.
     * The generated key is used to store and retrieve the value of the preference from the game's preferences file using the {@link GamePreferences} class.
     * @param key - the specific part of the key related to a music preference.
     * @return {@link String} - returns the generated key for the music preference.
     */
    @Override
    public String getKey(String key) {
        return "music." + key;
    }
}
