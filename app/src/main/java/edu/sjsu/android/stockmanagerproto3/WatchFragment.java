package edu.sjsu.android.stockmanagerproto3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.sjsu.android.stockmanagerproto3.databinding.FragmentWatchBinding;

public class WatchFragment extends Fragment {
    protected ArrayList<Stock> watchlist;

    public WatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //FragmentWatchBinding binding = FragmentWatchBinding.inflate(getLayoutInflater());
        //return binding.getRoot();
        generateData();
        View view = inflater.inflate(R.layout.fragment_watch, container, false);;
        return view;
    }

    public void onItemClicked(View view){

    }

    public void generateData(){
        watchlist = new ArrayList<>();
        watchlist.add(new Stock("TSLA", "$1999.56"));
        watchlist.add(new Stock("GME", "$265.50"));
    }
}
