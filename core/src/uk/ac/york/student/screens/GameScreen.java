package uk.ac.york.student.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.ac.york.student.GdxGame;
import uk.ac.york.student.assets.skins.SkinManager;
import uk.ac.york.student.assets.skins.Skins;
import uk.ac.york.student.assets.map.ActionMapObject;
import uk.ac.york.student.assets.map.ActivityMapObject;
import uk.ac.york.student.assets.map.MapManager;
import uk.ac.york.student.assets.map.TransitionMapObject;
import uk.ac.york.student.game.GameTime;
import uk.ac.york.student.game.activities.Activity;
import uk.ac.york.student.player.*;
import uk.ac.york.student.utils.MapOfSuppliers;
import uk.ac.york.student.utils.Pair;
import uk.ac.york.student.utils.StreamUtils;
import uk.ac.york.student.utils.Wait;

import javax.annotation.processing.SupportedSourceVersion;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@link GameScreen} class extends the {@link BaseScreen} class and implements the {@link InputProcessor} interface.
 * This class is responsible for handling the game screen and its related functionalities.
 * It includes methods for rendering the game screen, handling user inputs, managing game activities, and more.
 */
public class GameScreen extends BaseScreen implements InputProcessor {
    /**
     * The key code for the action key. This is used to trigger actions in the game.
     */
    private static final int ACTION_KEY = Input.Keys.E;
    /**
     * The key code for the exit key. This is used to open the game menu.
     */
    private static final int EXIT_KEY = Input.Keys.ESCAPE;
    /**
     * The stage for this game screen. This is where all the actors for the game are added.
     */
    @Getter
    private final Stage processor;

    /**
     * The player of the game. This is the main character that the user controls.
     */
    private final Player player;

    /**
     * The game time. This keeps track of the current time in the game.
     */
    private final GameTime gameTime;

    /**
     * The map for the game. This is loaded from the {@link MapManager} with {@link MapManager#getMaps()} then {@link MapOfSuppliers#getResult(Object)}.
     */
    private TiledMap map = MapManager.getMaps().getResult("map");

    /**
     * The scale of the map. This is used to adjust the size of the map to fit the screen.
     */
    private float mapScale;

    /**
     * The renderer for the map. This is used to draw the map on the screen.
     */
    private TiledMapRenderer renderer;

    /**
     * The skin for the game. This is used to style the game's UI elements.
     */
    private final Skin craftacularSkin = SkinManager.getSkins().getResult(Skins.CRAFTACULAR);

    /**
     * The table for the action UI. This is where the action label is added.
     */
    private final Table actionTable = new Table(craftacularSkin);

    /**
     * The table for the metrics UI. This is where the metrics labels and progress bars are added.
     */
    private final Table metricsTable = new Table();

    /**
     * The table for the time UI. This is where the time label and progress bar are added.
     */
    private final Table timeTable = new Table();

    /**
     * The label for the action UI. This displays the current action that the player can perform.
     */
    private final Label actionLabel = new Label("ENG1 Project. Super cool. (You will never see this)", craftacularSkin);

    /**
     * The label for the time UI. This displays the current time in the game.
     */
    private final Label timeLabel = new Label("You exist outside of the space-time continuum.", craftacularSkin);
    /**
     * Constructor for the {@link GameScreen} class.
     *
     * @param game The {@link GdxGame} instance that this screen is part of.
     */

    // Define a map to store counts of each activity type done in a day
//    private Map<Activity, Integer> dailyActivityCounts = new HashMap<>();
//
//    private Map<Activity, Integer> activitiesPerformedToday = new HashMap<>();

    private Map<Activity, Integer> activitiesPerformedToday = new HashMap<>();
    private Map<Activity, Integer> activityStreakCounts = new HashMap<>();

    private PlayerStreaks playerStreaks;

    public static int notStudiedCounter = 0;

    public GameScreen(GdxGame game) {
        super(game);

        playerStreaks = PlayerStreaks.getInstance();

        // Set up the tilemap
        // Note: cannot extract into a method because class variables are set as final

        //#region Load Tilemap
        // Get the first layer of the map
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        // Get the width and height of a tile
        int tileWidth = layer.getTileWidth();
        int tileHeight = layer.getTileHeight();
        // Calculate the scale of the map based on the screen size and tile size
        mapScale = Math.max(Gdx.graphics.getWidth() / (layer.getWidth() * tileWidth), Gdx.graphics.getHeight() / (layer.getHeight() * tileHeight));
        // Initialize the game time
        gameTime = new GameTime(mapScale);
        // Initialize the map renderer
        renderer = new OrthogonalTiledMapRenderer(map, mapScale);
        //#endregion

        // Initialize the starting point of the player
        Vector2 startingPoint = new Vector2(25, 25);
        // Get the layer of the map that contains game objects
        MapLayer gameObjectsLayer = map.getLayers().get("gameObjects");
        // Get all objects in the game objects layer
        MapObjects objects = gameObjectsLayer.getObjects();
        // Iterate over all objects to find the starting point
        for (MapObject object : objects) {
            if (!object.getName().equals("startingPoint")) continue;
            MapProperties properties = object.getProperties();
            if (!properties.containsKey("spawnpoint")) continue;
            Boolean spawnpoint = properties.get("spawnpoint", Boolean.class);
            if (spawnpoint == null || Boolean.FALSE.equals(spawnpoint)) continue;
            RectangleMapObject rectangleObject = (RectangleMapObject) object;
            Rectangle rectangle = rectangleObject.getRectangle();
            // Update the starting point based on the found object
            startingPoint = new Vector2(rectangle.getX() * mapScale, rectangle.getY() * mapScale);
            break;
        }

        // Initialize the player at the starting point
        player = new Player(map, startingPoint);

        // Initialize the stage and set it as the input processor
        processor = new Stage(new ScreenViewport());
        renderer.setView((OrthographicCamera) processor.getCamera());
        Gdx.input.setInputProcessor(processor);

        // Add a listener to the stage to handle key events
        processor.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return GameScreen.this.keyDown(keycode);
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                return GameScreen.this.keyUp(keycode);
            }
        });
    }

    /**
     * Changes the current map to a new map specified by the mapName parameter.
     * The screen fades out to black, then the new map is loaded and the screen fades back in.
     *
     * @param mapName The name of the new map to load.
     */
    public void changeMap(String mapName) {
        // make the screen black slowly
        processor.getRoot().getColor().a = 1;
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.fadeOut(0.5f));
        sequenceAction.addAction(Actions.run(() -> {
            // Dispose of the current map
            map.dispose();
            // Load the new map
            map = MapManager.getMaps().getResult(mapName);
            // Get the first layer of the new map
            TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
            // Get the width and height of a tile in the new map
            int tileWidth = layer.getTileWidth();
            int tileHeight = layer.getTileHeight();
            // Calculate the scale of the new map based on the screen size and tile size
            mapScale = Math.max(Gdx.graphics.getWidth() / (layer.getWidth() * tileWidth), Gdx.graphics.getHeight() / (layer.getHeight() * tileHeight));
            // Initialize the map renderer for the new map
            renderer = new OrthogonalTiledMapRenderer(map, mapScale);

            // Initialize the starting point of the player for the new map
            Vector2 startingPoint = new Vector2(25, 25);
            // Get the layer of the new map that contains game objects
            MapLayer gameObjectsLayer = map.getLayers().get("gameObjects");
            // Get all objects in the game objects layer
            MapObjects objects = gameObjectsLayer.getObjects();
            // Iterate over all objects to find the starting point
            for (MapObject object : objects) {
                if (object.getName() != null) {
                    if (!object.getName().equals("startingPoint")) continue;
                    MapProperties properties = object.getProperties();
                    if (!properties.containsKey("spawnpoint")) continue;
                    Boolean spawnpoint = properties.get("spawnpoint", Boolean.class);
                    if (spawnpoint == null || Boolean.FALSE.equals(spawnpoint)) continue;
                    RectangleMapObject rectangleObject = (RectangleMapObject) object;
                    Rectangle rectangle = rectangleObject.getRectangle();
                    // Update the starting point based on the found object
                    startingPoint = new Vector2(rectangle.getX() * mapScale, rectangle.getY() * mapScale);
                    break;
                }
            }

            // Set the new map and starting point for the player
            player.setMap(map, startingPoint);
            // Update the game time progress bar for the new map
            gameTime.updateProgressBar(mapScale);

            // Set the view of the map renderer to the camera of the stage
            renderer.setView((OrthographicCamera) processor.getCamera());

            // Set the input processor to the stage
            Gdx.input.setInputProcessor(processor);
        }));
        // Fade the screen back in
        sequenceAction.addAction(Actions.fadeIn(0.5f));
        // Add the sequence action to the root of the stage
        processor.getRoot().addAction(sequenceAction);
    }

    /**
     * This method is called when this screen becomes the current screen for the {@link GdxGame}.
     * It sets up the game UI, including the action table, metrics table, and time table.
     * It also updates the viewport of the stage.
     */
    @Override
    public void show() {
        // Get the width and height of the screen
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        // Get the batch for the stage and start it
        Batch batch = processor.getBatch();
        batch.begin();
        // Draw the player on the batch
        player.draw(batch, 1f);
        // End the batch
        batch.end();

        // Set up the action table
        actionTable.setFillParent(true);
        processor.addActor(actionTable);
        actionLabel.setVisible(false);
        actionTable.add(actionLabel);
        actionTable.bottom();
        actionTable.padBottom(10);

        // Set up the metrics table
        metricsTable.setFillParent(true);
        metricsTable.setWidth(500);
        processor.addActor(metricsTable);
        PlayerMetrics metrics = player.getMetrics();
        List<ProgressBar> playerMetrics = metrics.getMetrics().stream().map(PlayerMetric::getProgressBar).collect(Collectors.toList());
        List<String> metricLabels = metrics.getMetrics().stream().map(PlayerMetric::getLabel).collect(Collectors.toList());
        for (int i = 0; i < playerMetrics.size(); i++) {
            ProgressBar progressBar = playerMetrics.get(i);
            String label = metricLabels.get(i);
            metricsTable.add(new Label(label, craftacularSkin)).padRight(10).padBottom(10);
            metricsTable.add(progressBar).width(200).padBottom(10);
            metricsTable.row();
        }
        metricsTable.bottom().right();
        metricsTable.padBottom(10);
        metricsTable.padRight(20);

        // Set up the time table
        ProgressBar timeBar = gameTime.getProgressBar();
        String currentHour = getCurrentHourString();
        String currentDay = "Day " + (gameTime.getCurrentDay() + 1);
        String time = currentDay + " " + currentHour;
        timeTable.setFillParent(true);
        processor.addActor(timeTable);
        timeTable.setWidth(500);
        timeLabel.setText(time);
        timeTable.add(timeLabel);
        timeTable.row();
        timeTable.add(timeBar).width(500);
        timeTable.top();
        timeTable.padTop(10);

        // Update the viewport of the stage
        processor.getViewport().update((int) width, (int) height);
    }

    /**
     * This method returns a string representation of the current hour in the game.
     * The game starts at 8 AM and ends at 12 AM (midnight). The time is formatted as HH:MM AM/PM.
     * However, MM is always 00 because the game progresses in hourly increments.
     * If the game is at the end of the day, the method returns "00:00 - Time to sleep!".
     *
     * @return A string representing the current hour in the game.
     */
    @NotNull
    private String getCurrentHourString() {
        int currentHourNum = gameTime.getCurrentHour(); // Get the current hour number from the game time
        final int startHour = 8; // Define the start hour of the game
        final int midday = 12 - startHour; // Calculate the hour number for midday
        boolean isAm = currentHourNum < (12 - startHour); // Determine if the current time is AM
        String currentHour; // Initialize the string to hold the current hour
        if (!gameTime.isEndOfDay()) { // If it's not the end of the day
            // Calculate the current hour based on whether it's AM or PM
            currentHour = String.valueOf(isAm || currentHourNum == midday ? currentHourNum + startHour : currentHourNum - (12 - startHour)); // 9am start
            if (currentHour.length() == 1) currentHour = "0" + currentHour; // Add a leading zero if the hour is a single digit
            currentHour += ":00"; // Add the minutes (always 00)
            if (isAm) { // If it's AM
                currentHour += " AM"; // Add " AM" to the current hour
            } else { // If it's PM
                currentHour += " PM"; // Add " PM" to the current hour
            }
        } else { // If it's the end of the day
            currentHour = "00:00 - Time to sleep!"; // Set the current hour to "00:00 - Time to sleep!"
        }
        return currentHour; // Return the current hour
    }

    /**
     * An {@link AtomicReference} to an {@link ActionMapObject}. This object represents the current action that the player can perform.
     * It is nullable, meaning it can be null if there is no current action.
     * {@link AtomicReference} is used to ensure thread-safety when accessing and updating this variable.
     */
    private final AtomicReference<@Nullable ActionMapObject> currentActionMapObject = new AtomicReference<>(null);
    /**
     * This method is called every frame to render the game screen.
     * It clears the screen, updates the player's position, sets the opacity of the player and map layers,
     * calculates and sets the camera's position, updates the positions of the UI tables, renders the map,
     * draws the player, updates the action label, and updates and draws the stage.
     *
     * @param v The time in seconds since the last frame.
     */
    @Override
    public void render(float v) {
        // Set the blend function for the OpenGL context. This determines how new pixels are combined with existing pixels.
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Clear the screen. This wipes out all previous drawings and sets the screen to a blank state.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Set the active texture unit to texture unit 0. This is the default and most commonly used texture unit.
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

        // Set the clear color to black. This is the color that the screen is cleared to when glClear is called.
        Gdx.gl.glClearColor(0, 0, 0, 1);

        // Move the player. This updates the player's position based on their current velocity and the elapsed time since the last frame.
        player.move();

        // Set the opacity of the player. This determines how transparent the player is. A value of 1 means fully opaque, and a value of 0 means fully transparent.
        player.setOpacity(processor.getRoot().getColor().a);

        // Set the opacity of all layers in the map. This determines how transparent the layers are. A value of 1 means fully opaque, and a value of 0 means fully transparent.
        StreamUtils.parallelFromIterator(map.getLayers().iterator()).forEach(l -> l.setOpacity(processor.getRoot().getColor().a));

        // Get the first layer of the map. This is typically the background layer.
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);

        // Get the camera for the stage. This determines what part of the game world is visible on the screen.
        OrthographicCamera camera = (OrthographicCamera) processor.getCamera();

        // Calculate the player's position (center of the player's sprite)
        float playerCenterX = player.getX() + player.getWidth() / 2;
        float playerCenterY = player.getY() + player.getHeight() / 2;

        // Calculate the minimum and maximum x and y coordinates for the camera
        float cameraMinX = camera.viewportWidth / 2;
        float cameraMinY = camera.viewportHeight / 2;
        float cameraMaxX = (map.getProperties().get("width", Integer.class) * layer.getTileWidth() * mapScale) - cameraMinX;
        float cameraMaxY = (map.getProperties().get("height", Integer.class) * layer.getTileHeight() * mapScale) - cameraMinY;

        // Set the camera's position to the player's position, but constrained within the minimum and maximum x and y coordinates
        camera.position.set(Math.min(Math.max(playerCenterX, cameraMinX), cameraMaxX), Math.min(Math.max(playerCenterY, cameraMinY), cameraMaxY), 0);
        camera.update();

        // Set the positions of the action, metrics, and time tables. These are UI elements that display information to the player.
        actionTable.setPosition(camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2);
        metricsTable.setPosition(camera.position.x + camera.viewportWidth / 2 - metricsTable.getWidth(), camera.position.y - camera.viewportHeight / 2);
        timeTable.setPosition(camera.position.x - camera.viewportWidth / 2, camera.position.y + camera.viewportHeight / 2 - timeTable.getHeight());

        // Set the view of the map renderer to the camera. This determines what part of the map is drawn to the screen.
        renderer.setView(camera);

        // Render the map. This draws the map to the screen.
        renderer.render();

        // Get the batch for the stage. This is used to draw the player and other game objects.
        Batch batch = processor.getBatch();
        batch.begin();

        // Draw the player. This renders the player sprite to the screen.
        player.draw(batch, processor.getRoot().getColor().a);
        batch.end();

        // Check if the player is in a transition tile. If they are, update the action label to reflect the possible action.
        Player.Transition transitionTile = player.isInTransitionTile();
        if (transitionTile != null) {
            setActionLabel(transitionTile);
        } else {
            // If the player is not in a transition tile, hide the action label.
            currentActionMapObject.set(null);
            actionLabel.setVisible(false);
        }

        // Draw the stage. This renders all actors added to the stage, including the player and UI elements.
        processor.draw();

        // Update the stage. This updates the state of all actors added to the stage, including the player and UI elements.
        processor.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
    }

    /**
     * This method sets the action label based on the player's current transition tile.
     * It first gets the {@link ActionMapObject} associated with the transition tile and the player's current map object.
     * Then it constructs the action text based on the type of the {@link ActionMapObject}.
     * If the {@link ActionMapObject} is an {@link ActivityMapObject}, it checks if the player has enough time and resources to perform the activity.
     * If the player does not have enough time or resources, the action text is updated to reflect this.
     * Finally, the action text is set as the text of the action label, and the action label is made visible.
     *
     * @param transitionTile The player's current transition tile.
     */
    private void setActionLabel(Player.Transition transitionTile) {
        // Get the ActionMapObject associated with the transition tile and the player's current map object
        ActionMapObject actionMapObject = getActionMapObject(transitionTile, player.getCurrentMapObject());
        currentActionMapObject.set(actionMapObject);

        // Construct the action text based on the type of the ActionMapObject
        StringBuilder actionText = new StringBuilder(getActionText(actionMapObject));

        // Check if the ActionMapObject is an instance of ActivityMapObject
        if (actionMapObject instanceof ActivityMapObject) {
            // Cast the ActionMapObject to an ActivityMapObject
            ActivityMapObject activityMapObject = (ActivityMapObject) actionMapObject;

            // Get the required time for the activity
            int requiredTime = activityMapObject.getTime();

            // Get the type of the activity
            Activity activity = activityMapObject.getType();

            // Get a list of negative effects from the activity's effects
            // Negative effects are those that decrease a player metric
            List<Pair<PlayerMetrics.MetricType, PlayerMetrics.MetricEffect>> negativeEffects = activity.getEffects()
                .stream().filter(x -> x.getRight().equals(PlayerMetrics.MetricEffect.DECREASE))
                .collect(Collectors.toList());

            // Initialize a boolean to track if the player has enough resources for the activity
            boolean hasEnough = true;

            // Initialize a list to store the names of the metrics that the player does not have enough of
            List<String> negativeEffectNames = new ArrayList<>();

            // Iterate over the negative effects
            for (Pair<PlayerMetrics.MetricType, PlayerMetrics.MetricEffect> negativeEffect : negativeEffects) {
                // Get the type of the metric
                PlayerMetrics.MetricType metricType = negativeEffect.getLeft();

                // Get the amount by which the activity changes the metric
                float changeAmount = activityMapObject.getChangeAmount(metricType);

                // Get the current value of the metric for the player
                PlayerMetric metric = player.getMetrics().getMetric(metricType);
                float currentMetric = metric.get();

                // Check if the player has enough of the metric for the activity
                boolean tempEnough = currentMetric >= changeAmount;

                // Update the hasEnough boolean if the player does not have enough of the metric
                if (hasEnough) {
                    hasEnough = tempEnough;
                }

                // If the player does not have enough of the metric, add the metric's label to the list of negative effect names
                if (!tempEnough) {
                    negativeEffectNames.add(metric.getLabel());
                }
            }

            // Check if the player does not have enough time or resources to perform the activity
            // If it's the end of the day and the activity is not sleeping, set the action text to "Night owl, it's time to sleep!"
            if (gameTime.isEndOfDay() && !activity.equals(Activity.SLEEP)) {
                actionText = new StringBuilder("Night owl, it's time to sleep!");
            }
            // If the current hour plus the required time for the activity is greater than the length of the day and the activity is not sleeping,
            // set the action text to "You don't have enough time to do this activity."
            else if (gameTime.getCurrentHour() + requiredTime > GameTime.getDayLength() && !activity.equals(Activity.SLEEP)) {
                actionText = new StringBuilder("You don't have enough time to do this activity.");
            }
            // If there are negative effects and the player does not have enough resources and the activity is not sleeping,
            // set the action text to "You don't have enough [resource] to do this activity."
            else if (!negativeEffects.isEmpty() && !hasEnough && !activity.equals(Activity.SLEEP)) {
                actionText = new StringBuilder("You don't have enough ");
                // If there is only one resource the player does not have enough of, append the name of that resource to the action text
                if (negativeEffectNames.size() == 1) {
                    actionText.append(negativeEffectNames.get(0));
                }
                // If there are multiple resources the player does not have enough of, append each resource name to the action text,
                // separated by commas and an "and" before the last resource
                else {
                    for (int i = 0; i < negativeEffectNames.size(); i++) {
                        actionText.append(negativeEffectNames.get(i));
                        if (i == negativeEffectNames.size() - 2) {
                            actionText.append(" and ");
                        } else if (i < negativeEffectNames.size() - 2) {
                            actionText.append(", ");
                        }
                    }
                    actionText.append(" to do this activity.");
                }
            } else {
                // If the player has enough resources and time to perform the activity
                if (!activity.equals(Activity.SLEEP)) {
                    // If the activity is not sleeping, append the required time for the activity to the action text
                    actionText.append(" (").append(requiredTime).append(" hours)");
                } else {
                    // If the activity is sleeping
                    if (gameTime.isEndOfDays()) {
                        // If it's the last day of the game, append "End of the game!" to the action text
                        actionText.append(" (End of the game!)");
                    } else {
                        // If it's not the last day of the game, append "End of the day" to the action text
                        actionText.append(" (End of the day)");
                    }
                }
            }
        }

        // Set the action text as the text of the action label, and make the action label visible
        actionLabel.setText(actionText.toString());
        actionLabel.setVisible(true);
    }

    /**
     * This method constructs a string that represents the action text for a given {@link ActionMapObject}.
     * The action text is a string that instructs the player to press a certain key to perform an action.
     * The action is determined by the string representation of the {@link ActionMapObject}.
     *
     * @param actionMapObject The {@link ActionMapObject} for which to construct the action text.
     * @return A {@link String} representing the action text for the given {@link ActionMapObject}.
     */
    @NotNull
    private String getActionText(@NotNull ActionMapObject actionMapObject) {
        String actionText = "Press " + Input.Keys.toString(ACTION_KEY) + " to ";
        actionText += actionMapObject.getStr();
        return actionText;
    }

    /**
     * This method returns an {@link ActionMapObject} based on the player's current transition tile and the associated map object.
     * If the transition tile is an {@link Player.Transition#ACTIVITY}, it returns an {@link ActivityMapObject}.
     * If the transition tile is a {@link Player.Transition#NEW_MAP}, it returns a {@link TransitionMapObject}.
     * If the transition tile is neither an {@link Player.Transition#ACTIVITY} nor a {@link Player.Transition#NEW_MAP}, it throws an {@link IllegalStateException}.
     *
     * @param transitionTile The player's current transition tile.
     * @param tileObject The map object associated with the transition tile.
     * @return An {@link ActionMapObject} based on the transition tile and map object.
     * @throws IllegalStateException If the transition tile is neither an {@link Player.Transition#ACTIVITY} nor a {@link Player.Transition#NEW_MAP}.
     */
    @NotNull
    private static ActionMapObject getActionMapObject(Player.@NotNull Transition transitionTile, MapObject tileObject) {
        ActionMapObject actionMapObject;
        if (transitionTile.equals(Player.Transition.ACTIVITY)) {
            actionMapObject = new ActivityMapObject(tileObject);
        } else if (transitionTile.equals(Player.Transition.NEW_MAP)) {
            actionMapObject = new TransitionMapObject(tileObject);
        } else {
            throw new IllegalStateException("Unexpected value: " + transitionTile);
        }
        return actionMapObject;
    }

    /**
     * This method is called when the screen size changes. It resizes the game screen to fit the new screen size.
     * It recalculates the scale of the map based on the new screen size and tile size, and initializes the map renderer with the new map scale.
     * It also updates the viewport of the stage with the new screen width and height, and sets the camera's position to the player's position,
     * but constrained within the minimum and maximum x and y coordinates. Finally, it sets the view of the map renderer to the camera.
     *
     * @param screenWidth The new width of the screen.
     * @param screenHeight The new height of the screen.
     */
    @Override
    public void resize(int screenWidth, int screenHeight) {
        // Get the first layer of the map
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        // Get the width and height of a tile in the map
        int tileWidth = layer.getTileWidth();
        int tileHeight = layer.getTileHeight();
        // Calculate the scale of the map based on the screen size and tile size
        mapScale = Math.max(Gdx.graphics.getWidth() / (float)(layer.getWidth() * tileWidth), Gdx.graphics.getHeight() / (float)(layer.getHeight() * tileHeight));
        // Initialize the map renderer with the new map scale
        renderer = new OrthogonalTiledMapRenderer(map, mapScale);

        // Get the camera for the stage
        OrthographicCamera camera = (OrthographicCamera) processor.getCamera();
        // Set the viewport width and height of the camera to the screen width and height
        camera.viewportWidth = screenWidth;
        camera.viewportHeight = screenHeight;

        // Update the viewport of the stage with the new screen width and height
        processor.getViewport().update(screenWidth, screenHeight, true);

        // Calculate the player's position (center of the player's sprite)
        float playerCenterX = player.getX() + player.getWidth() / 2;
        float playerCenterY = player.getY() + player.getHeight() / 2;

        // Calculate the minimum and maximum x and y coordinates for the camera
        float cameraMinX = camera.viewportWidth / 2;
        float cameraMinY = camera.viewportHeight / 2;
        float cameraMaxX = (map.getProperties().get("width", Integer.class) * layer.getTileWidth() * mapScale) - cameraMinX;
        float cameraMaxY = (map.getProperties().get("height", Integer.class) * layer.getTileHeight() * mapScale) - cameraMinY;

        // Set the camera's position to the player's position, but constrained within the minimum and maximum x and y coordinates
        camera.position.set(Math.min(Math.max(playerCenterX, cameraMinX), cameraMaxX), Math.min(Math.max(playerCenterY, cameraMinY), cameraMaxY), 0);
        // Update the camera
        camera.update();

        // Set the view of the map renderer to the camera
        renderer.setView(camera);

        // Update the viewport of the stage with the new screen width and height
        processor.getViewport().update(screenWidth, screenHeight, true);
    }

    /**
     * This method is called when the game is paused.
     * Currently, it does not perform any actions when the game is paused.
     */
    @Override
    public void pause() {

    }

    /**
     * This method is called when the game is resumed from a paused state.
     * Currently, it does not perform any actions when the game is resumed.
     */
    @Override
    public void resume() {

    }

    /**
     * This method is called when the game screen is hidden or minimized.
     * Currently, it does not perform any actions when the game screen is hidden.
     */
    @Override
    public void hide() {

    }

    /**
     * This method is called when the game screen is being disposed of.
     * It disposes of the {@link GameScreen#map}, {@link GameScreen#processor}, {@link GameScreen#craftacularSkin}, and {@link GameScreen#player} to free up resources and prevent memory leaks.
     */
    @Override
    public void dispose() {
        map.dispose();
        processor.dispose();
        craftacularSkin.dispose();
        player.dispose();
    }

    /**
     * This method is called when a key is pressed down.
     * It first checks if the key pressed is the action key (defined as a constant).
     * If it is, it retrieves the current {@link ActionMapObject} (which represents the current action that the player can perform).
     * If the {@link ActionMapObject} is not null, it checks if it is an instance of {@link ActivityMapObject} or {@link TransitionMapObject}.
     * If it's an {@link ActivityMapObject}, it calls the {@link GameScreen#doActivity(ActivityMapObject)} method and returns its result.
     * If it's a {@link TransitionMapObject}, it calls the {@link GameScreen#doMapChange(TransitionMapObject)} method and returns its result.
     * If the {@link ActionMapObject} is neither an {@link ActivityMapObject} nor a {@link TransitionMapObject}, it throws an {@link IllegalStateException}.
     * If the key pressed is not the action key, it calls the {@link Player#keyDown(int)} method of the {@link Player} and returns its result.
     *
     * @param keycode The key code of the key that was pressed down.
     * @return A boolean indicating whether the key press was handled.
     * @throws IllegalStateException If the {@link ActionMapObject} is neither an {@link ActivityMapObject} nor a {@link TransitionMapObject}.
     */
    @Override
    public boolean keyDown(int keycode) {
        boolean playerKeyDown = player.keyDown(keycode);
        if (keycode == ACTION_KEY) {
            ActionMapObject actionMapObject = currentActionMapObject.get();
            if (actionMapObject != null) {
                if (actionMapObject instanceof ActivityMapObject) {
                    return doActivity((ActivityMapObject) actionMapObject);
                } else if (actionMapObject instanceof TransitionMapObject) {
                    return doMapChange((TransitionMapObject) actionMapObject);
                } else {
                    throw new IllegalStateException("Unexpected value: " + actionMapObject);
                }
            }
        } else if (keycode == EXIT_KEY) {
            Wait.async(400, TimeUnit.MILLISECONDS)
                    .thenRun(() -> {
                        Gdx.app.exit();
                    });
        }
        return playerKeyDown;
    }

    /**
     * This method is used to change the current map to a new map.
     * The new map is specified by the type of the provided {@link TransitionMapObject}.
     * After changing the map, it returns true to indicate that the map change was successful.
     *
     * @param actionMapObject The {@link TransitionMapObject} that contains the type of the new map.
     * @return A boolean indicating whether the map change was successful.
     */
    private boolean doMapChange(@NotNull TransitionMapObject actionMapObject) {
        changeMap(actionMapObject.getType());
        return true;
    }

    /**
     * This method is used to perform an activity in the game.
     * The activity is specified by the provided {@link ActivityMapObject}.
     * It first checks if the game is at the end of the day and if the activity is not sleeping, if so it returns false.
     * Then it checks if the current hour plus the required time for the activity is greater than the length of the day and if the activity is not sleeping, if so it returns false.
     * It then checks if the player has enough resources to perform the activity, if not it returns false.
     * If all checks pass, it performs the activity by changing the player's metrics based on the effects of the activity.
     * If the activity is sleeping, it resets the game time to the start of the next day.
     * Finally, it updates the time label with the current day and hour, and returns true to indicate that the activity was performed successfully.
     *
     * @param actionMapObject The {@link ActivityMapObject} that represents the activity to be performed.
     * @return A boolean indicating whether the activity was performed successfully.
     */
    private boolean doActivity(@NotNull ActivityMapObject actionMapObject) {
        // Get the type of the activity from the ActivityMapObject
        Activity type = actionMapObject.getType();


        // Check if the activity has already been performed today
//        if (activitiesPerformedToday.containsKey(type)) {
//            // Activity already performed today, return false
//            return false;
//        }
//
        // Increment the count for the activity in the dailyActivityCounts map
//        activitiesPerformedToday.put(type, 1);
        activitiesPerformedToday.put(type, activitiesPerformedToday.getOrDefault(type, 0) + 1);

        if (type == Activity.EAT && activitiesPerformedToday.getOrDefault(type, 0) == 3) {
            // Perform the action for eating three times
            // For example, trigger a scoring boost or any other action
            System.out.println("Player has eaten three times today!");
        }

        int currentDayCounter = gameTime.getCurrentDay();

        // Check if the game is at the end of the day and if the activity is not sleeping
        // If it is, return false to indicate that the activity cannot be performed
        if (gameTime.isEndOfDay() && !type.equals(Activity.SLEEP)) return false;

        // Get the required time for the activity from the ActivityMapObject
        int requiredTime = actionMapObject.getTime();

        // Check if the current hour plus the required time for the activity is greater than the length of the day
        // and if the activity is not sleeping
        // If it is, return false to indicate that the activity cannot be performed
        if (gameTime.getCurrentHour() + requiredTime > GameTime.getDayLength() && !type.equals(Activity.SLEEP)) return false;

        // Get the effects of the activity from the ActivityMapObject
        // These effects represent how the activity will change the player's metrics
        List<Pair<PlayerMetrics.MetricType, PlayerMetrics.MetricEffect>> effects = type.getEffects();

        // Filter the effects to get only the negative effects
        // Negative effects are those that decrease a player metric
        List<Pair<PlayerMetrics.MetricType, PlayerMetrics.MetricEffect>> negativeEffects = effects.stream().filter(x -> x.getRight().equals(PlayerMetrics.MetricEffect.DECREASE)).collect(Collectors.toList());

        // Get the player's current metrics
        PlayerMetrics metrics = player.getMetrics();
        // Check if there are any negative effects from the activity
        if (!negativeEffects.isEmpty()) {
            // Initialize a boolean to track if the player has enough resources for the activity
            boolean hasEnough = true;

            // Iterate over the negative effects
            for (Pair<PlayerMetrics.MetricType, PlayerMetrics.MetricEffect> negativeEffect : negativeEffects) {
                // Get the type of the metric
                PlayerMetrics.MetricType metricType = negativeEffect.getLeft();

                // Get the amount by which the activity changes the metric
                float changeAmount = actionMapObject.getChangeAmount(metricType);

                // Get the current value of the metric for the player
                PlayerMetric metric = metrics.getMetric(metricType);
                float currentMetric = metric.get();

                // Check if the player has enough of the metric for the activity
                hasEnough = currentMetric >= changeAmount;

                // If the player does not have enough of the metric, break the loop
                if (!hasEnough) break;
            }

            // If the player does not have enough resources to perform the activity, return false
            if (!hasEnough) return false;
        }


        // Check if the activity is sleeping
        if (type.equals(Activity.SLEEP)) {
            // Check streaks after completing the day's activities
            checkForStreaks();




        if (activitiesPerformedToday.get(type) == 1) {
            playerStreaks.incrementStreak(type, currentDayCounter);
        }

        // Check if the activity is sleeping
        if (type.equals(Activity.SLEEP)) {

            if (activitiesPerformedToday.getOrDefault(Activity.STUDY, 0) == 0) {
                notStudiedCounter += 1;
                // Perform the action for eating three times
                // For example, trigger a scoring boost or any other action
                System.out.println("Player hasn't studied!");
            }

//            activitiesPerformedToday.clear();

            // Get all player metrics
            List<PlayerMetric> allMetrics = metrics.getMetrics();
            // Iterate over all player metrics
            for (PlayerMetric m : allMetrics) {
                // Increase the total of each metric by its current value
                m.increaseTotal(m.get());
            }


            // Check if the current day plus one equals the total number of days
            if (gameTime.isEndOfDays()) {
                // If it does, transition the screen to the end screen and return true
                game.transitionScreen(Screens.END, player, true, 0.5f);
                return true;
            } else {
                // If it doesn't, increment the current day
                gameTime.incrementDay();
            }
        } else {
            // If the activity is not sleeping, increment the current hour by the required time for the activity
            gameTime.incrementHour(requiredTime);
        }
        // Iterate over the effects of the activity
        for (Pair<PlayerMetrics.MetricType, PlayerMetrics.MetricEffect> effect : effects) {
            // Get the type of the metric from the effect
            PlayerMetrics.MetricType metricType = effect.getLeft();
            // Get the effect on the metric (increase or decrease)
            PlayerMetrics.MetricEffect metricEffect = effect.getRight();
            // Get the amount by which the activity changes the metric
            float changeAmount = actionMapObject.getChangeAmount(metricType);
            // Apply the effect to the metric
            metrics.changeMetric(metricType, metricEffect, changeAmount);
        }
        // Get the current hour as a string using the getCurrentHourString method
        String currentHour = getCurrentHourString();

        // Construct a string representing the current day by adding 1 to the current day from gameTime
        String currentDay = "Day " + (gameTime.getCurrentDay() + 1);

        // Construct a string representing the current time by concatenating the current day and current hour
        String time = currentDay + " " + currentHour;

        // Set the text of the timeLabel to the constructed time string
        timeLabel.setText(time);

//        updateStreakCount(type);



        // Return true indicating the operation was successful
        return true;
    }

//    private void updateStreakCount(Activity activity) {
//        // Increment streak count for the activity if performed consecutively
//        if (activitiesPerformedToday.getOrDefault(activity, 0) == 1) {
//            playerStreaks.incrementStreak(activity);
//
//        }
//    }

    private void checkForStreaks() {
        // Check for streaks using the PlayerStreaks instance
        if (playerStreaks.getStreakCount(Activity.SLEEP) >= 4) {
            // Award additional points for achieving the study streak
            System.out.println("Sleep achievement unlocked!");
//            System.out.println(playerStreaks.getStreakCount(Activity.SLEEP));
        }

        if (playerStreaks.getStreakCount(Activity.ENTERTAIN) >= 4) {
            // Award additional points for achieving the entertainment streak
            System.out.println("Entertainment achievement unlocked!");
        }
    }




    /**
     * This method is called when a key is released.
     * It delegates the key release event to the player object.
     *
     * @param keycode The key code of the key that was released.
     * @return A boolean indicating whether the key release was handled by the player.
     */
    @Override
    public boolean keyUp(int keycode) {
        return player.keyUp(keycode);
    }

    /**
     * This method is called when a key is typed.
     * Currently, it does not perform any actions when a key is typed.
     *
     * @param character The character of the key that was typed.
     * @return A boolean indicating whether the key typing was handled. Always returns false.
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * This method is called when a touch down event occurs.
     * Currently, it does not perform any actions when a touch down event occurs.
     *
     * @param screenX The x-coordinate of the touch down event.
     * @param screenY The y-coordinate of the touch down event.
     * @param pointer The pointer for the touch down event.
     * @param button The button for the touch down event.
     * @return A boolean indicating whether the touch down event was handled. Always returns false.
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * This method is called when a touch up event occurs.
     * Currently, it does not perform any actions when a touch up event occurs.
     *
     * @param screenX The x-coordinate of the touch up event.
     * @param screenY The y-coordinate of the touch up event.
     * @param pointer The pointer for the touch up event.
     * @param button The button for the touch up event.
     * @return A boolean indicating whether the touch up event was handled. Always returns false.
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * This method is called when a touch cancelled event occurs.
     * Currently, it does not perform any actions when a touch cancelled event occurs.
     *
     * @param screenX The x-coordinate of the touch cancelled event.
     * @param screenY The y-coordinate of the touch cancelled event.
     * @param pointer The pointer for the touch cancelled event.
     * @param button The button for the touch cancelled event.
     * @return A boolean indicating whether the touch cancelled event was handled. Always returns false.
     */
    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * This method is called when a touch dragged event occurs.
     * Currently, it does not perform any actions when a touch dragged event occurs.
     *
     * @param screenX The x-coordinate of the touch dragged event.
     * @param screenY The y-coordinate of the touch dragged event.
     * @param pointer The pointer for the touch dragged event.
     * @return A boolean indicating whether the touch dragged event was handled. Always returns false.
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * This method is called when a mouse moved event occurs.
     * Currently, it does not perform any actions when a mouse moved event occurs.
     *
     * @param screenX The x-coordinate of the mouse moved event.
     * @param screenY The y-coordinate of the mouse moved event.
     * @return A boolean indicating whether the mouse moved event was handled. Always returns false.
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * This method is called when a scrolled event occurs.
     * Currently, it does not perform any actions when a scrolled event occurs.
     *
     * @param amountX The amount of horizontal scroll.
     * @param amountY The amount of vertical scroll.
     * @return A boolean indicating whether the scrolled event was handled. Always returns false.
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
