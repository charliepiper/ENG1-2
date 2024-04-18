package uk.ac.york.student.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import uk.ac.york.student.GdxGame;

/**
 * The BaseScreen class is an abstract class that implements the Screen interface from the libGDX library.
 * This class serves as a base for all other screen classes in the game.
 * It contains a protected final instance of the GdxGame class, which represents the game itself.
 * The class also includes an abstract method getProcessor() that returns a {@link Stage} object.
 * Each subclass of BaseScreen must implement this method.
 */
public abstract class BaseScreen implements Screen {
    /**
     * The GdxGame instance for the BaseScreen class.
     * This instance represents the game itself.
     */
    protected final GdxGame game;

    /**
     * Constructor for the BaseScreen class.
     * This constructor initializes the GdxGame instance with the provided game.
     * @param game the GdxGame instance representing the game
     */
    protected BaseScreen(GdxGame game) {
        this.game = game;
    }

    /**
     * Get the processor for the screen.
     * This is an abstract method that returns a {@link Stage} object.
     * Each subclass of BaseScreen must implement this method.
     * @return the processor for the screen
     */
    public abstract Stage getProcessor();
}