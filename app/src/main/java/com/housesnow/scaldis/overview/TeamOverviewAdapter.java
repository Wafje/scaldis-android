package com.housesnow.scaldis.overview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.housesnow.scaldis.R;
import com.housesnow.scaldis.objects.Team;

import java.util.List;

/**
 * Created by Sander on 1-10-2017.
 */

public class TeamOverviewAdapter extends ArrayAdapter<Team> {

    private String orgGuid;

    public TeamOverviewAdapter(Context context, List<Team> teams, String orgGuid) {
        super(context, 0, teams);
        this.orgGuid = orgGuid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /** Check if there is an existing list item view (called convertView) that we can reuse,
         otherwise, if convertView is null, then inflate a new list item layout. */
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.team_list_item, parent, false);
        }

        Team currentTeam = getItem(position);

        TextView nameView = (TextView) listItemView.findViewById(R.id.name);
        TextView categoryView = (TextView) listItemView.findViewById(R.id.category);
        TextView subcategoryView = (TextView) listItemView.findViewById(R.id.subcategory);

        nameView.setText(currentTeam.getName());
        categoryView.setText(currentTeam.getCategory());
        subcategoryView.setText(currentTeam.getSubcategory());

        return listItemView;
    }


    private String parseName(String guid) {
        guid = guid.replaceAll(orgGuid,"");
        return guid.substring(0,guid.length() - 3);
    }

    private String parseCategory(String category) {
        return category.replaceFirst("\\D\\d{2}\\s*","");
    }
}
