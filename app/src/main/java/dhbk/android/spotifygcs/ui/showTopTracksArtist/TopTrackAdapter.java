package dhbk.android.spotifygcs.ui.showTopTracksArtist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhbk.android.spotifygcs.BaseAdapter;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.domain.TopTrack;

/**
 * Created by phongdth.ky on 7/20/2016.
 * TODO: implement top track interface
 */
public class TopTrackAdapter extends BaseAdapter<TopTrack, TopTrackAdapter.View_Holder> {
    private final Context mContext;

    public TopTrackAdapter(Context context) {
        mContext = context;
        setOnClickListener(topTrack -> {
            // when click one view, return a top track
        });
    }

    @Override
    public void onBindViewHolder(View_Holder holder, TopTrack item) {
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

//
//    public class View_Holder extends RecyclerView.ViewHolder {
//        public View_Holder(View itemView) {
//            super(itemView);
//        }
//    }

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
}
