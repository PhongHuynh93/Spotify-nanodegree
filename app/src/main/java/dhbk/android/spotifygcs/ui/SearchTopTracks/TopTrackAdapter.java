package dhbk.android.spotifygcs.ui.SearchTopTracks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhbk.android.spotifygcs.BaseAdapter;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.domain.TopTrack;
import dhbk.android.spotifygcs.ui.recyclerview.TrackItemListener;

/**
 * Created by phongdth.ky on 7/20/2016.
 */
public class TopTrackAdapter extends BaseAdapter<TopTrack, TopTrackAdapter.View_Holder> {
    private final Context mContext;
    private TrackItemListener mView;

    public TopTrackAdapter(Context context) {
        mContext = context;
        setOnClickListener((topTrack, position) -> {
            //  when click one view, return a top track model
            mView.onTrackClick(topTrack, position);
        });
    }

    @Override
    public void onBindViewHolder(View_Holder holder, TopTrack item, int position) {
        holder.mTextviewNumberOfTrack.setText(Integer.toString(position));
        holder.mTextviewLengthOfSong.setText(item.getDurationOfTrack());
        holder.mTextviewNameOfArtist.setText(item.getArtistsOfTrack());
        holder.mTextviewNameOfSong.setText(item.getNameOfTrack());
    }

    @Override
    protected View_Holder makeViewHolderFromView(View v) {
        return new View_Holder(v);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_show_top_track;
    }

    @Override
    protected Context getContext() {
        return mContext;
    }

    static class View_Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.textview_number_of_track)
        TextView mTextviewNumberOfTrack;
        @BindView(R.id.textview_name_of_song)
        TextView mTextviewNameOfSong;
        @BindView(R.id.textview_name_of_artist)
        TextView mTextviewNameOfArtist;
        @BindView(R.id.textview_length_of_song)
        TextView mTextviewLengthOfSong;

        View_Holder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setClickListenerInterface(TrackItemListener clickListener) {
        mView = clickListener;
    }
}
