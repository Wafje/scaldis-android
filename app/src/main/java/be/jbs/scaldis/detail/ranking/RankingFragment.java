package be.jbs.scaldis.detail.ranking;

import android.content.Context;
import android.support.v4.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import be.jbs.scaldis.R;
import be.jbs.scaldis.objects.Match;
import be.jbs.scaldis.objects.Poule;
import be.jbs.scaldis.objects.Team;
import be.jbs.scaldis.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sander on 7-10-2017.
 */

public class RankingFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Poule>> {

    RankingAdapter adapter;

    private TextView errorDisplay;
    private SwipeRefreshLayout swipeRefreshLayout;

    private View rootView;

    String teamName;
    String teamGuid;

    private static final int RANKING_LOADER_ID = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ranking, container, false);

        Bundle bundle = getArguments();

        // Team and guid strings
        teamName = bundle.getString("teamName");
        teamGuid = bundle.getString("teamGuid");

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                initLoader();
            }

        });

        errorDisplay = (TextView) rootView.findViewById(R.id.error_message_display);

        // Find a reference to the {@link ListView} in the layout
        RecyclerView rankingRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        rankingRecyclerView.setLayoutManager(layoutManager);

        adapter = new RankingAdapter(getContext());
        rankingRecyclerView.setAdapter(adapter);

        initLoader();

        return rootView;
    }

    private void initLoader() {
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
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            swipeRefreshLayout.setRefreshing(false);

            // Update empty state with no connection error message
            errorDisplay.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Poule>> onCreateLoader(int id, Bundle args) {
        swipeRefreshLayout.setRefreshing(true);
        URL requestPoulesUrl = NetworkUtils.getUrlTeamDetail(teamGuid);

        return new RankingLoader(getActivity(), requestPoulesUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Poule>> loader, List<Poule> poules) {
        swipeRefreshLayout.setRefreshing(false);

        adapter.setTeamData(new Team(teamName,teamGuid));

        if (poules != null) {
            errorDisplay.setVisibility(View.GONE);
            adapter.setTeamData(new Team(teamName,teamGuid, new ArrayList<Match>(), poules));
        } else {
            errorDisplay.setText("No data found.");
            errorDisplay.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onLoaderReset(Loader<List<Poule>> loader) {
        adapter.setTeamData(new Team(teamName,teamGuid));
    }
}
