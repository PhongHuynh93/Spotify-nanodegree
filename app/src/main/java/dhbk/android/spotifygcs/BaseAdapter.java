package dhbk.android.spotifygcs;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huynhducthanhphong on 7/20/16.
 */
public abstract class BaseAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    protected List<T> itemList = new ArrayList<>();
    protected OnClickListener<T> onClickListener;

    @Override
    public void onBindViewHolder(V holder, int position) {

        final T item = itemList.get(position);

        // if click on row, return model for this row
        if (holder.itemView != null && onClickListener != null) {
            holder.itemView.setOnClickListener(v -> onClickListener.onClick(item));
        } else if (holder.itemView == null) {
            //Log.e("BaseAdapter","ViewHolder's itemView is null");
        }

        onBindViewHolder(holder, item);
    }

    public abstract void onBindViewHolder(V holder, T item);

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(getContext()).inflate(getLayout(), parent, false);
        return makeViewHolderFromView(v);
    }

    protected abstract V makeViewHolderFromView(View v);

    @LayoutRes
    protected abstract int getLayout();

    protected abstract Context getContext();

    public List<T> getItemList() {
        return itemList;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void addAll(List<T> items) {
        itemList.addAll(items);
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener<T> onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener<T> {
        void onClick(T t);
    }
}
