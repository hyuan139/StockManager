package edu.sjsu.android.stockmanagerproto3;

import android.os.Bundle;
import android.util.Log;
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
        //Toast.makeText(getContext(), "Search initiated", Toast.LENGTH_LONG).show();
        // send user input data
        // fetch data
        // send data to detail fragment; direct user to detail fragment of chosen stock
        String url = String.format(StockDataUtil.getURL_V1(), userInput.getText().toString().toUpperCase());
        System.out.println("FETCHING DATA");
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "alpha-vantage.p.rapidapi.com")
                .addHeader("x-rapidapi-key", StockDataUtil.getKey())
                .build();
       client.newCall(request).enqueue(new Callback() {
           @Override
           public void onFailure(@NonNull Call call, @NonNull IOException e) {
               Toast.makeText(getContext(),"Oops, something occurred. Please make sure the ticker symbol is correct or if you have internet conenction.", Toast.LENGTH_LONG).show();
               e.printStackTrace();
           }

           @Override
           public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    String result = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String data = jsonObject.getJSONObject("Time Series (Daily)").toString();
                        System.out.println("LENGTH of data is: " + jsonObject.getJSONObject("Time Series (Daily)").length());
                        String json = jsonObject.getJSONObject("Time Series (Daily)").toString();
                        int length = json.length();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
           }
       });



        Bundle bundle = new Bundle();
        bundle.putString("rawdata", url);
        NavController con = Navigation.findNavController(v);
        con.navigate(R.id.action_homeFragment_to_detailFragment);
    }

}
