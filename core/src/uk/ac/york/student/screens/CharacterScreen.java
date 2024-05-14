package uk.ac.york.student.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import uk.ac.york.student.GdxGame;
import uk.ac.york.student.game.activities.Activity;
import uk.ac.york.student.player.Player;
import uk.ac.york.student.player.PlayerMetric;
import uk.ac.york.student.player.PlayerMetrics;
import uk.ac.york.student.player.PlayerStreaks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import uk.ac.york.student.GdxGame;
import uk.ac.york.student.assets.skins.SkinManager;
import uk.ac.york.student.assets.skins.Skins;
import uk.ac.york.student.audio.sound.GameSound;
import uk.ac.york.student.audio.sound.SoundManager;
import uk.ac.york.student.audio.sound.Sounds;
import uk.ac.york.student.settings.DebugScreenPreferences;
import uk.ac.york.student.settings.GamePreferences;
import uk.ac.york.student.settings.MainMenuCloudsPreferences;
import uk.ac.york.student.utils.Wait;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

import java.util.List;

/**
 * UPDATED FROM ASSESSMENT 1
 *  Functionality for
 *  -Allowing a user to select a character to play with
 *  -Changes associated sprite to selected
 *  May 30, 2024
 */

/**
 * The Character class extends the BaseScreen class and represents the main menu screen of the game.
 * Represents the character selection screen in the game.
 * This screen allows players to select a character and provides various visual elements and animations.
 */
@Getter
public class CharacterScreen extends BaseScreen {

    private final Stage processor;

    private final boolean shouldFadeIn;
    private final float fadeInTime;


    private final Texture backgroundTexture = new Texture(Gdx.files.internal("images/MapOverview.png"));
    private final Texture vignetteTexture = new Texture(Gdx.files.internal("images/Vignette.png"));
    private final Texture cookeLogo = new Texture(Gdx.files.internal("images/logo/b/logo.png"));
    private final Texture clouds = new Texture(Gdx.files.internal("images/CloudsFormatted.png"));
    private final Image cloudsImage = new Image(new TextureRegionDrawable(new TextureRegion(clouds)));
    private final Skin craftacularSkin = SkinManager.getSkins().getResult(Skins.CRAFTACULAR);
    private final GameSound buttonClick = SoundManager.getSupplierSounds().getResult(Sounds.BUTTON_CLICK);
    private final boolean cloudsEnabled = ((MainMenuCloudsPreferences) GamePreferences.MAIN_MENU_CLOUDS.getPreference()).isEnabled();
    private final float cloudsSpeed = ((MainMenuCloudsPreferences) GamePreferences.MAIN_MENU_CLOUDS.getPreference()).getSpeed();

    // Add fields for character selection
    private final Texture character1Texture = new Texture(Gdx.files.internal("images/character1.png"));
    private final Texture character2Texture = new Texture(Gdx.files.internal("images/character2.png"));
    private final Texture character3Texture = new Texture(Gdx.files.internal("images/character3.png"));

    private final Image character1Image = new Image(character1Texture);
    private final Image character2Image = new Image(character2Texture);
    private final Image character3Image = new Image(character3Texture);

    private final TextButton selectCharacter1 = new TextButton("Select Character 1", craftacularSkin);
    private final TextButton selectCharacter2 = new TextButton("Select Character 2", craftacularSkin);
    private final TextButton selectCharacter3 = new TextButton("Select Character 3", craftacularSkin);

    private int selectedCharacter; // Variable to store the currently selected character


        public CharacterScreen(GdxGame game) {
        super(game);
        throw new UnsupportedOperationException("This constructor is not supported (must pass in object args!)");
    }
    public CharacterScreen(GdxGame game, boolean shouldFadeIn, float fadeInTime) {
        this(game, shouldFadeIn, fadeInTime, new Object[]{}); // Provide an empty array for args
    }

    /**
     * Constructs a CharacterScreen with the specified game, fade-in settings, and arguments.
     * Initializes the stage processor, input processor, fade-in settings, and executor service.
     * Sets the default selected character to 1.
     *
     * @param game The game instance.
     * @param shouldFadeIn Determines if the screen should fade in on display.
     * @param fadeInTime The duration of the fade-in animation in seconds.
     * @param args The arguments passed to the character screen (currently not used).
     */
    public CharacterScreen(GdxGame game, boolean shouldFadeIn, float fadeInTime, Object @NotNull [] args) {
        super(game);
        processor = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(processor);

        this.shouldFadeIn = shouldFadeIn;
        this.fadeInTime = fadeInTime;

        // Initialize the executorService
        executorService = Executors.newSingleThreadScheduledExecutor();

        selectedCharacter = 1;

    }




    /**
     * This method is currently not used.
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * Applies a zoom and move action to the specified actor based on the given direction.
     * The actor will zoom to twice its original size and move in the specified direction over a duration of one second.
     *
     * @param actor The actor to which the zoom and move actions will be applied.
     * @param direction The direction in which the actor will move. Must be one of the directions defined in MainMenuScreen.Direction.
     */
    public void zoomAndMove(@NotNull Actor actor, @NotNull MainMenuScreen.Direction direction) {
        // Create a new Vector2 instance to store the movement vector.
        Vector2 vector = new Vector2();

        // Set the scale factor for the zoom effect.
        float scale = 2;

        // Set the duration of the zoom and move actions in seconds.
        int duration = 1;

        // Set the distance of the movement in units.
        int distance = 800;

        // Determine the movement vector based on the specified direction.
        switch (direction) {
            case UP:
                vector.set(0, distance);
                break;
            case DOWN:
                vector.set(0, -distance);
                break;
            case LEFT:
                vector.set(-distance, 0);
                break;
            case RIGHT:
                vector.set(distance, 0);
                break;
        }

        // Set the origin of the actor to its center. This is necessary for the zoom effect to work correctly.
        actor.setOrigin(Align.center);

        // Add the zoom and move actions to the actor. These actions will be performed simultaneously.
        actor.addAction(Actions.parallel(
                Actions.scaleTo(scale, scale, duration),  // Scale the actor to the specified scale factor.
                Actions.moveBy(vector.x, vector.y, duration)  // Move the actor by the specified vector.
        ));
    }

    private final AtomicReference<Float> alpha = new AtomicReference<>(1f);

    private final ScheduledExecutorService executorService;

    /**
     * Applies a fade-out effect by gradually reducing the alpha value over a specified duration.
     * The alpha value is reduced in small increments at fixed intervals until it reaches zero.
     */
    public void fadeOut() {
        // Set the duration of the fade out effect in seconds.
        int duration = 1;

        // Calculate the total number of reductions in the alpha value that will be performed during the fade out effect.
        long totalReductions = (long) (1/0.01);

        // Calculate the period between each reduction in the alpha value in milliseconds.
        long period = (duration * 1000) / totalReductions;

        // Set the time unit for the duration of the fade out effect.
        TimeUnit timeUnit = TimeUnit.SECONDS;

        // Schedule a task to be executed at fixed intervals that reduces the alpha value by 0.01.
        // The task is scheduled to start immediately and to be executed every 'period' milliseconds.
        ScheduledFuture<?> scheduledFuture = executorService.scheduleAtFixedRate(() -> alpha.updateAndGet(v -> v <= 0 ? 0 : v - 0.01f), 0, period, TimeUnit.MILLISECONDS);

        // Schedule a task to be executed after the specified duration that cancels the updates to the alpha value.
        executorService.schedule(() -> scheduledFuture.cancel(true), duration, timeUnit);
    }

    /**
     * Sets up and displays the character selection screen.
     * If fade-in is enabled, it sets the initial alpha of the root actor to 0 and adds a fade-in action.
     * Adds character selection images and buttons to the stage, along with the Cooke logo and exit button.
     * Adds listeners to handle button clicks for selecting characters and exiting the application.
     * Adjusts the size of the background and cloud images based on the screen size.
     */
    public void show() {
        // If shouldFadeIn is true, set the alpha of the root actor to 0 and add a fade-in action to it.
        if (shouldFadeIn) {
            processor.getRoot().getColor().a = 0;
            processor.getRoot().addAction(fadeIn(fadeInTime));
        }

        // Create a new Table and add it to the stage.
        Table table = new Table();
        Table characterSelectionTable = new Table();
        Table SelectionTable = new Table();

        // Add character images to the character selection table
        characterSelectionTable.add(character1Image).size(200).uniformX();
        characterSelectionTable.add(character2Image).size(200).uniformX();
        characterSelectionTable.add(character3Image).size(200).uniformX();
        characterSelectionTable.row();
        // Add select button centered below the character images

        SelectionTable.add(selectCharacter1).colspan(1).pad(10);
        SelectionTable.add(selectCharacter2).colspan(1).pad(20);
        SelectionTable.add(selectCharacter3).colspan(1).pad(10);



        table.setFillParent(true);
        // If debug screen preferences are enabled, set the table to debug mode.
        if (((DebugScreenPreferences) GamePreferences.DEBUG_SCREEN.getPreference()).isEnabled()) {
            table.setDebug(true);
        }
        processor.addActor(table);

        // Create the buttons and the logo image for the main menu screen.
        Label TitleLabel = new Label("Select a character: ", craftacularSkin);

        TextButton exitButton = new TextButton("Exit", craftacularSkin);
        Image cookeLogoImage = new Image(cookeLogo);


        // Add the buttons and the logo image to the table.
        table.add(cookeLogoImage).fillX().uniformX().pad(0, 0, 150, 0);
        table.row();
        table.add(TitleLabel).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        // Add character selection UI to the table
        // Add character selection table to the main table
        table.add(characterSelectionTable).colspan(3).fillX().row(); // Spanning three columns of the main table
        table.row();

        table.add(SelectionTable).colspan(3).row(); // Spanning three columns of the main table
        table.row();
        table.add(exitButton).colspan(3).uniformX();


        // Add listeners to the buttons.
        // The exit button disposes the button click sound and exits the application after a delay of 400 milliseconds.
        exitButton.addListener(new ChangeListener() {
            /**
             * This method is triggered when a change event occurs on the actor, in this case, when the exit button is clicked.
             * It first plays the button click sound.
             * Then, it schedules a task to be executed after a delay of 400 milliseconds.
             * The scheduled task disposes the button click sound and exits the application.
             *
             * @param event The {@link com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent} triggered by the actor. This is not used in the method.
             * @param actor The actor that triggered the {@link com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent}. This is not used in the method.
             */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonClick.play();
                Wait.async(400, TimeUnit.MILLISECONDS)
                        .thenRun(() -> {
                            buttonClick.dispose();
                            Gdx.app.exit();
                        });
            }
        });

        // Sets character choice to 1, also adds zooming and moving effect when screen closes
        selectCharacter1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Player.setSelectedCharacter(1); // Set selected character
                // Change the screen to the main game screen
                SoundManager.getSounds().get(Sounds.BUTTON_CLICK).play();
                zoomAndMove(character1Image, MainMenuScreen.Direction.UP);
                zoomAndMove(character2Image, MainMenuScreen.Direction.UP);
                zoomAndMove(character3Image, MainMenuScreen.Direction.UP);
                zoomAndMove(selectCharacter1, MainMenuScreen.Direction.DOWN);
                zoomAndMove(selectCharacter2, MainMenuScreen.Direction.DOWN);
                zoomAndMove(selectCharacter3, MainMenuScreen.Direction.DOWN);
                zoomAndMove(exitButton, MainMenuScreen.Direction.DOWN);
                zoomAndMove(cookeLogoImage, MainMenuScreen.Direction.UP);
                fadeOut();
                Wait.async(500, TimeUnit.MILLISECONDS)
                        .thenRun(() -> Gdx.app.postRunnable(() -> game.setScreen(Screens.GAME)));
            }
        });

        // Sets character choice to 2, also adds zooming and moving effect when screen closes
        selectCharacter2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Player.setSelectedCharacter(2);
                SoundManager.getSounds().get(Sounds.BUTTON_CLICK).play();
                zoomAndMove(character1Image, MainMenuScreen.Direction.UP);
                zoomAndMove(character2Image, MainMenuScreen.Direction.UP);
                zoomAndMove(character3Image, MainMenuScreen.Direction.UP);
                zoomAndMove(selectCharacter1, MainMenuScreen.Direction.DOWN);
                zoomAndMove(selectCharacter2, MainMenuScreen.Direction.DOWN);
                zoomAndMove(selectCharacter3, MainMenuScreen.Direction.DOWN);
                zoomAndMove(exitButton, MainMenuScreen.Direction.DOWN);
                zoomAndMove(cookeLogoImage, MainMenuScreen.Direction.UP);
                fadeOut();
                Wait.async(500, TimeUnit.MILLISECONDS)
                        .thenRun(() -> Gdx.app.postRunnable(() -> game.setScreen(Screens.GAME)));
            }
        });

        // Sets character choice to 3, also adds zooming and moving effect when screen closes
        selectCharacter3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Player.setSelectedCharacter(3);
                SoundManager.getSounds().get(Sounds.BUTTON_CLICK).play();
                zoomAndMove(character1Image, MainMenuScreen.Direction.UP);
                zoomAndMove(character2Image, MainMenuScreen.Direction.UP);
                zoomAndMove(character3Image, MainMenuScreen.Direction.UP);
                zoomAndMove(selectCharacter1, MainMenuScreen.Direction.DOWN);
                zoomAndMove(selectCharacter2, MainMenuScreen.Direction.DOWN);
                zoomAndMove(selectCharacter3, MainMenuScreen.Direction.DOWN);
                zoomAndMove(exitButton, MainMenuScreen.Direction.DOWN);
                zoomAndMove(cookeLogoImage, MainMenuScreen.Direction.UP);
                fadeOut();
                Wait.async(500, TimeUnit.MILLISECONDS)
                        .thenRun(() -> Gdx.app.postRunnable(() -> game.setScreen(Screens.GAME)));
            }
        });

        // The preferences button plays the button click sound and transitions to the preferences screen.


        // Declare variables for width and height
        float width;
        float height;

        // Get the width and height of the screen
        float ratio = getRatio();

        // Calculate the new width and height for the background texture based on the ratio
        width = backgroundTexture.getWidth() * ratio;
        height = backgroundTexture.getHeight() * ratio;

        // Set the size of the clouds image to the new width and height
        cloudsImage.setSize(width, height);

    }

    public int getCharacterSelection() {
        return selectedCharacter;
    }

    /**
     * Calculates and returns the ratio to scale the background texture to fit the screen while maintaining its aspect ratio.
     * It retrieves the width and height of the screen and the background texture,
     * then calculates the ratio of the screen dimensions to the texture dimensions,
     * and returns the maximum of these ratios to ensure the texture covers the screen without distortion.
     *
     * @return The maximum ratio of screen dimensions to background texture dimensions.
     */
    private float getRatio() {
        // Retrieve the width of the screen
        float screenWidth = Gdx.graphics.getWidth();
        // Retrieve the height of the screen
        float screenHeight = Gdx.graphics.getHeight();

        // Calculate the ratio of the screen width to the background texture width
        float widthRatio = screenWidth / backgroundTexture.getWidth();

        // Calculate the ratio of the screen height to the background texture height
        float heightRatio = screenHeight / backgroundTexture.getHeight();

        // Return the maximum ratio to maintain the aspect ratio of the background texture
        return Math.max(widthRatio, heightRatio);
    }

    private float cycle = 0;

    /**
     * Renders the character screen, including the background texture, animated clouds, and vignette effect.
     * Clears the screen with a black color, enables blending for alpha transparency, and draws various elements in order.
     * Rendering steps include:
     *      Clearing the screen to a black color.
     *      Enabling alpha blending.
     *      Drawing the background texture while maintaining its aspect ratio.
     *      If clouds are enabled, animating and drawing them.
     *      Drawing the vignette texture over the entire screen.
     *      Updating and drawing the stage's actors.
     *
     *
     * @param delta The time in seconds since the last render.
     */
    public void render(float delta) {
        // Set the clear color to black and clear the screen.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Enable blending and set the blend function to standard alpha blending.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // Get the batch from the stage's processor.
        Batch batch = processor.getBatch();
        batch.begin();

        // Calculate the ratio to maintain the aspect ratio of the background texture.
        float ratio = getRatio();

        // Calculate the new width and height for the background texture based on the ratio.
        float width = backgroundTexture.getWidth() * ratio;
        float height = backgroundTexture.getHeight() * ratio;

        // Draw the background texture.
        batch.draw(backgroundTexture, 0, 0, width, height);

        // If clouds are enabled, animate the clouds.
        if (cloudsEnabled) {
            // If the cycle exceeds the width of the screen, reset it to 0.
            // Otherwise, increment the cycle by the speed of the clouds.
            if (cycle > width) {
                cycle = 0;
            } else cycle += cloudsSpeed;

            // Set the position of the clouds image and draw it with respect to the fade out alpha.
            cloudsImage.setPosition(cycle, 0);
            cloudsImage.draw(batch, alpha.get());

            // Set the position of the second clouds image and draw it with respect to the fade out alpha.
            cloudsImage.setPosition(cycle - width, 0);
            cloudsImage.draw(batch, alpha.get());
        }

        // Draw the vignette texture over the entire screen.
        batch.draw(vignetteTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // End the batch.
        batch.end();

        // Update the stage's actors and draw the stage.
        processor.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        processor.draw();
    }


    /**
     * Handles the resizing of the character screen. Updates the stage's viewport and adjusts the size of background and cloud images.
     * Steps include:
     * - Updating the viewport of the stage's processor with the new width and height, and centering the camera.
     * - Calculating the ratio to maintain the aspect ratio of the background texture.
     * - Adjusting the width and height of the background texture and clouds image based on the calculated ratio.
     *
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        // Update the viewport of the stage's processor with the new width and height, and center the camera.
        processor.getViewport().update(width, height, true);

        float ratio = getRatio();

        // Calculate the new width and height for the background texture based on the ratio.
        float newWidth = backgroundTexture.getWidth() * ratio;
        float newHeight = backgroundTexture.getHeight() * ratio;

        // Set the size of the clouds image to the new width and height.
        cloudsImage.setSize(newWidth, newHeight);
    }

    /**
     * This method is currently not used.
     */
    @Override
    public void pause() {

    }

    /**
     * This method is currently not used.
     */
    @Override
    public void resume() {

    }

    /**
     * This method is currently not used.
     */
    @Override
    public void hide() {

    }

    /**
     * Disposes of the resources used by the game to free up memory.
     * This includes textures, skins, sounds, and shutting down the executor service.
     * Resources disposed:
     * - Stage processor
     * - Background texture
     * - Vignette texture
     * - Craftacular skin
     * - Cooke logo texture
     * - Clouds texture
     * - Button click sound
     * - Executor service
     */
    @Override
    public void dispose() {
        // Dispose of the processor
        processor.dispose();
        // Dispose of the background texture
        backgroundTexture.dispose();
        // Dispose of the vignette texture
        vignetteTexture.dispose();
        // Dispose of the craftacular skin
        craftacularSkin.dispose();
        // Dispose of the cooke logo
        cookeLogo.dispose();
        // Dispose of the clouds texture
        clouds.dispose();
        // Dispose of the button click sound
        buttonClick.dispose();
        // Shutdown the executor service
        executorService.shutdown();
    }
}
