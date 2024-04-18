package uk.ac.york.student.settings;

/**
 * This is the {@link MainMenuCloudsPreferences} class which implements the {@link Preference} interface.
 * The class is used to manage the preferences related to the main menu clouds in the game.
 * It provides methods to get and set the enabled status and speed of the main menu clouds.
 * The class also provides a method to get the key for a specific preference.
 * The preferences are stored in the game's preferences file and are retrieved using the {@link GamePreferences} class.
 */
public class MainMenuCloudsPreferences implements Preference {
    /**
     * This is the key for the preference that determines whether the main menu clouds are enabled or not.
     */
    private static final String ENABLED = "enabled";

    /**
     * This is the default value for the preference that determines whether the main menu clouds are enabled or not.
     */
    private static final boolean DEFAULT_ENABLED = true;

    /**
     * This is the key for the preference that determines the speed of the main menu clouds.
     */
    private static final String SPEED = "speed";

    /**
     * This is the default value for the preference that determines the speed of the main menu clouds.
     */
    private static final float DEFAULT_SPEED = 1f;

    /**
     * This is the constructor for the {@link MainMenuCloudsPreferences} class.
     * It initializes the preferences for the main menu clouds with their default values.
     * The preferences are stored in the game's preferences file and are retrieved using the {@link GamePreferences} class.
     * The preferences are then flushed to ensure they are saved.
     */
    public MainMenuCloudsPreferences() {
        GamePreferences.getPreferences().putBoolean(getKey(ENABLED), DEFAULT_ENABLED);
        GamePreferences.getPreferences().putFloat(getKey(SPEED), DEFAULT_SPEED);
        GamePreferences.getPreferences().flush();
    }

    /**
     * This method checks if the main menu clouds are enabled or not.
     * It retrieves the value of the {@link MainMenuCloudsPreferences#ENABLED} preference from the game's preferences file using the {@link GamePreferences} class.
     * The key for the 'enabled' preference is obtained using the {@link MainMenuCloudsPreferences#getKey(String)} method with {@link MainMenuCloudsPreferences#ENABLED} as the argument.
     * @return boolean - returns true if the main menu clouds are enabled, false otherwise.
     */
    public boolean isEnabled() {
        return GamePreferences.getPreferences().getBoolean(getKey(ENABLED));
    }

    /**
     * This method sets the enabled status of the main menu clouds.
     * It stores the value of the {@link MainMenuCloudsPreferences#ENABLED} preference in the game's preferences file using the {@link GamePreferences} class.
     * The key for the 'enabled' preference is obtained using the {@link MainMenuCloudsPreferences#getKey(String)} method with {@link MainMenuCloudsPreferences#ENABLED} as the argument.
     * After setting the value, the preferences are flushed to ensure they are saved.
     * @param b - the boolean value to set the {@link MainMenuCloudsPreferences#ENABLED} preference to.
     */
    public void setEnabled(boolean b) {
        GamePreferences.getPreferences().putBoolean(getKey(ENABLED), b);
        GamePreferences.getPreferences().flush();
    }

    /**
     * This method retrieves the speed of the main menu clouds.
     * It retrieves the value of the {@link MainMenuCloudsPreferences#SPEED} preference from the game's preferences file using the {@link GamePreferences} class.
     * The key for the 'speed' preference is obtained using the {@link MainMenuCloudsPreferences#getKey(String)} method with {@link MainMenuCloudsPreferences#SPEED} as the argument.
     * @return float - returns the speed of the main menu clouds.
     */
    public float getSpeed() {
        return GamePreferences.getPreferences().getFloat(getKey(SPEED));
    }

    /**
     * This method sets the speed of the main menu clouds.
     * It stores the value of the {@link MainMenuCloudsPreferences#SPEED} preference in the game's preferences file using the {@link GamePreferences} class.
     * The key for the 'speed' preference is obtained using the {@link MainMenuCloudsPreferences#getKey(String)} method with {@link MainMenuCloudsPreferences#SPEED} as the argument.
     * After setting the value, the preferences are flushed to ensure they are saved.
     * @param speed - the float value to set the {@link MainMenuCloudsPreferences#SPEED} preference to.
     */
    public void setSpeed(float speed) {
        GamePreferences.getPreferences().putFloat(getKey(SPEED), speed);
        GamePreferences.getPreferences().flush();
    }

    /**
     * This method generates the key for a specific preference related to the main menu clouds.
     * It appends the provided key to the string "main_menu_clouds." to create a unique key for each preference.
     * @param key - the specific part of the key related to a preference (e.g. {@link MainMenuCloudsPreferences#ENABLED}, {@link MainMenuCloudsPreferences#SPEED}).
     * @return String - returns the complete key for the preference.
     */
    @Override
    public String getKey(String key) {
        return "main_menu_clouds." + key;
    }
}
