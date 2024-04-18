package uk.ac.york.student.assets.map;

/**
 * Interface for the map objects that have actions
 */
public interface ActionMapObject {
    /**
     * Gets the string to display on the screen on the action label
     * @return the string to display on the screen on the action label
     */
    String getStr();

    /**
     * Gets the type of the action
     * @return the type of the action
     */
    Object getType();
}
