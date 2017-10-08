package com.housesnow.scaldis.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.housesnow.scaldis.R;
import com.housesnow.scaldis.objects.PouleTeam;

import java.util.List;

/**
 * Created by Sander on 7-10-2017.
 */

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingAdapterViewHolder> {

    private List<PouleTeam> teams;

    @Override
    public RankingAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_list_item, parent, false);

        return new RankingAdapterViewHolder(view);
    }

    public void setRankingData(List<PouleTeam> teams) {
        this.teams = teams;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RankingAdapterViewHolder holder, int position) {
        holder.rankView.setText(teams.get(position).getRank().toString());
        holder.nameView.setText(teams.get(position).getName());

        String score = teams.get(position).getNumberOfPoints() + "P";
        String games = teams.get(position).getGamesWon() + "W " + teams.get(position).getGamesDraw() + "D " + teams.get(position).getGamesLost() + "L";
        String points = teams.get(position).getPointsScored() + " - " + teams.get(position).getPointsAgainst();

        holder.scoreView.setText(score);
        holder.gamesView.setText(games);
        holder.pointsView.setText(points);
    }

    /**
     * Returns an integer code related to the type of View we want the ViewHolder to be at a given
     * position. This method is useful when we want to use different layouts for different items
     * depending on their position.
     *
     * @param position index within our RecyclerView and Cursor
     * @return the view type (Title of team)
     */
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items in the team list
     */
    @Override
    public int getItemCount() {
        if (teams == null) {
            return 0;
        }
        return teams.size();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    public class RankingAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView rankView;
        final TextView nameView;
        final TextView scoreView;
        final TextView gamesView;
        final TextView pointsView;

        public RankingAdapterViewHolder(View view) {
            super(view);

            rankView = (TextView) view.findViewById(R.id.rank);
            nameView = (TextView) view.findViewById(R.id.name);

            scoreView = (TextView) view.findViewById(R.id.score);
            gamesView = (TextView) view.findViewById(R.id.games);
            pointsView = (TextView) view.findViewById(R.id.points);
        }
    }
}