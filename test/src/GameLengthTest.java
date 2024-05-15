//import org.junit.Test;
//import uk.ac.york.student.game.GameTime;
//
//import static org.junit.Assert.assertEquals;
//
//public class GameLengthTest {
//
//    @Test
//    public void testDayCounterChangesAtEndOfDay() {
//        // Initialize GameTime object
//        GameTime gameTime = new GameTime(1.0f);
//
//        // Set the current hour to the end of the day
//        gameTime.setCurrentHour(GameTime.getDayLength());
//
//        // Check current day
//        int initialDay = gameTime.getCurrentDay();
//        assertEquals(0, initialDay);
//
//        // Increment the day: reset the hour and increment the day
//        gameTime.incrementDay();
//
//        // Check if the day counter has incremented
//        int newDay = gameTime.getCurrentDay();
//        assertEquals(initialDay + 1, newDay);
//
//        // Check if the current hour is reset to 0
//        int currentHour = gameTime.getCurrentHour();
//        assertEquals(0, currentHour);
//    }
//}


