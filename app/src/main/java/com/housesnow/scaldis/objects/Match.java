package com.housesnow.scaldis.objects;

import java.util.Date;

/**
 * Created by Sander on 5-10-2017.
 */

public class Match {
    private Team homeTeam;
    private Team awayTeam;
    private Integer homeScore;
    private Integer awayScore;
    private Date datetime;
    private String guid;

    public Match(Team homeTeam, Team awayTeam, Integer homeScore, Integer awayScore, Date datetime, String guid) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.datetime = datetime;
        this.guid = guid;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public Date getDatetime() {
        return datetime;
    }

    public String getGuid() {
        return guid;
    }
}
