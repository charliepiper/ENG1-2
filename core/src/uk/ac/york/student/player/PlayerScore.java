package uk.ac.york.student.player;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import uk.ac.york.student.screens.GameScreen;

/**
 * The PlayerScore interface provides methods to calculate and convert a player's score.
 * The score is calculated based on the player's energy, study time, and the game's difficulty level.
 * The score is then converted to a string representation of a degree class.
 */
public interface PlayerScore {
    /**
     * Calculate a score for the player based on their energy, study level, and happiness.
     * The score is calculated using the provided weightings for each parameter.
     * The weightings are used to determine the importance of each parameter in the final score.
     *
     * @param energy The player's energy level.
     * @param maxEnergy The maximum possible energy level.
     * @param studyLevel The player's study level.
     * @param maxStudyLevel The maximum possible study level.
     * @param happiness The player's happiness level.
     * @param maxHappiness The maximum possible happiness level.
     * @return The player's score, calculated based on the provided parameters and weightings.
     */
    default float calculateScore(float energy, float maxEnergy, float studyLevel, float maxStudyLevel, float happiness, float maxHappiness) {
        float energyWeighting = 1.2f;
        float studyWeighting = 2f;
        float happinessWeighting = 1f;

        float energyScore = (energy / maxEnergy) * energyWeighting;
        float studyScore = (studyLevel / maxStudyLevel) * studyWeighting;
        float happinessScore = (happiness / maxHappiness) * happinessWeighting;

        float totalScore = energyScore + studyScore + happinessScore;
        float maxPossibleScore = energyWeighting + studyWeighting + happinessWeighting;

        if (GameScreen.notStudiedCounter > 1) {
            return 39f;
        }

        return (totalScore / maxPossibleScore) * 100;
    }


    /**
     * Calculate a score for the player based on their energy, study time, and the game's difficulty level.
     * The score is calculated using a specific algorithm that has diminishing returns for the amount of work the player has done.
     * The algorithm also allows for a choice of difficulties, with a custom difficulty range.
     *
     * @deprecated
     * @param energy The player's energy level.
     * @param maxEnergy The maximum possible energy level.
     * @param studyTime The time the player has spent studying.
     * @param maxStudyTime The maximum possible study time.
     * @param difficulty The game's difficulty level.
     * @param maxDifficulty The maximum possible difficulty level.
     * @return The player's score, calculated based on the provided parameters.
     */
    @Deprecated(forRemoval = true)
    default int calculateScore(float energy, float maxEnergy, float studyTime,
                               float maxStudyTime, int difficulty,
                               int maxDifficulty) {
        // Calculate the energy score, study score, and difficulty score.
        float energyScore = Math.min(energy / maxEnergy, 1.0f) * 100;
        float studyScore = Math.min(studyTime / maxStudyTime, 1.0f) * 100;
        float difficultyScore = (float) difficulty / (float) maxDifficulty;

        // Calculate the final score using the algorithm.
        double percentScoreDouble = Math.min(
            (200.0f * (1.0f
                - 10.0f * (1.0f / (energyScore + 20.0f)
                + 1.0f / (studyScore + 20.0f))))
                / (1.4 + difficultyScore * (0.26f))
            , 100.0f);

        // Return the final score as an integer.
        return (int) Math.round(percentScoreDouble);
    }

    /**
     * Overload of the calculateScore method that only requires energy and study time parameters.
     * This method assumes a default difficulty level of 1 for both the game and the maximum possible difficulty.
     *
     * @deprecated
     * @param energy The player's energy level.
     * @param maxEnergy The maximum possible energy level.
     * @param studyTime The time the player has spent studying.
     * @param maxStudyTime The maximum possible study time.
     * @return The player's score, calculated based on the provided parameters and the default difficulty level.
     */
    @Deprecated(forRemoval = true)
    default int calculateScore(float energy, float maxEnergy, float studyTime, float maxStudyTime) {
        return calculateScore(energy, maxEnergy, studyTime, maxStudyTime, 1, 1);
    }
    /**
     * Overload of the calculateScore method that accepts integer parameters.
     * This method simply converts the integer parameters to floats and calls the other calculateScore method.
     *
     * @deprecated
     * @param energy The player's energy level.
     * @param maxEnergy The maximum possible energy level.
     * @param studyTime The time the player has spent studying.
     * @param maxStudyTime The maximum possible study time.
     * @param difficulty The game's difficulty level.
     * @param maxDifficulty The maximum possible difficulty level.
     * @return The player's score, calculated based on the provided parameters.
     */
    @Deprecated(forRemoval = true)
    default int calculateScore(int energy, int maxEnergy, int studyTime,
                               int maxStudyTime, int difficulty,
                               int maxDifficulty) {
        return calculateScore(
            (float) energy, (float) maxEnergy, (float) studyTime,
            (float) maxStudyTime, difficulty, maxDifficulty
        );
    }

    /**
     * Convert a score to a string representation of a degree class.
     * The degree class is determined based on the score's value.
     *
     * @param score The score to convert.
     * @return A string representation of a degree class.
     */
    @Contract(pure = true)
    default @NotNull String convertScoreToString(float score) {
        if (score >= 70) {
            return "First-class Honours";
        } else if (score >= 60) {
            return "Upper second-class Honours";
        } else if (score >= 50) {
            return "Lower second-class Honours";
        } else if (score >= 40) {
            return "Third-class Honours";
        } else {
            return "Fail";
        }
    }
}