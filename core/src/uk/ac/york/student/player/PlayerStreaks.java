package uk.ac.york.student.player;

import uk.ac.york.student.game.activities.Activity;

import java.util.HashMap;
import java.util.Map;

public class PlayerStreaks {
    private static PlayerStreaks instance;
    private Map<String, Integer> activityStreakCounts;
    private Map<String, Integer> lastPerformedDay;

    // Private constructor to prevent direct instantiation
    private PlayerStreaks() {
        activityStreakCounts = new HashMap<>();
        lastPerformedDay = new HashMap<>();
        for (Activity activity : Activity.values()) {
            activityStreakCounts.put(activity.name(), 0);
            lastPerformedDay.put(activity.name(), -1);
        }
    }

    // Static method to retrieve the single instance
    public static PlayerStreaks getInstance() {
        if (instance == null) {
            instance = new PlayerStreaks();
        }
        return instance;
    }

    /**
     * Increment the streak count for the specified activity if it was last performed on the previous day.
     *
     * @param activity The activity for which to increment the streak count.
     * @param currentDay The current day in the game.
     */
    public void incrementStreak(Activity activity, int currentDay) {
        String activityName = activity.name();
        Integer lastDayPerformed = lastPerformedDay.get(activityName);

        if (lastDayPerformed != null && (currentDay - lastDayPerformed) == 1) {
            // Activity was last performed on the previous day, so increment the streak
            activityStreakCounts.put(activityName, activityStreakCounts.getOrDefault(activityName, 0) + 1);
        } else {
            // Reset the streak if there's a gap in the days
            activityStreakCounts.put(activityName, 1);
        }

        // Update the last performed day to the current day
        lastPerformedDay.put(activityName, currentDay);

        System.out.print(activityName + " streak " + getStreakCount(activity) + " days");
    }

    /**
     * Get the streak count for the specified activity.
     *
     * @param activity The activity for which to get the streak count.
     * @return The streak count for the specified activity.
     */
    public int getStreakCount(Activity activity) {
        return activityStreakCounts.getOrDefault(activity.name(), 0);
    }

    /**
     * Reset the streak count for the specified activity.
     *
     * @param activity The activity for which to reset the streak count.
     */
    public void resetStreak(Activity activity) {
        activityStreakCounts.put(activity.name(), 0);
        lastPerformedDay.put(activity.name(), -1);
    }
}
