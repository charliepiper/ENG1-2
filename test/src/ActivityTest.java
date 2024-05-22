import org.junit.Test;
import uk.ac.york.student.game.activities.Activity;
import uk.ac.york.student.player.PlayerMetrics;

import static org.junit.Assert.assertEquals;

public class ActivityTest {
    /**
     * Test class for the Activity class.
     * This class uses JUnit for testing.
     */
    private PlayerMetrics playerMetrics;

    /**
     * Checking effects of activity completion on each metric - directional : increase and decrease
     */
    @Test
    public void testGetEffectStudy() {
        // Check that STUDY correctly modifies STUDY_LEVEL, ENERGY, HAPPINESS, and HEALTH
        assertEquals(PlayerMetrics.MetricEffect.INCREASE, Activity.STUDY.getEffect(PlayerMetrics.MetricType.STUDY_LEVEL));
        assertEquals(PlayerMetrics.MetricEffect.DECREASE, Activity.STUDY.getEffect(PlayerMetrics.MetricType.ENERGY));
        assertEquals(PlayerMetrics.MetricEffect.DECREASE, Activity.STUDY.getEffect(PlayerMetrics.MetricType.HAPPINESS));
        assertEquals(PlayerMetrics.MetricEffect.DECREASE, Activity.STUDY.getEffect(PlayerMetrics.MetricType.HEALTH));
    }

    @Test
    public void testGetEffectNap() {
        // Check that nap correctly modifies STUDY_LEVEL, ENERGY, HAPPINESS, and HEALTH
        assertEquals(PlayerMetrics.MetricEffect.INCREASE, Activity.NAP.getEffect(PlayerMetrics.MetricType.HEALTH));
    }

    @Test
    public void testGetEffectSleep() {
        // Check that sleep correctly modifies STUDY_LEVEL, ENERGY, HAPPINESS, and HEALTH
        assertEquals(PlayerMetrics.MetricEffect.INCREASE, Activity.SLEEP.getEffect(PlayerMetrics.MetricType.HEALTH));
        assertEquals(PlayerMetrics.MetricEffect.RESET, Activity.SLEEP.getEffect(PlayerMetrics.MetricType.ENERGY));
        assertEquals(PlayerMetrics.MetricEffect.RESET, Activity.SLEEP.getEffect(PlayerMetrics.MetricType.STUDY_LEVEL));

    }

    /**
     * Tests the effects of the EAT activity on player metrics.
     * It verifies that the EAT activity increases both ENERGY and HEALTH metrics.
     */
    @Test
    public void testEatActivity() {
        assertEquals(PlayerMetrics.MetricEffect.INCREASE, Activity.EAT.getEffect(PlayerMetrics.MetricType.ENERGY));
        assertEquals(PlayerMetrics.MetricEffect.INCREASE, Activity.EAT.getEffect(PlayerMetrics.MetricType.HEALTH));
    }

    /**
     * Tests the effects of the EXERCISE activity on player metrics.
     * It verifies that the EXERCISE activity decreases ENERGY but increases HEALTH metrics.
     */
    @Test
    public void testExcerciseActivity() {
        assertEquals(PlayerMetrics.MetricEffect.DECREASE, Activity.EXERCISE.getEffect(PlayerMetrics.MetricType.ENERGY));
        assertEquals(PlayerMetrics.MetricEffect.INCREASE, Activity.EXERCISE.getEffect(PlayerMetrics.MetricType.HEALTH));
    }

    /**
     * Tests the effects of the ENTERTAIN activity on player metrics.
     * It verifies that the ENTERTAIN activity increases HAPPINESS but decreases ENERGY metrics.
     */
    @Test
    public void testEntertainActivity() {
        assertEquals(PlayerMetrics.MetricEffect.INCREASE, Activity.ENTERTAIN.getEffect(PlayerMetrics.MetricType.HAPPINESS));
        assertEquals(PlayerMetrics.MetricEffect.DECREASE, Activity.ENTERTAIN.getEffect(PlayerMetrics.MetricType.ENERGY));
    }







}