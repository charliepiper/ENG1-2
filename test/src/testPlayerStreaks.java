
import org.junit.Before;
import org.junit.Test;
import uk.ac.york.student.game.activities.Activity;
import uk.ac.york.student.player.PlayerStreaks;

import static org.junit.Assert.assertEquals;

public class testPlayerStreaks {

    private PlayerStreaks playerStreaks;
    private int currentDay;

    @Before
    public void setUp() {
        playerStreaks = PlayerStreaks.getInstance();
        currentDay = 1;
    }

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

//    @Test
//    public void testGetStreakCount() {
//        Activity activity = Activity.SLEEP;
//        int expectedCount = 0;
//        int actualCount = playerStreaks.getStreakCount(activity);
//        assertEquals(expectedCount, actualCount);
//
//        // Increment streak
//        playerStreaks.incrementStreak(activity);
//        expectedCount = 1;
//        actualCount = playerStreaks.getStreakCount(activity);
//        assertEquals(expectedCount, actualCount);
//    }

//    @Test
//    public void testAllActivitiesInitialized() {
//        for (Activity activity : Activity.values()) {
//            int expectedCount = 0;
//            int actualCount = playerStreaks.getStreakCount(activity);
//            assertEquals(expectedCount, actualCount);
//        }
//    }
}