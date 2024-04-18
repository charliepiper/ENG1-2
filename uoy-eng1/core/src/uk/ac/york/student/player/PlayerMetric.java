package uk.ac.york.student.player;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import uk.ac.york.student.assets.skins.SkinManager;
import uk.ac.york.student.assets.skins.Skins;

/**
 * The PlayerMetric interface provides a contract for player metrics in the game.
 * It includes methods for getting a progress bar and a label, and a method for disposing resources.
 */
public interface PlayerMetric {
    /**
     * The minimum value for the progress bar to prevent visual jankyness when the value is 0.
     */
    float PROGRESS_BAR_MINIMUM = 0.1f;

    /**
     * The skin for the player metric, obtained from the SkinManager.
     */
    Skin skin = SkinManager.getSkins().getResult(Skins.CRAFTACULAR);

    /**
     * Get the progress bar for the player metric.
     * @return ProgressBar object representing the player's progress.
     */
    ProgressBar getProgressBar();

    /**
     * Get the label for the player metric to display on the screen
     * @return String representing the label of the player metric.
     */
    String getLabel();

    /**
     * Returns the current value of the player metric.
     *
     * @return the current value of the player metric
     */
    float get();

    /**
     * Sets the current value of the player metric.
     *
     * @param value the new value of the player metric
     */
    void set(float value);

    /**
     * Increases the current value of the player metric by a specified amount.
     *
     * @param amount the amount to add to the current value of the player metric
     */
    void increase(float amount);

    /**
     * Decreases the current value of the player metric by a specified amount.
     *
     * @param amount the amount to subtract from the current value of the player metric
     */
    void decrease(float amount);

    /**
     * Returns the default value of the player metric.
     *
     * @return the default value of the player metric
     */
    float getDefault();

    /**
     * Sets the total accumulated value of the player metric.
     *
     * @param total the new total accumulated value of the player metric
     */
    void setTotal(float total);

    /**
     * Increases the total accumulated value of the player metric by a specified amount.
     *
     * @param amount the amount to add to the total accumulated value of the player metric
     */
    void increaseTotal(float amount);

    /**
     * Returns the total accumulated value of the player metric.
     *
     * @return the total accumulated value of the player metric
     */
    float getTotal();

    /**
     * Returns the maximum total accumulated value that a player can achieve.
     *
     * @return the maximum total accumulated value that a player can achieve
     */
    float getMaxTotal();

    /**
     * Dispose resources when they are no longer needed.
     * By default, it disposes the skin.
     */
    default void dispose() {
        skin.dispose();
    }
}