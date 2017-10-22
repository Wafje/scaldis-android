package be.jbs.scaldis.overview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.jbs.scaldis.R;
import be.jbs.scaldis.objects.Team;

import java.util.List;

/**
 * Created by Sander on 1-10-2017.
 */

public class TeamOverviewAdapter extends RecyclerView.Adapter<TeamOverviewAdapter.OverViewAdapterViewHolder> {

    private List<Team> teams;

    final private ListItemClickListener clickHandler;

    public TeamOverviewAdapter(ListItemClickListener clickHandler) {
        this.clickHandler = clickHandler;
    }

    /**
    * The interface that receives onClick messages.
    */
    public interface ListItemClickListener {
        void onListItemClick(Team clickedTeam);
    }

    @Override
    public OverViewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_overview_list_item, viewGroup, false);
        return new OverViewAdapterViewHolder(view);
    }

    public void setTeamData(List<Team> teams) {
        this.teams = teams;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(OverViewAdapterViewHolder holder, int position) {
        holder.categoryView.setText(teams.get(position).getCategory() + " " + teams.get(position).getSubcategory());
    }

    @Override
    public int getItemCount() {
        if (teams == null) {
            return 0;
        }
        return teams.size();
    }

    public class OverViewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView categoryView;

        public OverViewAdapterViewHolder(View itemView) {
            super(itemView);

            categoryView = (TextView) itemView.findViewById(R.id.category);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickHandler.onListItemClick(teams.get(getAdapterPosition()));
        }
    }

}
