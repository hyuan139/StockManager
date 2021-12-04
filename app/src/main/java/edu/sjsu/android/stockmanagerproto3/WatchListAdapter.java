package edu.sjsu.android.stockmanagerproto3;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import edu.sjsu.android.stockmanagerproto3.databinding.FragmentWatchListBinding;

import java.util.List;

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.ViewHolder> {

    private final List<String> mValues;

    public WatchListAdapter(List<String> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentWatchListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String stock = mValues.get(position);
        holder.binding.watchlistStock.setText(stock);
        holder.binding.watchlistStock.setOnClickListener(v ->
                Toast.makeText(v.getContext(), stock + " clicked",
                        Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected final FragmentWatchListBinding binding;

        public ViewHolder(FragmentWatchListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}