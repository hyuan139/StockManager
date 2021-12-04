package edu.sjsu.android.stockmanagerproto3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import edu.sjsu.android.stockmanagerproto3.databinding.FragmentWatchListBinding;

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
        //return new ViewHolder(FragmentWatchListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        stock = mValues.get(position);
        System.out.println("Position: " + position + " Stock: " + stock);
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
        //protected final FragmentWatchListBinding binding;
        private OnItemClickListener listener;
        private TextView stock;
        private LinearLayout itemView;
        /*public ViewHolder(FragmentWatchListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }*/

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