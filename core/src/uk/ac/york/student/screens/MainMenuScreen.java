package uk.ac.york.student.screens;

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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

/**
 * The MainMenuScreen class extends the BaseScreen class and represents the main menu screen of the game.
 * It contains a Stage object, which is used to handle input events and draw the elements of the screen.
 * The class overrides the methods of the Screen interface, which are called at different points in the game's lifecycle.
 * The MainMenuScreen class also includes several private fields for textures, images, skins, sounds, and settings used in the main menu.
 * It provides three constructors that allow for different levels of customization of the fade-in effect when the main menu screen is shown.
 * The class also includes several methods for handling the rendering and animation of the main menu screen, as well as the actions performed when different buttons are clicked.
 */
public class MainMenuScreen extends BaseScreen {
    /**
     * The {@link Stage} instance for the {@link MainMenuScreen} class.
     * This instance is used to handle input events and draw the elements of the screen.
     */
    @Getter
    private final Stage processor;

    /**
     * A boolean value that determines whether the screen should fade in when it is shown.
     * If true, the screen will fade in; if false, it will not.
     */
    private final boolean shouldFadeIn;

    /**
     * The time in seconds for the fade-in effect when the screen is shown.
     * This value is used only if {@link MainMenuScreen#shouldFadeIn} is true.
     */
    private final float fadeInTime;

    /**
     * The Texture instance for the background of the {@link MainMenuScreen}.
     * This texture is loaded from the "images/MapOverview.png" file.
     */
    private final Texture backgroundTexture = new Texture(Gdx.files.internal("images/MapOverview.png"));

    /**
     * The Texture instance for the vignette effect on the {@link MainMenuScreen}.
     * This texture is loaded from the "images/Vignette.png" file.
     */
    private final Texture vignetteTexture = new Texture(Gdx.files.internal("images/Vignette.png"));

    /**
     * The Texture instance for the logo on the {@link MainMenuScreen}.
     * This texture is loaded from the "images/logo/b/logo.png" file.
     * The subfolder is "b" to represent version B of the logo
     */
    private final Texture cookeLogo = new Texture(Gdx.files.internal("images/logo/b/logo.png"));

    /**
     * The Texture instance for the clouds on the {@link MainMenuScreen}.
     * This texture is loaded from the "images/CloudsFormatted.png" file.
     */
    private final Texture clouds = new Texture(Gdx.files.internal("images/CloudsFormatted.png"));

    /**
     * The Image instance for the clouds on the {@link MainMenuScreen}.
     * This image is created from the clouds texture.
     */
    private final Image cloudsImage = new Image(new TextureRegionDrawable(new TextureRegion(clouds)));

    /**
     * The Skin instance for the {@link MainMenuScreen}.
     * This skin is loaded from the {@link SkinManager} using the {@link Skins#CRAFTACULAR} skin.
     */
    private final Skin craftacularSkin = SkinManager.getSkins().getResult(Skins.CRAFTACULAR);

    /**
     * The GameSound instance for the button click sound on the {@link MainMenuScreen}.
     * This sound is loaded from the {@link SoundManager} using the {@link Sounds#BUTTON_CLICK} sound.
     */
    private final GameSound buttonClick = SoundManager.getSupplierSounds().getResult(Sounds.BUTTON_CLICK);

    /**
     * A boolean value that determines whether the clouds are enabled on the {@link MainMenuScreen}.
     * This value is retrieved from the {@link MainMenuCloudsPreferences} in the {@link GamePreferences}.
     * If true, the clouds will be displayed on the {@link MainMenuScreen}; if false, they will not.
     */
    private final boolean cloudsEnabled = ((MainMenuCloudsPreferences) GamePreferences.MAIN_MENU_CLOUDS.getPreference()).isEnabled();

    /**
     * The speed of the clouds on the {@link MainMenuScreen}.
     * This value is retrieved from the {@link MainMenuCloudsPreferences} in the {@link GamePreferences}.
     * It represents the speed at which the clouds move across the {@link MainMenuScreen}.
     */
    private final float cloudsSpeed = ((MainMenuCloudsPreferences) GamePreferences.MAIN_MENU_CLOUDS.getPreference()).getSpeed();
    /**
     * Constructor for the {@link MainMenuScreen} class.
     * This constructor initializes the {@link MainMenuScreen} with the provided game.
     * It sets {@link MainMenuScreen#shouldFadeIn} to false, meaning the screen will not fade in when shown.
     * @param game the {@link GdxGame} instance representing the game
     */
    public MainMenuScreen(GdxGame game) {
        this(game, false);
    }

    /**
     * Constructor for the {@link MainMenuScreen} class.
     * This constructor initializes the {@link MainMenuScreen} with the provided game and shouldFadeIn value.
     * It sets fadeInTime to 0.75 seconds, which is the time for the fade-in effect when the screen is shown.
     * @param game the {@link GdxGame} instance representing the game
     * @param shouldFadeIn a boolean value that determines whether the screen should fade in when it is shown
     */
    public MainMenuScreen(GdxGame game, boolean shouldFadeIn) {
        this(game, shouldFadeIn, 0.75f);
    }

    /**
     * Constructor for the {@link MainMenuScreen} class.
     * This constructor initializes the {@link MainMenuScreen} with the provided game, shouldFadeIn value, and fadeInTime.
     * It also initializes the processor with a new {@link Stage} with a {@link ScreenViewport}, and sets this processor as the input processor for {@link Gdx}.
     * Additionally, it initializes the {@link MainMenuScreen#executorService} with a new single thread scheduled executor.
     * @param game the {@link GdxGame} instance representing the game
     * @param shouldFadeIn a boolean value that determines whether the screen should fade in when it is shown
     * @param fadeInTime the time in seconds for the fade-in effect when the screen is shown
     */
    public MainMenuScreen(GdxGame game, boolean shouldFadeIn, float fadeInTime) {
        super(game);
        this.shouldFadeIn = shouldFadeIn;
        this.fadeInTime = fadeInTime;
        processor = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(processor);
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * The Direction enum represents the four cardinal directions: UP, DOWN, LEFT, and RIGHT.
     * It is used in the {@link MainMenuScreen} class to specify the direction of certain animations and movements.
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * This method is used to zoom and move an actor in a specified direction.
     * The actor is scaled up and moved in the direction specified by the 'direction' parameter.
     * The scaling and movement are performed simultaneously over a duration of 1 second.
     * The distance of the movement is 800 units.
     *
     * @param actor The actor to be zoomed and moved. This should be an instance of the {@link Actor} class or any of its subclasses.
     * @param direction The direction in which the actor should be moved. This should be one of the values of the {@link MainMenuScreen.Direction} enum.
     */
    public void zoomAndMove(@NotNull Actor actor, @NotNull Direction direction) {
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

    /**
     * An {@link AtomicReference} to a {@link Float} value representing the alpha value for fade effects.
     * This value is used to control the transparency of certain elements during fade in/out animations.
     * The initial value is set to 1f, representing full opacity.
     */
    private final AtomicReference<Float> alpha = new AtomicReference<>(1f);

    /**
     * A {@link ScheduledExecutorService} instance used for scheduling tasks to be executed after a certain delay, or at fixed intervals.
     * This is used in the {@link MainMenuScreen} class to schedule tasks such as fade out animations or other time-dependent actions.
     */
    private final ScheduledExecutorService executorService;

    /**
     * This method is used to create a fade out effect over a specified duration.
     * The fade out effect is achieved by gradually reducing the alpha value of the elements to be faded.
     * The alpha value is updated at fixed intervals until it reaches 0, at which point it is maintained at 0.
     * The duration of the fade out effect is 1 second.
     * The method uses a {@link ScheduledExecutorService}, specifically {@link MainMenuScreen#executorService}, to schedule the updates to the alpha value and to cancel the updates after the specified duration.
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
     * This method is called when the {@link MainMenuScreen} is shown.
     * It sets up the UI elements and their actions for the main menu screen.
     * It also sets up the fade-in effect if {@link MainMenuScreen#shouldFadeIn} is true.
     */
    @Override
    public void show() {
        // If shouldFadeIn is true, set the alpha of the root actor to 0 and add a fade-in action to it.
        if (shouldFadeIn) {
            processor.getRoot().getColor().a = 0;
            processor.getRoot().addAction(fadeIn(fadeInTime));
        }

        // Create a new Table and add it to the stage.
        Table table = new Table();
        table.setFillParent(true);
        // If debug screen preferences are enabled, set the table to debug mode.
        if (((DebugScreenPreferences) GamePreferences.DEBUG_SCREEN.getPreference()).isEnabled()) {
            table.setDebug(true);
        }
        processor.addActor(table);

        // Create the buttons and the logo image for the main menu screen.
        TextButton playButton = new TextButton("Let Ron Cooke", craftacularSkin);
        TextButton tutorialButton = new TextButton("Tutorial", craftacularSkin);
        TextButton preferencesButton = new TextButton("Settings", craftacularSkin);
        TextButton exitButton = new TextButton("Exit", craftacularSkin);
        Image cookeLogoImage = new Image(cookeLogo);

        // Add the buttons and the logo image to the table.
        table.add(cookeLogoImage).fillX().uniformX().pad(0, 0, 150, 0);
        table.row();
        table.add(playButton).fillX().uniformX();
        table.row().pad(10, 0, 5, 0);
        table.add(tutorialButton).fillX().uniformX();
        table.row().pad(10, 0, 5, 0);
        table.add(preferencesButton).fillX().uniformX();
        table.row().pad(10, 0, 5, 0);
        table.add(exitButton).fillX().uniformX();

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

        // The play button plays the button click sound, moves all elements, fades out, and then switches to the game screen after a delay of 1500 milliseconds.
        playButton.addListener(new ChangeListener() {
            /**
             * This method is triggered when a change event occurs on the actor, in this case, when the preferences button is clicked.
             * It first plays the button click sound.
             * Then, it transitions the screen to the preferences screen.
             *
             * @param event The {@link com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent} triggered by the actor. This is not used in the method.
             * @param actor The actor that triggered the {@link com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent}. This is not used in the method.
             */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.getSounds().get(Sounds.BUTTON_CLICK).play();
                game.transitionScreen(Screens.CHARACTER,true, 0.5f);

            }
        });

        // The preferences button plays the button click sound and transitions to the preferences screen.
        preferencesButton.addListener(new ChangeListener() {
            /**
             * This method is triggered when a change event occurs on the actor, in this case, when the preferences button is clicked.
             * It first plays the button click sound.
             * Then, it transitions the screen to the preferences screen.
             *
             * @param event The {@link com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent} triggered by the actor. This is not used in the method.
             * @param actor The actor that triggered the {@link com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent}. This is not used in the method.
             */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.getSounds().get(Sounds.BUTTON_CLICK).play();
                game.transitionScreen(Screens.PREFERENCES);
            }
        });

        // The tutorial button plays the button click sound and transitions to the preferences screen.
        tutorialButton.addListener(new ChangeListener() {
            /**
             * This method is triggered when a change event occurs on the actor, in this case, when the preferences button is clicked.
             * It first plays the button click sound.
             * Then, it transitions the screen to the preferences screen.
             *
             * @param event The {@link com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent} triggered by the actor. This is not used in the method.
             * @param actor The actor that triggered the {@link com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent}. This is not used in the method.
             */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.getSounds().get(Sounds.BUTTON_CLICK).play();
                game.transitionScreen(Screens.TUTORIAL);
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
     * This method calculates and returns the ratio to maintain the aspect ratio of the background texture.
     * It first retrieves the width and height of the screen.
     * Then, it calculates the ratio of the screen width to the background texture width and the ratio of the screen height to the background texture height.
     * Finally, it returns the maximum of these two ratios to maintain the aspect ratio of the background texture.
     *
     * @return the maximum ratio to maintain the aspect ratio of the background texture
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

    /**
     * A private float variable that represents the cycle of the cloud movement in the {@link MainMenuScreen}.
     * It is initially set to 0 and is incremented by the speed of the clouds each time the screen is rendered.
     * When the cycle exceeds the width of the screen, it is reset to 0.
     * This creates a continuous loop of the cloud movement across the screen.
     */
    private float cycle = 0;
    /**
     * This method is responsible for rendering the {@link MainMenuScreen}
     * It first clears the screen and sets the clear color to black.
     * Then, it enables blending and sets the blend function to standard alpha blending.
     * After that, it retrieves the batch from the stage's processor and begins the batch.
     * It calculates the ratio to maintain the aspect ratio of the background texture and calculates the new width and height for the background texture based on the ratio.
     * It then draws the background texture.
     * If clouds are enabled, it animates the clouds by incrementing the cycle by the speed of the clouds and resetting the cycle to 0 if it exceeds the width of the screen.
     * It sets the position of the clouds image and the second clouds image and draws them with respect to the fade out alpha.
     * It then draws the vignette texture over the entire screen and ends the batch.
     * Finally, it updates the stage's actors and draws the stage.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
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
     * This method is called when the screen is resized.
     * It first updates the viewport of the stage's processor with the new width and height, and centers the camera.
     * Then, it retrieves the width and height of the screen.
     * It calculates the ratio of the screen width to the background texture width and the ratio of the screen height to the background texture height.
     * It takes the maximum of these two ratios to maintain the aspect ratio of the background texture.
     * It calculates the new width and height for the background texture based on the ratio.
     * Finally, it sets the size of the clouds image to the new width and height.
     *
     * @param width  The new width in pixels
     * @param height The new height in pixels
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
     * This method is called when the application is paused, typically when it loses focus.
     * For example, this can happen when the user switches to another application or when a system dialog is shown.
     */
    @Override
    public void pause() {

    }

    /**
     * This method is called when the application is resumed from a paused state, typically when it regains focus.
     * For example, this can happen when the user switches back to the application or when a system dialog is dismissed.
     * The application should resume all processes that were stopped in the pause() method in this method.
     */
    @Override
    public void resume() {

    }

    /**
     * This method is called when the current screen is being hidden or replaced by another screen.
     * The application should stop all processes related to the current screen in this method to save resources.
     */
    @Override
    public void hide() {

    }

    /**
     * This method is called when the {@link MainMenuScreen} is being disposed of.
     * It is responsible for freeing up resources and stopping any processes that were started in the MainMenuScreen.
     * It disposes of the {@link MainMenuScreen#processor}, {@link MainMenuScreen#backgroundTexture}, {@link MainMenuScreen#vignetteTexture}, {@link MainMenuScreen#craftacularSkin}, {@link MainMenuScreen#cookeLogo}, {@link MainMenuScreen#clouds}, and {@link MainMenuScreen#buttonClick}.
     * It also shuts down the {@link MainMenuScreen#executorService}.
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
