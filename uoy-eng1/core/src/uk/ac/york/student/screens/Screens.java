package uk.ac.york.student.screens;

import com.badlogic.gdx.Gdx;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * This is a utility class that holds references to all the screen classes in the game.
 * It uses reflection to initialize these references, and provides a method to get a screen class by its name.
 * This class is final and cannot be subclassed.
 *
 * This class is designed to imitate an enum, but with the ability to dynamically load screen classes.
 */
@UtilityClass
public final class Screens {
    /**
     * Holds the class reference to the {@link MainMenuScreen}. This is used to dynamically load the {@link MainMenuScreen} when needed.
     */
    public static Class<MainMenuScreen> MAIN_MENU;

    /**
     * Holds the class reference to the {@link LoadingScreen}. This is used to dynamically load the {@link LoadingScreen} when needed.
     */
    public static Class<LoadingScreen> LOADING;

    /**
     * Holds the class reference to the {@link GameScreen}. This is used to dynamically load the {@link GameScreen} when needed.
     */
    public static Class<GameScreen> GAME;

    /**
     * Holds the class reference to the {@link PreferencesScreen}. This is used to dynamically load the {@link PreferencesScreen} when needed.
     */
    public static Class<PreferencesScreen> PREFERENCES;

    /**
     * Holds the class reference to the {@link EndScreen}. This is used to dynamically load the {@link EndScreen} when needed.
     */
    public static Class<EndScreen> END;

    static {
        // Get all the fields of the Screens class
        Field[] fields = Screens.class.getFields();

        // Iterate over each field
        for (Field field : fields) {
            // Get the generic type of the field and convert it to a string
            // Remove the "java.lang.Class<" and ">" from the string to get the class path
            String path = field.getGenericType().getTypeName().replace("java.lang.Class<", "").replace(">", "");

            try {
                // Set the value of the field to the Class object of the class at the path
                // This is done using reflection, which allows for dynamic loading of classes
                //
                // Note: Many linters will complain about this, but it's fine in this case
                // as it does not have an effect on the program's security,
                // performance, and it does not violate encapsulation.
                //
                // Although the fields should be final, they are not because
                // this utility class is set up with reflection and designed to emulate an enum
                field.set(null, Class.forName(path));
            } catch (ClassNotFoundException | IllegalAccessException e) {
                // If the class at the path could not be found or accessed, log an error
                Gdx.app.error("Screens", "Could not find class " + path);
            }
        }
    }

    /**
     * This method is used to get the class reference of a screen by its name.
     * It iterates over all the fields in the {@link Screens} class, and if a field's name matches the provided name,
     * it returns the value of that field, which is a {@link Class} object.
     * If no field with the provided name is found, it returns null.
     *
     * @param name The name of the screen class to get the reference of.
     * @return The {@link Class} object of the screen class with the provided name, or null if no such class is found.
     */
    @SuppressWarnings("unchecked")
    public static @Nullable Class<? extends BaseScreen> valueOf(String name) {
        Field[] fields = Screens.class.getFields();
        // This loop iterates over all the fields in the Screens class.
        for (Field field : fields) {
            // Checks if the name of the current field matches the provided name.
            if (field.getName().equals(name)) {
                try {
                    // If a match is found, it attempts to return the value of the field.
                    // The value of the field is a Class object representing a screen class.
                    // The field's value is accessed using reflection, and it is cast to Class<? extends BaseScreen>.
                    return (Class<? extends BaseScreen>) field.get(null);
                } catch (IllegalAccessException e) {
                    // If the field cannot be accessed, it logs an error and continues to the next field.
                    Gdx.app.error("Screens", "Could not access field " + name);
                }
            }
        }
        // If no field with the provided name is found after iterating over all the fields, it returns null.
        return null;
    }
}
