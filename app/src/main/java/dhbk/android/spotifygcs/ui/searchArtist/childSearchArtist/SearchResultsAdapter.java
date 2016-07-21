package dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.domain.Artist;
import dhbk.android.spotifygcs.ui.recyclerview.ArtistItemListener;
import dhbk.android.spotifygcs.util.ViewUtils;

/**
 * Created by huynhducthanhphong on 7/16/16.
 */
public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ArtistViewHolder> {
    private ArtistItemListener mClickListener;
    private ArrayList<Artist> mArtists;
    private Context mContext;

    public SearchResultsAdapter(Context context) {
        mContext = context;
        mArtists = new ArrayList<>(); // create an empty list
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_search_artist, parent, false);
        return new ArtistViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        Artist currentArtist = mArtists.get(position);

        if (currentArtist.getMediumImage() != null) {
            holder.setArtistImage(currentArtist.getMediumImage().getUrl());
        } else {
            holder.setPlaceholderImage();
        }

        holder.mTextViewNameArtist.setText(currentArtist.getNameArtist());
        holder.containerView.setOnClickListener(v -> mClickListener.onArtistClick(currentArtist, holder.mImageviewItemSearchArtist));
        holder.containerView.setOnTouchListener((view, motionEvent) -> mClickListener.onArtistTouch(holder.mImageviewItemSearchArtist, motionEvent));
    }

    @Override
    public int getItemCount() {
        return mArtists.isEmpty() ? 0 : mArtists.size();
    }

    // replace artists data and notify change
    public void replaceAnotherData(ArrayList<Artist> artists) {
        mArtists = artists;
        notifyDataSetChanged();
    }

    // clear the recyclerview
    public void clear() {
        mArtists.clear();
        notifyDataSetChanged();
    }

    public void setClickListenerInterface(ArtistItemListener clickListener) {
        mClickListener = clickListener;
    }


    public class ArtistViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageview_item_search_artist)
        ImageView mImageviewItemSearchArtist;
        @BindView(R.id.textview_item_search_artist_name)
        TextView mTextViewNameArtist;
        @BindView(R.id.viewgroup_container)
        View containerView;


        ArtistViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        // we have url of image, so download it by picasso and cache it, so the other time, not download it again but get in cache
        // resize image depend on width height of viewholder
        public void setArtistImage(String urlImage) {
            ViewUtils.setImagePicasso(mContext, urlImage, mImageviewItemSearchArtist);
        }

        // if not found, add a place holder for artist
        public void setPlaceholderImage() {
            mImageviewItemSearchArtist.setImageDrawable(mContext.getResources().getDrawable(R.drawable.no_artist));
        }
    }
}
