package uk.ac.york.student.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for creating {@link Drawable}'s with specific colours.
 * This class is marked with Lombok's {@link UtilityClass}, which means it has an implicit private constructor.
 * All methods in this class should be static.
 */
@UtilityClass
public class DrawableUtils {
    /**
     * Creates a {@link Drawable} with the specified width, height and colour.
     *
     * @param width  The width of the {@link Drawable} to be created.
     * @param height The height of the {@link Drawable} to be created.
     * @param color  The colour of the {@link Drawable} to be created.
     * @return A {@link Drawable} of the specified width, height and colour.
     */
    public static @NotNull Drawable getColouredDrawable(int width, int height, Color color) {
        // Create a new Pixmap with the specified width, height, and format.
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        // Set the color of the Pixmap to the specified color.
        pixmap.setColor(color);

        // Fill the Pixmap with the specified color.
        pixmap.fill();

        // Create a new Texture from the Pixmap, then create a new TextureRegion from the Texture.
        // Finally, create a new TextureRegionDrawable from the TextureRegion.
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        // Dispose of the Pixmap to free up memory resources.
        pixmap.dispose();

        // Return the created TextureRegionDrawable.
        return drawable;
    }
}
