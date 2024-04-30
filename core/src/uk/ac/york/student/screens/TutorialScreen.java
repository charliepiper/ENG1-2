package uk.ac.york.student.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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

import java.util.function.Supplier;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

/**
 * This is a new class which explains the game to a new user.
 * It explains all movement options and how to interact with the game.
 * The screen also provides a back button for the user to return to the main menu.
 * The screen is designed using the libGDX framework and uses a {@link Stage} to manage and render the UI elements.
 * The UI elements are organized in a {@link Table} for layout purposes.
 */
public class TutorialScreen extends BaseScreen{
    /**
     * The main processor for the stage where the UI elements are drawn and managed.
     */
    @Getter
    private final Stage processor;

    /**
     * A flag indicating whether the screen should fade in when shown.
     */
    private final boolean shouldFadeIn;

    /**
     * The time it takes for the screen to fade in, in seconds.
     * This variable is only used if {@link TutorialScreen#shouldFadeIn} is true.
     */
    private final float fadeInTime;

    /**
     * An instance of the {@link TutorialScreen.ScreenData} class that holds references to the UI elements.
     */
    private final TutorialScreen.ScreenData screenData = new TutorialScreen.ScreenData();

    /**
     * The main table where the UI elements are laid out.
     */
    private final Table table = new Table();

    /**
     * The sound that is played when a button is clicked.
     * By default, this is {@link Sounds#BUTTON_CLICK} from {@link SoundManager}
     */
    private final GameSound buttonClick = SoundManager.getSupplierSounds().getResult(Sounds.BUTTON_CLICK);

    /**
     * The skin used for the UI elements.
     * By default, this is {@link Skins#CRAFTACULAR} from {@link SkinManager}.
     */
    private final Skin craftacularSkin = SkinManager.getSkins().getResult(Skins.CRAFTACULAR);

    /**
     * The texture used for the background of the screen.
     */
    private final Texture stoneWallTexture = new Texture(Gdx.files.internal("images/StoneWall.png"));

    /**
     * The texture used for the gradient at the bottom of the screen.
     */
    private final Texture bottomUpBlackGradient = new Texture(Gdx.files.internal("images/BottomUpBlackGradient.png"));
    /**
     * This is an enumeration of labels used in the {@link TutorialScreen} class.
     * Each label is associated with a {@link Supplier <String>} that provides the label's text.
     * The labels are used for various UI elements in the {@link TutorialScreen}, such as buttons and sliders.
     * The labels can also include placeholders (e.g. "{0}") that can be replaced with specific values using the {@link TutorialScreen.Labels#getLabel(String...)} method.
     */
    private enum Labels {
        /**
         * The label for the back button.
         */
        BACK_BUTTON("Head Back"),

        /**
         * The label for the title of the tutorial screen.
         */
        TITLE("Tutorial"),

        /**
         * The text for the movement
         */
        INFORMATION_MOVEMENT("Movement: The character can be moved using WASD or arrow keys."),

        /**
         * The text for the speed
         */
        INFORMATION_SPEED("Speed: Movement speed can be increased using CTRL."),

        /**
         * The text for the movement
         */
        INFORMATION_ACTIVITES("Activities: When an activity is accessible, text is displayed on the screen. \n                  Activities can be interacted with by pressing E.");

        private final Supplier<String> label;

        /**
         * Calls the supplier to get the text of the label.
         *
         * @return The text of the label.
         */
        public String getLabel() {
            return label.get();
        }

        /**
         * Returns the text of the label, replacing any placeholders with the provided values.
         *
         * @param placeholders The values to replace the placeholders in the label text.
         * @return The text of the label with placeholders replaced.
         */
        public String getLabel(String @NotNull ... placeholders) {
            String localLabel = this.label.get();
            for (int i = 0; i < placeholders.length; i++) {
                localLabel = localLabel.replace("{" + i + "}", placeholders[i]);
            }
            return localLabel;
        }

        /**
         * Constructs a new Labels enumeration value with the given label text.
         *
         * @param label The text of the label.
         */
        Labels(String label) {
            this.label = () -> label;
        }
    }

    private static class ScreenData {

        /**
         * The back button for returning to the main menu.
         */
        private TextButton backButton;
    }
    /**
     * Constructor for the {@link TutorialScreen} class.
     * This constructor initializes a new {@link TutorialScreen} with the given game instance.
     * The screen will not fade in when shown.
     *
     * @param game The {@link GdxGame} game instance to associate with this screen.
     */
    public TutorialScreen(GdxGame game) {
        this(game, false);
    }

    /**
     * Constructor for the {@link TutorialScreen} class.
     * This constructor initializes a new {@link TutorialScreen} with the given {@link GdxGame} game instance and fade-in flag.
     * If the fade-in flag is true, the screen will fade in when shown with a default fade-in time of 0.75 seconds.
     *
     * @param game The {@link GdxGame} game instance to associate with this screen.
     * @param shouldFadeIn A flag indicating whether the screen should fade in when shown.
     */
    public TutorialScreen(GdxGame game, boolean shouldFadeIn) {
        this(game, shouldFadeIn, 0.75f);
    }

    /**
     * Constructor for the {@link TutorialScreen} class.
     * This constructor initializes a new {@link TutorialScreen} with the given game instance, fade-in flag, and fade-in time.
     * If the fade-in flag is true, the screen will fade in when shown with the given fade-in time.
     * The constructor also initializes the {@link Stage} for the screen and sets it as the {@link Gdx} input processor.
     *
     * @param game The {@link GdxGame} game instance to associate with this screen.
     * @param shouldFadeIn A flag indicating whether the screen should fade in when shown.
     * @param fadeInTime The time it takes for the screen to fade in, in seconds.
     */
    public TutorialScreen(GdxGame game, boolean shouldFadeIn, float fadeInTime) {
        super(game);
        this.shouldFadeIn = shouldFadeIn;
        this.fadeInTime = fadeInTime;
        processor = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(processor);
    }

    /**
     * This method is called when the {@link TutorialScreen} is shown.
     * It initializes the UI elements and sets up the listeners for the UI elements.
     * If the {@link TutorialScreen#shouldFadeIn} flag is true, it also sets up a fade-in animation for the screen.
     */
    @Override
    public void show() {
        // If the screen should fade in when shown, set the alpha of the root actor to 0 and add a fade-in action.
        if (shouldFadeIn) {
            processor.getRoot().getColor().a = 0;
            processor.getRoot().addAction(fadeIn(fadeInTime));
        }

        // Create and initialize the back button and its listener.
        createBackButton();
        listenBackButton();

        // Set up the layout of the UI elements in the table.
        setupTable();
    }
    /**
     * This method creates a back button for the preferences screen.
     * The button's text is obtained from the {@link TutorialScreen.Labels#BACK_BUTTON} label in the {@link TutorialScreen.Labels} enumeration.
     * The button is created with the {@link TutorialScreen#craftacularSkin} skin.
     * The reference to the button is stored in the {@link TutorialScreen#screenData} object for later use.
     */
    private void createBackButton() {
        screenData.backButton = new TextButton(TutorialScreen.Labels.BACK_BUTTON.getLabel(), craftacularSkin);
    }

    /**
     * This method sets up a listener for the {@link TutorialScreen.ScreenData#backButton}.
     * When the back button is clicked, it plays a button click sound and transitions the game to the main menu screen.
     * The button click sound is obtained from the {@link SoundManager#getSounds()} method with the {@link Sounds#BUTTON_CLICK} parameter.
     * The transition to the main menu screen is performed by calling the {@link GdxGame#transitionScreen(Class)} method with the {@link Screens#MAIN_MENU} parameter.
     */
    private void listenBackButton() {
        TextButton backButton = screenData.backButton;
        backButton.addListener(new ChangeListener() {
            /**
             * This method is triggered when the back button is clicked.
             * It plays a button click sound and transitions the game to the main menu screen.
             * The button click sound is obtained from the {@link SoundManager#getSounds()} method with the {@link Sounds#BUTTON_CLICK} parameter.
             * The transition to the main menu screen is performed by calling the {@link GdxGame#transitionScreen(Class)} method with the {@link Screens#MAIN_MENU} parameter.
             *
             * @param event The {@link com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent} triggered by the click.
             * @param actor The {@link Actor} that was clicked.
             */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.getSounds().get(Sounds.BUTTON_CLICK).play();
                game.transitionScreen(Screens.MAIN_MENU);
            }
        });
    }

    /**
     * This method sets up the layout of the UI elements in the table.
     * It creates a grid layout and adds the UI elements to the grid.
     * The UI elements are retrieved from the {@link TutorialScreen#screenData} object.
     * The table is set to fill the parent actor and its skin is set to {@link TutorialScreen#craftacularSkin}.
     * If the debug screen is enabled in the {@link GamePreferences#DEBUG_SCREEN}, the table's debug lines are shown.
     * The table is then added to the {@link TutorialScreen#processor} {@link Stage}.
     * The UI elements are added to the table in a specific order and layout to create the desired appearance for the {@link TutorialScreen}.
     * The method also sets up some properties for the UI elements, such as the alignment of the labels and the touchable state of the sliders.
     */
    private void setupTable() {
        // Retrieve the UI elements from the screen data
        TextButton backButton = screenData.backButton;

        // Set the table to fill the parent actor and set its skin
        table.setFillParent(true);
        // If the debug screen is enabled, show the table's debug lines
        if (((DebugScreenPreferences) GamePreferences.DEBUG_SCREEN.getPreference()).isEnabled()) {
            table.setDebug(true);
        }
        table.setSkin(craftacularSkin);
        // Add the table to the stage
        processor.addActor(table);

        // Add the title label to the table
        Cell<Label> titleCell = table.add(TutorialScreen.Labels.TITLE.getLabel()).colspan(2).pad(0, 0, 100, 0);
        // Set the font scale of the title label
        titleCell.getActor().setFontScale(1.5f);

        table.row().pad(5, 0, 0, 0);
        // Add the movement info to the table
        Cell<Label> moveCell = table.add(Labels.INFORMATION_MOVEMENT.getLabel()).colspan(2).pad(0, 0, 10, 0);
        // Set the font scale of the movement info
        moveCell.getActor().setFontScale(1f);

        table.row().pad(5, 0, 0, 0);
        // Add the speed info to the table
        Cell<Label> speedCell = table.add(Labels.INFORMATION_SPEED.getLabel()).colspan(2).pad(0, 0, 10, 0);
        // Set the font scale of the speed info
        speedCell.getActor().setFontScale(1f);

        table.row().pad(5, 0, 0, 0);
        // Add the activity info to the table
        Cell<Label> activityCell = table.add(Labels.INFORMATION_ACTIVITES.getLabel()).colspan(2).pad(0, 0, 10, 0);
        // Set the font scale of the information info
        activityCell.getActor().setFontScale(1f);

        // Add a large gap before the back button
        table.row().pad(150, 0, 0, 0);
        // Add the back button to the table, spanning across both columns
        table.add(backButton).colspan(2).fillX().uniformX();
    }

    /**
     * This method is responsible for rendering the game screen.
     * It first clears the screen with a black color using {@link Gdx#gl}
     * Then, it begins a new batch of drawing commands using the {@link TutorialScreen#processor}'s batch.
     * It draws the {@link TutorialScreen#stoneWallTexture} in a grid pattern across the entire screen.
     * The size of each tile in the grid is determined by dividing the width and height of the texture by 6.
     * After that, it draws a gradient texture at the bottom of the screen using the {@link TutorialScreen#bottomUpBlackGradient}.
     * The gradient texture is stretched to cover the entire width and height of the screen.
     * It then ends the batch of drawing commands.
     * Finally, it updates and draws the stage with {@link Stage#act(float)} and {@link Stage#draw()} respectively. The {@link Stage} is updated with the minimum of the time since the last render and 1/30 seconds.
     *
     * @param v The time in seconds since the last render.
     */
    @Override
    public void render(float v) {
        // Clears the screen with a black color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Begins a new batch of drawing commands
        Batch batch = processor.getBatch();
        batch.begin();

        // Draws the stone wall texture in a grid pattern across the entire screen
        // The size of each tile in the grid is determined by dividing the width and height of the texture by 6
        int width = stoneWallTexture.getWidth() / 6;
        int height = stoneWallTexture.getHeight() / 6;
        for (int x = 0; x < Gdx.graphics.getWidth(); x += width) {
            for (int y = 0; y < Gdx.graphics.getHeight(); y += height) {
                batch.draw(stoneWallTexture, x, y, width, height);
            }
        }

        // Draws a gradient texture at the bottom of the screen
        // The gradient texture is stretched to cover the entire width and height of the screen
        batch.draw(bottomUpBlackGradient, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Ends the batch of drawing commands
        batch.end();

        // Updates and draws the stage
        // The stage is updated with a fixed time step, which is the smaller of the actual time passed and 1/30 seconds
        processor.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        processor.draw();
    }

    /**
     * This method is called when the game window is resized.
     * It updates the viewport of the {@link TutorialScreen#processor} {@link Stage} to match the new window size.
     * The viewport is updated to maintain the aspect ratio of the game, which may result in black bars on the sides or top and bottom of the game window.
     * The viewport's camera is also updated to ensure that the game is centered in the window.
     *
     * @param width The new width of the game window, in pixels.
     * @param height The new height of the game window, in pixels.
     */
    @Override
    public void resize(int width, int height) {
        processor.getViewport().update(width, height, true);
    }

    /**
     * This method is called when the game is paused.
     * Currently, this method is empty and does not perform any actions when the game is paused.
     */
    @Override
    public void pause() {

    }

    /**
     * This method is called when the game is resumed from a paused state.
     * Currently, this method is empty and does not perform any actions when the game is resumed.
     */
    @Override
    public void resume() {

    }

    /**
     * This method is called when the game screen is hidden.
     * Currently, this method is empty and does not perform any actions when the game screen is hidden.
     */
    @Override
    public void hide() {

    }

    /**
     * This method is called when the game screen is disposed.
     * It disposes of the resources that were created in the {@link TutorialScreen} class to free up memory.
     * The resources that are disposed of include:
     * - The {@link TutorialScreen#processor} {@link Stage}, which is used to manage and render the UI elements.
     * - The {@link TutorialScreen#craftacularSkin} {@link Skin}, which is used for the UI elements.
     * - The {@link TutorialScreen#buttonClick} {@link GameSound}, which is the sound that is played when a button is clicked.
     */
    @Override
    public void dispose() {
        processor.dispose();
        craftacularSkin.dispose();
        buttonClick.dispose();
    }
}
