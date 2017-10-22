package be.jbs.scaldis.detail.matches;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import be.jbs.scaldis.R;
import be.jbs.scaldis.objects.Match;
import be.jbs.scaldis.utilities.NetworkUtils;
import be.jbs.scaldis.utilities.PreCachingLayoutManager;

import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created by Sander on 7-10-2017.
 */

public class MatchesFragment extends Fragment
        implements MatchesAdapter.MatchesAdapterOnClickHandler {

    MatchesAdapter adapter;
    LinearLayoutManager layoutManager;

    private TextView errorDisplay;
    private SwipeRefreshLayout swipeRefreshLayout;

    private View rootView;

    String teamName;
    String teamGuid;

    private static final int MATCHES_LOADER_ID = 3;
    private static final int ADDRESS_LOADER_ID = 4;

    private LoaderManager.LoaderCallbacks<List<Match>> matchLoader = new LoaderManager.LoaderCallbacks<List<Match>>() {
        @Override
        public Loader<List<Match>> onCreateLoader(int id, Bundle args) {
            swipeRefreshLayout.setRefreshing(true);
            URL requestMatchesUrl = NetworkUtils.getUrlMatchesByGuid(teamGuid);

            return new MatchesLoader(getActivity(), requestMatchesUrl);
        }

        @Override
        public void onLoadFinished(Loader<List<Match>> loader, List<Match> matches) {
            swipeRefreshLayout.setRefreshing(false);

            adapter.setMatchesData(Collections.<Match>emptyList());

            if (matches != null) {
                errorDisplay.setVisibility(View.GONE);

                Collections.sort(matches);
                adapter.setMatchesData(matches);

                int i;

                for (i = 0; i < matches.size(); i++) {
                    if (adapter.getItemViewType(i) == adapter.VIEW_TYPE_UNPLAYED) {
                        break;
                    }
                }

                if (i > 0)
                    layoutManager.scrollToPosition(i - 1);
                else
                    layoutManager.scrollToPosition(i);
            } else {
                errorDisplay.setText("No data found.");
                errorDisplay.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Match>> loader) {
            adapter.setMatchesData(Collections.<Match>emptyList());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_matches, container, false);

        Bundle bundle = getArguments();

        // Team and guid strings
        teamName = bundle.getString("teamName");
        teamGuid = bundle.getString("teamGuid");

        errorDisplay = (TextView) rootView.findViewById(R.id.error_message_display);

        // Find a reference to the {@link ListView} in the layout
        RecyclerView matchesRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);

        layoutManager = new PreCachingLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
        matchesRecyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // Get a reference to the LoaderManager, in order to interact with loaders.
                LoaderManager loaderManager = getLoaderManager();

                // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                // because this activity implements the LoaderCallbacks interface).
                loaderManager.initLoader(MATCHES_LOADER_ID, null, matchLoader);
            }

        });

        adapter = new MatchesAdapter(this);
        matchesRecyclerView.setAdapter(adapter);

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
            loaderManager.initLoader(MATCHES_LOADER_ID, null, matchLoader);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            swipeRefreshLayout.setRefreshing(false);

            // Update empty state with no connection error message
            errorDisplay.setText(R.string.no_internet_connection);
        }
    }



    @Override
    public void onClick(MatchesAdapter.MatchesAdapterClickType type, final String matchGuid, final String accomodation) {
        switch (type) {
            case MAPS:
                swipeRefreshLayout.setRefreshing(true);

                LoaderManager.LoaderCallbacks<String> addressLoader = new LoaderManager.LoaderCallbacks<String>() {
                    @Override
                    public Loader<String> onCreateLoader(int id, Bundle args) {
                        URL requestAddressUrl = NetworkUtils.getUrlMatchDetail(matchGuid);

                        return new AddressLoader(getActivity(), requestAddressUrl);
                    }

                    @Override
                    public void onLoadFinished(Loader<String> loader, String address) {
                        swipeRefreshLayout.setRefreshing(false);

                        if (address != null &&  !address.isEmpty()) {
                            String parsedAccomodation = accomodation.replaceAll(" ","+");
                            Uri mapsUri = Uri.parse("geo:0,0?q=" + parsedAccomodation + "+" + address);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapsUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        } else {
                            Toast toast = Toast.makeText(getContext(),"Not able to fetch address", Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        LoaderManager loaderManager = getLoaderManager();
                        loaderManager.destroyLoader(ADDRESS_LOADER_ID);
                    }

                    @Override
                    public void onLoaderReset(Loader<String> loader) {
                        System.console().flush();
                    }
                };

                LoaderManager loaderManager = getLoaderManager();

                // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                // because this activity implements the LoaderCallbacks interface).
                loaderManager.initLoader(ADDRESS_LOADER_ID, null, addressLoader);
                break;

            case DATE:
                break;
        }
    }
}
