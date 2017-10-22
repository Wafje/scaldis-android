package be.jbs.scaldis.utilities;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Sander on 16-1-2017.
 */

public class PreCachingLayoutManager extends LinearLayoutManager {
    private static final int DEFAULT_EXTRA_LAYOUT_SPACE = 300;
    private int mExtraLayoutSpace = -1;
    private Context mContext;

    public PreCachingLayoutManager(Context context) {
        super(context);
        mContext = context;
    }

    public PreCachingLayoutManager(Context context, int extraLayoutSpace) {
        super(context);
        mContext = context;
        mExtraLayoutSpace = extraLayoutSpace;
    }

    public PreCachingLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        mContext = context;
    }

    public void setExtraLayoutSpace(int extraLayoutSpace) {
        mExtraLayoutSpace = extraLayoutSpace;
    }

    @Override
    protected int getExtraLayoutSpace(RecyclerView.State state) {
        if (mExtraLayoutSpace > 0) {
            return mExtraLayoutSpace;
        }
        return DEFAULT_EXTRA_LAYOUT_SPACE;
    }
}
