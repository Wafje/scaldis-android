package be.jbs.scaldis.utilities;

import android.text.TextUtils;
import android.util.Log;

import be.jbs.scaldis.objects.Match;
import be.jbs.scaldis.objects.Poule;
import be.jbs.scaldis.objects.PouleTeam;
import be.jbs.scaldis.objects.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sander on 1-10-2017.
 */

public final class QueryUtils {

    public static String fetchAddressFromMatchGuid(URL requestUrl) {
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(requestUrl);
        } catch (IOException e) {

        }

        // Extract relevant fields from the JSON response
        return extractAddressFromMatchJson(jsonResponse);
    }

    private static String extractAddressFromMatchJson(String matchJson) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(matchJson)) {
            return null;
        }

        // Create empty arraylist
        String address = "";

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONArray(matchJson).getJSONObject(0);

            JSONObject addressJson = baseJsonResponse.getJSONObject("doc").getJSONObject("accommodatieDoc").getJSONObject("adres");

            String street = addressJson.getString("straat");
            if (street.equals("null"))
                street = "";
            String number = addressJson.getString("huisNr");
            if (number.equals("null"))
                number = "";
            String city = addressJson.getString("plaats");
            if (city.equals("null"))
                city = "";

            address = street + "+" + number + "+" + city;

        } catch (JSONException e) {

        }

        // Return the list
        return address;
    }


    public static List<Team> fetchTeamFromOrganisation(URL requestUrl) {
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(requestUrl);
        } catch (IOException e) {

        }

        // Extract relevant fields from the JSON response and create a list of {@link Team}s
        List<Team> teams = extractFromOrgJson(jsonResponse);

        // Return the list of {@link Team}s
        return teams;
    }

    private static List<Team> extractFromOrgJson(String teamJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(teamJSON)) {
            return null;
        }

        // Create empty arraylist
        List<Team> teams = new ArrayList<>();

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONArray(teamJSON).getJSONObject(0);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or teams).
            JSONArray teamArray = baseJsonResponse.getJSONArray("teams");

            for (int i = 0; i < teamArray.length(); i++) {

                //Get a single team at position i
                JSONObject currentTeam = teamArray.getJSONObject(i);

                String name = currentTeam.getString("naam");
                String guid = currentTeam.getString("guid");

                Team team = new Team(name, guid);

                teams.add(team);
            }

        } catch (JSONException e) {

        }

        // Return the list
        return teams;
    }

    public static List<Poule> fetchPoulesFromTeam(URL requestUrl) {
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(requestUrl);
        } catch (IOException e) {

        }

        // Extract relevant fields from the JSON response and create a list of {@link Team}s
        List<Poule> poules = extractPoulesFromTeamJson(jsonResponse);

        return poules;
    }

    public static List<Match> fetchMatchesFromTeam(URL requestUrl) {
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(requestUrl);
        } catch (IOException e) {

        }

        // Extract relevant fields from the JSON response and create a list of {@link Team}s
        List<Match> matches = extractMatchesFromMatchesJson(jsonResponse);

        return matches;
    }

    private static List<Poule> extractPoulesFromTeamJson(String teamJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(teamJSON)) {
            return null;
        }

        // Create empty arraylist
        List<Poule> poules = new ArrayList<>();

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONArray(teamJSON).getJSONObject(0);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or teams).
            JSONArray poulesArray = baseJsonResponse.getJSONArray("poules");

            for (int i = 0; i < poulesArray.length(); i++) {
                JSONObject currentPoule = poulesArray.getJSONObject(i);

                String pouleName = currentPoule.getString("naam");
                JSONArray teamsInPouleArray = currentPoule.getJSONArray("teams");
                List<PouleTeam> teamsInPoule = new ArrayList<>();

                for (int j = 0; j < teamsInPouleArray.length(); j++) {
                    JSONObject currentTeam = teamsInPouleArray.getJSONObject(j);

                    String teamName = currentTeam.getString("naam");
                    String teamGuid = currentTeam.getString("guid");
                    String teamCategory = currentTeam.getString("categorie");

                    Integer rank = parseStringFromJson(currentTeam.getString("rangNr"));
                    Integer numberOfGames = parseStringFromJson(currentTeam.getString("wedAant"));
                    Integer numberOfPoints = parseStringFromJson(currentTeam.getString("wedPunt"));
                    Integer pointsScore = parseStringFromJson(currentTeam.getString("ptVoor"));
                    Integer pointsAgainst = parseStringFromJson(currentTeam.getString("ptTegen"));
                    Integer gamesWon = parseStringFromJson(currentTeam.getString("wedWinst"));
                    Integer gamesDraw = parseStringFromJson(currentTeam.getString("wedGelijk"));
                    Integer gamesLost = parseStringFromJson(currentTeam.getString("wedVerloren"));

                    PouleTeam teamInPoule = new PouleTeam(teamName,teamGuid,
                            rank, numberOfGames, numberOfPoints,
                            pointsScore, pointsAgainst, gamesWon, gamesDraw, gamesLost);

                    teamsInPoule.add(teamInPoule);
                }

                Poule poule = new Poule(pouleName, teamsInPoule);

                poules.add(poule);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the team JSON results", e);
        }

        // Return the list
        return poules;
    }

    private static List<Match> extractMatchesFromMatchesJson(String matchJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(matchJSON)) {
            return null;
        }

        // Create empty arraylist
        List<Match> matches = new ArrayList<>();

        try {
            // Create a JSONObject from the JSON response string
            JSONArray baseJsonResponse = new JSONArray(matchJSON);

            for (int i = 0; i < baseJsonResponse.length(); i++) {
                JSONObject currentMatch = baseJsonResponse.getJSONObject(i);

                String homeTeamName = currentMatch.getString("tTNaam");
                String awayTeamName = currentMatch.getString("tUNaam");
                String homeTeamGuid = currentMatch.getString("tTGUID");
                String awayTeamGuid = currentMatch.getString("tUGUID");

                String score = currentMatch.getString("uitslag");

                String date = currentMatch.getString("datumString");
                String time = currentMatch.getString("beginTijd");

                String guid = currentMatch.getString("guid");

                String accomodation = currentMatch.getString("accNaam");

                        String dateTime;
                SimpleDateFormat dateTimeFormat;

                if (time.isEmpty()) {
                    dateTime = date;
                    dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy");
                } else {
                    dateTime = date.concat(" ").concat(time);
                    dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy kk.mm");
                }

                Date dateObj = new Date();

                try {
                    dateObj = dateTimeFormat.parse(dateTime);
                } catch (ParseException e) {
                    System.out.println("ERROR: Failed to parse start time.");
                    e.printStackTrace();
                }

                Integer homeScore = 0;
                Integer awayScore = 0;
                if (!score.isEmpty()) {
                    String[] scores = score.replaceAll("AFOR","").trim().split("-\\s*");

                    homeScore = Integer.valueOf(scores[0]);
                    awayScore = Integer.valueOf(scores[1]);
                }

                Team homeTeam = new Team(homeTeamName,homeTeamGuid);
                Team awayTeam = new Team(awayTeamName,awayTeamGuid);


                Match match = new Match(homeTeam, awayTeam, homeScore, awayScore, dateObj, guid, accomodation);

                matches.add(match);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the team JSON results", e);
        }

        // Return the list
        return matches;
    }

    private static Integer parseStringFromJson(String string) {
        try {
            return Integer.valueOf(string.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
            * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {

            }
        } catch (IOException e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
