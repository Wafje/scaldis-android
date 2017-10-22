package be.jbs.scaldis.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sander on 1-10-2017.
 */

public class Team implements Comparable<Team> {

    // General
    private String name = "";
    private String guid = "";
    private String category = "";
    private String subcategory = "";
    private Integer age = 0;

    private List<Match> matches = new ArrayList<>();
    private List<Poule> poules = new ArrayList<>();

    public Team() {}

    public Team(String name, String guid) {
        this.name = name;
        this.guid = guid;

        this.category = parseCategory(name);
        this.subcategory = parseSubcategory(name);
        this.age = parseAge(name);
    }

    public Team(String name, String guid, List<Match> matches, List<Poule> poules) {
        this(name,guid);
        this.matches = matches;
        this.poules = poules;
    }

    public String getName() {
        return name;
    }

    public String getGuid() {
        return guid;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public Integer getAge() {
        return age;
    }


    public List<Match> getMatches() {
        return matches;
    }

    public List<Poule> getPoules() {
        return poules;
    }


    private Integer parseAge(String name) {
        if (name == null || name.isEmpty()) {
            return 0;
        }

        String stringAge = name.replaceAll("\\D","");

        try {
            return Integer.parseInt(stringAge);
        } catch (NumberFormatException e) {
            return 99;
        }
    }

    private String parseCategory(String name) {
        // Ploeg J16 A
        Pattern p = Pattern.compile("\\D\\d{2,}");
        Matcher m = p.matcher(name);

        if (m.find()) {
            return name.substring(m.start(), m.end());
        }

        // Ploeg HSE A
        p = Pattern.compile("\\s\\D{3}\\s");
        m = p.matcher(name);
        if (m.find()) {
            return name.substring(m.start(), m.end()).trim();
        }

        // Else
        return "";

    }

    private String parseSubcategory(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        return name.substring(name.length() - 1, name.length());
    }

    @Override
    public int compareTo(Team o2) {
        if (o2.getAge() == this.getAge()) {
            return this.getSubcategory().compareTo(o2.getSubcategory());
        } else {
            return o2.getAge() - this.getAge();
        }
    }
}
