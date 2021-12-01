package edu.sjsu.android.stockmanagerproto3;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import edu.sjsu.android.stockmanagerproto3.databinding.FragmentHomeBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HomeFragment extends Fragment {
    private EditText userInput;
    private OkHttpClient client = new OkHttpClient();

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
        String url = String.format(StockDataUtil.getURL_V1(), userInput.getText().toString().toUpperCase());
        StockDataUtil.fetchRawDataInit(url);
        while(true){
            // check if fetch done before going to detail
            if(StockDataUtil.getFetchDone()){
                break;
            }
        }
        NavController con = Navigation.findNavController(v);
        con.navigate(R.id.action_homeFragment_to_detailFragment);
    }

}
