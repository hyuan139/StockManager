package edu.sjsu.android.stockmanagerproto3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import java.net.URI;

import edu.sjsu.android.stockmanagerproto3.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private EditText userInput;

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
        userInput = binding.userInput;
        binding.searchBtn.setOnClickListener(this::searchBtn);
        return binding.getRoot();
    }

    public void searchBtn(View v){
        if(userInput.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Please enter a ticker symbol", Toast.LENGTH_LONG).show();
            return;
        }
        //Toast.makeText(getContext(), "Search initiated", Toast.LENGTH_LONG).show();
        // send user input data
        // fetch data
        // send data to detail fragment; direct user to detail fragment of chosen stock
        String url = String.format(StockDataUtil.getURL_V1(), userInput.getText().toString().toUpperCase());





        Bundle bundle = new Bundle();
        bundle.putString("rawdata", url);
        NavController con = Navigation.findNavController(v);
        con.navigate(R.id.action_homeFragment_to_detailFragment);
    }

}
