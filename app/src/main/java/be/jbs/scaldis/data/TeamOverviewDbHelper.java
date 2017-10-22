package be.jbs.scaldis.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import be.jbs.scaldis.data.TeamOverviewContract.TeamOverviewEntry;

/**
 * Created by Sander on 5-10-2017.
 */

public class TeamOverviewDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "scaldis.db";

    private static final int DATABASE_VERSION = 1;

    public TeamOverviewDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TEAM_OVERVIEW_TABLE =
                "CREATE TABLE " + TeamOverviewEntry.TABLE_NAME + "( " +
                        TeamOverviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +
                        TeamOverviewEntry.COLUMN_NAME + "TEXT NOT NULL, "               +
                        TeamOverviewEntry.COLUMN_AGE + "INTEGER NOT NULL, "             +
                        TeamOverviewEntry.COLUMN_CATEGORY + "TEXT NOT NULL, "           +
                        TeamOverviewEntry.COLUMN_PLAYING_CLASS + "TEXT NOT NULL, "      +
                        TeamOverviewEntry.COLUMN_GUID + "TEXT NOT NULL;";

        db.execSQL(SQL_CREATE_TEAM_OVERVIEW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TeamOverviewEntry.TABLE_NAME);
        onCreate(db);
    }
}
