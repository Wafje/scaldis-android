package com.housesnow.scaldis.detail;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.housesnow.scaldis.R;
import com.housesnow.scaldis.objects.Match;
import com.housesnow.scaldis.utilities.NetworkUtils;

import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sander on 7-10-2017.
 */

public class MatchesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Match>>, MatchesAdapter.MatchesAdapterDateOnClickHandler {

    MatchesAdapter adapter;
    LinearLayoutManager layoutManager;

    String teamName;
    String teamGuid;

    private static final int MATCHES_LOADER_ID = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_matches, container, false);

        Bundle bundle = getArguments();

        // Team and guid strings
        teamName = bundle.getString("teamName");
        teamGuid = bundle.getString("teamGuid");

        // Find a reference to the {@link ListView} in the layout
        RecyclerView matchesRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        matchesRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(matchesRecyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.category_divider, null));
        matchesRecyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new MatchesAdapter(this);
        matchesRecyclerView.setAdapter(adapter);

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
            loaderManager.initLoader(MATCHES_LOADER_ID, null, this);


        }
        return rootView;
    }

    @Override
    public Loader<List<Match>> onCreateLoader(int id, Bundle args) {
        URL requestMatchesUrl = NetworkUtils.getUrlMatchDetail(teamGuid);

        return new MatchesLoader(getActivity(), requestMatchesUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Match>> loader, List<Match> matches) {
        adapter.setMatchesData(Collections.<Match>emptyList());

        if (matches != null) {
            Collections.sort(matches);
            adapter.setMatchesData(matches);

            int i;

            for (i = 0; i < matches.size(); i++) {
                if (adapter.getItemViewType(i) == adapter.VIEW_TYPE_UNPLAYED) {
                    break;
                }
            }

            layoutManager.scrollToPosition(i);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Match>> loader) {
        adapter.setMatchesData(Collections.<Match>emptyList());
    }

    @Override
    public void onClick(String text) {

    }
}
