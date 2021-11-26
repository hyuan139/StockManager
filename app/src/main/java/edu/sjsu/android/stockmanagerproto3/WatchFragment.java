package edu.sjsu.android.stockmanagerproto3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import edu.sjsu.android.stockmanagerproto3.databinding.FragmentWatchBinding;

public class WatchFragment extends Fragment {

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
        FragmentWatchBinding binding = FragmentWatchBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}
