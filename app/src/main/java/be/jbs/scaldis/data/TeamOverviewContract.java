package be.jbs.scaldis.data;

import android.provider.BaseColumns;

/**
 * Created by Sander on 5-10-2017.
 */

public class TeamOverviewContract {

    // Inner class that defines the table contents of the teamoverview table
    public static final class TeamOverviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "team_overview";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_PLAYING_CLASS = "playing_class";
        public static final String COLUMN_GUID = "guid";
    }
}
