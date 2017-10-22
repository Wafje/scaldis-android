package be.jbs.scaldis.detail.matches;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import be.jbs.scaldis.utilities.QueryUtils;

import java.net.URL;

/**
 * Created by Sander on 5-10-2017.
 */

public class AddressLoader extends AsyncTaskLoader<String> {

    private URL requestMatchesUrl;

    public AddressLoader(Context context, URL requestMatchesUrl) {
        super(context);
        this.requestMatchesUrl = requestMatchesUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        if (requestMatchesUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list
        String address = QueryUtils.fetchAddressFromMatchGuid(requestMatchesUrl);
        return address;
    }
}
