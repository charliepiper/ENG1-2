package uk.ac.york.student.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Actor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.HashMap;
import java.util.Map;

/**
 * The Player class extends the Actor class and implements the PlayerScore and InputProcessor interfaces.
 * This class represents a player in the game, handling player movement, interaction with the game map, and input processing.
 * It also manages the player's score and the player's sprite on the screen.
 */
@Getter
public class Player extends Actor implements PlayerScore, InputProcessor {
    /**
     * PlayerMetrics object to store and manage player-specific metrics.
     */
    private final PlayerMetrics metrics = new PlayerMetrics();

    /**
     * Scale of the map relative to the screen size.
     */
    private float mapScale;

    /**
     * Sprite object representing the player's character on the screen.
     */
    private Sprite sprite;

    /**
     * Sprite objects for each direction to face in.
     *<p>
     *     There's no right facing region in the Atlas so the left one is flipped
     *     when facing right.
     *</p>
     */
    private final TextureAtlas.AtlasRegion SPRITETOWARDSREGION;
    private final TextureAtlas.AtlasRegion SPRITEAWAYREGION;
    private final TextureAtlas.AtlasRegion SPRITELEFTREGION;
    /**
     * TiledMap object representing the current game map.
     */
    private TiledMap map;

    /**
     * TextureAtlas object containing the textures for the player's sprite.
     */
    private final TextureAtlas textureAtlas = new TextureAtlas("sprite-atlases/character-sprites.atlas");
    /**
     * Constructor for the Player class.
     *
     * @param map The TiledMap object representing the current game map.
     * @param startPosition The Vector2 object representing the starting position of the player on the map.
     */
    public Player(@NotNull TiledMap map, @NotNull Vector2 startPosition) {
        super(); // Call to the parent class constructor
        this.map = map; // Assign the provided map to the player's map

        // Get the first layer of the map (the bottom layer)
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);

        // Calculate the maximum height and width of the map
        final int maxHeight = layer.getHeight() * layer.getTileHeight();
        final int maxWidth = layer.getWidth() * layer.getTileWidth();

        // Calculate the scale of the map relative to the screen size
        mapScale = Math.max(Gdx.graphics.getWidth() / maxWidth, Gdx.graphics.getHeight() / maxHeight);

        // Create a sprite for the player and set its position, opacity, and size
        SPRITETOWARDSREGION = textureAtlas.findRegion("char3_towards");
        SPRITEAWAYREGION = textureAtlas.findRegion("char3_away");
        SPRITELEFTREGION = textureAtlas.findRegion("char3_left");
        sprite = textureAtlas.createSprite("char3_towards");
        sprite.setPosition(startPosition.x, startPosition.y);
        sprite.setAlpha(1);
        sprite.setSize(sprite.getWidth() * mapScale, sprite.getHeight() * mapScale);

        // Set the bounds of the player
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());

        // Load the bounding boxes of the map objects
        loadMapObjectBoundingBoxes();
    }

    /**
     * Sets the current game map for the player and updates related properties.
     *
     * @param map The TiledMap object representing the new game map.
     */
    public void setMap(@NotNull TiledMap map) {
        this.map = map; // Assign the provided map to the player's map

        // Get the first layer of the map (the bottom layer)
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);

        // Calculate the maximum height and width of the map
        final int maxHeight = layer.getHeight() * layer.getTileHeight();
        final int maxWidth = layer.getWidth() * layer.getTileWidth();

        // Calculate the scale of the map relative to the screen size
        mapScale = Math.max(Gdx.graphics.getWidth() / maxWidth, Gdx.graphics.getHeight() / maxHeight);

        // Create a sprite for the player and set its position, opacity, and size
        sprite = textureAtlas.createSprite("char3_towards");
        sprite.setAlpha(1);

        // Scale the sprite according to the map scale
        sprite.setSize(sprite.getWidth() * mapScale, sprite.getHeight() * mapScale);

        // Set the bounds of the player
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());

        // Clear the existing bounding boxes of the map objects
        tileObjectBoundingBoxes.clear();

        // Load the bounding boxes of the new map objects
        loadMapObjectBoundingBoxes();
    }

    /**
     * Sets the current game map for the player, updates related properties, and sets the player's position.
     *
     * @param map The TiledMap object representing the new game map.
     * @param startPosition The Vector2 object representing the new position of the player on the map.
     */
    public void setMap(@NotNull TiledMap map, Vector2 startPosition) {
        setMap(map);
        setPosition(startPosition);
    }

    /**
     * Enum representing the possible movements of the player.
     * It includes UP, DOWN, LEFT, RIGHT.
     * BOOST is also included to represent the boost movement (faster movement).
     * Each movement has a boolean state indicating whether it is currently active or not.
     */
    @Getter
    private enum Movement {
        UP, DOWN, LEFT, RIGHT, BOOST;

        /**
         * Boolean state of the movement.
         * If true, the movement is currently active.
         * If false, the movement is not active.
         */
        private boolean is;

        /**
         * Sets the state of the movement.
         *
         * @param is The new state of the movement. True if the movement is active, false otherwise.
         */
        void set(boolean is) {
            this.is = is;
        }
    }

    /**
     * Moves the player's sprite on the game map.
     * The movement is based on the current active movements (UP, DOWN, LEFT, RIGHT) and BOOST.
     * The sprite's position is updated and the player's bounds are set to the new position.
     * The sprite cannot move outside the bounds of the game map.
     */
    public void move() {
        // Get the first layer of the map (the bottom layer)
        final TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);

        // Calculate the maximum height and width of the map
        final int maxHeight = layer.getHeight() * layer.getTileHeight();
        final int maxWidth = layer.getWidth() * layer.getTileWidth();

        // Calculate the maximum height and width of the map scaled to the screen size
        final float maxHeightScaled = maxHeight * mapScale;
        final float maxWidthScaled = maxWidth * mapScale;

        // Calculate the amount to move the sprite by
        // If the BOOST movement is active, the sprite moves twice as fast
        final float amount = (Movement.BOOST.is ? 2 : 1) * mapScale;

        // Move the sprite up if the UP movement is active and the sprite is not at the top of the map
        if (Movement.UP.is && (sprite.getY() + sprite.getHeight() < maxHeightScaled)) {
            sprite.setRegion(SPRITEAWAYREGION);
            sprite.translateY(amount);
            setY(sprite.getY());
        }

        // Move the sprite down if the DOWN movement is active and the sprite is not at the bottom of the map
        if (Movement.DOWN.is && (sprite.getY() > 0)) {
            sprite.setRegion(SPRITETOWARDSREGION);
            sprite.translateY(-amount);
            setY(sprite.getY());
        }

        // Move the sprite left if the LEFT movement is active and the sprite is not at the left edge of the map
        if (Movement.LEFT.is && (sprite.getX() > 0)) {
            sprite.setRegion(SPRITELEFTREGION);
            sprite.translateX(-amount);
            setX(sprite.getX());
        }

        // Move the sprite right if the RIGHT movement is active and the sprite is not at the right edge of the map
        if (Movement.RIGHT.is && (sprite.getX() + sprite.getWidth() < maxWidthScaled)) {
            sprite.setRegion(SPRITELEFTREGION);
            sprite.setFlip(true, false);
            sprite.translateX(amount);
            setX(sprite.getX());
        }
    }

    /**
     * Returns the center position of the player's sprite on the game map.
     * This is calculated as the sprite's position plus half its width and height.
     *
     * @return A new Vector2 object representing the center position of the sprite.
     */
    @Contract(pure = true)
    public Vector2 getCenter() {
        return new Vector2(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2);
    }

    /**
     * Enum representing the possible transitions for the player.
     * It includes NEW_MAP and ACTIVITY transitions.
     * NEW_MAP is used when the player transitions to a new map.
     * ACTIVITY is used when the player does an activity on the map.
     */
    public enum Transition {
        NEW_MAP, ACTIVITY
    }

    /**
     * A HashMap storing the bounding boxes of the map objects.
     * The key is a MapObject and the value is a BoundingBox.
     * This is used to store the bounding boxes of the map objects for collision detection.
     */
    private final HashMap<MapObject, BoundingBox> tileObjectBoundingBoxes = new HashMap<>();

    /**
     * Returns the bounding box of a given map object.
     * The bounding box is calculated based on the object's properties (x, y, width, height) scaled to the map scale.
     * The bounding box is represented by two Vector3 objects (pos1 and pos2) which are the bottom-left and top-right corners of the bounding box respectively.
     *
     * @param object The MapObject for which the bounding box is to be calculated.
     * @return A BoundingBox object representing the bounding box of the map object.
     */
    public BoundingBox getTileObjectBoundingBox(@NotNull MapObject object) {
        // Retrieve the properties of the map object
        float x = object.getProperties().get("x", Float.class); // x-coordinate of the map object
        float y = object.getProperties().get("y", Float.class); // y-coordinate of the map object
        float width = object.getProperties().get("width", Float.class); // width of the map object
        float height = object.getProperties().get("height", Float.class); // height of the map object

        // Scale the properties of the map object according to the map scale
        float xScaled = x * mapScale; // scaled x-coordinate of the map object
        float yScaled = y * mapScale; // scaled y-coordinate of the map object
        float widthScaled = width * mapScale; // scaled width of the map object
        float heightScaled = height * mapScale; // scaled height of the map object

        // Create two Vector3 objects representing the bottom-left and top-right corners of the bounding box
        Vector3 pos1 = new Vector3(xScaled, yScaled, 0); // bottom-left corner of the bounding box
        Vector3 pos2 = new Vector3(xScaled + widthScaled, yScaled + heightScaled, 0); // top-right corner of the bounding box

        // Return a new BoundingBox object representing the bounding box of the map object
        return new BoundingBox(pos1, pos2);
    }

    /**
     * Retrieves the game objects from the map layer named "gameObjects".
     *
     * @return MapObjects from the "gameObjects" layer of the map.
     */
    public MapObjects getMapObjects() {
        MapLayer gameObjects = map.getLayers().get("gameObjects"); // Get the "gameObjects" layer from the map
        return gameObjects.getObjects(); // Return the objects from the "gameObjects" layer
    }

    /**
     * Loads the bounding boxes of the actionable game objects from the map.
     * The bounding boxes are stored in the tileObjectBoundingBoxes HashMap.
     * The key is a MapObject and the value is a BoundingBox.
     * This method is typically called when a new map is set for the player.
     */
    public void loadMapObjectBoundingBoxes() {
        // Retrieve the game objects from the map
        MapObjects objects = getMapObjects();

        // Iterate over each game object
        for (MapObject object : objects) {
            // Check if the game object is actionable
            Boolean actionable = object.getProperties().get("actionable", Boolean.class);

            // If the game object is not actionable, skip it
            // If the actionable property is not set, or it is set to true, assume the game object is actionable
            if (Boolean.FALSE.equals(actionable)) continue;

            // Calculate the bounding box of the game object
            BoundingBox boundingBox = getTileObjectBoundingBox(object);

            // Store the bounding box in the tileObjectBoundingBoxes HashMap
            tileObjectBoundingBoxes.put(object, boundingBox);
        }
    }

    /**
     * Returns the current map object that the player's sprite is on.
     * This is determined by checking if the player's center position is within the bounding box of each map object.
     * If the player's sprite is not on any map object, null is returned.
     *
     * @return The MapObject that the player's sprite is currently on, or null if the sprite is not on any map object.
     */
    public @Nullable MapObject getCurrentMapObject() {
        // Iterate over each entry in the tileObjectBoundingBoxes HashMap
        for (Map.Entry<MapObject, BoundingBox> entry : tileObjectBoundingBoxes.entrySet()) {
            // Get the center position of the player's sprite
            Vector2 center = getCenter();

            // Check if the bounding box of the map object contains the center position of the player's sprite
            if (entry.getValue().contains(new Vector3(center.x, center.y, 0))) {
                // If it does, return the map object
                return entry.getKey();
            }
        }

        // If the player's sprite is not on any map object, return null
        return null;
    }

    /**
     * Sets the position of the player's sprite on the game map.
     * The sprite's position is updated and the player's bounds are set to the new position.
     *
     * @param position The Vector2 object representing the new position of the player on the map.
     */
    public void setPosition(@NotNull Vector2 position) {
        sprite.setPosition(position.x, position.y); // Set the sprite's position
        setPosition(position.x, position.y); // Set the player's bounds to the new position
    }

    /**
     * Checks if the player's sprite is currently on a transition tile.
     * A transition tile is a tile that has either the "isNewMap" or "isActivity" property set to true.
     * If the player's sprite is on a tile with the "isNewMap" property set to true, the NEW_MAP transition is returned.
     * If the player's sprite is on a tile with the "isActivity" property set to true, the ACTIVITY transition is returned.
     * If the player's sprite is not on any transition tile, null is returned.
     *
     * @return The Transition that the player's sprite is currently on, or null if the sprite is not on any transition tile.
     */
    public @Nullable Transition isInTransitionTile() {
        // Retrieve the current map object that the player's sprite is on
        MapObject tileObject = getCurrentMapObject();

        // If the player's sprite is not on any map object, return null
        if (tileObject == null) return null;

        // If the map object has the "isNewMap" property set to true, return the NEW_MAP transition
        if (Boolean.TRUE.equals(tileObject.getProperties().get("isNewMap", Boolean.class))) {
            return Transition.NEW_MAP;
        }
        // If the map object has the "isActivity" property set to true, return the ACTIVITY transition
        else if (Boolean.TRUE.equals(tileObject.getProperties().get("isActivity", Boolean.class))) {
            return Transition.ACTIVITY;
        }

        // If the map object does not have either the "isNewMap" or "isActivity" property set to true, return null
        return null;
    }

    /**
     * Draws the player's sprite on the screen.
     * This method is called every frame to update the sprite's position on the screen.
     * The sprite is drawn using the provided Batch object.
     * The parent's draw method is also called to ensure that other actors are drawn correctly.
     *
     * @param batch The Batch object used to draw the sprite.
     * @param parentAlpha The parent's alpha value, used to handle transparency.
     */
    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        sprite.draw(batch); // Draw the player's sprite
        super.draw(batch, parentAlpha); // Call the parent's draw method
    }

    /**
     * Handles the key press events for the player's movement.
     * This method is called when a key is pressed.
     * The player's movement is updated based on the key pressed.
     * The possible keys are W (up), S (down), A (left), D (right), and CONTROL_LEFT (boost).
     * If the key pressed is not one of these, the method returns false.
     * If the key pressed is one of these, the corresponding movement is set to active and the method returns true.
     *
     * @param keycode The integer value representing the key pressed. Use {@link Input.Keys} to get the key codes.
     * @return True if the key pressed corresponds to a movement, false otherwise.
     */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
            case Input.Keys.W:
                Movement.UP.set(true);
                break;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                Movement.DOWN.set(true);
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                Movement.LEFT.set(true);
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                Movement.RIGHT.set(true);
                break;
            case Input.Keys.CONTROL_LEFT:
                Movement.BOOST.set(true);
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Handles the key release events for the player's movement.
     * This method is called when a key is released.
     * The player's movement is updated based on the key released.
     * The possible keys are W (up), S (down), A (left), D (right), and CONTROL_LEFT (boost).
     * If the key released is not one of these, the method returns false.
     * If the key released is one of these, the corresponding movement is set to inactive and the method returns true.
     *
     * @param keycode The integer value representing the key released. Use {@link Input.Keys} to get the key codes.
     * @return True if the key released corresponds to a movement, false otherwise.
     */
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
            case Input.Keys.W:
                Movement.UP.set(false);
                break;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                Movement.DOWN.set(false);
                break;
            case Input.Keys.LEFT:
            case Input.Keys.A:
                Movement.LEFT.set(false);
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                Movement.RIGHT.set(false);
                break;
            case Input.Keys.CONTROL_LEFT:
                Movement.BOOST.set(false);
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * Handles the key typed event.
     * This method is called when a key is typed.
     * Currently, this method does not perform any action and always returns false.
     *
     * @param character The character of the key typed.
     * @return False, as this method does not perform any action.
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Handles the touch down event.
     * This method is called when the screen is touched.
     * Currently, this method does not perform any action and always returns false.
     *
     * @param screenX The x-coordinate of the touch event.
     * @param screenY The y-coordinate of the touch event.
     * @param pointer The pointer for the touch event.
     * @param button The button of the touch event.
     * @return False, as this method does not perform any action.
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Handles the touch up event.
     * This method is called when a touch on the screen is released.
     * Currently, this method does not perform any action and always returns false.
     *
     * @param screenX The x-coordinate of the touch event.
     * @param screenY The y-coordinate of the touch event.
     * @param pointer The pointer for the touch event.
     * @param button The button of the touch event.
     * @return False, as this method does not perform any action.
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Handles the touch cancelled event.
     * This method is called when a touch event is cancelled.
     * Currently, this method does not perform any action and always returns false.
     *
     * @param screenX The x-coordinate of the touch event.
     * @param screenY The y-coordinate of the touch event.
     * @param pointer The pointer for the touch event.
     * @param button The button of the touch event.
     * @return False, as this method does not perform any action.
     */
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Handles the touch dragged event.
     * This method is called when a touch on the screen is dragged.
     * Currently, this method does not perform any action and always returns false.
     *
     * @param screenX The x-coordinate of the touch event.
     * @param screenY The y-coordinate of the touch event.
     * @param pointer The pointer for the touch event.
     * @return False, as this method does not perform any action.
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Handles the mouse moved event.
     * This method is called when the mouse is moved.
     * Currently, this method does not perform any action and always returns false.
     *
     * @param screenX The x-coordinate of the mouse event.
     * @param screenY The y-coordinate of the mouse event.
     * @return False, as this method does not perform any action.
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Handles the scrolled event.
     * This method is called when the mouse wheel is scrolled.
     * Currently, this method does not perform any action and always returns false.
     *
     * @param amountX The amount of horizontal scroll.
     * @param amountY The amount of vertical scroll.
     * @return False, as this method does not perform any action.
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    /**
     * Disposes of the resources used by the player.
     * This method is called when the player is no longer needed, to free up memory.
     */
    public void dispose() {
        textureAtlas.dispose(); // Dispose of the TextureAtlas
        metrics.dispose(); // Dispose of the PlayerMetrics
    }

    /**
     * Sets the opacity of the player's sprite.
     * The opacity is a value between 0 (completely transparent) and 1 (completely opaque).
     *
     * @param opacity The opacity value to set. This should be a float between 0 and 1.
     */
    public void setOpacity(@Range(from = 0, to = 1) float opacity) {
        sprite.setAlpha(opacity);
    }
}
