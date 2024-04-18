package uk.ac.york.student.player;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * The PlayerMetrics class encapsulates the metrics related to a player.
 * It includes metrics such as energy, happiness, and study level.
 * It also provides methods to get these metrics and to dispose them when they are no longer needed.
 */
@Getter
public final class PlayerMetrics {

    /**
     * The {@link MetricType} enum represents the types of metrics related to a player.
     * It includes {@link MetricType#ENERGY}, {@link MetricType#HAPPINESS}, and {@link MetricType#STUDY_LEVEL}.
     */
    @Getter
    public enum MetricType {
        ENERGY,       // Represents the energy level of the player
        HAPPINESS,    // Represents the happiness level of the player
        STUDY_LEVEL;  // Represents the study level of the player
    }

    /**
     * The {@link MetricEffect} enum represents the possible effects on a player's metrics.
     * It includes {@link MetricEffect#INCREASE}, {@link MetricEffect#DECREASE}, and {@link MetricEffect#RESET}.
     */
    public enum MetricEffect {
        INCREASE,    // Represents an increase in a player's metric
        DECREASE,    // Represents a decrease in a player's metric
        RESET;       // Represents resetting a player's metric to its initial value
    }

    /**
     * The energy metric of the player.
     */
    private final PlayerEnergy energy = new PlayerEnergy();

    /**
     * The happiness metric of the player.
     */
    private final PlayerHappiness happiness = new PlayerHappiness();

    /**
     * The study level metric of the player.
     */
    private final PlayerStudyLevel studyLevel = new PlayerStudyLevel();

    /**
     * Changes the specified player metric based on the given effect and change amount.
     *
     * @param type The type of the player metric to change. This should be one of the values from the {@link MetricType} enum.
     * @param effect The effect to apply to the player metric. This should be one of the values from the {@link MetricEffect} enum.
     * @param changeAmount The amount by which to change the player metric. This is used when the effect is either {@link MetricEffect#INCREASE} or {@link MetricEffect#DECREASE}.
     * @throws IllegalArgumentException If an invalid metric type or effect is provided.
     */
    public void changeMetric(@NotNull MetricType type, MetricEffect effect, float changeAmount) throws IllegalArgumentException {
        PlayerMetric metric;
        // Determine the metric to change based on the provided type
        switch (type) {
            case ENERGY:
                metric = energy;
                break;
            case HAPPINESS:
                metric = happiness;
                break;
            case STUDY_LEVEL:
                metric = studyLevel;
                break;
            default:
                throw new IllegalArgumentException("Invalid metric type: " + type);
        }

        // Apply the specified effect to the determined metric
        switch (effect) {
            case INCREASE:
                metric.increase(changeAmount);
                break;
            case DECREASE:
                metric.decrease(changeAmount);
                break;
            case RESET:
                metric.set(metric.getDefault());
                break;
            default:
                throw new IllegalArgumentException("Invalid metric effect: " + effect);
        }
    }

    /**
     * Returns the player metric of the specified type.
     *
     * @param type The type of the player metric to return. This should be one of the values from the {@link MetricType} enum.
     * @return The player metric of the specified type.
     * @throws IllegalArgumentException If an invalid metric type is provided.
     */
    public PlayerMetric getMetric(@NotNull MetricType type) throws IllegalArgumentException {
        switch (type) {
            case ENERGY:
                return energy;
            case HAPPINESS:
                return happiness;
            case STUDY_LEVEL:
                return studyLevel;
            default:
                throw new IllegalArgumentException("Invalid metric type: " + type);
        }
    }

    /**
     * Get the list of all player metrics.
     * @return An unmodifiable list of PlayerMetric objects.
     */
    @Contract(value = " -> new", pure = true)
    public @Unmodifiable List<PlayerMetric> getMetrics() {
        return List.of(energy, happiness, studyLevel);
    }

    /**
     * Dispose the resources related to the player metrics when they are no longer needed.
     */
    public void dispose() {
        energy.dispose();
        happiness.dispose();
        studyLevel.dispose();
    }
}