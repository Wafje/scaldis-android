package com.housesnow.scaldis.objects;

import java.util.List;

/**
 * Created by Sander on 5-10-2017.
 */

public class Poule {
    private String name;
    private String guid;
    private List<PouleTeam> teams;

    public Poule(String name, List<PouleTeam> teams) {
        this.name = name;
        this.teams = teams;
    }

    public String getName() {
        return name;
    }

    public String getGuid() {return guid; }

    public List<PouleTeam> getTeams() {
        return teams;
    }
}
