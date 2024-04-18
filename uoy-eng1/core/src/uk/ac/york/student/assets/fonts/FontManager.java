package uk.ac.york.student.assets.fonts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import lombok.Getter;

/**
 * Singleton class that manages the fonts for the game.
 * It provides a BitmapFont object that can be used to draw text in the game.
 */
@Getter
public class FontManager {
    /**
     * The instance of the font manager.
     * It is a static final instance of FontManager.
     */
    @Getter
    private static final FontManager instance = new FontManager();

    /**
     * The BitmapFont object that can be used to draw text in the game
     */
    private final BitmapFont font;

    /**
     * Private constructor to prevent instantiation.
     * As this is a singleton class, the constructor is private.
     * It initializes the BitmapFont object with a font file and a font size.
     */
    private FontManager() {
        // The FreeTypeFontGenerator object is used to generate a BitmapFont object
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PixelifySans-Regular.ttf"));
        // The FreeTypeFontParameter object is used to set the parameters for the BitmapFont object
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        // Set the font size
        parameter.size = 12;
        // Generate the BitmapFont object
        font = generator.generateFont(parameter);
        // Dispose the FreeTypeFontGenerator object as it is no longer needed
        generator.dispose();
    }
}