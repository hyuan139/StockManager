package edu.sjsu.android.stockmanagerproto3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

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

    /**
     * Fetch the stock data based on user input
     * @param v
     */
    public void searchBtn(View v){
        if(userInput.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Please enter a ticker symbol", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = String.format(StockDataUtil.getURL_V1(), userInput.getText().toString().toUpperCase());
        MyAsyncTask task = new MyAsyncTask(getContext());
        task.execute(url);
        userInput.setText("");
    }

}
