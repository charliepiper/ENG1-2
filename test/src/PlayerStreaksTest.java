import org.junit.Before;
import org.junit.Test;
import uk.ac.york.student.game.activities.Activity;
import uk.ac.york.student.player.PlayerStreaks;

import static org.junit.Assert.assertEquals;

/**
 * PlayerStreaksTest is a JUnit test class that verifies the functionality of the PlayerStreaks class.
 * It ensures that the player's activity streaks are correctly incremented and retrieved.
 */
public class PlayerStreaksTest {

    private PlayerStreaks playerStreaks;
    private int currentDay;

    /**
     * Sets up the testing environment before each test. It initializes a singleton instance of
     * PlayerStreaks and sets the current day to 1.
     */
    @Before
    public void setUp() {
        playerStreaks = PlayerStreaks.getInstance();
        currentDay = 1;
    }

    /**
     * Tests the incrementStreak method of PlayerStreaks for a specific activity.
     * It verifies that the streak count is correctly incremented over consecutive days.
     */
    @Test
    public void testIncrementStreak() {
        Activity activity = Activity.STUDY;
        playerStreaks.incrementStreak(activity, currentDay);
        int expectedCount = 1;
        int actualCount = playerStreaks.getStreakCount(activity);
        assertEquals(expectedCount, actualCount);

        // Increment streak again on the next day
        currentDay++;
        playerStreaks.incrementStreak(activity, currentDay); // Increment on day 2
        expectedCount = 2;
        actualCount = playerStreaks.getStreakCount(activity);
        assertEquals(expectedCount, actualCount);
    }

}
