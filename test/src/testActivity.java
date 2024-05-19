import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.ac.york.student.game.activities.Activity;
import uk.ac.york.student.player.PlayerMetrics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class testActivity {
    private PlayerMetrics playerMetrics;

    @Before
    public void setUp() {
        playerMetrics = new PlayerMetrics();
    }

    @Test
    public void testEatActivity() {

//        assertEquals(PlayerMetrics.MetricEffect.INCREASE, Activity.EAT.getEffect(PlayerMetrics.MetricType.ENERGY));
        assertEquals(PlayerMetrics.MetricEffect.INCREASE,Activity.EAT.getEffect(PlayerMetrics.MetricType.HEALTH));
    }

    @Test
    public void testBoundaryAbove1() {

        playerMetrics.changeMetric(PlayerMetrics.MetricType.HEALTH, PlayerMetrics.MetricEffect.INCREASE, 1);
        playerMetrics.changeMetric(PlayerMetrics.MetricType.ENERGY, PlayerMetrics.MetricEffect.INCREASE, 1);

        assertTrue(playerMetrics.getMetric(PlayerMetrics.MetricType.HEALTH).get() <= 1.0f);
        assertTrue(playerMetrics.getMetric(PlayerMetrics.MetricType.ENERGY).get() <= 1.0f);


    }

    public void testBoundarybelow() {

        playerMetrics.changeMetric(PlayerMetrics.MetricType.HEALTH, PlayerMetrics.MetricEffect.DECREASE, 0);
        playerMetrics.changeMetric(PlayerMetrics.MetricType.ENERGY, PlayerMetrics.MetricEffect.DECREASE, 0);

        assertTrue(playerMetrics.getMetric(PlayerMetrics.MetricType.HEALTH).get() == 0f);
        assertTrue(playerMetrics.getMetric(PlayerMetrics.MetricType.ENERGY).get() == 0f);


    }

    @Test
    public void testExcerciseActivity() {

        assertEquals(PlayerMetrics.MetricEffect.DECREASE, Activity.EXERCISE.getEffect(PlayerMetrics.MetricType.ENERGY));
        assertEquals(PlayerMetrics.MetricEffect.INCREASE,Activity.EXERCISE.getEffect(PlayerMetrics.MetricType.HEALTH));
    }

    @Test
    public void testEntertainActivity() {

        assertEquals(PlayerMetrics.MetricEffect.INCREASE, Activity.ENTERTAIN.getEffect(PlayerMetrics.MetricType.HAPPINESS));
        assertEquals(PlayerMetrics.MetricEffect.DECREASE,Activity.ENTERTAIN.getEffect(PlayerMetrics.MetricType.ENERGY));
    }





}