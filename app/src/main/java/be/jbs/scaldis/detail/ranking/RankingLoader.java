package be.jbs.scaldis.detail.ranking;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import be.jbs.scaldis.objects.Poule;
import be.jbs.scaldis.utilities.QueryUtils;

import java.net.URL;
import java.util.List;

/**
 * Created by Sander on 5-10-2017.
 */

public class RankingLoader extends AsyncTaskLoader<List<Poule>> {

    private URL requestPoulesUrl;

    public RankingLoader(Context context, URL requestPoulesUrl) {
        super(context);
        this.requestPoulesUrl = requestPoulesUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Poule> loadInBackground() {
        if (requestPoulesUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list
        List<Poule> poules = QueryUtils.fetchPoulesFromTeam(requestPoulesUrl);
        return poules;
    }
}
