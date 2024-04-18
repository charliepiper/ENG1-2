package uk.ac.york.student.assets.skins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import uk.ac.york.student.utils.EnumMapOfSuppliers;

import java.util.function.Supplier;

/**
 * This utility class manages the loading and storage of Skin objects.
 * It uses an EnumMapOfSuppliers to store the skins, allowing for lazy loading.
 */
@UtilityClass
public class SkinManager {
    /**
     * Supplier for the Craftacular skin
     */
    private static final Supplier<Skin> craftacular = () -> new Skin(Gdx.files.internal("skins/craftacular/skin/craftacular-ui.json"));

    /**
     * EnumMapOfSuppliers that maps from Skins enum to Skin objects
     * <p>
     * Use {@link uk.ac.york.student.utils.EnumMapOfSuppliers#getResult(Enum)} to get the Skin object for a given skin
     */
    @Getter
    private static final EnumMapOfSuppliers<Skins, Skin> skins = new EnumMapOfSuppliers<>(Skins.class);

    // Static initializer block that loads the skins
    static {
        skins.put(Skins.CRAFTACULAR, craftacular);
    }
}