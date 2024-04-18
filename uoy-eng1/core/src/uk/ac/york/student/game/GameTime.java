package uk.ac.york.student.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import uk.ac.york.student.utils.DrawableUtils;

/**
 * The {@link GameTime} class represents the concept of time in the game.
 * It includes methods for managing the progression of time, such as incrementing the hour or the day.
 * It also includes a progress bar to visually represent the progression of time.
 * This class is final and cannot be subclassed.
 */
@Getter
public final class GameTime {
    // The total number of days in the game
    private static final int DAYS = 7;

    // The length of a day in the game, in hours
    private static final int DAY_LENGTH = 16;

    // The width of the progress bar representing the progression of time
    private static final int WIDTH = 50;

    // The height of the progress bar representing the progression of time
    private static final int HEIGHT = 5;

    // The ProgressBar instance used to visually represent the progression of time in the game
    private final ProgressBar progressBar;

    /**
     * Constructor for the {@link GameTime} class.
     * It initializes the progress bar with the given scale.
     *
     * @param scale The scale factor for the progress bar's width and height.
     */
    public GameTime(float scale) {
        System.out.println(scale);
        final int scaledWidth = (int) (WIDTH * scale);
        final int scaledHeight = (int) (HEIGHT * scale);

        progressBar = getProgressBar(scaledWidth, scaledHeight);
    }

    /**
     * This method is used to get the length of a day in the game.
     *
     * @return The length of a day in the game, in hours.
     */
    public static int getDayLength() {
        return DAY_LENGTH;
    }

    /**
     * This method is used to get the total number of days in the game.
     *
     * @return The total number of days in the game.
     */
    public static int getDays() {
        return DAYS;
    }

    /**
     * This method is used to create a new {@link ProgressBar} instance with the given dimensions.
     * The {@link ProgressBar} is styled and its initial value is set to the current hour.
     *
     * @param scaledWidth The width of the {@link ProgressBar}, scaled according to the game's scale factor.
     * @param scaledHeight The height of the {@link ProgressBar}, scaled according to the game's scale factor.
     * @return A new {@link ProgressBar} instance.
     */
    @NotNull
    private ProgressBar getProgressBar(int scaledWidth, int scaledHeight) {
        // Create a new ProgressBarStyle instance
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();

        // Create a new ProgressBar instance with the given parameters
        final ProgressBar progressBar = new ProgressBar(0, DAY_LENGTH, 1, false, style);

        // Get the ProgressBar's style
        ProgressBar.ProgressBarStyle barStyle = progressBar.getStyle();

        // Set the background of the ProgressBar to a gray color
        barStyle.background = DrawableUtils.getColouredDrawable(scaledWidth, scaledHeight, Color.GRAY);

        // Set the color of the ProgressBar before the knob to green
        barStyle.knobBefore = DrawableUtils.getColouredDrawable(scaledWidth, scaledHeight, Color.GREEN);

        // Set the color of the knob of the ProgressBar to green
        barStyle.knob = DrawableUtils.getColouredDrawable(0, scaledHeight, Color.GREEN);

        // Apply the style to the ProgressBar
        progressBar.setStyle(barStyle);

        // Set the width and height of the ProgressBar
        progressBar.setWidth(scaledWidth);
        progressBar.setHeight(scaledHeight);

        // Set the duration of the animation of the ProgressBar
        progressBar.setAnimateDuration(0.25f);

        // Set the initial value of the ProgressBar to the current hour
        progressBar.setValue(currentHour);

        // Return the configured ProgressBar instance
        return progressBar;
    }

    /**
     * This method is used to create a new {@link ProgressBar} instance with the given scale.
     * It calls the {@link GameTime#getProgressBar(int, int)} method with the width and height scaled according to the given scale factor.
     *
     * @param scale The scale factor for the {@link ProgressBar}'s width and height.
     * @return A new {@link ProgressBar} instance with the given scale.
     */
    private @NotNull ProgressBar getProgressBar(float scale) {
        return getProgressBar((int) (WIDTH * scale), (int) (HEIGHT * scale));
    }

    /**
     * This method is used to update the {@link ProgressBar} instance with a new scale.
     * It calls the {@link GameTime#getProgressBar(float)} method with the new scale, effectively resizing the {@link ProgressBar}.
     *
     * @param scale The new scale factor for the {@link ProgressBar}'s width and height.
     */
    public void updateProgressBar(float scale) {
        getProgressBar(scale);
    }

    /**
     * The current hour in the game. It ranges from 0 to {@link GameTime#DAY_LENGTH}
     */
    private int currentHour = 0;
    /**
     * This method is used to set the current hour in the game.
     * It also updates the {@link ProgressBar}'s value to reflect the new current hour.
     *
     * @param hour The new current hour in the game. It should be within the range [0, {@link GameTime#DAY_LENGTH}].
     */
    private void setCurrentHour(int hour) {
        currentHour = hour;
        progressBar.setValue(currentHour);
    }

    /**
     * This method is used to increment the current hour in the game by a given amount.
     * The new current hour is the minimum of the sum of the current hour and the given amount, and the length of a day.
     * This ensures that the current hour does not exceed the length of a day.
     *
     * @param amount The amount by which the current hour is to be incremented.
     */
    public void incrementHour(int amount) {
        setCurrentHour(Math.min(DAY_LENGTH, currentHour + amount));
    }

    /**
     * This method is used to reset the current hour in the game to 0.
     * It calls the {@link GameTime#setCurrentHour(int)} method with 0 as the argument.
     */
    public void resetHour() {
        setCurrentHour(0);
    }

    /**
     * This method is used to check if the current hour equals the length of a day.
     * It returns true if the current hour is equal {@link GameTime#DAY_LENGTH}, indicating the end of the day.
     *
     * @return A boolean value indicating whether the current hour equals the length of a day.
     */
    public boolean isEndOfDay() {
        return currentHour == DAY_LENGTH;
    }

    /**
     * The current day in the game. It ranges from 0 to {@link GameTime#DAYS}-1
     */
    private int currentDay = 0;

    /**
     * This method is used to increment the current day in the game by 1.
     * It calls the {@link GameTime#incrementDay(int)} method with 1 as the argument, and then resets the current hour by calling the {@link GameTime#resetHour()} method.
     */
    public void incrementDay() {
        incrementDay(1);
        resetHour();
    }

    /**
     * This method is used to increment the current day in the game by a given amount.
     * The new current day is the minimum of the sum of the current day and the given amount, and one less than the total number of days.
     * This ensures that the current day does not exceed the total number of days.
     * After incrementing the day, it resets the current hour by calling the resetHour method.
     *
     * @param amount The amount by which the current day is to be incremented.
     */
    public void incrementDay(int amount) {
        currentDay = Math.min(DAYS - 1, currentDay + amount);
        resetHour();
    }

    /**
     * This method is used to reset the current day in the game to 0.
     * It sets the current day to 0 and then resets the current hour by calling the {@link GameTime#resetHour()} method.
     */
    public void resetDay() {
        currentDay = 0;
        resetHour();
    }

    /**
     * This method is used to check if the current day equals {@link GameTime#DAYS}-1.
     * It returns true if the current day is equal to {@link GameTime#DAYS}-1, indicating the end of the game.
     *
     * @return A boolean value indicating whether the current day equals {@link GameTime#DAYS}-1.
     */
    public boolean isEndOfDays() {
        return currentDay == DAYS - 1;
    }
}
