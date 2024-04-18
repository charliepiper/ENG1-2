package uk.ac.york.student.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
import uk.ac.york.student.GdxGame;

/**
 * The LoadingScreen class extends the BaseScreen class and represents the loading screen of the game.
 * It contains a Stage object, which is used to handle input events and draw the elements of the screen.
 * The class overrides the methods of the Screen interface, which are called at different points in the game's lifecycle.
 */
@Getter
public class LoadingScreen extends BaseScreen {
    /**
     * The Stage instance for the LoadingScreen class.
     * This instance is used to handle input events and draw the elements of the screen.
     */
    private final Stage processor;

    /**
     * Constructor for the LoadingScreen class.
     * This constructor initializes the BaseScreen with the provided game and creates a new Stage with a ScreenViewport.
     * @param game the GdxGame instance representing the game
     */
    public LoadingScreen(GdxGame game) {
        super(game);
        processor = new Stage(new ScreenViewport());
    }

    /**
     * This method is called when the screen becomes the current screen for the game.
     * It is empty in the LoadingScreen class.
     */
    @Override
    public void show() {

    }

    /**
     * This method is called by the game's render loop.
     * In the LoadingScreen class, it sets the current screen of the game to the main menu screen.
     * @param v the time in seconds since the last render
     */
    @Override
    public void render(float v) {
        game.setScreen(Screens.MAIN_MENU, true);
    }

    /**
     * This method is called when the screen should resize itself.
     * It is empty in the LoadingScreen class.
     * @param i the new width in pixels
     * @param i1 the new height in pixels
     */
    @Override
    public void resize(int i, int i1) {

    }

    /**
     * This method is called when the game is paused.
     * It is empty in the LoadingScreen class.
     */
    @Override
    public void pause() {

    }

    /**
     * This method is called when the game is resumed from a paused state.
     * It is empty in the LoadingScreen class.
     */
    @Override
    public void resume() {

    }

    /**
     * This method is called when the screen is no longer the current screen for the game.
     * It is empty in the LoadingScreen class.
     */
    @Override
    public void hide() {

    }

    /**
     * This method is called when the screen should release all resources.
     * It is empty in the LoadingScreen class.
     */
    @Override
    public void dispose() {

    }
}