package fpl.score;

import model.Footballer;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FixtureDifficultyCalculatorTest {

    @Test
    public void canSetDifficultyGivenFootballerAndWeek() throws IOException {
        // Given
        Footballer liverpoolPlayer = new Footballer();
        liverpoolPlayer.setTeamId(10);

        // When
        FixtureDifficultyCalculator calculator = new FixtureDifficultyCalculator();
        calculator.setDifficulty(liverpoolPlayer, 1);

        // Then
        int expectedDifficultyForLiverpoolWeek1 = 2;
        assertEquals(expectedDifficultyForLiverpoolWeek1, liverpoolPlayer.getDifficultyTotal());
    }

}
