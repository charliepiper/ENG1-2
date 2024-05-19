import uk.ac.york.student.game.GameTime;

import static org.junit.Assert.*;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test class for the GameTime class.
 * This class uses JUnit for testing and GdxTestRunner for setting up a LibGDX headless environment.
 */
@RunWith(GdxTestRunner.class)
public class GameLengthTest {


    private GameTime gameTime;

    /**
     * Sets up the test environment before each test.
     * Initializes a GameTime instance with a scale of 1.0.
     */
    @Before
    public void setUp() {
        gameTime = new GameTime(1.0f); // using scale 1.0 for simplicity
    }

    /**
     * Tests the initial hour value.
     * It should be 0 when the GameTime instance is first created.
     */
    @Test
    public void testInitialHour() {
        assertEquals(0, gameTime.getCurrentHour());
    }

    /**
     * Tests the initial day value.
     * It should be 0 when the GameTime instance is first created.
     */
    @Test
    public void testInitialDay() {
        assertEquals(0, gameTime.getCurrentDay());
    }

    /**
     * Tests the increment hour method.
     * Increments the current hour by 1, and checks if the current hour is updated correctly.
     */
    @Test
    public void testIncrementHour() {
        gameTime.incrementHour(1);
        assertEquals(1, gameTime.getCurrentHour());
    }

    /**
     * Tests the increment hour method again, but this time beyond the length of a day.
     * The current hour should not exceed the max day length.
     */
    @Test
    public void testIncrementHourBeyondDayLength() {
        gameTime.incrementHour(GameTime.getDayLength() + 1);
        assertEquals(GameTime.getDayLength(), gameTime.getCurrentHour());
    }

    /**
     * Tests the resetHour method.
     * Increments the current hour and then resets it. The current hour should be 0 after reset.
     */
    @Test
    public void testResetHour() {
        gameTime.incrementHour(5);
        gameTime.resetHour();
        assertEquals(0, gameTime.getCurrentHour());
    }

    /**
     * Tests the isEndOfDay method.
     * Increments the current hour to the maximum day length, then checks if the end of day is "true"
     */
    @Test
    public void testEndOfDay() {
        gameTime.incrementHour(GameTime.getDayLength());
        assertTrue(gameTime.isEndOfDay());
    }

    /**
     * Tests the incrementDay method.
     * Increments the current day by 1 and checks if the current day is updated correctly.
     * The current hour should also be reset to 0.
     */
    @Test
    public void testIncrementDay() {
        gameTime.incrementDay();
        assertEquals(1, gameTime.getCurrentDay());
        assertEquals(0, gameTime.getCurrentHour()); // hour should also be reset when the day increments
    }

    /**
     * Tests the incrementDay method again, but beyond the total number of days
     * the current day should not exceed the max number of days
     */
    @Test
    public void testIncrementDayBeyondTotalDays() {
        for (int i = 0; i < GameTime.getDays(); i++) {
            gameTime.incrementDay();
        }
        assertEquals(GameTime.getDays() - 1, gameTime.getCurrentDay());
    }

    /**
     * Tests the resetDay method.
     * Increments the current day, then resets it, the current day should be 0 after the reset
     * the current hour should also be reset to 0

     */
    @Test
    public void testResetDay() {
        gameTime.incrementDay();
        gameTime.resetDay();
        assertEquals(0, gameTime.getCurrentDay());
        assertEquals(0, gameTime.getCurrentHour());
    }

    /**
     * Tests the isEndOfDays method.
     * Increments the current day to the max number of days (7), then checks if it is the end of days, "true"
     */
    @Test
    public void testEndOfDays() {
        for (int i = 0; i < GameTime.getDays(); i++) {
            gameTime.incrementDay();
        }
        assertTrue(gameTime.isEndOfDays());
    }

    /**
     * ---------- PROGRESS BAR TESTING ---------------
     */

    /**
     * Tests the initial value of the progress bar.
     * The progress bar value should be 0 when the GameTime instance is first created.
     */
    @Test
    public void testProgressBarInitialValue() {
        ProgressBar progressBar = gameTime.getProgressBar();
        assertEquals(0, progressBar.getValue(), 0.01);
    }

    /**
     * Tests the value of the progress bar after incrementing the hour.
     * The progress bar value should be increased and reflect the correct hour.
     */
    @Test
    public void testProgressBarValueAfterHourIncrement() {
        gameTime.incrementHour(5);
        ProgressBar progressBar = gameTime.getProgressBar();
        assertEquals(5, progressBar.getValue(), 0.01);
    }

    /**
     * Tests the value of the progress bar after incrementing the day.
     * The progress bar value should be reset to 0 after incrementing the day.
     */
    @Test
    public void testProgressBarValueAfterDayIncrement() {
        gameTime.incrementDay();
        ProgressBar progressBar = gameTime.getProgressBar();
        assertEquals(0, progressBar.getValue(), 0.01); // should be reset to 0
    }

    /**
     * Tests the updateProgressBarScale method.
     * Updates the scale of the progress bar and checks if it is updated correctly.
     * Assumes that if no exception is thrown, the progress bar is correctly updated.
     */
    @Test
    public void testUpdateProgressBarScale() {
        gameTime.updateProgressBar(2.0f); // update scale
        ProgressBar progressBar = gameTime.getProgressBar();
        // Assume that if no exception is thrown, it's correctly updated.
        assertNotNull(progressBar);
    }
}
