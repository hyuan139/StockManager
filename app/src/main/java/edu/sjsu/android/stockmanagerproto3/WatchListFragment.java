package edu.sjsu.android.stockmanagerproto3;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 */
public class WatchListFragment extends Fragment {
    static WatchListAdapter adapter;
    RecyclerView recyclerView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WatchListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch_list_list, container, false);
        ArrayList<String> data = StockDataUtil.getWatchlistData();
        adapter = new WatchListAdapter(data);
        recyclerView = (RecyclerView) view;
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this::onItemClicked);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int swipeDir) {
                        int position = viewHolder.getLayoutPosition();
                        data.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                };
        ItemTouchHelper itemTouch = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouch.attachToRecyclerView(recyclerView);
        return view;
    }

    public void onItemClicked(View view, String ticker){
        goToDetail(view, ticker);
    }

    public void goToDetail(View v, String stock){
        String request = String.format(StockDataUtil.getURL_V1(), stock);
        /*Intent toDetailAct = new Intent(getContext(), DetailActivity.class);
        toDetailAct.putExtra("url", request);
        getContext().startActivity(toDetailAct);*/
        MyAsyncTask task = new MyAsyncTask(getContext());
        task.execute(request);
    }
}