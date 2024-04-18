package uk.ac.york.student.screens;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
import uk.ac.york.student.GdxGame;
import uk.ac.york.student.assets.fonts.FontManager;

@Deprecated(forRemoval = true)
public class ExitScreen extends BaseScreen {
    @Getter
    private final Stage processor;
    public ExitScreen(GdxGame game) {
        super(game);
        processor = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        BitmapFont font = FontManager.getInstance().getFont();
    }

    @Override
    public void render(float v) {

    }

    @Override
    public void resize(int i, int i1) {

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

    }
}
