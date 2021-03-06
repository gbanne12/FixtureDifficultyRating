package data.model;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Footballer implements Comparable<Footballer> {

    private int id;
    private int teamId;
    private int position;
    private String webName;
    private String teamName;
    private List<Opponent> opponentList = new ArrayList<>();
    private int difficultyTotal;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDifficultyTotal() {
        int total = 0;
        for (Opponent opponent : opponentList) {
            total += opponent.getDifficultyRating();
        }
        return total;
    }

    public void setDifficultyTotal(int difficultyTotal) {
        this.difficultyTotal = difficultyTotal;
    }

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<Opponent> getOpponentList() {
        return opponentList;
    }

    public void setOpponentList(List<Opponent> opponentList) {
        this.opponentList = opponentList;
    }

    @Override
    public int compareTo(Footballer footballer) {
        return Integer.compare(getDifficultyTotal(), footballer.getDifficultyTotal());
    }
}
