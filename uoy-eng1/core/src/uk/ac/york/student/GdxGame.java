package uk.ac.york.student;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import org.jetbrains.annotations.NotNull;
import uk.ac.york.student.assets.map.MapManager;
import uk.ac.york.student.audio.AudioManager;
import uk.ac.york.student.audio.music.MusicManager;
import uk.ac.york.student.audio.sound.SoundManager;
import uk.ac.york.student.screens.BaseScreen;
import uk.ac.york.student.screens.Screens;

import java.lang.reflect.InvocationTargetException;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;

/**
 * This is the main game class for the application. It extends the {@link Game} class from the libGDX library.
 * The {@link Game} class is the base class for all games, handling the lifecycle of the application and the transition between screens.
 * This class is declared as final, meaning it cannot be subclassed.
 */
public final class GdxGame extends Game {

	/**
	 * Default constructor for the {@link GdxGame} class.
	 * Calls the superclass constructor.
	 */
	public GdxGame() {
		super();
	}

	/**
	 * This method is called when the application is created.
	 * It initializes the {@link MusicManager} and {@link SoundManager} and sets the initial screen to {@link Screens#LOADING}
	 */
	@Override
	public void create() {
		// Get the instance of the music manager and enable it
		final AudioManager musicManager = MusicManager.getInstance();
		musicManager.onEnable();

		// Get the instance of the sound manager and enable it
		final AudioManager soundManager = SoundManager.getInstance();
		soundManager.onEnable();

		MapManager.onEnable();

		// Set the initial screen to the loading screen
		setScreen(Screens.LOADING);
	}

	/**
	 * Sets the current screen to the specified screen class.
	 * The current screen, retrieved from {@link GdxGame#getScreen()}, is disposed using {@link BaseScreen#dispose()} before the new screen is set.
	 * The new screen is instantiated using reflection, with the constructor that takes a single argument of type {@link GdxGame}.
	 * If an error occurs during the instantiation of the new screen, an error is logged and the application is exited.
	 *
	 * @param screen The class of the screen to set. This class must extend {@link BaseScreen} and have a constructor that takes a single argument of type {@link GdxGame}.
	 */
	public void setScreen(@NotNull Class<? extends BaseScreen> screen) {
		// Dispose the current screen if it exists
		final Screen currentScreen = getScreen();
		if (currentScreen != null) {
			currentScreen.dispose();
		}

		// Create a new screen instance
		BaseScreen newScreen;
		try {
			// Instantiate the new screen using reflection
			newScreen = screen.getConstructor(GdxGame.class).newInstance(this);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// Log the error and exit the application if the new screen cannot be instantiated
			Gdx.app.error("LetRonCooke", "Error loading screen", e);
			Gdx.app.exit();
			return;
		}

		// Set the new screen
		super.setScreen(newScreen);
	}

	/**
	 * Sets the current screen to the specified screen class with an option to fade in.
	 * The current screen, retrieved from {@link GdxGame#getScreen()}, is disposed using {@link BaseScreen#dispose()} before the new screen is set.
	 * The new screen is instantiated using reflection, with the constructor that takes a single argument of type {@link GdxGame} and a boolean indicating whether the screen should fade in.
	 * If an error occurs during the instantiation of the new screen, an error is logged and the application is exited.
	 *
	 * @param screen The class of the screen to set. This class must extend {@link BaseScreen} and have a constructor that takes a single argument of type {@link GdxGame} and a boolean.
	 * @param shouldFadeIn A boolean indicating whether the new screen should fade in.
	 */
	public void setScreen(@NotNull Class<? extends BaseScreen> screen, boolean shouldFadeIn) {
		// Dispose the current screen if it exists
		final Screen currentScreen = getScreen();
		if (currentScreen != null) {
			currentScreen.dispose();
		}

		// Create a new screen instance
		BaseScreen newScreen;
		try {
			// Instantiate the new screen using reflection
			newScreen = screen.getConstructor(GdxGame.class, boolean.class).newInstance(this, shouldFadeIn);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// Log the error and exit the application if the new screen cannot be instantiated
			Gdx.app.error("LetRonCooke", "Error loading screen", e);
			Gdx.app.exit();
			return;
		}

		// Set the new screen
		super.setScreen(newScreen);
	}

	/**
	 * Sets the current screen to the specified screen class with an option to fade in and a specified fade-in time.
	 * The current screen, retrieved from {@link GdxGame#getScreen()}, is disposed using {@link BaseScreen#dispose()} before the new screen is set.
	 * The new screen is instantiated using reflection, with the constructor that takes a single argument of type {@link GdxGame}, a boolean indicating whether the screen should fade in, and a float specifying the fade-in time.
	 * If an error occurs during the instantiation of the new screen, an error is logged and the application is exited.
	 *
	 * @param screen The class of the screen to set. This class must extend {@link BaseScreen} and have a constructor that takes a single argument of type {@link GdxGame}, a boolean, and a float.
	 * @param shouldFadeIn A boolean indicating whether the new screen should fade in.
	 * @param fadeInTime A float specifying the time for the fade-in effect in seconds
	 */
	public void setScreen(@NotNull Class<? extends BaseScreen> screen, boolean shouldFadeIn, float fadeInTime) {
		// Dispose the current screen if it exists
		final Screen currentScreen = getScreen();
		if (currentScreen != null) {
			currentScreen.dispose();
		}

		// Create a new screen instance
		BaseScreen newScreen;
		try {
			// Instantiate the new screen using reflection
			newScreen = screen.getConstructor(GdxGame.class, boolean.class, float.class).newInstance(this, shouldFadeIn, fadeInTime);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// Log the error and exit the application if the new screen cannot be instantiated
			Gdx.app.error("LetRonCooke", "Error loading screen", e);
			Gdx.app.exit();
			return;
		}

		// Set the new screen
		super.setScreen(newScreen);
	}

	/**
	 * Sets the current screen to the specified screen class with an option to fade in, a specified fade-in time, and additional initialization arguments.
	 * The current screen, retrieved from {@link GdxGame#getScreen()}, is disposed using {@link BaseScreen#dispose()} before the new screen is set.
	 * The new screen is instantiated using reflection, with the constructor that takes a single argument of type {@link GdxGame}, a boolean indicating whether the screen should fade in, a float specifying the fade-in time, and an array of Objects as additional initialization arguments.
	 * If an error occurs during the instantiation of the new screen, an error is logged and the application is exited.
	 *
	 * @param screen The class of the screen to set. This class must extend {@link BaseScreen} and have a constructor that takes a single argument of type {@link GdxGame}, a boolean, a float, and an array of {@link Object}s.
	 * @param shouldFadeIn A boolean indicating whether the new screen should fade in.
	 * @param fadeInTime A float specifying the time for the fade-in effect in seconds.
	 * @param initArgs An array of {@link Object}s that are passed as additional initialization arguments to the new screen's constructor.
	 */
	public void setScreen(@NotNull Class<? extends BaseScreen> screen, boolean shouldFadeIn, float fadeInTime, Object ... initArgs) {
		// Dispose the current screen if it exists
		final Screen currentScreen = getScreen();
		if (currentScreen != null) {
			currentScreen.dispose();
		}

		// Create a new screen instance
		BaseScreen newScreen;
		try {
			// Instantiate the new screen using reflection
			newScreen = screen.getConstructor(GdxGame.class, boolean.class, float.class, Object[].class).newInstance(this, shouldFadeIn, fadeInTime, initArgs);
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// Log the error and exit the application if the new screen cannot be instantiated
			Gdx.app.error("LetRonCooke", "Error loading screen", e);
			Gdx.app.exit();
			return;
		}

		// Set the new screen
		super.setScreen(newScreen);
	}

	/**
	 * Transitions to a new screen with a fade-out effect.
	 * The current screen, retrieved from {@link GdxGame#getScreen()}, is checked if it's an instance of {@link BaseScreen}.
	 * If the current screen is null or not an instance of {@link BaseScreen}, the new screen is set immediately without transition.
	 * If the current screen is an instance of {@link BaseScreen}, a fade-out effect is applied to the current screen before transitioning to the new screen.
	 * The new screen is set with a fade-in effect.
	 *
	 * @param screen The class of the screen to transition to. This class must extend {@link BaseScreen} and have a constructor that takes a single argument of type {@link GdxGame}.
	 */
	public void transitionScreen(@NotNull Class<? extends BaseScreen> screen) {
		// Dispose current screen
		final Screen currentScreen = getScreen();
		if (currentScreen == null) {
			setScreen(screen);
			return;
		}

		if (!(currentScreen instanceof BaseScreen)) {
			setScreen(screen);
			return;
		}

		final BaseScreen baseScreen = (BaseScreen) currentScreen;

		// Set the alpha value of the root's color to 1
		baseScreen.getProcessor().getRoot().getColor().a = 1;
		SequenceAction sequenceAction = new SequenceAction();
		// Add a fade-out action to the sequence
		sequenceAction.addAction(Actions.fadeOut(0.5f));
		// Add a run action to the sequence that sets the new screen with a fade-in effect
		sequenceAction.addAction(Actions.run(() -> setScreen(screen, true, 0.5f)));
		// Add the sequence action to the root
		baseScreen.getProcessor().getRoot().addAction(sequenceAction);
	}

	/**
	 * Transitions to a new screen with a fade-out effect and additional initialization arguments.
	 * The current screen, retrieved from {@link GdxGame#getScreen()}, is checked if it's an instance of {@link BaseScreen}.
	 * If the current screen is null or not an instance of {@link BaseScreen}, the new screen is set immediately with a fade-in effect and the provided initialization arguments.
	 * If the current screen is an instance of {@link BaseScreen}, a fade-out effect is applied to the current screen before transitioning to the new screen.
	 * The new screen is set with a fade-in effect and the provided initialization arguments.
	 *
	 * @param screen The class of the screen to transition to. This class must extend {@link BaseScreen} and have a constructor that takes a single argument of type {@link GdxGame}, a boolean, a float, and an array of {@link Object}s.
	 * @param initArgs An array of {@link Object}s that are passed as additional initialization arguments to the new screen's constructor.
	 */
	public void transitionScreen(@NotNull Class<? extends BaseScreen> screen, Object ... initArgs) {
		// Dispose current screen
		final Screen currentScreen = getScreen();
		if (currentScreen == null) {
			setScreen(screen, true, 0.5f, initArgs);
			return;
		}

		if (!(currentScreen instanceof BaseScreen)) {
			setScreen(screen, true, 0.5f, initArgs);
			return;
		}

		final BaseScreen baseScreen = (BaseScreen) currentScreen;

		// Set the alpha value of the root's color to 1
		baseScreen.getProcessor().getRoot().getColor().a = 1;
		SequenceAction sequenceAction = new SequenceAction();
		// Add a fade-out action to the sequence
		sequenceAction.addAction(Actions.fadeOut(0.5f));
		// Add a run action to the sequence that sets the new screen with a fade-in effect and the provided initialization arguments
		sequenceAction.addAction(Actions.run(() -> setScreen(screen, true, 0.5f, initArgs)));
		// Add the sequence action to the root
		baseScreen.getProcessor().getRoot().addAction(sequenceAction);
	}


	/**
	 * Renders the game, updating the screen display.
	 * This method is called by the game loop from the application every time rendering should be performed.
	 * This method calls the render method of the superclass {@link Game}, which in turn calls the render method of the current screen.
	 */
	@Override
	public void render() {
		super.render();
	}

	/**
	 * Disposes the game, cleaning up resources.
	 * This method is called when the application is about to be closed.
	 * It retrieves the instances of {@link MusicManager} and {@link SoundManager} and disables them, stopping all audio playback and releasing audio resources.
	 */
	@Override
	public void dispose() {
		// Get the instance of the music manager and disable it
		final AudioManager musicManager = MusicManager.getInstance();
		musicManager.onDisable();

		// Get the instance of the sound manager and disable it
		final AudioManager soundManager = SoundManager.getInstance();
		soundManager.onDisable();
	}
}
