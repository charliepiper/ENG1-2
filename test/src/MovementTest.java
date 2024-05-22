import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.ac.york.student.player.Player;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class MovementTest {
    private Player player;
    private InputAdapter inputProcessor;

    @Before
    public void setup() {
        TiledMap mockTiledMap = new TiledMap();
        TiledMapTileLayer layer = new TiledMapTileLayer(10, 10, 32, 32); // Create a dummy layer

        // Add the gameObjects layer to the map
        TiledMapTileLayer gameObjectsLayer = new TiledMapTileLayer(10, 10, 32, 32);
        gameObjectsLayer.setName("gameObjects");
        mockTiledMap.getLayers().add(gameObjectsLayer); // Add the gameObjects layer to the map

        mockTiledMap.getLayers().add(layer); // Add the layer to the map
        Vector2 mockVector2 = new Vector2();
        player = new Player(mockTiledMap, mockVector2);
    }


    @Test
    public void testUP() {
        assertTrue(player.keyUp(Input.Keys.UP));
        assertTrue(player.keyUp(Input.Keys.W));
    }

    @Test
    public void testDown() {
        assertTrue(player.keyUp(Input.Keys.DOWN));
        assertTrue(player.keyUp(Input.Keys.S));

    }
    @Test
    public void testLEFT() {
        assertTrue(player.keyUp(Input.Keys.LEFT));
        assertTrue(player.keyUp(Input.Keys.A));
    }

    @Test
    public void testRIGHT() {
        assertTrue(player.keyUp(Input.Keys.RIGHT));
        assertTrue(player.keyUp(Input.Keys.D));

    }


}