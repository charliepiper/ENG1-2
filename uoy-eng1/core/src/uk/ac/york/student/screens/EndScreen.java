package uk.ac.york.student.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import uk.ac.york.student.GdxGame;
import uk.ac.york.student.player.Player;
import uk.ac.york.student.player.PlayerMetric;
import uk.ac.york.student.player.PlayerMetrics;

import java.util.List;

@Getter
public class EndScreen extends BaseScreen {
    private final Stage processor;
    private final Player player;
    public EndScreen(GdxGame game) {
        super(game);
        throw new UnsupportedOperationException("This constructor is not supported (must pass in object args!)");
    }

    public EndScreen(GdxGame game, boolean shouldFadeIn, float fadeInTime, Object @NotNull [] args) {
        super(game);
        processor = new Stage(new ScreenViewport());
        player = (Player) args[0];

        PlayerMetrics metrics = player.getMetrics();
        float energyTotal = metrics.getEnergy().getTotal();
        float studyLevelTotal = metrics.getStudyLevel().getTotal();
        float happinessTotal = metrics.getHappiness().getTotal();
        float energyMax = metrics.getEnergy().getMaxTotal();
        float studyLevelMax = metrics.getStudyLevel().getMaxTotal();
        float happinessMax = metrics.getHappiness().getMaxTotal();

        float score = player.calculateScore(energyTotal, energyMax, studyLevelTotal, studyLevelMax, happinessTotal, happinessMax);
        System.out.println(score);
        String scoreString = player.convertScoreToString(score);
        System.out.println(scoreString);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
