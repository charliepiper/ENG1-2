package uk.ac.york.student.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import uk.ac.york.student.GdxGame;
import uk.ac.york.student.assets.skins.SkinManager;
import uk.ac.york.student.assets.skins.Skins;
import uk.ac.york.student.audio.music.MusicManager;
import uk.ac.york.student.audio.sound.GameSound;
import uk.ac.york.student.audio.sound.SoundManager;
import uk.ac.york.student.audio.sound.Sounds;
import uk.ac.york.student.settings.*;

import java.util.function.Supplier;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;

/**
 * This class represents the preferences screen of the game. It extends the {@link BaseScreen} class.
 * The preferences screen allows the user to adjust various settings of the game such as music volume,
 * sound volume, enabling/disabling music, enabling/disabling sound, enabling/disabling debug screen,
 * enabling/disabling main menu clouds, and adjusting the speed of the clouds.
 * The screen also provides a back button for the user to return to the main menu.
 * The screen is designed using the libGDX framework and uses a {@link Stage} to manage and render the UI elements.
 * The UI elements are organized in a {@link Table} for layout purposes.
 * The class also uses the Singleton pattern for the {@link MusicManager} and {@link SoundManager} classes.
 */
public class PreferencesScreen extends BaseScreen {
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
     * This variable is only used if {@link PreferencesScreen#shouldFadeIn} is true.
     */
    private final float fadeInTime;

    /**
     * An instance of the {@link ScreenData} class that holds references to the UI elements.
     */
    private final ScreenData screenData = new ScreenData();

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
     * This is an enumeration of labels used in the {@link PreferencesScreen} class.
     * Each label is associated with a {@link Supplier<String>} that provides the label's text.
     * The labels are used for various UI elements in the {@link PreferencesScreen}, such as buttons and sliders.
     * The labels can also include placeholders (e.g. "{0}") that can be replaced with specific values using the {@link Labels#getLabel(String...)} method.
     */
    private enum Labels {
        /**
         * The label for the back button.
         */
        BACK_BUTTON("Head Back"),

        /**
         * The label for the title of the settings screen.
         */
        TITLE("Settings"),

        /**
         * The label for the music volume control. It includes a placeholder for the current volume level.
         */
        MUSIC_VOLUME("Music Volume {0}"),

        /**
         * The label for the music toggle button. It includes a placeholder for the current state (ON/OFF).
         */
        MUSIC_ENABLED("Music Toggle: {0}"),

        /**
         * The label for the sound toggle button. It includes a placeholder for the current state (ON/OFF).
         */
        SOUND_ENABLED("Sound Toggle: {0}"),

        /**
         * The label for the sound volume control. It includes a placeholder for the current volume level.
         */
        SOUND_VOLUME("Sound Volume {0}"),

        /**
         * The label for the debug screen toggle button. It includes a placeholder for the current state (ON/OFF).
         */
        DEBUG_SCREEN_ENABLED("Debug Screen Toggle: {0}"),

        /**
         * The label for the main menu clouds toggle button. It includes a placeholder for the current state (ON/OFF).
         */
        MAIN_MENU_CLOUDS_ENABLED("Main Menu Clouds Toggle: {0}"),

        /**
         * The label for the clouds speed control. It includes a placeholder for the current speed level.
         */
        MAIN_MENU_CLOUDS_SPEED("Clouds Speed {0}");

        /**
         * The supplier that provides the text for the label.
         */
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
    /**
     * This is a private static class within the {@link PreferencesScreen} class.
     * It is used to hold references to the various UI elements that are used in the {@link PreferencesScreen}.
     * Each field in this class represents a UI element such as a button, slider, or label.
     * The UI elements are created and initialized in the {@link PreferencesScreen} class and their references are stored in an instance of this class for easy access.
     * TODO: think of a better name for this class
     */
    private static class ScreenData {
        /**
         * The toggle button for enabling/disabling music.
         */
        private TextButton musicToggleButton;

        /**
         * The slider for adjusting the music volume.
         */
        private Slider musicVolumeSlider;

        /**
         * The label for displaying the current music volume.
         */
        private Label musicVolumeLabel;

        /**
         * The toggle button for enabling/disabling sound.
         */
        private TextButton soundToggleButton;

        /**
         * The slider for adjusting the sound volume.
         */
        private Slider soundVolumeSlider;

        /**
         * The label for displaying the current sound volume.
         */
        private Label soundVolumeLabel;

        /**
         * The toggle button for enabling/disabling the debug screen.
         */
        private TextButton debugScreenToggleButton;

        /**
         * The toggle button for enabling/disabling the main menu clouds.
         */
        private TextButton cloudsToggleButton;

        /**
         * The slider for adjusting the speed of the clouds.
         */
        private Slider cloudsSpeedSlider;

        /**
         * The label for displaying the current speed of the clouds.
         */
        private Label cloudsSpeedLabel;

        /**
         * The back button for returning to the main menu.
         */
        private TextButton backButton;
    }

    /**
     * Constructor for the {@link PreferencesScreen} class.
     * This constructor initializes a new {@link PreferencesScreen} with the given game instance.
     * The screen will not fade in when shown.
     *
     * @param game The {@link GdxGame} game instance to associate with this screen.
     */
    public PreferencesScreen(GdxGame game) {
        this(game, false);
    }

    /**
     * Constructor for the {@link PreferencesScreen} class.
     * This constructor initializes a new {@link PreferencesScreen} with the given {@link GdxGame} game instance and fade-in flag.
     * If the fade-in flag is true, the screen will fade in when shown with a default fade-in time of 0.75 seconds.
     *
     * @param game The {@link GdxGame} game instance to associate with this screen.
     * @param shouldFadeIn A flag indicating whether the screen should fade in when shown.
     */
    public PreferencesScreen(GdxGame game, boolean shouldFadeIn) {
        this(game, shouldFadeIn, 0.75f);
    }

    /**
     * Constructor for the {@link PreferencesScreen} class.
     * This constructor initializes a new {@link PreferencesScreen} with the given game instance, fade-in flag, and fade-in time.
     * If the fade-in flag is true, the screen will fade in when shown with the given fade-in time.
     * The constructor also initializes the {@link Stage} for the screen and sets it as the {@link Gdx} input processor.
     *
     * @param game The {@link GdxGame} game instance to associate with this screen.
     * @param shouldFadeIn A flag indicating whether the screen should fade in when shown.
     * @param fadeInTime The time it takes for the screen to fade in, in seconds.
     */
    public PreferencesScreen(GdxGame game, boolean shouldFadeIn, float fadeInTime) {
        super(game);
        this.shouldFadeIn = shouldFadeIn;
        this.fadeInTime = fadeInTime;
        processor = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(processor);
    }

    /**
     * This method is called when the {@link PreferencesScreen} is shown.
     * It initializes the UI elements and sets up the listeners for the UI elements.
     * If the {@link PreferencesScreen#shouldFadeIn} flag is true, it also sets up a fade-in animation for the screen.
     */
    @Override
    public void show() {
        // If the screen should fade in when shown, set the alpha of the root actor to 0 and add a fade-in action.
        if (shouldFadeIn) {
            processor.getRoot().getColor().a = 0;
            processor.getRoot().addAction(fadeIn(fadeInTime));
        }

        // Create and initialize the music toggle button and its listener.
        createMusicToggleButton();
        listenMusicToggle();

        // Create and initialize the music volume slider and its label, and set up the listener for the slider.
        createMusicVolumeSlider();
        createMusicVolumeLabel();
        listenMusicVolume();

        // Create and initialize the sound toggle button and its listener.
        createSoundToggleButton();
        listenSoundToggle();

        // Create and initialize the sound volume slider and its label, and set up the listener for the slider.
        createSoundVolumeSlider();
        createSoundVolumeLabel();
        listenSoundVolume();

        // Create and initialize the debug screen toggle button and its listener.
        createDebugScreenToggleButton();
        listenDebugScreenToggle();

        // Create and initialize the clouds toggle button and its listener.
        createCloudsToggleButton();
        listenCloudsToggle();

        // Create and initialize the clouds speed slider and its label, and set up the listener for the slider.
        createCloudsSpeedSlider();
        createCloudsSpeedLabel();
        listenCloudsSpeed();

        // Create and initialize the back button and its listener.
        createBackButton();
        listenBackButton();

        // Set up the layout of the UI elements in the table.
        setupTable();
    }

    /**
     * This method sets up a listener for the {@link ScreenData#cloudsSpeedSlider}
     * When the slider value changes, it updates the speed of the main menu clouds in the {@link MainMenuCloudsPreferences} in the {@link GamePreferences},
     * and updates the {@link ScreenData#cloudsSpeedLabel} to reflect the current speed using {@link Labels#MAIN_MENU_CLOUDS_SPEED}.
     * The speed is displayed as a percentage, rounded to the nearest whole number.
     */
    private void listenCloudsSpeed() {
        // Get the slider and label from the screen data
        Slider cloudsSpeedSlider = screenData.cloudsSpeedSlider;
        Label cloudsSpeedLabel = screenData.cloudsSpeedLabel;

        // Add a listener to the slider
        cloudsSpeedSlider.addListener(event -> {
            // Get the main menu clouds preferences
            MainMenuCloudsPreferences preference = (MainMenuCloudsPreferences) GamePreferences.MAIN_MENU_CLOUDS.getPreference();

            // Update the speed in the preferences
            preference.setSpeed(cloudsSpeedSlider.getValue());

            // Update the label text with the current speed
            cloudsSpeedLabel.setText(Labels.MAIN_MENU_CLOUDS_SPEED.getLabel(Math.round(cloudsSpeedSlider.getValue() * 100) + "%"));

            // Return true to indicate the event has been handled successfully
            return true;
        });
    }

    /**
     * This method creates a label for the clouds speed control.
     * The label text is obtained from the {@link Labels#MAIN_MENU_CLOUDS_SPEED} label in the {@link Labels} enumeration,
     * and includes the current speed of the clouds as a percentage (rounded to the nearest whole number).
     * The label is created with the {@link PreferencesScreen#craftacularSkin} skin and its reference is stored in the {@link PreferencesScreen#screenData} object.
     */
    private void createCloudsSpeedLabel() {
        screenData.cloudsSpeedLabel = new Label(Labels.MAIN_MENU_CLOUDS_SPEED.getLabel(Math.round(screenData.cloudsSpeedSlider.getValue() * 100) + "%"), craftacularSkin);
    }

    /**
     * This method creates a slider for controlling the speed of the clouds in the main menu.
     * The slider is created with the following parameters:
     * - The minimum value is 0 (the clouds are stationary).
     * - The maximum value is 3 (the clouds move at maximum speed).
     * - The step size is 0.01 (the smallest change in speed that can be made by moving the slider).
     * - The slider is horizontal (not vertical).
     * - The slider uses the {@link PreferencesScreen#craftacularSkin} skin.
     * The slider's visual position and value are set to the current speed of the main menu clouds, which is obtained from the {@link GamePreferences#MAIN_MENU_CLOUDS}.
     * The reference to the slider is stored in the {@link PreferencesScreen#screenData} object for later use.
     */
    private void createCloudsSpeedSlider() {
        screenData.cloudsSpeedSlider = new Slider(0f, 3f, 0.01f, false, craftacularSkin);
        screenData.cloudsSpeedSlider.setVisualPercent(((MainMenuCloudsPreferences) GamePreferences.MAIN_MENU_CLOUDS.getPreference()).getSpeed());
        screenData.cloudsSpeedSlider.setValue(((MainMenuCloudsPreferences) GamePreferences.MAIN_MENU_CLOUDS.getPreference()).getSpeed());
    }

    /**
     * This method sets up a listener for the {@link ScreenData#cloudsToggleButton}.
     * When the toggle button is clicked, it plays a button click sound, retrieves the current state of the main menu clouds from the {@link GamePreferences#MAIN_MENU_CLOUDS},
     * toggles the enabled state of the main menu clouds, and updates the text of the toggle button to reflect the new state.
     * The new state is obtained by negating the current state (i.e., if the clouds were enabled, they are now disabled, and vice versa).
     * The text of the toggle button is obtained from the {@link Labels#MAIN_MENU_CLOUDS_ENABLED} label in the {@link Labels} enumeration,
     * and includes the new state of the clouds (either "ON" or "OFF").
     */
    private void listenCloudsToggle() {
        TextButton cloudsToggleButton = screenData.cloudsToggleButton;
        cloudsToggleButton.addListener(new ChangeListener() {
            /**
             * This method is triggered when the {@link ScreenData#cloudsToggleButton} is clicked.
             * It plays a button click sound, retrieves the current state of the {@link GamePreferences#MAIN_MENU_CLOUDS} from the {@link GamePreferences},
             * toggles the enabled state of the {@link GamePreferences#MAIN_MENU_CLOUDS}, and updates the text of the {@link ScreenData#cloudsToggleButton} to reflect the new state.
             * The new state is obtained by negating the current state (i.e., if the clouds were enabled, they are now disabled, and vice versa).
             * The text of the toggle button is obtained from the {@link Labels} enumeration,
             * and includes the new state of the clouds (either "ON" or "OFF").
             *
             * @param event The {@link com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent} triggered by the click.
             * @param actor The {@link Actor} that was clicked.
             */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonClick.play();
                MainMenuCloudsPreferences preference = (MainMenuCloudsPreferences) GamePreferences.MAIN_MENU_CLOUDS.getPreference();
                boolean nowEnabled = !preference.isEnabled();
                preference.setEnabled(nowEnabled);
                cloudsToggleButton.setText(Labels.MAIN_MENU_CLOUDS_ENABLED.getLabel(nowEnabled ? "ON" : "OFF"));
            }
        });
    }

    /**
     * This method creates a toggle button for enabling/disabling the main menu clouds.
     * The button's initial text is obtained from the {@link Labels#MAIN_MENU_CLOUDS_ENABLED} label in the {@link Labels} enumeration,
     * and includes the current state of the main menu clouds (either "ON" or "OFF").
     * The current state of the main menu clouds is obtained from the {@link GamePreferences#MAIN_MENU_CLOUDS}.
     * The button is created with the {@link PreferencesScreen#craftacularSkin} skin.
     * The reference to the button is stored in the {@link PreferencesScreen#screenData} object for later use.
     */
    private void createCloudsToggleButton() {
        screenData.cloudsToggleButton = new TextButton(Labels.MAIN_MENU_CLOUDS_ENABLED.getLabel(((MainMenuCloudsPreferences) GamePreferences.MAIN_MENU_CLOUDS.getPreference()).isEnabled() ? "ON" : "OFF"), craftacularSkin);
    }

    /**
     * This method creates a back button for the preferences screen.
     * The button's text is obtained from the {@link Labels#BACK_BUTTON} label in the {@link Labels} enumeration.
     * The button is created with the {@link PreferencesScreen#craftacularSkin} skin.
     * The reference to the button is stored in the {@link PreferencesScreen#screenData} object for later use.
     */
    private void createBackButton() {
        screenData.backButton = new TextButton(Labels.BACK_BUTTON.getLabel(), craftacularSkin);
    }

    /**
     * This method creates a toggle button for enabling/disabling the debug screen.
     * The button's initial text is obtained from the {@link Labels#DEBUG_SCREEN_ENABLED} label in the {@link Labels} enumeration,
     * and includes the current state of the debug screen (either "ON" or "OFF").
     * The current state of the debug screen is obtained from the {@link GamePreferences#DEBUG_SCREEN}.
     * The button is created with the {@link PreferencesScreen#craftacularSkin} skin.
     * The reference to the button is stored in the {@link PreferencesScreen#screenData} object for later use.
     */
    private void createDebugScreenToggleButton() {
        screenData.debugScreenToggleButton = new TextButton(Labels.DEBUG_SCREEN_ENABLED.getLabel(((DebugScreenPreferences) GamePreferences.DEBUG_SCREEN.getPreference()).isEnabled() ? "ON" : "OFF"), craftacularSkin);
    }

    /**
     * This method creates a label for the sound volume control.
     * The label text is obtained from the {@link Labels#SOUND_VOLUME} label in the {@link Labels} enumeration,
     * and includes the current sound volume as a percentage (rounded to the nearest whole number).
     * The current sound volume is obtained from the {@link ScreenData#soundVolumeSlider}.
     * The label is created with the {@link PreferencesScreen#craftacularSkin} skin and its reference is stored in the {@link PreferencesScreen#screenData} object.
     */
    private void createSoundVolumeLabel() {
        screenData.soundVolumeLabel = new Label(Labels.SOUND_VOLUME.getLabel(Math.round(screenData.soundVolumeSlider.getValue() * 100) + "%"), craftacularSkin);
    }

    /**
     * This method creates a slider for controlling the sound volume.
     * The slider is created with the following parameters:
     * - The minimum value is 0 (no sound).
     * - The maximum value is 1 (maximum volume).
     * - The step size is 0.01 (the smallest change in volume that can be made by moving the slider).
     * - The slider is horizontal (not vertical).
     * - The slider uses the {@link PreferencesScreen#craftacularSkin} skin.
     * The slider's visual position and value are set to the current sound volume, which is obtained from the {@link GamePreferences#SOUND}.
     * The reference to the slider is stored in the {@link PreferencesScreen#screenData} object for later use.
     */
    private void createSoundVolumeSlider() {
        screenData.soundVolumeSlider = new Slider(0f, 1f, 0.01f, false, craftacularSkin);
        screenData.soundVolumeSlider.setVisualPercent(((SoundPreferences) GamePreferences.SOUND.getPreference()).getVolume());
    }

    /**
     * This method creates a toggle button for enabling/disabling the game sound.
     * The button's initial text is obtained from the {@link Labels#SOUND_ENABLED} label in the {@link Labels} enumeration,
     * and includes the current state of the game sound (either "ON" or "OFF").
     * The current state of the game sound is obtained from the {@link GamePreferences#SOUND}.
     * The button is created with the {@link PreferencesScreen#craftacularSkin} skin.
     * The reference to the button is stored in the {@link PreferencesScreen#screenData} object for later use.
     */
    private void createSoundToggleButton() {
        screenData.soundToggleButton = new TextButton(Labels.SOUND_ENABLED.getLabel(((SoundPreferences) GamePreferences.SOUND.getPreference()).isEnabled() ? "ON" : "OFF"), craftacularSkin);
    }

    /**
     * This method creates a label for the music volume control.
     * The label text is obtained from the {@link Labels#MUSIC_VOLUME} label in the {@link Labels} enumeration,
     * and includes the current music volume as a percentage (rounded to the nearest whole number).
     * The current music volume is obtained from the {@link ScreenData#musicVolumeSlider}.
     * The label is created with the {@link PreferencesScreen#craftacularSkin} skin and its reference is stored in the {@link PreferencesScreen#screenData} object.
     */
    private void createMusicVolumeLabel() {
        screenData.musicVolumeLabel = new Label(Labels.MUSIC_VOLUME.getLabel(Math.round(screenData.musicVolumeSlider.getValue() * 100) + "%"), craftacularSkin);
    }

    /**
     * This method creates a slider for controlling the music volume.
     * The slider is created with the following parameters:
     * - The minimum value is 0 (no music).
     * - The maximum value is 1 (maximum volume).
     * - The step size is 0.01 (the smallest change in volume that can be made by moving the slider).
     * - The slider is horizontal (not vertical).
     * - The slider uses the {@link PreferencesScreen#craftacularSkin} skin.
     * The slider's visual position and value are set to the current music volume, which is obtained from the {@link GamePreferences#MUSIC}.
     * The reference to the slider is stored in the {@link PreferencesScreen#screenData} object for later use.
     */
    private void createMusicVolumeSlider() {
        screenData.musicVolumeSlider = new Slider(0f, 1f, 0.01f, false, craftacularSkin);
        screenData.musicVolumeSlider.setVisualPercent(((MusicPreferences) GamePreferences.MUSIC.getPreference()).getVolume());
    }

    /**
     * This method creates a toggle button for enabling/disabling the game music.
     * The button's initial text is obtained from the {@link Labels#MUSIC_ENABLED} label in the {@link Labels} enumeration,
     * and includes the current state of the game music (either "ON" or "OFF").
     * The current state of the game music is obtained from the {@link GamePreferences#MUSIC}.
     * The button is created with the {@link PreferencesScreen#craftacularSkin} skin.
     * The reference to the button is stored in the {@link PreferencesScreen#screenData} object for later use.
     */
    private void createMusicToggleButton() {
        screenData.musicToggleButton = new TextButton(Labels.MUSIC_ENABLED.getLabel(((MusicPreferences) GamePreferences.MUSIC.getPreference()).isEnabled() ? "ON" : "OFF"), craftacularSkin);
    }

    /**
     * This method sets up a listener for the {@link ScreenData#backButton}.
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
     * The UI elements are retrieved from the {@link PreferencesScreen#screenData} object.
     * The table is set to fill the parent actor and its skin is set to {@link PreferencesScreen#craftacularSkin}.
     * If the debug screen is enabled in the {@link GamePreferences#DEBUG_SCREEN}, the table's debug lines are shown.
     * The table is then added to the {@link PreferencesScreen#processor} {@link Stage}.
     * The UI elements are added to the table in a specific order and layout to create the desired appearance for the {@link PreferencesScreen}.
     * The method also sets up some properties for the UI elements, such as the alignment of the labels and the touchable state of the sliders.
     */
    private void setupTable() {
        // Retrieve the UI elements from the screen data
        TextButton musicToggleButton = screenData.musicToggleButton;
        Slider musicVolumeSlider = screenData.musicVolumeSlider;
        Label musicVolumeLabel = screenData.musicVolumeLabel;
        TextButton soundToggleButton = screenData.soundToggleButton;
        Slider soundVolumeSlider = screenData.soundVolumeSlider;
        Label soundVolumeLabel = screenData.soundVolumeLabel;
        TextButton debugScreenToggleButton = screenData.debugScreenToggleButton;
        TextButton backButton = screenData.backButton;
        TextButton cloudsToggleButton = screenData.cloudsToggleButton;
        Slider cloudsSpeedSlider = screenData.cloudsSpeedSlider;
        Label cloudsSpeedLabel = screenData.cloudsSpeedLabel;

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
        Cell<Label> titleCell = table.add(Labels.TITLE.getLabel()).colspan(2).pad(0, 0, 100, 0);
        // Set the font scale of the title label
        titleCell.getActor().setFontScale(1.5f);

        // Add a new row to the table
        table.row();
        // Add the music toggle button to the table
        table.add(musicToggleButton).fillX().uniformX();
        // Add the clouds toggle button to the table
        table.add(cloudsToggleButton).fillX().minWidth(cloudsToggleButton.getWidth()*1.1f).uniformX().pad(0, 50, 0, 0);
        // Add a new row to the table
        table.row();

        // Create a stack for the music volume slider and label
        Stack stack = new Stack();
        // Add the music volume slider to the stack
        stack.add(musicVolumeSlider);
        // Centre the music volume label
        musicVolumeLabel.setAlignment(1);
        // Make it so the music volume label is not interactable
        musicVolumeLabel.setTouchable(Touchable.disabled);
        // Add the music volume label to the stack
        stack.add(musicVolumeLabel);
        // Add the stack to the table
        table.add(stack).center().fillX().uniformX().pad(0, 0, 25 ,0);

        // Create a stack for the clouds speed slider and label
        stack = new Stack();
        // Add the clouds speed slider to the stack
        stack.add(cloudsSpeedSlider);
        // Centre the clouds speed label
        cloudsSpeedLabel.setAlignment(1);
        // Make it so the clouds speed label is not interactable
        cloudsSpeedLabel.setTouchable(Touchable.disabled);
        // Add the clouds speed label to the stack
        stack.add(cloudsSpeedLabel);
        // Add the stack to the table
        table.add(stack).center().fillX().uniformX().pad(0, 50, 25, 0);

        // Add a new row to the table
        table.row();
        // Add the sound toggle button to the table
        table.add(soundToggleButton).fillX().uniformX();
        // Add a new row to the table
        table.row();

        // Create a stack for the sound volume slider and label
        stack = new Stack();
        // Add the sound volume slider to the stack
        stack.add(soundVolumeSlider);
        // Centre the sound volume label
        soundVolumeLabel.setAlignment(1);
        // Make it so the sound volume label is not interactable
        soundVolumeLabel.setTouchable(Touchable.disabled);
        // Add the sound volume label to the stack
        stack.add(soundVolumeLabel);
        // Add the stack to the table
        table.add(stack).center().fillX().uniformX().pad(0, 0, 25, 0);

        // Add a new row to the table
        table.row();
        // Add the debug screen toggle button to the table
        table.add(debugScreenToggleButton).fillX().uniformX().pad(0, 0, 25, 0);
        // Add a new row to the table
        table.row();

        // Add a large gap before the back button
        table.row().pad(150, 0, 0, 0);
        // Add the back button to the table, spanning across both columns
        table.add(backButton).colspan(2).fillX().uniformX();
    }

    /**
     * This method sets up a listener for the {@link ScreenData#debugScreenToggleButton}.
     * When the debug screen toggle button is clicked, it plays a button click sound, retrieves the current state of the debug screen from the {@link GamePreferences#DEBUG_SCREEN},
     * toggles the enabled state of the debug screen, and transitions the game to the preferences screen.
     * The new state is obtained by negating the current state (i.e., if the debug screen was enabled, it is now disabled, and vice versa).
     * The transition to the preferences screen is performed by calling the {@link GdxGame#setScreen(Class)} method with the {@link Screens#PREFERENCES} parameter.
     */
    private void listenDebugScreenToggle() {
        TextButton debugScreenToggleButton = screenData.debugScreenToggleButton;
        debugScreenToggleButton.addListener(new ChangeListener() {
            /**
            * This method is triggered when the debug screen toggle button is clicked.
            * It plays a button click sound, retrieves the current state of the debug screen from the {@link GamePreferences#DEBUG_SCREEN},
            * toggles the enabled state of the debug screen, and transitions the game to the preferences screen.
            * The new state is obtained by negating the current state (i.e., if the debug screen was enabled, it is now disabled, and vice versa).
            * The transition to the preferences screen is performed by calling the {@link GdxGame#setScreen(Class)} method with the {@link Screens#PREFERENCES} parameter.
            *
            * @param event The {@link com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent} triggered by the click.
            * @param actor The {@link Actor} that was clicked.
            */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonClick.play();
                DebugScreenPreferences preference = (DebugScreenPreferences) GamePreferences.DEBUG_SCREEN.getPreference();
                boolean nowEnabled = !preference.isEnabled();
                preference.setEnabled(nowEnabled);
                game.setScreen(Screens.PREFERENCES);
            }
        });
    }

    /**
     * This method sets up a listener for the {@link ScreenData#soundVolumeSlider}.
     * When the slider value changes, it updates the volume of the game sound in the {@link GamePreferences#SOUND},
     * and updates the {@link ScreenData#soundVolumeLabel} to reflect the current volume using {@link Labels#SOUND_VOLUME}.
     * The volume is displayed as a percentage, rounded to the nearest whole number.
     */
    private void listenSoundVolume() {
        Slider soundVolumeSlider = screenData.soundVolumeSlider;
        Label soundVolumeLabel = screenData.soundVolumeLabel;
        soundVolumeSlider.addListener(event -> {
            SoundPreferences preference = (SoundPreferences) GamePreferences.SOUND.getPreference();
            preference.setVolume(soundVolumeSlider.getValue());
            soundVolumeLabel.setText(Labels.SOUND_VOLUME.getLabel(Math.round(soundVolumeSlider.getValue() * 100) + "%"));
            return false;
        });
    }

    /**
     * This method sets up a listener for the {@link ScreenData#soundToggleButton}.
     * When the sound toggle button is clicked, it plays a button click sound, retrieves the current state of the game sound from the {@link GamePreferences#SOUND},
     * toggles the enabled state of the game sound, and updates the text of the toggle button to reflect the new state.
     * The new state is obtained by negating the current state (i.e., if the sound was enabled, it is now disabled, and vice versa).
     * The text of the toggle button is obtained from the {@link Labels#SOUND_ENABLED} label in the {@link Labels} enumeration,
     * and includes the new state of the sound (either "ON" or "OFF").
     */
    private void listenSoundToggle() {
        TextButton soundToggleButton = screenData.soundToggleButton;
        soundToggleButton.addListener(new ChangeListener() {
            /**
             * This method is triggered when the sound toggle button is clicked.
             * It retrieves the current state of the game sound from the {@link GamePreferences#SOUND},
             * toggles the enabled state of the game sound, and updates the text of the toggle button to reflect the new state.
             * The new state is obtained by negating the current state (i.e., if the sound was enabled, it is now disabled, and vice versa).
             * The text of the toggle button is obtained from the {@link Labels#SOUND_ENABLED} label in the {@link Labels} enumeration,
             * and includes the new state of the sound (either "ON" or "OFF").
             * After these operations, it plays a button click sound.
             *
             * @param event The {@link com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent} triggered by the click.
             * @param actor The {@link Actor} that was clicked.
             */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundPreferences preference = (SoundPreferences) GamePreferences.SOUND.getPreference();
                boolean nowEnabled = !preference.isEnabled();
                preference.setEnabled(nowEnabled);
                soundToggleButton.setText(Labels.SOUND_ENABLED.getLabel(nowEnabled ? "ON" : "OFF"));
                buttonClick.play();
            }
        });
    }

    /**
     * This method sets up a listener for the {@link ScreenData#musicVolumeSlider}.
     * When the slider value changes, it updates the volume of the game music in the {@link GamePreferences#MUSIC},
     * and updates the {@link MusicManager#BACKGROUND_MUSIC} volume to match the slider value.
     * It also updates the {@link ScreenData#musicVolumeLabel} to reflect the current volume using {@link Labels#MUSIC_VOLUME}.
     * The volume is displayed as a percentage, rounded to the nearest whole number.
     */
    private void listenMusicVolume() {
        Slider musicVolumeSlider = screenData.musicVolumeSlider;
        Label musicVolumeLabel = screenData.musicVolumeLabel;
        musicVolumeSlider.addListener(event -> {
            MusicPreferences preference = (MusicPreferences) GamePreferences.MUSIC.getPreference();
            preference.setVolume(musicVolumeSlider.getValue());
            MusicManager.BACKGROUND_MUSIC.setVolume(musicVolumeSlider.getValue());
            musicVolumeLabel.setText(Labels.MUSIC_VOLUME.getLabel(Math.round(musicVolumeSlider.getValue() * 100) + "%"));
            return false;
        });
    }

    /**
     * This method sets up a listener for the {@link ScreenData#musicToggleButton}.
     * When the music toggle button is clicked, it plays a button click sound, retrieves the current state of the game music from the {@link GamePreferences#MUSIC},
     * toggles the enabled state of the game music, and updates the text of the toggle button to reflect the new state.
     * The new state is obtained by negating the current state (i.e., if the music was enabled, it is now disabled, and vice versa).
     * The text of the toggle button is obtained from the {@link Labels#MUSIC_ENABLED} label in the {@link Labels} enumeration,
     * and includes the new state of the music (either "ON" or "OFF").
     * If the music is now enabled, it calls the {@link MusicManager#onEnable()} method, otherwise it calls the {@link MusicManager#onDisable()} method.
     */
    private void listenMusicToggle() {
        TextButton musicToggleButton = screenData.musicToggleButton;
        musicToggleButton.addListener(new ChangeListener() {
            /**
             * This method is triggered when the music toggle button is clicked.
             * It plays a button click sound, retrieves the current state of the game music from the {@link GamePreferences#MUSIC},
             * toggles the enabled state of the game music, and updates the text of the toggle button to reflect the new state.
             * The new state is obtained by negating the current state (i.e., if the music was enabled, it is now disabled, and vice versa).
             * The text of the toggle button is obtained from the {@link Labels#MUSIC_ENABLED} label in the {@link Labels} enumeration,
             * and includes the new state of the music (either "ON" or "OFF").
             * If the music is now enabled, it calls the {@link MusicManager#onEnable()} method, otherwise it calls the {@link MusicManager#onDisable()} method.
             *
             * @param event The {@link com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent} triggered by the click.
             * @param actor The {@link Actor} that was clicked.
             *
             */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                buttonClick.play();
                MusicPreferences preference = (MusicPreferences) GamePreferences.MUSIC.getPreference();
                boolean nowEnabled = !preference.isEnabled();
                preference.setEnabled(nowEnabled);
                musicToggleButton.setText(Labels.MUSIC_ENABLED.getLabel(nowEnabled ? "ON" : "OFF"));
                if (nowEnabled) {
                    MusicManager.getInstance().onEnable();
                } else {
                    MusicManager.getInstance().onDisable();
                }
            }
        });
    }

    /**
     * This method is responsible for rendering the game screen.
     * It first clears the screen with a black color using {@link Gdx#gl}
     * Then, it begins a new batch of drawing commands using the {@link PreferencesScreen#processor}'s batch.
     * It draws the {@link PreferencesScreen#stoneWallTexture} in a grid pattern across the entire screen.
     * The size of each tile in the grid is determined by dividing the width and height of the texture by 6.
     * After that, it draws a gradient texture at the bottom of the screen using the {@link PreferencesScreen#bottomUpBlackGradient}.
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
     * It updates the viewport of the {@link PreferencesScreen#processor} {@link Stage} to match the new window size.
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
     * It disposes of the resources that were created in the {@link PreferencesScreen} class to free up memory.
     * The resources that are disposed of include:
     * - The {@link PreferencesScreen#processor} {@link Stage}, which is used to manage and render the UI elements.
     * - The {@link PreferencesScreen#craftacularSkin} {@link Skin}, which is used for the UI elements.
     * - The {@link PreferencesScreen#buttonClick} {@link GameSound}, which is the sound that is played when a button is clicked.
     */
    @Override
    public void dispose() {
        processor.dispose();
        craftacularSkin.dispose();
        buttonClick.dispose();
    }
}
