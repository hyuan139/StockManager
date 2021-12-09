package edu.sjsu.android.stockmanagerproto3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.ViewHolder> {

    private final List<String> mValues;
    private String stock;
    private OnItemClickListener listener;

    public WatchListAdapter(List<String> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_watch_list, parent, false);
        return new ViewHolder(view, this.listener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        stock = mValues.get(position);
        holder.stock.setText(stock);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private OnItemClickListener listener;
        private TextView stock;
        private LinearLayout itemView;

        public ViewHolder(View view, OnItemClickListener listener) {
            super(view);
            this.stock = view.findViewById(R.id.watchlistStock);
            this.itemView = view.findViewById(R.id.itemView);
            this.listener = listener;
            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getBindingAdapterPosition();
            String ticker = mValues.get(position);
            this.listener.onItemClicked(view, ticker);
        }
    }

    public interface OnItemClickListener{
        void onItemClicked(View view, String ticker);
    }

}