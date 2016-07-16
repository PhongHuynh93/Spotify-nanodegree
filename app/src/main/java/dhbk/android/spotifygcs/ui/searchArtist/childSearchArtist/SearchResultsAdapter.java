package dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhbk.android.spotifygcs.R;

/**
 * Created by huynhducthanhphong on 7/16/16.
 * // TODO: 7/16/16 extend recycler view adapter
 */
public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ArtistViewHolder> {
    public SearchResultsAdapter(Context context) {

    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_artist, parent, false);
        ArtistViewHolder holder = new ArtistViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageview_item_search_artist)
        ImageView mImageviewItemSearchArtist;

        ArtistViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
