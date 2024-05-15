import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.ac.york.student.game.GameTime;
import uk.ac.york.student.player.PlayerMetric;
import uk.ac.york.student.player.PlayerStudyLevel;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class testPlayerStudylevel {
    private PlayerStudyLevel playerStudyLevel;

    @Before
    public void setUp() {
        playerStudyLevel = new PlayerStudyLevel();
    }

//    @Test
//    public void testGetDefault() {
//        float expectedDefault = 0.1f;
//        float actualDefault = playerStudyLevel.getDefault();
//        assertEquals(expectedDefault, actualDefault, 0.0f);
//    }

//    @Test
//    public void testGetAndSet() {
//        float newStudyLevel = 0.5f;
//        playerStudyLevel.set(newStudyLevel);
//        float actualStudyLevel = playerStudyLevel.get();
//        assertEquals(newStudyLevel, actualStudyLevel, 0.0f);
//    }

    @Test
    public void testIncrease() {
        float initialStudyLevel = playerStudyLevel.get();
        float increaseAmount = 0.3f;
        playerStudyLevel.increase(increaseAmount);
        //float expectedStudyLevel = Math.min(1.0f, initialStudyLevel + increaseAmount);
        float expectedStudyLevel = initialStudyLevel + increaseAmount;
        float actualStudyLevel = playerStudyLevel.get();
        assertEquals(expectedStudyLevel, actualStudyLevel, 0.0f);
    }

    @Test
    public void testDecrease() {
        float initialStudyLevel = playerStudyLevel.get();
        float decreaseAmount = 0.2f;
        playerStudyLevel.decrease(decreaseAmount);
        float expectedStudyLevel = Math.max(PlayerMetric.PROGRESS_BAR_MINIMUM, initialStudyLevel - decreaseAmount);
        float actualStudyLevel = playerStudyLevel.get();
        assertEquals(expectedStudyLevel, actualStudyLevel, 0.0f);
    }

//    @Test
//    public void testGetTotal() {
//        float expectedTotal = 0.0f;
//        float actualTotal = playerStudyLevel.getTotal();
//        assertEquals(expectedTotal, actualTotal, 0.0f);
//    }

//    @Test
//    public void testSetTotal() {
//        float newTotal = 2.5f;
//        playerStudyLevel.setTotal(newTotal);
//        float actualTotal = playerStudyLevel.getTotal();
//        assertEquals(newTotal, actualTotal, 0.0f);
//    }

    @Test
    public void testIncreaseTotal() {
        float initialTotal = playerStudyLevel.getTotal();
        float increaseAmount = 1.0f;
        playerStudyLevel.increaseTotal(increaseAmount);
        float expectedTotal = initialTotal + increaseAmount;
        float actualTotal = playerStudyLevel.getTotal();
        assertEquals(expectedTotal, actualTotal, 0.0f);
    }

//    @Test
//    public void testGetMaxTotal() {
//        int expectedMaxTotal = GameTime.getDays();
//        float actualMaxTotal = playerStudyLevel.getMaxTotal();
//        assertEquals(expectedMaxTotal, actualMaxTotal, 0.0f);
//    }
}