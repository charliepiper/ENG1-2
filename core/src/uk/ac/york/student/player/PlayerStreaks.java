package uk.ac.york.student.player;

import uk.ac.york.student.game.activities.Activity;

import java.util.HashMap;
import java.util.Map;

public class PlayerStreaks {
    private static PlayerStreaks instance;
    private Map<String, Integer> activityStreakCounts;

    // Private constructor to prevent direct instantiation
    private PlayerStreaks() {
        activityStreakCounts = new HashMap<>();
        for (Activity activity : Activity.values()) {
            activityStreakCounts.put(activity.name(), 0);
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
     * Increment the streak count for the specified activity.
     *
     * @param activity The activity for which to increment the streak count.
     */
    public void incrementStreak(Activity activity) {
        activityStreakCounts.put(activity.name(), activityStreakCounts.getOrDefault(activity.name(), 0) + 1);
    }

    /**
     * Get the streak count for the specified activity.
     *
     * @param activity The activity for which to get the streak count.
     * @return The streak count for the specified activity.
     */
    public int getStreakCount(Activity activity) {
        // Use the activity name as key in the map
        System.out.println(activityStreakCounts);

        return activityStreakCounts.get(activity.name());

    }
    /**
     * Reset the streak count for the specified activity.
     *
     * @param activity The activity for which to reset the streak count.
     */
//    public void resetStreak(Activity activity) {
//        activityStreakCounts.put(activity.toString(), 0);
//    }
}
