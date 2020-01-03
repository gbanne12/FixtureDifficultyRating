import fpl.event.GameWeek;
import model.Footballer;
import fpl.teams.fantasy.Squad;
import fpl.score.FixtureDifficultyCalculator;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Run {

    private static final int WEEKS_TO_EVALUATE = 5;

    public static void main(String[] args) throws IOException {
        int gameWeek = new GameWeek().getCurrent();
        Squad squad = new Squad();
        List<Footballer> footballers = squad.get(gameWeek - 1);

        FixtureDifficultyCalculator calculator = new FixtureDifficultyCalculator();
        int i = 0;
        while (i < WEEKS_TO_EVALUATE) {
            for (Footballer footballer : footballers) {
                calculator.getDifficulty(footballer, gameWeek + i);
            }
            i++;
        }

        Collections.sort(footballers);
        Collections.reverse(footballers);

        for (Footballer footballer : footballers) {
            System.out.println("Player: " + footballer.getWebName()
                    + "| Opponent: " + footballer.getOpponentList().toString()
                    + "| Total: " + footballer.getDifficultyTotal());
        }
    }
}
