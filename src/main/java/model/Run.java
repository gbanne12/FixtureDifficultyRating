package model;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import model.json.Element;
import model.json.Fixture;
import model.json.Pick;
import model.json.Team;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import url.JsonUrl;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Run {

    private static final int CURRENT_WEEK = 4;
    private static final int WEEKS_TO_EVALUATE = 5;
    private static List<Team> teamList;


    public static void main(String[] args) throws IOException {
        Run run = new Run();
        teamList = run.getTeamList();
        List<Footballer> footballers = run.getFootballersInSquad();
        run.setFootballerNameAndTeamId(footballers);
        run.setTeamName(footballers);
        run.setFixtureDifficultyRating(footballers);
        Collections.sort(footballers);
        Collections.reverse(footballers);

        for (Footballer footballer : footballers) {
            System.out.println("Player: " + footballer.getWebName() +
                    "| Opposition: " + footballer.getOppositionList().toString() +
                    "| Total: " + footballer.getDifficultyTotal());
        }
    }

    private JSONObject getJsonObject(String url) throws IOException {
        return new JSONObject(IOUtils.toString(new URL(url), Charset.forName("UTF-8")));
    }

    private List<Footballer> getFootballersInSquad() throws IOException {
        JSONObject picksJson = getJsonObject(JsonUrl.PICKS_PREFIX.url + CURRENT_WEEK + JsonUrl.PICKS_SUFFIX.url);
        JSONArray picksArray = picksJson.getJSONArray("picks");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Pick> picksAdapter = moshi.adapter(Pick.class);

        List<Footballer> footballers = new ArrayList<>();
        for (int i = 0; i < picksArray.length(); i++) {
            Pick pick = picksAdapter.fromJson((picksArray.get(i)).toString());
            if (pick != null) {
                Footballer footballer = new Footballer();
                footballer.setId(pick.element);
                footballer.setPosition(pick.position);
                footballers.add(footballer);
            }
        }
        return footballers;
    }

    private void setFootballerNameAndTeamId(List<Footballer> footballers) throws IOException {
        JSONObject elementsJson = getJsonObject(JsonUrl.STATIC.url);
        JSONArray elementsArray = elementsJson.getJSONArray("elements");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Element> elementsAdapter = moshi.adapter(Element.class);

        for (int j = 0; j < elementsArray.length(); j++) {
            Element element = elementsAdapter.fromJson((elementsArray.get(j)).toString());
            for (Footballer footballer : footballers) {
                if (element != null && element.id == footballer.getId()) {
                    footballer.setTeamId(element.team);
                    footballer.setWebName(element.web_name);
                }
            }
        }
    }

    private void setTeamName(List<Footballer> footballers) throws IOException {
        JSONObject elementsJson = getJsonObject(JsonUrl.STATIC.url);
        JSONArray teamsArray = elementsJson.getJSONArray("teams");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Team> teamsAdapter = moshi.adapter(Team.class);
        for (int i = 0; i < teamsArray.length(); i++) {
            Team team = teamsAdapter.fromJson(teamsArray.get(i).toString());
            for (Footballer footballer : footballers) {
                if (team != null && team.id == footballer.getTeamId()) {
                    footballer.setTeamName(team.short_name);
                }
            }
        }
    }

    private void setFixtureDifficultyRating(List<Footballer> footballers) throws IOException {
        for (int i = 0; i < WEEKS_TO_EVALUATE; i++) {
            List<Fixture> fixtures = getFixtureList((CURRENT_WEEK + i));
            for (Footballer footballer : footballers) {
                for (Fixture fixture : fixtures) {
                    if (footballer.getTeamId() == fixture.team_a) {
                        setOppositionNameAndDifficulty(footballer, fixture, false);
                        break;
                    } else if (footballer.getTeamId() == fixture.team_h) {
                        setOppositionNameAndDifficulty(footballer, fixture, true);
                        break;
                    }
                }
            }
        }
    }


    private void setOppositionNameAndDifficulty(Footballer footballer, Fixture fixture, boolean isFootballerAtHome) {
        Opposition opposition = new Opposition();
        if (isFootballerAtHome) {
            opposition.setTeamId(fixture.team_a);
            opposition.setDifficultyRating(fixture.team_h_difficulty);
            for (Team team : teamList) {
                if (fixture.team_a == team.id) {
                    opposition.setName(team.short_name);
                }
            }
        } else {
            opposition.setTeamId(fixture.team_h);
            opposition.setDifficultyRating(fixture.team_a_difficulty);
            for (Team team : teamList) {
                if (fixture.team_a == team.id) {
                    opposition.setName(team.short_name);
                }
            }
        }

        List<Opposition> oppositionList = footballer.getOppositionList();
        oppositionList.add(opposition);
        footballer.setOppositionList(oppositionList);
        int currentDifficultyScore = footballer.getDifficultyTotal();
        footballer.setDifficultyTotal(currentDifficultyScore + opposition.getDifficultyRating());
    }


    private List<Fixture> getFixtureList(int gameweek) throws IOException {
        URL fixturesEndpoint = new URL(JsonUrl.FIXTURES.url + (gameweek));
        JSONArray fixturesArray = new JSONArray(IOUtils.toString(fixturesEndpoint, Charset.forName("UTF-8")));
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Fixture> fixturesAdapter = moshi.adapter(Fixture.class);

        List<Fixture> fixtures = new ArrayList<>();
        for (int i = 0; i < fixturesArray.length(); i++) {
            Fixture fixture = fixturesAdapter.fromJson(fixturesArray.get(i).toString());
            fixtures.add(fixture);
        }

        return fixtures;
    }

    private List<Team> getTeamList() throws IOException {
        JSONObject elementsJson = getJsonObject(JsonUrl.STATIC.url);
        JSONArray teamsArray = elementsJson.getJSONArray("teams");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Team> teamsAdapter = moshi.adapter(Team.class);
        List<Team> teamList = new ArrayList<>();
        for (int j = 0; j < teamsArray.length(); j++) {
            teamList.add(teamsAdapter.fromJson(teamsArray.get(j).toString()));
        }
        return teamList;
    }
}
