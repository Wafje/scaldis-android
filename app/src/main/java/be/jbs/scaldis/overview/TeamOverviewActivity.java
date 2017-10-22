package be.jbs.scaldis.overview;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import be.jbs.scaldis.R;
import be.jbs.scaldis.detail.TeamDetailActivity;
import be.jbs.scaldis.objects.Team;
import be.jbs.scaldis.utilities.NetworkUtils;

import java.net.URL;
import java.util.Collections;
import java.util.List;

public class TeamOverviewActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Team>>, TeamOverviewAdapter.ListItemClickListener {

    private static final String SCALDIS_GUID = "BVBL1413";

    /**
     * Constant value for the team loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int TEAMS_LOADER_ID = 1;

    /** Adapter for the list of teams */
    private TeamOverviewAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView errorDisplay;

    private List<Team> teams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_overview);

        // Find a reference to the {@link ListView} in the layout
        RecyclerView overviewRecyclerView = (RecyclerView) findViewById(R.id.list);

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        overviewRecyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                initLoader();
            }

        });

        errorDisplay = (TextView) findViewById(R.id.error_message_display);

        // Create a new adapter that takes an empty list of teams as input
        adapter = new TeamOverviewAdapter(this);
        overviewRecyclerView.setAdapter(adapter);

        initLoader();

    }

    private void initLoader() {
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
            swipeRefreshLayout.setRefreshing(false);

            // Update empty state with no connection error message
            errorDisplay.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Team>> onCreateLoader(int i, Bundle bundle) {
        swipeRefreshLayout.setRefreshing(true);
        URL url = NetworkUtils.getUrlOrgDetail(SCALDIS_GUID);

        return new TeamOverviewLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<Team>> loader, List<Team> teams) {
        swipeRefreshLayout.setRefreshing(false);

        // Clear the adapter of previous earthquake data
        adapter.setTeamData(Collections.<Team>emptyList());

        this.teams = teams;

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (teams != null) {
            errorDisplay.setVisibility(View.GONE);

            Collections.sort(teams);
            adapter.setTeamData(teams);
        } else {
            errorDisplay.setText("No data found.");
            errorDisplay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Team>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.setTeamData(Collections.<Team>emptyList());
    }

    @Override
    public void onListItemClick(Team clickedTeam) {
        Intent teamDetailIntent = new Intent(TeamOverviewActivity.this, TeamDetailActivity.class);

        teamDetailIntent.putExtra("teamGuid", clickedTeam.getGuid());
        teamDetailIntent.putExtra("teamName", clickedTeam.getName());
        teamDetailIntent.putExtra("teamTitle", clickedTeam.getCategory() + " " + clickedTeam.getSubcategory());
        startActivity(teamDetailIntent);
    }
}
