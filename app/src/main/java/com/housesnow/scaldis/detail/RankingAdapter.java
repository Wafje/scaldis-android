package com.housesnow.scaldis.detail;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.housesnow.scaldis.R;
import com.housesnow.scaldis.objects.PouleTeam;
import com.housesnow.scaldis.objects.Team;

import java.util.List;

/**
 * Created by Sander on 7-10-2017.
 */

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingAdapterViewHolder> {

    private static final int VIEW_TYPE_SMALL = 0;
    private static final int VIEW_TYPE_LARGE = 1;
    private static final int VIEW_TYPE_TITLE = 2;

    private Team team;
    private int selectedPosition = -1;

    private Context context;

    public RankingAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RankingAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;

        switch (viewType)  {
            case VIEW_TYPE_SMALL:
                layoutId = R.layout.ranking_list_item;
                break;
            case VIEW_TYPE_LARGE:
                layoutId = R.layout.ranking_list_item_large;
                break;
            case VIEW_TYPE_TITLE:
                layoutId = R.layout.ranking_list_item;
                break;

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new RankingAdapterViewHolder(view);
    }

    public void setTeamData(Team team) {
        this.team = team;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RankingAdapterViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        holder.rankView.setText(team.getPoules().get(0).getTeams().get(position).getRank().toString());
        holder.nameView.setText(team.getPoules().get(0).getTeams().get(position).getName());

        if (team.getGuid().equals(team.getPoules().get(0).getTeams().get(position).getGuid())) {
            holder.view.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimaryLight, null));
        }


        switch (viewType) {
            case VIEW_TYPE_SMALL:
                String score = team.getPoules().get(0).getTeams().get(position).getNumberOfPoints() + "P";
                String games = team.getPoules().get(0).getTeams().get(position).getGamesWon() + "W " + team.getPoules().get(0).getTeams().get(position).getGamesDraw() + "D " + team.getPoules().get(0).getTeams().get(position).getGamesLost() + "L";
                String points = team.getPoules().get(0).getTeams().get(position).getPointsScored() + " - " + team.getPoules().get(0).getTeams().get(position).getPointsAgainst();

                holder.scoreView.setText(score);
                holder.gamesView.setText(games);
                holder.pointsView.setText(points);
                break;
            case VIEW_TYPE_LARGE:
                holder.scoreView.setText(team.getPoules().get(0).getTeams().get(position).getNumberOfPoints().toString());
                holder.gamesView.setText(team.getPoules().get(0).getTeams().get(position).getNumberOfGames().toString());
                holder.winsView.setText(team.getPoules().get(0).getTeams().get(position).getGamesWon().toString());
                holder.drawsView.setText(team.getPoules().get(0).getTeams().get(position).getGamesDraw().toString());
                holder.lossesView.setText(team.getPoules().get(0).getTeams().get(position).getGamesLost().toString());
                holder.scoredView.setText(team.getPoules().get(0).getTeams().get(position).getPointsScored().toString());
                holder.againstView.setText(team.getPoules().get(0).getTeams().get(position).getPointsAgainst().toString());
                break;
        }
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
        if (position == selectedPosition) {
            return VIEW_TYPE_LARGE;
        } else {
            return VIEW_TYPE_SMALL;
        }
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items in the team list
     */
    @Override
    public int getItemCount() {
        if (team == null) {
            return 0;
        }
        return team.getPoules().get(0).getTeams().size();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    public class RankingAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView rankView;
        final TextView nameView;

        final TextView pointsView;

        final TextView scoreView;
        final TextView gamesView;
        final TextView winsView;
        final TextView drawsView;
        final TextView lossesView;
        final TextView scoredView;
        final TextView againstView;

        final View view;

        public RankingAdapterViewHolder(View view) {
            super(view);

            rankView = (TextView) view.findViewById(R.id.rank);
            nameView = (TextView) view.findViewById(R.id.name);

            scoreView = (TextView) view.findViewById(R.id.score);
            gamesView = (TextView) view.findViewById(R.id.games);
            pointsView = (TextView) view.findViewById(R.id.points);

            winsView = (TextView) view.findViewById(R.id.wins);
            drawsView = (TextView) view.findViewById(R.id.draws);
            lossesView = (TextView) view.findViewById(R.id.losses);
            scoredView = (TextView) view.findViewById(R.id.scored);
            againstView = (TextView) view.findViewById(R.id.against);

            this.view = view;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();

            if (selectedPosition == clickedPosition) {
                selectedPosition = -1;
            } else {
                selectedPosition = clickedPosition;
            }

            notifyItemChanged(selectedPosition);
        }
    }
}
