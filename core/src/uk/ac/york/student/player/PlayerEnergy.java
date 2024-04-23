package uk.ac.york.student.player;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import org.jetbrains.annotations.Range;
import uk.ac.york.student.game.GameTime;

/**
 * The PlayerEnergy class represents the energy level of a player in the game.
 * It implements the PlayerMetric interface, indicating that it is a type of metric used for the player.
 * The energy level is represented as a float value between 0 and 1, where 0 is no energy and 1 is full energy.
 * The class also includes a ProgressBar to visually represent the energy level.
 */
public class PlayerEnergy implements PlayerMetric {
    /**
     * The ProgressBar instance for the PlayerEnergy class.
     * This ProgressBar represents the energy level of the player in the game.
     * The minimum value is 0 (no energy), the maximum value is 1 (full energy), and the step size is 0.1.
     * The ProgressBar does not have a vertical orientation (false), and uses the Craftacular skin from {@link PlayerMetric#skin}.
     */
    private final ProgressBar progressBar = new ProgressBar(0, 1, 0.1f, false, skin);

    /**
     * Constructor for the PlayerEnergy class.
     * This constructor initializes the ProgressBar that represents the player's energy level.
     */
    public PlayerEnergy() {
        progressBar.setWidth(200); // Set the width of the ProgressBar to 200
        progressBar.setHeight(50); // Set the height of the ProgressBar to 50
        progressBar.setAnimateDuration(0.25f); // Set the animation duration of the ProgressBar to 0.25 seconds
    }

    /**
     * The energy level of the player.
     * This is a float value between 0 and 1, where 0 represents no energy and 1 represents full energy.
     * By default, the energy level is set to 1 (full energy).
     */
    private @Range(from=0, to=1) float energy = getDefault();
    /**
     * The total amount of energy accumulated by the player across all days of the game.
     * This is a float value that starts at 0 and increases as the player gains more energy.
     * This value is incremented when the player sleeps based on what their energy level is at that given time.
     */
    private float totalEnergy = 0f;

    /**
     * Returns the maximum total energy that a player can accumulate.
     * This is equivalent to the number of days in the game, as represented by {@link GameTime#getDays()}.
     *
     * @return the maximum total energy that a player can accumulate
     */
    public float getMaxTotal() {
        return GameTime.getDays();
    }

    /**
     * Returns the total amount of energy accumulated by the player.
     *
     * @return the total amount of energy accumulated by the player
     */
    public float getTotal() {
        return totalEnergy;
    }

    /**
     * Sets the total amount of energy accumulated by the player.
     *
     * @param total the new total amount of energy accumulated by the player
     */
    public void setTotal(float total) {
        this.totalEnergy = total;
    }

    /**
     * Increases the total amount of energy accumulated by the player by a specified amount.
     *
     * @param amount the amount of energy to add to the total
     */
    public void increaseTotal(float amount) {
        this.totalEnergy += amount;
    }
    /**
     * Returns the default energy level for the player.
     * This is a float value of 1, representing full energy.
     *
     * @return the default energy level of the player
     */
    @Override
    public float getDefault() {
        return 1f;
    }
    /**
     * Get the energy level of the player.
     * This is a float value between 0 and 1, where 0 represents no energy and 1 represents full energy.
     * @return the energy level of the player
     */
    @Override
    public @Range(from=0, to=1) float get() {
        return energy;
    }

    /**
     * Set the energy level of the player.
     * This method takes a float value between 0 and 1 as an argument, where 0 represents no energy and 1 represents full energy.
     * The energy level is then set to the maximum of the minimum energy level ({@link PlayerMetric#PROGRESS_BAR_MINIMUM}) and the minimum of 1 and the provided energy level.
     * This ensures that the energy level is always within the valid range.
     * @param energy the new energy level of the player
     */
    @Override
    public void set(@Range(from=0, to=1) float energy) {
        this.energy = Math.max(PROGRESS_BAR_MINIMUM, Math.min(1, energy));
        this.progressBar.setValue(this.energy);
    }

    /**
     * Increase the energy level of the player.
     * This method takes a float value as an argument, which represents the amount of energy to be added to the player's current energy level.
     * The new energy level is then set to the minimum of 1 and the sum of the current energy level and the provided amount.
     * This ensures that the energy level does not exceed 1 (full energy).
     * @param amount the amount of energy to be added to the player's current energy level
     */
    @Override
    public void increase(float amount) {
        set(Math.min(1, energy + amount));
    }

    /**
     * Decrease the energy level of the player.
     * This method takes a float value as an argument, which represents the amount of energy to be subtracted from the player's current energy level.
     * The new energy level is then set to the maximum of the minimum energy level ({@link PlayerMetric#PROGRESS_BAR_MINIMUM}) and the difference between the current energy level and the provided amount.
     * This ensures that the energy level does not go below the minimum energy level.
     * @param amount the amount of energy to be subtracted from the player's current energy level
     */
    @Override
    public void decrease(float amount) {
        set(Math.max(PROGRESS_BAR_MINIMUM, energy - amount));
    }

    /**
     * Get the ProgressBar representing the player's energy level.
     * This method first sets the value of the ProgressBar to the current energy level of the player.
     * It then returns the ProgressBar.
     * @return the ProgressBar representing the player's energy level
     */
    @Override
    public ProgressBar getProgressBar() {
        progressBar.setValue(get());
        return progressBar;
    }

    /**
     * Get the label used to display on the screen for the player's energy level.
     * @return the label for the player's energy level
     */
    @Override
    public String getLabel() {
        return "Energy";
    }
}
