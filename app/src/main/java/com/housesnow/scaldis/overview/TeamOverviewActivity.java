package com.housesnow.scaldis.overview;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.housesnow.scaldis.R;
import com.housesnow.scaldis.detail.TeamDetailActivity;
import com.housesnow.scaldis.objects.Team;
import com.housesnow.scaldis.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamOverviewActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Team>> {

    private static final String SCALDIS_GUID = "BVBL1413";

    /**
     * Constant value for the team loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int TEAMS_LOADER_ID = 1;

    /** Adapter for the list of teams */
    private TeamOverviewAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_overview);

        // Find a reference to the {@link ListView} in the layout
        ListView teamsListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        teamsListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of teams as input
        mAdapter = new TeamOverviewAdapter(this, new ArrayList<Team>(), SCALDIS_GUID);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        teamsListView.setAdapter(mAdapter);

        teamsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Team team = (Team) parent.getItemAtPosition(position);

                Intent teamDetailIntent = new Intent(TeamOverviewActivity.this, TeamDetailActivity.class);
                teamDetailIntent.putExtra("teamGuid", team.getGuid());
                teamDetailIntent.putExtra("teamName", team.getName());
                teamDetailIntent.putExtra("teamTitle", team.getCategory().replaceFirst("\\D\\d{2}\\s*",""));
                startActivity(teamDetailIntent);
            }
        });


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(TEAMS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Team>> onCreateLoader(int i, Bundle bundle) {
        URL url = NetworkUtils.getUrlOrgDetail(SCALDIS_GUID);

        return new TeamOverviewLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Team>> loader, List<Team> teams) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No teams found."
        mEmptyStateTextView.setText(R.string.no_teams);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (teams != null && !teams.isEmpty()) {
            Collections.sort(teams);
            mAdapter.addAll(teams);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Team>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
