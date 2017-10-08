package com.housesnow.scaldis.overview;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.housesnow.scaldis.objects.Team;
import com.housesnow.scaldis.utilities.QueryUtils;

import java.net.URL;
import java.util.List;

/**
 * Created by Sander on 1-10-2017.
 */

public class TeamOverviewLoader extends AsyncTaskLoader<List<Team>>  {
    private URL mUrl;

    public TeamOverviewLoader(Context context, URL url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Team> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list
        List<Team> teams = QueryUtils.fetchTeamFromOrganisation(mUrl);
        return teams;
    }
}
