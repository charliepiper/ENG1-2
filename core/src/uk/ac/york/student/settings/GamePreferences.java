package uk.ac.york.student.settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link GamePreferences} enum is used to manage the different types of game preferences.
 * It includes preferences for music ({@link GamePreferences#MUSIC}), sound ({@link GamePreferences#SOUND}), debug screen ({@link GamePreferences#DEBUG_SCREEN}), and main menu clouds ({@link GamePreferences#MAIN_MENU_CLOUDS}).
 * Each preference type is associated with a specific {@link Preference} object.
 * The enum also provides a method {@link GamePreferences#getName()} to get the name of the preference type in lowercase.
 * Additionally, it provides a static method {@link GamePreferences#getPreferences()} to retrieve the {@link Preferences} object from the application.
 */
@Getter
public enum GamePreferences {
    MUSIC(new MusicPreferences()),
    SOUND(new SoundPreferences()),
    DEBUG_SCREEN(new DebugScreenPreferences()),
    MAIN_MENU_CLOUDS(new MainMenuCloudsPreferences());

    /**
     * This is a private final field of type {@link Preference}.
     * Each enum constant in the {@link GamePreferences} enum is associated with a specific {@link Preference} object.
     * This field is initialized through the constructor {@link GamePreferences#GamePreferences(Preference)} of the {@link GamePreferences} enum.
     */
    private final Preference preference;

    /**
     * This is the constructor for the {@link GamePreferences} enum.
     * It takes a {@link Preference} object as a parameter and assigns it to the {@link GamePreferences#preference} field.
     * Each enum constant in the {@link GamePreferences} enum is associated with a specific {@link Preference} object.
     * This association is established through this constructor.
     *
     * @param preference A {@link Preference} object which is associated with the enum constant.
     */
    GamePreferences(Preference preference) {
        this.preference = preference;
    }

    /**
     * This method is used to get the name of the enum constant in lowercase.
     * It does not take any parameters.
     * The method returns a string which is the name of the enum constant in lowercase.
     * This can be useful when the enum constant's name is needed in a format that is not case-sensitive.
     *
     * @return A string value which is the name of the enum constant in lowercase.
     */
    public @NotNull String getName() {
        return this.name().toLowerCase();
    }

    /**
     * It represents the name of the preferences file where the game settings are stored.
     * The value of this field is used in the {@link GamePreferences#getPreferences()} method to retrieve the {@link Preferences} object from the application.
     */
    private static final String NAME = "settings";

    /**
     * This is a static method that retrieves the {@link Preferences} object from the application.
     * The method uses the {@link com.badlogic.gdx.Application#getPreferences(String)} method to retrieve the {@link Preferences} object.
     * The name of the preferences file, stored in the static field {@link GamePreferences#NAME}, is passed as a parameter to the {@link GamePreferences#getPreferences()} method.
     * The {@link Preferences} object contains the game settings which are stored in the preferences file.
     *
     * @return A {@link Preferences} object which contains the game settings.
     */
    static Preferences getPreferences() {
        return Gdx.app.getPreferences(NAME);
    }
}
