package com.cegep.sportify.model;

import java.util.List;

public class SportWithTeams {

    private String sport;

    private List<String> teams;

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public List<String> getTeams() {
        return teams;
    }

    public void setTeams(List<String> teams) {
        this.teams = teams;
    }
}
