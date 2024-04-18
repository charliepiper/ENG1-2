package uk.ac.york.student.settings;

import lombok.Getter;

/**
 * This class represents the sound preferences in the game.
 * It implements the {@link Preference} interface and provides methods to get and set the {@link SoundPreferences#ENABLED} and {@link SoundPreferences#VOLUME} preferences for the sound settings.
 * The {@link SoundPreferences#ENABLED} preference determines whether the sound is enabled or disabled.
 * The {@link SoundPreferences#VOLUME} preference determines the volume level of the sound.
 * The default values for the {@link SoundPreferences#ENABLED} and {@link SoundPreferences#VOLUME} preferences are set in the constructor.
 * The values of the preferences are stored in the game's preferences file using the {@link GamePreferences} class.
 * The keys for the preferences are generated using the {@link #getKey(String)} method.
 */
@Getter
public class SoundPreferences implements Preference {
    private static final String ENABLED = "enabled";
    private static final boolean DEFAULT_ENABLED = true;
    private static final String VOLUME = "volume";
    private static final float DEFAULT_VOLUME = 1f;


    /**
     * This is the constructor for the {@link SoundPreferences} class.
     * It sets the default values for the {@link SoundPreferences#ENABLED} and {@link SoundPreferences#VOLUME} preferences in the game's preferences file.
     * The {@link SoundPreferences#ENABLED} preference is set to {@link SoundPreferences#DEFAULT_ENABLED} and the {@link SoundPreferences#VOLUME} preference is set to {@link SoundPreferences#DEFAULT_VOLUME}.
     * The keys for the preferences are generated using the {@link #getKey(String)} method with {@link SoundPreferences#ENABLED} and {@link SoundPreferences#VOLUME} as the arguments.
     * After setting the values, the preferences are flushed to ensure the changes are saved.
     */
    public SoundPreferences() {
        GamePreferences.getPreferences().putBoolean(getKey(ENABLED), DEFAULT_ENABLED);
        GamePreferences.getPreferences().putFloat(getKey(VOLUME), DEFAULT_VOLUME);
        GamePreferences.getPreferences().flush();
    }

    /**
     * This method retrieves the {@link SoundPreferences#ENABLED} preference of the sound settings from the game's preferences file.
     * It uses the {@link GamePreferences} class to get the value of the preference.
     * The key for the {@link SoundPreferences#ENABLED} preference is obtained using the {@link SoundPreferences#getKey(String)} method with {@link SoundPreferences#ENABLED} as the argument.
     * @return boolean - returns the value of the {@link SoundPreferences#ENABLED} preference. It represents whether the sound is enabled or disabled.
     */
    public boolean isEnabled() {
        return GamePreferences.getPreferences().getBoolean(getKey(ENABLED));
    }

    /**
     * This method sets the {@link SoundPreferences#ENABLED} preference of the sound settings in the game's preferences file.
     * It takes a boolean as an argument and uses the {@link GamePreferences} class to set the value of the preference.
     * The key for the {@link SoundPreferences#ENABLED} preference is obtained using the {@link SoundPreferences#getKey(String)} method with {@link SoundPreferences#ENABLED} as the argument.
     * After setting the value, the preferences are flushed to ensure the changes are saved.
     * @param b - the new value for the {@link SoundPreferences#ENABLED} preference. It represents whether the sound should be enabled or disabled.
     */
    public void setEnabled(boolean b) {
        GamePreferences.getPreferences().putBoolean(getKey(ENABLED), b);
        GamePreferences.getPreferences().flush();
    }

    /**
     * This method retrieves the {@link SoundPreferences#VOLUME} preference of the sound settings from the game's preferences file.
     * It uses the {@link GamePreferences} class to get the value of the preference.
     * The key for the {@link SoundPreferences#VOLUME} preference is obtained using the {@link SoundPreferences#getKey(String)} method with {@link SoundPreferences#VOLUME} as the argument.
     * @return float - returns the value of the {@link SoundPreferences#VOLUME} preference. It represents the volume level of the sound.
     */
    public float getVolume() {
        return GamePreferences.getPreferences().getFloat(getKey(VOLUME));
    }

    /**
     * This method sets the {@link SoundPreferences#VOLUME} preference of the sound settings in the game's preferences file.
     * It takes a float as an argument and uses the {@link GamePreferences} class to set the value of the preference.
     * The key for the {@link SoundPreferences#VOLUME} preference is obtained using the {@link SoundPreferences#getKey(String)} method with {@link SoundPreferences#VOLUME} as the argument.
     * After setting the value, the preferences are flushed to ensure the changes are saved.
     * @param vol - the new value for the {@link SoundPreferences#VOLUME} preference. It represents the volume level of the sound.
     */
    public void setVolume(float vol) {
        GamePreferences.getPreferences().putFloat(getKey(VOLUME), vol);
        GamePreferences.getPreferences().flush();
    }

    /**
     * This method generates the key for a given sound preference.
     * It takes a string as an argument, which represents the name of the preference.
     * The method concatenates the string "sound." with the name of the preference to generate the key.
     * The generated key is used to store and retrieve the value of the preference in the game's preferences file.
     * @param key - the name of the preference.
     * @return {@link String} - returns the generated key for the preference.
     */
    @Override
    public String getKey(String key) {
        return "sound." + key;
    }
}
