package edu.sjsu.android.stockmanagerproto3;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;

import edu.sjsu.android.stockmanagerproto3.databinding.FragmentDetailBinding;


public class DetailFragment extends Fragment {
    private CandleStickChart chart;
    private static TextView stockSymbol;
    private TextView theDate;
    private TextView open;
    private TextView close;
    private TextView high;
    private TextView low;
    private TextView dateAtPrice;
    private RadioGroup theRange;
    private ArrayList<CandleEntry> yValues;
    private CandleData data;
    private int selectedOption = 0;
    private FragmentDetailBinding binding;
    private RadioButton daily;
    private RadioButton weekly;
    private RadioButton monthly;
    private String rawData;
    private HashMap<String, Stock> stockData;
    private ArrayList<String> dateKeys;
    private ArrayList<String> metainfo;
    private TextView requestURL;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(getLayoutInflater());
        // initialize the private variables
        initVars();
        //initTextViews();
        // set the listeners
        setListeners();
        //printHashMapStock();
        //setUpChart();
        chart.invalidate();
        return binding.getRoot();
    }

    public void initVars(){
        chart = binding.stockChart;
        theDate = binding.theDate;
        theRange = binding.range;
        open = binding.stockOpen;
        close = binding.stockClose;
        high = binding.stockHigh;
        low = binding.stockLow;
        daily = binding.daily;
        weekly = binding.weekly;
        monthly = binding.monthly;
        stockSymbol = binding.stockSymbol;
        dateAtPrice = binding.dateAtPrice;
        stockData = StockDataUtil.getStockData();
        dateKeys = StockDataUtil.getDateKeys2();
        metainfo = StockDataUtil.getMetadata();
        requestURL = binding.requestURL;
    }

    // Set the text views with data of their respective name
    public void initTextViews(){
        // get from meta info list
        theDate.setText(String.format(getResources().getString(R.string.date), metainfo.get(1))); // date always at position 1
        stockSymbol.setText(metainfo.get(0)); // symbol/ticker always at position 0
        // date list in order from oldest to recent
        dateAtPrice.setText(dateKeys.get(dateKeys.size()-1));
        high.setText(String.format(getResources().getString(R.string.highPrice), stockData.get(dateKeys.get(dateKeys.size()-1)).getHigh()));
        low.setText(String.format(getResources().getString(R.string.lowPrice), stockData.get(dateKeys.get(dateKeys.size()-1)).getLow()));
        open.setText(String.format(getResources().getString(R.string.openPrice), stockData.get(dateKeys.get(dateKeys.size()-1)).getOpen()));
        close.setText(String.format(getResources().getString(R.string.closePrice), stockData.get(dateKeys.get(dateKeys.size()-1)).getClose()));
        requestURL.setText(StockDataUtil.getTicker_test());
    }

    public void setListeners(){
        daily.setOnClickListener(this::checked);
        weekly.setOnClickListener(this::checked);
        monthly.setOnClickListener(this::checked);
        binding.watchListAdd.setOnClickListener(this::addToWatchlist);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float entryPos = e.getX();
                //Toast.makeText(getContext(), "X value: " + entryPos, Toast.LENGTH_SHORT).show();

                if(entryPos < yValues.size()){
                    CandleEntry selectedEntry = yValues.get((int)entryPos);
                    high.setText(String.format(getResources().getString(R.string.highPrice), Float.toString(selectedEntry.getHigh())));
                    low.setText(String.format(getResources().getString(R.string.lowPrice),Float.toString(selectedEntry.getLow())));
                    open.setText(String.format(getResources().getString(R.string.openPrice),Float.toString(selectedEntry.getOpen())));
                    close.setText(String.format(getResources().getString(R.string.closePrice),Float.toString(selectedEntry.getClose())));
                    dateAtPrice.setText(stockData.get(dateKeys.get((int)entryPos)).getDate());
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }
    public void checked(View v){
        if(daily.isChecked()){
            Toast.makeText(getContext(), "one day radio button clicked", Toast.LENGTH_SHORT).show();
            // TODO: ACTUAL ACTION -- redraw graph with values based on checked value
            resetData();
            chart.invalidate();
            return;
        }
        else if(weekly.isChecked()){
            Toast.makeText(getContext(), "five days radio button clicked", Toast.LENGTH_SHORT).show();
            // TODO: ACTUAL ACTION -- redraw graph with values based on checked value
            resetData();
            // pass ticker, the range
            //StockDataUtil.fetchRawData();
            //stockData = StockDataUtil.getStockData();
            //dateKeys = StockDataUtil.getDateKeys2();
            //metainfo = StockDataUtil.getMetadata();
            //setUpChart();
            chart.invalidate();
            return;
        }
        else if(monthly.isChecked()){
            Toast.makeText(getContext(), "three months radio button clicked", Toast.LENGTH_SHORT).show();
            // TODO: ACTUAL ACTION -- redraw graph with values based on checked value
            resetData();
            //setUpChart();
            chart.invalidate();
            return;
        }
    }
    /**
     * Configure the candlestick chart
     */
    // (possible) args: date list, stock data in HashMap<Date(String,Stock)>
    public void setUpChart(){
        chart.setDoubleTapToZoomEnabled(false);
        chart.setPinchZoom(false); // for now
        chart.setDrawBorders(true);
        chart.setBorderColor(getResources().getColor(R.color.teal_700));
        chart.setBackgroundColor(getResources().getColor(R.color.black));
        Description desc = new Description();
        desc.setEnabled(false);
        chart.setDescription(desc);
        YAxis yAxis = chart.getAxisLeft();
        YAxis ryAxis = chart.getAxisRight();
        yAxis.setDrawGridLines(true);
        ryAxis.setDrawGridLines(false);
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false); // disable x axis grid lines
        xAxis.setDrawLabels(true);

        ryAxis.setTextColor(getResources().getColor(R.color.white));
        xAxis.setTextColor(getResources().getColor(R.color.white));
        yAxis.setDrawLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawLabels(false);
        Legend l = chart.getLegend();
        l.setEnabled(false);
        generateData();
        chart.setData(data);
    }


    public void generateData(){
        yValues = new ArrayList<>();
        for(int i = 0; i < stockData.size(); i++){
            // CandleEntry(x, high, low, open, close);
            yValues.add(new CandleEntry(i, Float.parseFloat(stockData.get(dateKeys.get(i)).getHigh())
                    , Float.parseFloat(stockData.get(dateKeys.get(i)).getLow())
                    , Float.parseFloat(stockData.get(dateKeys.get(i)).getOpen())
                    , Float.parseFloat(stockData.get(dateKeys.get(i)).getClose())));
        }

        CandleDataSet set1 = new CandleDataSet(yValues, "DataSet 1");
        set1.setColor(Color.rgb(80, 80, 80));
        set1.setShadowColor(getResources().getColor(R.color.teal_200));
        set1.setShadowWidth(0.8f);
        set1.setDecreasingColor(getResources().getColor(R.color.red));
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(getResources().getColor(R.color.teal_700));
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(Color.LTGRAY);
        set1.setDrawValues(false);

        data = new CandleData(set1);
    }

    public void addToWatchlist(View v){
        Toast.makeText(getContext(), "Stock added", Toast.LENGTH_LONG).show();
    }

    public void resetData(){
        chart.clearValues();
    }

    public static void printHashMapStock(){
        HashMap<String, Stock> stockData = StockDataUtil.getStockData();
        for(String key: stockData.keySet()){
            System.out.println(key + " => " + "HIGH: " + stockData.get(key).getHigh()  + " LOW: " + stockData.get(key).getLow()
                    + " OPEN: " + stockData.get(key).getOpen() + " CLOSE: " + stockData.get(key).getClose());
        }
    }
}
