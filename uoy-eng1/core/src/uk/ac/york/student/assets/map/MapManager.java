package uk.ac.york.student.assets.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import uk.ac.york.student.utils.MapOfSuppliers;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * This utility class manages the loading and storage of TiledMap objects.
 * It uses a MapOfSuppliers to store the maps, allowing for lazy loading.
 */
@UtilityClass
public final class MapManager {
    /**
     * A MapOfSuppliers that maps from map names (as a string) to TiledMap objects
     * <p>
     * Use {@link uk.ac.york.student.utils.MapOfSuppliers#getResult(Object)} to get the TiledMap object for a given map name
     */
    @Getter
    private static final MapOfSuppliers<String, TiledMap> maps = new MapOfSuppliers<>();

    public static void onEnable() {

        List<String> maps = List.of("map", "blankMap", "inside_house");
        // Create parameters for loading the maps
        TmxMapLoader.Parameters parameter = new TmxMapLoader.Parameters();
        parameter.textureMinFilter = Texture.TextureFilter.Nearest;
        parameter.textureMagFilter = Texture.TextureFilter.Nearest;

        // Load each map file in the directory (and hide the potential NullPointerException with Objects.requireNonNull)
        for (String map : maps) {
            map = "map/" + map + ".tmx";
            FileHandle internal = Gdx.files.internal(map);
            File file = internal.file();
            // Only load files with the ".tmx" extension
            if (file.getName().endsWith(".tmx")) {
                // Add the map to the MapOfSuppliers, using a lambda to allow for lazy loading
                MapManager.maps.put(file.getName().replace(".tmx", ""), () -> new TmxMapLoader().load("map/" + file.getName(), parameter));
            }
        }
    }
}