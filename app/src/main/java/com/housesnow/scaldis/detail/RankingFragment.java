package com.housesnow.scaldis.detail;

import android.content.Context;
import android.support.v4.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.housesnow.scaldis.R;
import com.housesnow.scaldis.objects.Poule;
import com.housesnow.scaldis.objects.PouleTeam;
import com.housesnow.scaldis.objects.Team;
import com.housesnow.scaldis.utilities.NetworkUtils;

import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sander on 7-10-2017.
 */

public class RankingFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Poule>> {

    RankingAdapter adapter;

    String teamName;
    String teamGuid;

    private static final int RANKING_LOADER_ID = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ranking, container, false);

        Bundle bundle = getArguments();

        // Team and guid strings
        teamName = bundle.getString("teamName");
        teamGuid = bundle.getString("teamGuid");

        // Find a reference to the {@link ListView} in the layout
        RecyclerView rankingRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        rankingRecyclerView.setLayoutManager(layoutManager);

        adapter = new RankingAdapter();
        rankingRecyclerView.setAdapter(adapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(RANKING_LOADER_ID, null, this);


        }
        return rootView;
    }

    @Override
    public Loader<List<Poule>> onCreateLoader(int id, Bundle args) {
        URL requestPoulesUrl = NetworkUtils.getUrlTeamDetail(teamGuid);

        return new RankingLoader(getActivity(), requestPoulesUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Poule>> loader, List<Poule> poules) {
        adapter.setRankingData(Collections.<PouleTeam>emptyList());

        if (poules != null) {
            adapter.setRankingData(poules.get(0).getTeams());
        }
    }


    @Override
    public void onLoaderReset(Loader<List<Poule>> loader) {
        adapter.setRankingData(Collections.<PouleTeam>emptyList());
    }
}
