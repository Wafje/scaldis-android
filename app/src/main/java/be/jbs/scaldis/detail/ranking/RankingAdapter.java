package be.jbs.scaldis.detail.ranking;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.jbs.scaldis.R;
import be.jbs.scaldis.objects.Poule;
import be.jbs.scaldis.objects.PouleTeam;
import be.jbs.scaldis.objects.Team;

import java.util.ArrayList;

/**
 * Created by Sander on 7-10-2017.
 */

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingAdapterViewHolder> {

    private static final int VIEW_TYPE_SMALL = 0;
    private static final int VIEW_TYPE_TITLE = 2;

    private Team team;
    private ArrayList<Integer> viewTypes = new ArrayList<>();
    private ArrayList<PouleTeam> viewTeams = new ArrayList<>();

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
            case VIEW_TYPE_TITLE:
                layoutId = R.layout.ranking_list_item_title;
                break;

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new RankingAdapterViewHolder(view);
    }

    public void setTeamData(Team team) {
        this.team = team;

        viewTypes = new ArrayList<>();
        viewTeams = new ArrayList<>();

        for (Poule poule : team.getPoules()) {
            viewTypes.add(VIEW_TYPE_TITLE);
            viewTeams.add(new PouleTeam(poule.getName(), poule.getGuid()));
            for (PouleTeam pouleTeam : poule.getTeams()) {
                viewTypes.add(VIEW_TYPE_SMALL);
                viewTeams.add(pouleTeam);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RankingAdapterViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        switch (viewType) {
            case VIEW_TYPE_SMALL:
                holder.rankView.setText(viewTeams.get(position).getRank().toString());
                holder.nameView.setText(viewTeams.get(position).getName());

                if (team.getGuid().equals(viewTeams.get(position).getGuid())) {
                    holder.view.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimaryLight, null));
                } else {
                    holder.view.setBackgroundColor(Color.WHITE);
                }

                String score = viewTeams.get(position).getNumberOfPoints() + "P";
                String games = viewTeams.get(position).getGamesWon() + "W " + viewTeams.get(position).getGamesDraw() + "D " + viewTeams.get(position).getGamesLost() + "L";
                String points = viewTeams.get(position).getPointsScored() + " - " + viewTeams.get(position).getPointsAgainst();

                holder.scoreView.setText(score);
                holder.gamesView.setText(games);
                holder.pointsView.setText(points);
                break;

            case VIEW_TYPE_TITLE:
                holder.nameView.setText(viewTeams.get(position).getName());
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
        if (viewTypes.isEmpty())
            return VIEW_TYPE_SMALL;

        return viewTypes.get(position);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items in the team list
     */
    @Override
    public int getItemCount() {
        return viewTypes.size();
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

        final View view;

        public RankingAdapterViewHolder(View view) {
            super(view);

            rankView = (TextView) view.findViewById(R.id.rank);
            nameView = (TextView) view.findViewById(R.id.name);

            scoreView = (TextView) view.findViewById(R.id.score);
            gamesView = (TextView) view.findViewById(R.id.games);
            pointsView = (TextView) view.findViewById(R.id.points);

            this.view = view;

            // view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
