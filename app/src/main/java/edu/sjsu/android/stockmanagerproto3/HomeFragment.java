package edu.sjsu.android.stockmanagerproto3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.sjsu.android.stockmanagerproto3.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    public HomeFragment() {
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
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(getLayoutInflater());
        binding.searchBtn.setOnClickListener(this::searchBtn);
        return binding.getRoot();
    }

    public void searchBtn(View v){
        Toast.makeText(getContext(), "Search initiated", Toast.LENGTH_LONG).show();
        // send user input data
        // fetch data
        // direct user to detail fragment of chosen stock
    }

}
