package uk.ac.york.student.settings;

/**
 * The {@link DebugScreenPreferences} class implements the {@link Preference} interface.
 * This class is used to manage the preferences related to screen debugging.
 * It provides methods to enable or disable screen debugging and to get the current status of screen debugging.
 * The preferences are stored using the {@link GamePreferences} class.
 */
public class DebugScreenPreferences implements Preference {
    /**
     * The constant string {@link DebugScreenPreferences#ENABLED} is used as a key to store and retrieve the preference related to the enabled status of screen debugging.
     */
    private static final String ENABLED = "enabled";

    /**
     * The constant boolean {@link DebugScreenPreferences#DEFAULT_ENABLED} is used to set the default value for the enabled status of screen debugging. By default, screen debugging is disabled.
     */
    private static final boolean DEFAULT_ENABLED = false;

    /**
     * Constructor for the {@link DebugScreenPreferences} class.
     * It initializes the preferences related to screen debugging.
     * The enabled status of screen debugging is set to the default value ({@link DebugScreenPreferences#DEFAULT_ENABLED}) using the {@link GamePreferences} class.
     * The preferences are then saved to the persistent storage.
     */
    public DebugScreenPreferences() {
        GamePreferences.getPreferences().putBoolean(getKey(ENABLED), DEFAULT_ENABLED);
        GamePreferences.getPreferences().flush();
    }

    /**
     * This method is used to check if screen debugging is enabled.
     * It retrieves the preference related to the enabled status of screen debugging using the key {@link DebugScreenPreferences#ENABLED}.
     * The preference is retrieved using the {@link GamePreferences} class.
     * The method returns a boolean value indicating whether screen debugging is enabled.
     *
     * @return A boolean value indicating whether screen debugging is enabled.
     */
    public boolean isEnabled() {
        return GamePreferences.getPreferences().getBoolean(getKey(ENABLED));
    }

    /**
     * This method is used to enable or disable screen debugging.
     * It takes a boolean parameter 'b' which indicates the desired status of screen debugging.
     * The method stores the preference related to the enabled status of screen debugging using the key {@link DebugScreenPreferences#ENABLED}.
     * The preference is stored using the {@link GamePreferences} class.
     * After storing the preference, the method saves the changes to the persistent storage.
     *
     * @param b A boolean value indicating the desired status of screen debugging.
     */
    public void setEnabled(boolean b) {
        GamePreferences.getPreferences().putBoolean(getKey(ENABLED), b);
        GamePreferences.getPreferences().flush();
    }

    /**
     * This method is used to generate a key for the preferences related to screen debugging.
     * It takes a string parameter 'key' which is the base key.
     * The method returns a string which is a concatenation of the string "debug_screen." and the base key.
     * This key is used to store and retrieve the preferences related to screen debugging.
     *
     * @param key A string value which is the base key.
     * @return A string value which is the key for the preferences related to screen debugging.
     */
    @Override
    public String getKey(String key) {
        return "debug_screen." + key;
    }
}
