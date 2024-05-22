import com.badlogic.gdx.Gdx;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

/**
 * Test class for the Asset class.
 * This class uses JUnit for testing and GdxTestRunner for setting up a LibGDX headless environment.
 */

@RunWith(GdxTestRunner.class)
public class AssetTest {
    /**
     * This test class checks if all assets required for the game exist and can be located.
     */

    @Test
    public void testBottomUpBlackGradientExists() {
        assertTrue("BottomUpBlackGradient exists",
                Gdx.files.internal("images/BottomUpBlackGradient.png").exists());
    }

    @Test
    public void testCharacter1Exists(){
        assertTrue("Character 1 exists",
                Gdx.files.internal("images/character1.png").exists());
    }

    @Test
    public void testCharacter2Exists() {
        assertTrue("Character 2 exists",
                Gdx.files.internal("images/character2.png").exists());
    }

    @Test
    public void testCharacter3Exists() {
        assertTrue("Character 3 exists",
                Gdx.files.internal("images/character3.png").exists());
    }

    @Test
    public void CloudExists() {
        assertTrue("Clouds exists",
                Gdx.files.internal("images/Clouds.png").exists());
    }

    @Test
    public void CloudsFormattedExists() {
        assertTrue("CloudsFormatted exists",
                Gdx.files.internal("images/CloudsFormatted.png").exists());
    }

    @Test
    public void MapOverviewExists() {
        assertTrue("MapOverview exists",
                Gdx.files.internal("images/MapOverview.png").exists());
    }

    @Test
    public void StoneWallExists() {
        assertTrue("StoneWall exists",
                Gdx.files.internal("images/StoneWall.png").exists());
    }

    @Test
    public void VignetteExists() {
        assertTrue("Vignette exists",
                Gdx.files.internal("images/Vignette.png").exists());
    }

}
