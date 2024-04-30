package uk.ac.york.student.player;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import org.jetbrains.annotations.Range;
import uk.ac.york.student.game.GameTime;

/**
 * This is a new class which adds a metric, adding additional functionality.
 * The PlayerHealth class implements the PlayerMetric interface.
 * This class is responsible for managing the health level of a player in the game.
 * It includes methods to get, set, increase, and decrease the health level.
 * It also includes methods to get a ProgressBar representing the health level and a label for the health level.
 */
public class PlayerHealth implements PlayerMetric {
    /**
     * The ProgressBar instance for the PlayerHealth class.
     * This ProgressBar represents the health level of the player in the game.
     * The minimum value is 0 (not happy), the maximum value is 1 (very happy), and the step size is 0.1.
     * The ProgressBar does not have a vertical orientation (false), and uses the skin from {@link PlayerMetric#skin}.
     */
    private final ProgressBar progressBar = new ProgressBar(0, 1, 0.1f, false, skin);
    /**
     * Constructor for the PlayerHealth class.
     * This constructor initializes the ProgressBar representing the player's health level.
     */
    public PlayerHealth() {
        progressBar.setWidth(200);
        progressBar.setHeight(50);
        progressBar.setAnimateDuration(0.25f);
    }

    /**
     * The health level of the player.
     * This is a float value ranging from 0 to 1, where 0 represents not happy and 1 represents very happy.
     * It is initially set to 1, indicating that the player starts the game being very happy.
     */
    private @Range(from=0, to=1) float health = getDefault();
    /**
     * The total amount of health accumulated by the player across all days of the game.
     * This is a float value that starts at 0 and increases as the player becomes happier.
     * This value is incremented when the player sleeps based on what their health level is at that given time.
     */
    private float totalhealth = 0f;

    /**
     * Returns the maximum total health that a player can accumulate.
     * This is equivalent to the number of days in the game, as represented by {@link GameTime#getDays()}.
     *
     * @return the maximum total health that a player can accumulate
     */
    public float getMaxTotal() {
        return GameTime.getDays();
    }

    /**
     * Returns the total amount of health accumulated by the player.
     *
     * @return the total amount of health accumulated by the player
     */
    public float getTotal() {
        return totalhealth;
    }

    /**
     * Sets the total amount of health accumulated by the player.
     *
     * @param total the new total amount of health accumulated by the player
     */
    public void setTotal(float total) {
        this.totalhealth = total;
    }

    /**
     * Increases the total amount of health accumulated by the player by a specified amount.
     *
     * @param amount the amount of health to add to the total
     */
    public void increaseTotal(float amount) {
        this.totalhealth += amount;
    }

    /**
     * This method is used to get the default health level for a player.
     * The default health level is set to 1.0, indicating that a player starts the game at full health.
     *
     * @return float This returns the default health level of 1.0.
     */
    @Override
    public float getDefault() {
        return 0.6f;
    }
    /**
     * Getter method for the health level of the player.
     * This method returns the current health level of the player.
     * The health level is a float value ranging from 0 to 1, where 0 represents not happy and 1 represents very happy.
     * @return the current health level of the player.
     */
    @Override
    public @Range(from=0, to=1) float get() {
        return health;
    }


    /**
     * Set the health level of the player.
     * This method takes a float value between 0 and 1 as an argument, where 0 represents not happy and 1 represents very happy.
     * The health level is then set to the maximum of the minimum health level ({@link PlayerMetric#PROGRESS_BAR_MINIMUM}) and the minimum of 1 and the provided health level.
     * This ensures that the health level is always within the valid range.
     * @param health the new health level of the player
     */
    @Override
    public void set(@Range(from=0, to=1) float health) {
        this.health = Math.max(PROGRESS_BAR_MINIMUM, Math.min(1, health));
        this.progressBar.setValue(this.health);
    }


    /**
     * Increase the health level of the player.
     * This method takes a float value as an argument, which represents the amount to increase the health level by.
     * The health level after the increase is the minimum of 1 and the sum of the current health level and the provided amount.
     * This ensures that the health level does not exceed 1.
     * @param amount the amount to increase the health level by
     */
    @Override
    public void increase(float amount) {
        set(Math.min(1, health + amount));
    }


    /**
     * Decrease the health level of the player.
     * This method takes a float value as an argument, which represents the amount to decrease the health level by.
     * The health level after the decrease is the maximum of the minimum health level ({@link PlayerMetric#PROGRESS_BAR_MINIMUM}) and the difference between the current health level and the provided amount.
     * This ensures that the health level does not go below the minimum health level.
     * @param amount the amount to decrease the health level by
     */
    @Override
    public void decrease(float amount) {
        set(Math.max(PROGRESS_BAR_MINIMUM, health - amount));
    }

    /**
     * Get the ProgressBar representing the health level of the player.
     * This method sets the value of the ProgressBar to the current health level of the player and then returns the ProgressBar.
     * The ProgressBar represents the health level of the player in the game, with a minimum value of 0 (not happy) and a maximum value of 1 (very happy).
     * @return the ProgressBar representing the health level of the player.
     */
    @Override
    public ProgressBar getProgressBar() {
        progressBar.setValue(get());
        return progressBar;
    }

    /**
     * Get the label used to display on the screen for the player's health level.
     * @return the label for the player's health level
     */
    @Override
    public String getLabel() {
        return "Health";
    }
}
