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

/**
 * MovementTest is a JUnit test class that verifies the movement functionality of the Player class.
 * It ensures that the player can move in all four cardinal directions using both arrow keys and
 * corresponding W, A, S, D keys.
 */
@RunWith(GdxTestRunner.class)
public class MovementTest {
    private Player player;
    private InputAdapter inputProcessor;

    /**
     * Sets up the testing environment before each test. It initializes a mock TiledMap with layers
     * and a Player instance.
     */
    @Before
    public void setup() {
        TiledMap mockTiledMap = new TiledMap();
        TiledMapTileLayer layer = new TiledMapTileLayer(10, 10, 32, 32); // Create a dummy layer

        TiledMapTileLayer gameObjectsLayer = new TiledMapTileLayer(10, 10, 32, 32);
        gameObjectsLayer.setName("gameObjects");
        mockTiledMap.getLayers().add(gameObjectsLayer);

        mockTiledMap.getLayers().add(layer);
        Vector2 mockVector2 = new Vector2();
        player = new Player(mockTiledMap, mockVector2);
    }

    /**
     * Tests the player's movement upwards using the UP arrow key and W key.
     */
    @Test
    public void testUP() {
        assertTrue(player.keyUp(Input.Keys.UP));
        assertTrue(player.keyUp(Input.Keys.W));
    }

    /**
     * Tests the player's movement downwards using the DOWN arrow key and S key.
     */
    @Test
    public void testDown() {
        assertTrue(player.keyUp(Input.Keys.DOWN));
        assertTrue(player.keyUp(Input.Keys.S));
    }

    /**
     * Tests the player's movement to the left using the LEFT arrow key and A key.
     */
    @Test
    public void testLEFT() {
        assertTrue(player.keyUp(Input.Keys.LEFT));
        assertTrue(player.keyUp(Input.Keys.A));
    }

    /**
     * Tests the player's movement to the right using the RIGHT arrow key and D key.
     */
    @Test
    public void testRIGHT() {
        assertTrue(player.keyUp(Input.Keys.RIGHT));
        assertTrue(player.keyUp(Input.Keys.D));
    }
}
