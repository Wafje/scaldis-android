package com.housesnow.scaldis.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.housesnow.scaldis.R;
import com.housesnow.scaldis.objects.Match;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Sander on 7-10-2017.
 */

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchesAdapterViewHolder> {

    public static final int VIEW_TYPE_PLAYED = 0;
    public static final int VIEW_TYPE_UNPLAYED = 1;

    private final MatchesAdapterOnClickHandler clickHandler;

    private List<Match> matches;

    public MatchesAdapter(MatchesAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public MatchesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutId;

        switch (viewType)  {
            case VIEW_TYPE_PLAYED:
                layoutId = R.layout.matches_list_item;
                break;
            case VIEW_TYPE_UNPLAYED:
                layoutId = R.layout.matches_list_item_unplayed;
                break;

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new MatchesAdapterViewHolder(view);
    }

    public void setMatchesData(List<Match> matches) {
        this.matches = matches;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MatchesAdapterViewHolder holder, final int position) {

        int viewType = getItemViewType(position);

        holder.homeTeam.setText(matches.get(position).getHomeTeam().getName());
        holder.awayTeam.setText(matches.get(position).getAwayTeam().getName());

        switch (viewType) {
            case VIEW_TYPE_PLAYED:
                holder.homeScore.setText(matches.get(position).getHomeScore().toString());
                holder.awayScore.setText(matches.get(position).getAwayScore().toString());
                break;
            case VIEW_TYPE_UNPLAYED:
                Date date = matches.get(position).getDatetime();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy\nkk:mm");
                holder.date.setText(sdf.format(date));

                holder.mapsIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickHandler.onClick(MatchesAdapterClickType.MAPS, matches.get(position).getGuid());
                    }
                });

                holder.dateIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickHandler.onClick(MatchesAdapterClickType.DATE, "");
                    }
                });
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
        Date date = matches.get(position).getDatetime();
        Date now = new Date();

        if (date.after(now))
            return VIEW_TYPE_UNPLAYED;
        else
            return VIEW_TYPE_PLAYED;
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items in the team list
     */
    @Override
    public int getItemCount() {
        if (matches == null) {
            return 0;
        }
        return matches.size();
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    public class MatchesAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView homeTeam;
        final TextView awayTeam;

        final TextView homeScore;
        final TextView awayScore;
        final TextView date;

        final View dateIcon;
        final View mapsIcon;

        final View divider;

        public MatchesAdapterViewHolder(View view) {
            super(view);

            homeTeam = (TextView) view.findViewById(R.id.home_team);
            awayTeam = (TextView) view.findViewById(R.id.away_team);

            homeScore = (TextView) view.findViewById(R.id.home_score);
            awayScore = (TextView) view.findViewById(R.id.away_score);
            date = (TextView) view.findViewById(R.id.date);

            divider = view.findViewById(R.id.divider_score);

            dateIcon = view.findViewById(R.id.date_icon);
            mapsIcon = view.findViewById(R.id.maps_icon);
        }
    }

    public interface MatchesAdapterOnClickHandler {
        void onClick(MatchesAdapterClickType type, String data);
    }

    public enum MatchesAdapterClickType {
        DATE,
        MAPS
    }
}