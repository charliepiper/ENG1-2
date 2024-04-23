package uk.ac.york.student.settings;

/**
 * This is an interface for a {@link Preference}.
 * It provides a method to generate a key for a specific preference.
 * The key is used to store and retrieve the value of the preference from the game's preferences file.
 */
public interface Preference {
    /**
     * This method generates the key for a specific preference.
     * It takes a string as an argument and returns a string.
     * The implementation of this method is expected to generate a unique key for each preference.
     * @param key - the specific part of the key related to a preference.
     * @return {@link String} - returns the generated key for the preference.
     */
    String getKey(String key);
}
