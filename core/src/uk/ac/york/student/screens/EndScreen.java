package uk.ac.york.student.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import uk.ac.york.student.GdxGame;
import uk.ac.york.student.game.Leaderboard;
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
 *  -"A leaderboard with the name and score of the top 10 people who have completed the game successfully."
 *  -Displaying final score
 *  -Displaying hidden achievements
 *  14 May, 2024
 */

/**
 * The EndScreen class extends the BaseScreen class and represents the main menu screen of the game.
 * It contains a Stage object, which is used to handle input events and draw the elements of the screen.
 * The class overrides the methods of the Screen interface, which are called at different points in the game's lifecycle.
 * The EndScreen class also includes several private fields for textures, images, skins, sounds, and settings used in the main menu.
 * It provides three constructors that allow for different levels of customization of the fade-in effect when the main menu screen is shown.
 * The class also includes several methods for handling the rendering and animation of the main menu screen, as well as the actions performed when different buttons are clicked.
 */
@Getter
public class EndScreen extends BaseScreen {

    private final Stage processor;
    private final Player player;

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
    private PlayerStreaks playerStreaks;

    private boolean scoreSaved = false;


    /**
     * Constructs the EndScreen with the specified game, fade-in settings, and arguments.
     * Initializes the stage processor, player, and input processor.
     * Retrieves player metrics and initializes the executor service and player streaks.
     *
     * @param game The game instance.
     * @param shouldFadeIn Determines if the screen should fade in.
     * @param fadeInTime The duration of the fade-in animation.
     * @param args The arguments passed to the end screen, where the first argument is expected to be a Player object.
     */
    public EndScreen(GdxGame game, boolean shouldFadeIn, float fadeInTime, Object @NotNull [] args) {
        super(game);
        processor = new Stage(new ScreenViewport());
        player = (Player) args[0];
        Gdx.input.setInputProcessor(processor);

        this.shouldFadeIn = shouldFadeIn;
        this.fadeInTime = fadeInTime;

        PlayerMetrics metrics = player.getMetrics();

        //OBSOLETE FOR NOW
        float energyTotal = metrics.getEnergy().getTotal();
        float studyLevelTotal = metrics.getStudyLevel().getTotal();
        float happinessTotal = metrics.getHappiness().getTotal();
        float energyMax = metrics.getEnergy().getMaxTotal();
        float studyLevelMax = metrics.getStudyLevel().getMaxTotal();
        float happinessMax = metrics.getHappiness().getMaxTotal();

        executorService = Executors.newSingleThreadScheduledExecutor();
        playerStreaks = PlayerStreaks.getInstance();

    }




    /**
     * This method is currently not used.
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * This method is currently not used.
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
     * This method is currently not used.
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
     * Displays the main screen with player statistics, achievements, leaderboard, and options to save the player's name or exit the application.
     * If the screen should fade in, it sets the alpha of the root actor to 0 and adds a fade-in action.
     * Retrieves player metrics and calculates the player's score and grade.
     * Displays the player's statistics, hidden achievements, and a leaderboard.
     * Provides input fields and buttons for player interaction, including saving the player's name and exiting the application.
     *
     * The screen layout includes two main sections:
     *
     *   Left side: Player statistics, achievements, and a logo.
     *   Right side: Leaderboard, name input field, and exit button.
     *
     * @see GamePreferences
     * @see DebugScreenPreferences
     * @see PlayerMetrics
     * @see Leaderboard
     * @see SoundManager
     * @see Wait
     * @see Activity
     * @see Screens
     */
    public void show() {
        // If shouldFadeIn is true, set the alpha of the root actor to 0 and add a fade-in action to it.
        if (shouldFadeIn) {
            processor.getRoot().getColor().a = 0;
            processor.getRoot().addAction(fadeIn(fadeInTime));
        }

        PlayerMetrics metrics = player.getMetrics();
        float energyTotal = metrics.getEnergy().getTotal();
        float studyLevelTotal = metrics.getStudyLevel().getTotal();
        float happinessTotal = metrics.getHappiness().getTotal();
        float energyMax = metrics.getEnergy().getMaxTotal();
        float studyLevelMax = metrics.getStudyLevel().getMaxTotal();
        float happinessMax = metrics.getHappiness().getMaxTotal();
        float score = player.calculateScore(energyTotal, energyMax, studyLevelTotal, studyLevelMax, happinessTotal, happinessMax, playerStreaks);
        String scoreString = player.convertScoreToString(score);
        //Leaderboard stuff added here - Chris
        String leaderboard = Leaderboard.getLeaderboard();

        // Create a new Table and add it to the stage.
        Table table = new Table();
        table.setFillParent(true);
        // If debug screen preferences are enabled, set the table to debug mode.
        if (((DebugScreenPreferences) GamePreferences.DEBUG_SCREEN.getPreference()).isEnabled()) {
            table.setDebug(true);
        }
        processor.addActor(table);

        // Create the left side table for other elements
        Table leftTable = new Table();
        leftTable.top().left();

        // Create the buttons and the logo image for the main menu screen.
        Label yourStatsLabel = new Label("Your stats: ", craftacularSkin);
        Label yourScoreLabel = new Label("Score: " + score, craftacularSkin);
        Label yourGradeLabel = new Label("Grade: " + scoreString, craftacularSkin);
        Label yourHiddenAchievementsLabel = new Label("Hidden Achievements: ", craftacularSkin);
        Label bookworm = new Label("BOOKWORM - studied more than 4x in a row!", craftacularSkin);
        Label exercise = new Label("JOGGER - walked more than 4x in a row!", craftacularSkin);
        Label ducks = new Label("Duck duck go - fed the ducks more than 4x in a row!", craftacularSkin);

        TextButton exitButton = new TextButton("Exit", craftacularSkin);
        Image cookeLogoImage = new Image(cookeLogo);

        // Add elements to the left table
        leftTable.add(cookeLogoImage).expandX().pad(0, 0, 150, 0).colspan(2);
        leftTable.row();
        leftTable.add(yourStatsLabel).uniformX();
        leftTable.row();
        leftTable.add(yourScoreLabel).uniformX();
        leftTable.row();
        leftTable.add(yourGradeLabel).uniformX();
        leftTable.row();
        leftTable.add(yourHiddenAchievementsLabel).uniformX();
        leftTable.row();
        if (playerStreaks.getStreakCount(Activity.STUDY) >= 4) {
            leftTable.add(bookworm).uniformX().row();
        }
        if (playerStreaks.getStreakCount(Activity.EXERCISE) >= 4) {
            leftTable.add(exercise).uniformX().row();
        }
        if (playerStreaks.getStreakCount(Activity.FEEDTHEDUCKS) >= 4) {
            leftTable.add(ducks).uniformX().row();
        }
        leftTable.row();

        // Add left table to main table
        table.add(leftTable).expand().fill().pad(50);


        // Create the right side table for leaderboard and name field
        Table rightTable = new Table();

        // Leaderboard label
        Label leaderboardLabel = new Label("Leaderboard", craftacularSkin);
        rightTable.add(leaderboardLabel).row();

        // Leaderboard text
        Label leaderboardText = new Label(leaderboard, craftacularSkin);
        rightTable.add(leaderboardText).row();

        // Name label and field
        Label nameLabel = new Label("Please type your name:", craftacularSkin);
        TextField nameField = new TextField("", craftacularSkin);
        TextButton saveName = new TextButton("Save Name", craftacularSkin);
        rightTable.add(nameLabel).uniformX().padBottom(10).row();
        rightTable.add(nameField).uniformX().padBottom(10).row();
        rightTable.add(saveName).uniformX().padBottom(5).row();
        rightTable.add(exitButton).uniformX();

        // Add right table to main table
        table.add(rightTable).expand().fill().pad(50);
        table.row();

        // Add listeners to the buttons.
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.getSounds().get(Sounds.BUTTON_CLICK).play();
                Wait.async(400, TimeUnit.MILLISECONDS)
                        .thenRun(() -> {
                            Gdx.app.exit();
                        });
            }
        });

        saveName.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                SoundManager.getSounds().get(Sounds.BUTTON_CLICK).play();
                Wait.async(400, TimeUnit.MILLISECONDS)
                        .thenRun(() -> {
                            if (!Leaderboard.scoreSaved) {
                                Leaderboard.saveScore(nameField.getText(), score);
                                Leaderboard.scoreSaved = true;
                                game.transitionScreen(Screens.END, player, true, 0.5f);
                            }
                        });
            }
        });

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



    /**
     * Calculates and returns the ratio to scale the background texture to fit the screen while maintaining its aspect ratio.
     * It retrieves the width and height of the screen and the background texture,
     * then calculates the ratio of the screen dimensions to the texture dimensions,
     * and returns the maximum of these ratios to ensure the texture covers the screen without distortion.
     *
     * @return the maximum ratio of screen dimensions to background texture dimensions.
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
     * Renders the end screen, including the background texture, animated clouds, and vignette effect.
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
     * Handles the resizing of the end screen. Updates the stage's viewport and adjusts the size of background and cloud images.
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
