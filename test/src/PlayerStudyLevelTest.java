import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.ac.york.student.game.GameTime;
import uk.ac.york.student.player.PlayerMetric;
import uk.ac.york.student.player.PlayerStudyLevel;

import static org.junit.Assert.assertEquals;

/**
 * PlayerStudyLevelTest is a JUnit test class that verifies the functionality of the PlayerStudyLevel class.
 * It ensures that the player's study level can be correctly increased, decreased, and managed.
 */
@RunWith(GdxTestRunner.class)
public class PlayerStudyLevelTest {
    private PlayerStudyLevel playerStudyLevel;

    /**
     * Sets up the testing environment before each test. It initializes a new instance of PlayerStudyLevel.
     */
    @Before
    public void setUp() {
        playerStudyLevel = new PlayerStudyLevel();
    }

    /**
     * Tests the getTotal method of PlayerStudyLevel.
     * It verifies that the total study level is correctly retrieved.
     */
    @Test
    public void testGetTotal() {
        float expectedTotal = 0.0f;
        float actualTotal = playerStudyLevel.getTotal();
        assertEquals(expectedTotal, actualTotal, 0.0f);
    }

    /**
     * Tests the setTotal method of PlayerStudyLevel.
     * It verifies that the total study level is correctly set to a specified value.
     */
    @Test
    public void testSetTotal() {
        float newTotal = 2.5f;
        playerStudyLevel.setTotal(newTotal);
        float actualTotal = playerStudyLevel.getTotal();
        assertEquals(newTotal, actualTotal, 0.0f);
    }

    /**
     * Tests the getMaxTotal method of PlayerStudyLevel.
     * It verifies that the maximum total study level is correctly retrieved.
     */
    @Test
    public void testGetMaxTotal() {
        int expectedMaxTotal = GameTime.getDays();
        float actualMaxTotal = playerStudyLevel.getMaxTotal();
        assertEquals(expectedMaxTotal, actualMaxTotal, 0.0f);
    }
    /**
     * Tests the increase method of PlayerStudyLevel.
     * It verifies that the study level is correctly increased by a specified amount.
     */

    @Test
    public void testIncrease() {
        float initialStudyLevel = playerStudyLevel.get();
        float increaseAmount = 0.3f;
        playerStudyLevel.increase(increaseAmount);
        float expectedStudyLevel = initialStudyLevel + increaseAmount;
        float actualStudyLevel = playerStudyLevel.get();
        assertEquals(expectedStudyLevel, actualStudyLevel, 0.0f);
    }

    /**
     * Tests the decrease method of PlayerStudyLevel.
     * It verifies that the study level is correctly decreased by a specified amount, without falling below the minimum threshold.
     */
    @Test
    public void testDecrease() {
        float initialStudyLevel = playerStudyLevel.get();
        float decreaseAmount = 0.2f;
        playerStudyLevel.decrease(decreaseAmount);
        float expectedStudyLevel = Math.max(PlayerMetric.PROGRESS_BAR_MINIMUM, initialStudyLevel - decreaseAmount);
        float actualStudyLevel = playerStudyLevel.get();
        assertEquals(expectedStudyLevel, actualStudyLevel, 0.0f);
    }

    /**
     * Tests the increaseTotal method of PlayerStudyLevel.
     * It verifies that the total study level is correctly increased by a specified amount.
     */
    @Test
    public void testIncreaseTotal() {
        float initialTotal = playerStudyLevel.getTotal();
        float increaseAmount = 1.0f;
        playerStudyLevel.increaseTotal(increaseAmount);
        float expectedTotal = initialTotal + increaseAmount;
        float actualTotal = playerStudyLevel.getTotal();
        assertEquals(expectedTotal, actualTotal, 0.0f);
    }
}



