package be.jbs.scaldis.utilities;

import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Created by Sander on 5-10-2017.
 */

public final class NetworkUtils {

    private static final String ORG_DETAIL_URL =
            "http://vblcb.wisseq.eu/VBLCB_WebService/data/OrgDetailByGuid";

    private static final String TEAM_DETAIL_URL =
            "http://vblcb.wisseq.eu/VBLCB_WebService/data/TeamDetailByGuid";

    private static final String TEAM_MATCHES_URL =
            "http://vblcb.wisseq.eu/VBLCB_WebService/data/TeamMatchesByGuid";
    
    private static final String MATCH_DETAIL_URL =
            "https://vblcb.wisseq.eu/VBLCB_WebService/data/MatchesByWedGuid";

    private static final String PARAM_ORG_GUID = "issguid";

    private static final String PARAM_TEAM_GUID = "teamguid";

    public static URL getUrlOrgDetail(String guid) {
        Uri uri = Uri.parse(ORG_DETAIL_URL).buildUpon()
                .appendQueryParameter(PARAM_ORG_GUID, guid)
                .build();

        try {
            URL url = new URL(uri.toString());
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getUrlTeamDetail(String guid) {
        String teamguid = guid.replaceAll(" ","+");

        Uri uri = Uri.parse(TEAM_DETAIL_URL).buildUpon()
                .appendQueryParameter(PARAM_TEAM_GUID, teamguid)
                .build();

        try {
            URL url = new URL(URLDecoder.decode(uri.toString(), "UTF-8"));
            return url;
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getUrlMatchesByGuid(String guid) {
        String teamguid = guid.replaceAll(" ","+");

        Uri uri = Uri.parse(TEAM_MATCHES_URL).buildUpon()
                .appendQueryParameter(PARAM_TEAM_GUID, teamguid)
                .build();

        try {
            URL url = new URL(URLDecoder.decode(uri.toString(), "UTF-8"));
            return url;
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static URL getUrlMatchDetail(String guid) {
        Uri uri = Uri.parse(MATCH_DETAIL_URL).buildUpon()
                .appendQueryParameter(PARAM_ORG_GUID, guid)
                .build();

        try {
            URL url = new URL(URLDecoder.decode(uri.toString(), "UTF-8"));
            return url;
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
