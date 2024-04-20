package uk.ac.york.student.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import uk.ac.york.student.GdxGame;
import uk.ac.york.student.player.Player;
import uk.ac.york.student.player.PlayerMetric;
import uk.ac.york.student.player.PlayerMetrics;

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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

import java.util.List;

/**
 * The EndScreen class extends the BaseScreen class and represents the main menu screen of the game.
 * It contains a Stage object, which is used to handle input events and draw the elements of the screen.
 * The class overrides the methods of the Screen interface, which are called at different points in the game's lifecycle.
 * The MainMenuScreen class also includes several private fields for textures, images, skins, sounds, and settings used in the main menu.
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



//    public EndScreen(GdxGame game) {
//        super(game);
//        throw new UnsupportedOperationException("This constructor is not supported (must pass in object args!)");
//    }
public EndScreen(GdxGame game, boolean shouldFadeIn, float fadeInTime) {
    this(game, shouldFadeIn, fadeInTime, new Object[]{}); // Provide an empty array for args
}

    public EndScreen(GdxGame game, boolean shouldFadeIn, float fadeInTime, Object @NotNull [] args) {
        super(game);
        processor = new Stage(new ScreenViewport());
        player = (Player) args[0];
        Gdx.input.setInputProcessor(processor);

        this.shouldFadeIn = shouldFadeIn;
        this.fadeInTime = fadeInTime;

        PlayerMetrics metrics = player.getMetrics();
        float energyTotal = metrics.getEnergy().getTotal();
        float studyLevelTotal = metrics.getStudyLevel().getTotal();
        float happinessTotal = metrics.getHappiness().getTotal();
        float energyMax = metrics.getEnergy().getMaxTotal();
        float studyLevelMax = metrics.getStudyLevel().getMaxTotal();
        float happinessMax = metrics.getHappiness().getMaxTotal();

        float score = player.calculateScore(energyTotal, energyMax, studyLevelTotal, studyLevelMax, happinessTotal, happinessMax);
        System.out.println(score);
        String scoreString = player.convertScoreToString(score);
        System.out.println(scoreString);

        executorService = Executors.newSingleThreadScheduledExecutor();




    }





    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

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
        float score = player.calculateScore(energyTotal, energyMax, studyLevelTotal, studyLevelMax, happinessTotal, happinessMax);
        String scoreString = player.convertScoreToString(score);

        // Create a new Table and add it to the stage.
        Table table = new Table();
        table.setFillParent(true);
        // If debug screen preferences are enabled, set the table to debug mode.
        if (((DebugScreenPreferences) GamePreferences.DEBUG_SCREEN.getPreference()).isEnabled()) {
            table.setDebug(true);
        }
        processor.addActor(table);

        // Create the buttons and the logo image for the main menu screen.
        Label yourStatsLabel = new Label("Your stats: ", craftacularSkin);
        Label yourScoreLabel = new Label("Score: " + score, craftacularSkin);
        Label yourGradeLabel = new Label("Grade: " + scoreString, craftacularSkin);

        Label yourHiddenAchievementsLabel = new Label("Hidden Achievements: ", craftacularSkin);

        Label leaderboardLabel = new Label("Leaderboard", craftacularSkin);


        TextButton exitButton = new TextButton("Exit", craftacularSkin);
        Image cookeLogoImage = new Image(cookeLogo);

        // Add the buttons and the logo image to the table.
        table.add(cookeLogoImage).expandX().pad(0, 0, 150, 0).colspan(2);
        table.row();

        table.add(yourStatsLabel).uniformX();
        table.add(leaderboardLabel).uniformX();
        table.row();
        table.add(yourScoreLabel).uniformX();
        table.row();
        table.add(yourGradeLabel).uniformX();
        table.row();
        table.add(yourHiddenAchievementsLabel).uniformX();;
        table.row().pad(100, 0, 10, 0);
        table.add(exitButton).expandX().uniformX().colspan(2);

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

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

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
