package uk.ac.york.student.assets.map;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * This class extends MapObject and implements ActionMapObject.
 * It represents a transition to a different map on the GameScreen with specific properties.
 */
public class TransitionMapObject extends MapObject implements ActionMapObject {
    /**
     * MapProperties object to store the properties of the transition
     */
    private final MapProperties properties;

    /**
     * String representing the transition to display on the screen on the action label
     */
    @Getter
    private final String str;

    /**
     * String representing the map name
     */
    @Getter
    private final String type;

    /**
     * Constructor for the TransitionMapObject class.
     * It initialises the object with the properties of the given MapObject.
     *
     * @param object The MapObject to initialize the TransitionMapObject with.
     */
    public TransitionMapObject(@NotNull MapObject object) {
        // Calls the superclass constructor
        super();

        // Sets the name of the TransitionMapObject to the name of the given MapObject
        setName(object.getName());

        // Sets the color of the TransitionMapObject to the color of the given MapObject
        setColor(object.getColor());

        // Sets the opacity of the TransitionMapObject to the opacity of the given MapObject
        setOpacity(object.getOpacity());

        // Sets the visibility of the TransitionMapObject to the visibility of the given MapObject
        setVisible(object.isVisible());

        // Retrieves the properties of the given MapObject and assigns them to the properties of the TransitionMapObject
        properties = object.getProperties();

        // Retrieves the "newMap" property from the properties of the TransitionMapObject and assigns it to the type field
        type = properties.get("newMap", String.class);

        // Retrieves the "newMapStr" property from the properties of the TransitionMapObject and assigns it to the str field
        str = properties.get("newMapStr", String.class);
    }

    /**
     * Getter for the properties of the TransitionMapObject.
     * @return The properties of the TransitionMapObject.
     */
    @Override
    public MapProperties getProperties() {
        return properties;
    }
}