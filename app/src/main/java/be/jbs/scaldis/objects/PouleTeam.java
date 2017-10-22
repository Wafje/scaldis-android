package be.jbs.scaldis.objects;

/**
 * Created by Sander on 8-10-2017.
 */

public class PouleTeam {

    // General
    private String name = "";
    private String guid = "";

    private Integer rank = 0;
    private Integer numberOfGames = 0;
    private Integer numberOfPoints = 0;
    private Integer pointsScored = 0;
    private Integer pointsAgainst = 0;
    private Integer gamesWon = 0;
    private Integer gamesDraw = 0;
    private Integer gamesLost = 0;

    public PouleTeam(String name, String guid){
        this.name = name;
        this.guid = guid;
    }

    public PouleTeam(String name, String guid, Integer rank, Integer numberOfGames, Integer numberOfPoints, Integer pointsScored, Integer pointsAgainst, Integer gamesWon, Integer gamesDraw, Integer gamesLost) {
        this.name = name;
        this.guid = guid;
        this.rank = rank;
        this.numberOfGames = numberOfGames;
        this.numberOfPoints = numberOfPoints;
        this.pointsScored = pointsScored;
        this.pointsAgainst = pointsAgainst;
        this.gamesWon = gamesWon;
        this.gamesDraw = gamesDraw;
        this.gamesLost = gamesLost;
    }

    public String getName() {
        return name;
    }

    public String getGuid() {
        return guid;
    }

    public Integer getRank() {
        return rank;
    }

    public Integer getNumberOfGames() {
        return numberOfGames;
    }

    public Integer getNumberOfPoints() {
        return numberOfPoints;
    }

    public Integer getPointsScored() {
        return pointsScored;
    }

    public Integer getPointsAgainst() {
        return pointsAgainst;
    }

    public Integer getGamesWon() {
        return gamesWon;
    }

    public Integer getGamesDraw() {
        return gamesDraw;
    }

    public Integer getGamesLost() {
        return gamesLost;
    }
}
