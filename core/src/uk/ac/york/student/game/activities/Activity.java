package uk.ac.york.student.game.activities;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import uk.ac.york.student.player.Player;
import uk.ac.york.student.player.PlayerMetrics;
import uk.ac.york.student.utils.Pair;

import java.util.List;

/**
 * The {@link Activity} enum represents the different activities a player can perform.
 * Each activity has a list of effects on the player's metrics, represented as {@link Pair}s of {@link PlayerMetrics.MetricType} and {@link PlayerMetrics.MetricEffect}.
 * The effects of each activity are defined in the constructor.
 * The effects are stored in an {@link Unmodifiable} {@link List}, which can be retrieved using the getter method {@link Activity#getEffects()}.
 */
@Getter
public enum Activity {
    /**
     * The {@link Activity#STUDY} activity increases the {@link Player}'s {@link PlayerMetrics#getStudyLevel()} and decreases their {@link PlayerMetrics#getEnergy()} and {@link PlayerMetrics#getHappiness()} and {@link PlayerMetrics#getHealth()}.
     */
    STUDY(
        Pair.of(PlayerMetrics.MetricType.STUDY_LEVEL, PlayerMetrics.MetricEffect.INCREASE),
        Pair.of(PlayerMetrics.MetricType.ENERGY, PlayerMetrics.MetricEffect.DECREASE),
        Pair.of(PlayerMetrics.MetricType.HAPPINESS, PlayerMetrics.MetricEffect.DECREASE),
        Pair.of(PlayerMetrics.MetricType.HEALTH, PlayerMetrics.MetricEffect.DECREASE)
    ),
    /**
     * The {@link Activity#SLEEP} activity resets the {@link Player}'s {@link PlayerMetrics#getEnergy()} and {@link PlayerMetrics#getStudyLevel()} and increases their {@link PlayerMetrics#getHealth()}.
     */
    SLEEP(
        Pair.of(PlayerMetrics.MetricType.ENERGY, PlayerMetrics.MetricEffect.RESET),
        Pair.of(PlayerMetrics.MetricType.STUDY_LEVEL, PlayerMetrics.MetricEffect.RESET),
        Pair.of(PlayerMetrics.MetricType.HEALTH, PlayerMetrics.MetricEffect.INCREASE)
    ),
    /**
     * The {@link Activity#NAP} activity increases the {@link Player}'s {@link PlayerMetrics#getEnergy()}.
     */
    NAP(
        Pair.of(PlayerMetrics.MetricType.ENERGY, PlayerMetrics.MetricEffect.INCREASE),
        Pair.of(PlayerMetrics.MetricType.HEALTH, PlayerMetrics.MetricEffect.INCREASE)
    ),
    /**
     * The {@link Activity#EAT} activity increases the {@link Player}'s {@link PlayerMetrics#getHappiness()}.
     */
    EAT(
        Pair.of(PlayerMetrics.MetricType.HEALTH, PlayerMetrics.MetricEffect.INCREASE),
        Pair.of(PlayerMetrics.MetricType.ENERGY, PlayerMetrics.MetricEffect.INCREASE)
    ),
    /**
     * The {@link Activity#ENTERTAIN} activity increases the {@link Player}'s {@link PlayerMetrics#getHappiness()} and decreases their {@link PlayerMetrics#getEnergy()}.
     */
    ENTERTAIN(
        Pair.of(PlayerMetrics.MetricType.HAPPINESS, PlayerMetrics.MetricEffect.INCREASE),
        Pair.of(PlayerMetrics.MetricType.ENERGY, PlayerMetrics.MetricEffect.DECREASE)
    ),
    /**
     * The {@link Activity#EXERCISE} activity increases the {@link Player}'s {@link PlayerMetrics#getHealth()} and decreases their {@link PlayerMetrics#getEnergy()}.
     */
    EXERCISE(
        Pair.of(PlayerMetrics.MetricType.HEALTH, PlayerMetrics.MetricEffect.INCREASE),
        Pair.of(PlayerMetrics.MetricType.ENERGY, PlayerMetrics.MetricEffect.DECREASE)
            );

    /**
     * The effects of the activity on the {@link Player}'s metrics.
     * Each effect, in the {@link Unmodifiable} {@link List}, is represented as a {@link Pair} of {@link PlayerMetrics.MetricType} and {@link PlayerMetrics.MetricEffect}.
     */
    private final @Unmodifiable List<Pair<PlayerMetrics.MetricType, PlayerMetrics.MetricEffect>> effects;

    /**
     * Constructs a new {@link Activity} enum constant with the specified effects.
     *
     * @param effects the effects of the activity on the {@link Player}'s metrics
     */
    @SafeVarargs
    Activity(Pair<PlayerMetrics.MetricType, PlayerMetrics.MetricEffect> ... effects) {
        this.effects = List.of(effects);
    }

    /**
     * Returns the {@link PlayerMetrics.MetricEffect} of the activity on the specified {@link PlayerMetrics.MetricType}.
     * This method iterates over {@link Activity#effects}.
     * If a {@link PlayerMetrics.MetricEffect} with the specified {@link PlayerMetrics.MetricType} is found, the {@link PlayerMetrics.MetricEffect} is returned.
     * If no {@link PlayerMetrics.MetricEffect} with the specified {@link PlayerMetrics.MetricType} is found, null is returned.
     *
     * @param metricType the {@link PlayerMetrics.MetricType} to get the {@link PlayerMetrics.MetricEffect} for
     * @return the {@link PlayerMetrics.MetricEffect} of the activity on the specified {@link PlayerMetrics.MetricType}, or null if no {@link PlayerMetrics.MetricEffect} is found
     */
    public @Nullable PlayerMetrics.MetricEffect getEffect(PlayerMetrics.MetricType metricType) {
        for (Pair<PlayerMetrics.MetricType, PlayerMetrics.MetricEffect> effect : effects) {
            if (effect.getLeft() == metricType) {
                return effect.getRight();
            }
        }
        return null;
    }

    /**
     * Returns the index of the first occurrence of the specified {@link PlayerMetrics.MetricType} in {@link Activity#effects}.
     * This method iterates over {@link Activity#effects}.
     * If an effect with the specified {@link PlayerMetrics.MetricType} is found, the index of the effect in {@link Activity#effects} is returned.
     * If no effect with the specified {@link PlayerMetrics.MetricType} is found, -1 is returned.
     *
     * @param metricType the {@link PlayerMetrics.MetricType} to find in {@link Activity#effects}
     * @return the index of the first occurrence of the specified {@link PlayerMetrics.MetricType} in {@link Activity#effects}, or -1 if the {@link PlayerMetrics.MetricType} is not found
     */
    public int indexOf(PlayerMetrics.MetricType metricType) {
        for (int i = 0; i < effects.size(); i++) {
            if (effects.get(i).getLeft() == metricType) {
                return i;
            }
        }
        return -1;
    }
}
