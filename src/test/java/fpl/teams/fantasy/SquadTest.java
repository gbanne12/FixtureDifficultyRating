package fpl.teams.fantasy;

import model.Footballer;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SquadTest {

    @Test
    public void canGetSquadOf15() throws IOException {
        Squad squad = new Squad();
        List<Footballer> gameweekOneSquad = squad.get(1);
        assertEquals("Full squad with subs should be 15 players", 15, gameweekOneSquad.stream().distinct().count());
        assertTrue(containsName(gameweekOneSquad, "Begovic"));
        assertTrue(containsName(gameweekOneSquad, "Digne"));
        assertTrue(containsName(gameweekOneSquad, "Alexander-Arnold"));
        assertTrue(containsName(gameweekOneSquad, "Zinchenko"));
        assertTrue(containsName(gameweekOneSquad, "Sterling"));
        assertTrue(containsName(gameweekOneSquad, "Zaha"));
        assertTrue(containsName(gameweekOneSquad, "Maddison"));
        assertTrue(containsName(gameweekOneSquad, "Salah"));
        assertTrue(containsName(gameweekOneSquad, "Lucas Moura"));
        assertTrue(containsName(gameweekOneSquad, "Origi"));
        assertTrue(containsName(gameweekOneSquad, "Callum Wilson"));
        assertTrue(containsName(gameweekOneSquad, "Button"));
        assertTrue(containsName(gameweekOneSquad, "Dunk"));
        assertTrue(containsName(gameweekOneSquad, "Lundstram"));
        assertTrue(containsName(gameweekOneSquad, "Greenwood"));
    }

    private boolean containsName(final List<Footballer> list, final String name){
        return list.stream().anyMatch(footballer -> footballer.getWebName().equals(name));
    }
}
