package dhbk.android.spotifygcs.ui.showTopTracksArtist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dhbk.android.spotifygcs.domain.TopTrack;

/**
 * Created by phongdth.ky on 7/20/2016.
 * TODO: implement top track interface
 */
public class TopTrackAdapter extends RecyclerView.Adapter<TopTrackAdapter.View_Holder>{
    private final Context mContext;
    private final ArrayList<TopTrack> mTopTracks;

    public TopTrackAdapter(Context context) {
        mContext = context;
        mTopTracks = new ArrayList<>(); // create an empty list
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        public View_Holder(View itemView) {
            super(itemView);
        }
    }
}
