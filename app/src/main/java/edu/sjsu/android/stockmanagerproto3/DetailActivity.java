package edu.sjsu.android.stockmanagerproto3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import edu.sjsu.android.stockmanagerproto3.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding binding;
    private CandleStickChart chart;
    private TextView stockSymbol;
    private RadioGroup theRange;
    private TextView theDate;
    private TextView open;
    private TextView close;
    private TextView high;
    private TextView low;
    private TextView dateAtPrice;
    private ArrayList<CandleEntry> yValues;
    private CandleData data;
    private RadioButton daily;
    private RadioButton weekly;
    private RadioButton monthly;
    private HashMap<String, Stock> stockData;
    private ArrayList<String> dateKeys;
    private ArrayList<String> metainfo;
    private FetchDataAsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String request = extras.getString("url");
            /*StockDataUtil.fetchRawDataInit(request);
            while (true) {
                // check if fetch done before going to detail
                if (StockDataUtil.getFetchDone()) {
                    // set back to false
                    StockDataUtil.setFetchNotDone();
                    break;
                }
            }
            MyAsyncTask task = new MyAsyncTask(this);
            task.execute(request);
        }*/
        initVars();
        // set the listeners
        setListeners();
        setTextViews();
        setUpChart();
        chart.invalidate();
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        resetData();
        super.onDestroy();
    }

    public void initVars() {
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
    }

    // Set the text views with data of their respective name
    public void setTextViews() {
        // get from meta info list
        theDate.setText(String.format(getResources().getString(R.string.date), metainfo.get(1))); // date always at position 1
        stockSymbol.setText(metainfo.get(0)); // symbol/ticker always at position 0
        // date list in order from oldest to recent
        dateAtPrice.setText(dateKeys.get(dateKeys.size() - 1));
        high.setText(String.format(getResources().getString(R.string.highPrice), stockData.get(dateKeys.get(dateKeys.size() - 1)).getHigh()));
        low.setText(String.format(getResources().getString(R.string.lowPrice), stockData.get(dateKeys.get(dateKeys.size() - 1)).getLow()));
        open.setText(String.format(getResources().getString(R.string.openPrice), stockData.get(dateKeys.get(dateKeys.size() - 1)).getOpen()));
        close.setText(String.format(getResources().getString(R.string.closePrice), stockData.get(dateKeys.get(dateKeys.size() - 1)).getClose()));
    }

    public void updateTextViews() {
        dateAtPrice.setText(dateKeys.get(dateKeys.size() - 1));
        high.setText(String.format(getResources().getString(R.string.highPrice), stockData.get(dateKeys.get(dateKeys.size() - 1)).getHigh()));
        low.setText(String.format(getResources().getString(R.string.lowPrice), stockData.get(dateKeys.get(dateKeys.size() - 1)).getLow()));
        open.setText(String.format(getResources().getString(R.string.openPrice), stockData.get(dateKeys.get(dateKeys.size() - 1)).getOpen()));
        close.setText(String.format(getResources().getString(R.string.closePrice), stockData.get(dateKeys.get(dateKeys.size() - 1)).getClose()));
    }

    public void setListeners() {
        daily.setOnClickListener(this::checked);
        weekly.setOnClickListener(this::checked);
        monthly.setOnClickListener(this::checked);
        binding.watchListAdd.setOnClickListener(this::addToWatchlist);
        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                float entryPos = e.getX();

                if (entryPos < yValues.size()) {
                    CandleEntry selectedEntry = yValues.get((int) entryPos);
                    high.setText(String.format(getResources().getString(R.string.highPrice), Float.toString(selectedEntry.getHigh())));
                    low.setText(String.format(getResources().getString(R.string.lowPrice), Float.toString(selectedEntry.getLow())));
                    open.setText(String.format(getResources().getString(R.string.openPrice), Float.toString(selectedEntry.getOpen())));
                    close.setText(String.format(getResources().getString(R.string.closePrice), Float.toString(selectedEntry.getClose())));
                    dateAtPrice.setText(stockData.get(dateKeys.get((int) entryPos)).getDate());
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public void checked(View v) {
        if (daily.isChecked()) {
            String newURL = String.format(StockDataUtil.getURL_V1(), metainfo.get(0));
            resetData();
            /*StockDataUtil.fetchRawData(newURL, StockDataUtil.getTimeSeriesDaily());
            while (true) {
                if (StockDataUtil.getFetchDone()) {
                    // set back to false
                    StockDataUtil.setFetchNotDone();
                    break;
                }
            }*/
            task = new FetchDataAsyncTask(this);
            task.execute(newURL, StockDataUtil.getTimeSeriesDaily());
            /*stockData = StockDataUtil.getStockData();
            dateKeys = StockDataUtil.getDateKeys2();
            metainfo = StockDataUtil.getMetadata();
            updateTextViews();
            setUpChart();
            chart.invalidate();*/
            return;
        } else if (weekly.isChecked()) {
            String newURL = String.format(StockDataUtil.getURL_V2(), metainfo.get(0));
            resetData();
            /*StockDataUtil.fetchRawData(newURL, StockDataUtil.getTimeSeriesWeekly());
            while (true) {
                if (StockDataUtil.getFetchDone()) {
                    // set back to false
                    StockDataUtil.setFetchNotDone();
                    break;
                }
            }*/
            task = new FetchDataAsyncTask(this);
            task.execute(newURL, StockDataUtil.getTimeSeriesWeekly());
            /*stockData = StockDataUtil.getStockData();
            dateKeys = StockDataUtil.getDateKeys2();
            metainfo = StockDataUtil.getMetadata();
            updateTextViews();
            setUpChart();
            chart.invalidate();*/
            return;
        } else if (monthly.isChecked()) {
            String newURL = String.format(StockDataUtil.getURL_V3(), metainfo.get(0));
            resetData();
            /*StockDataUtil.fetchRawData(newURL, StockDataUtil.getTimesSeriesMonthly());
            while (true) {
                if (StockDataUtil.getFetchDone()) {
                    // set back to false
                    StockDataUtil.setFetchNotDone();
                    break;
                }
            }*/
            task = new FetchDataAsyncTask(this);
            task.execute(newURL, StockDataUtil.getTimesSeriesMonthly());
            /*stockData = StockDataUtil.getStockData();
            dateKeys = StockDataUtil.getDateKeys2();
            metainfo = StockDataUtil.getMetadata();
            updateTextViews();
            setUpChart();
            chart.invalidate();*/
            return;
        }
    }

    /**
     * Configure the candlestick chart
     */
    // (possible) args: date list, stock data in HashMap<Date(String,Stock)>
    public void setUpChart() {
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


    public void generateData() {
        yValues = new ArrayList<>();
        for (int i = 0; i < stockData.size(); i++) {
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

    public void addToWatchlist(View v) {
        if (StockDataUtil.getWatchlistData().contains(metainfo.get(0))) {
            Toast.makeText(this, metainfo.get(0) + " is already in watchlist!", Toast.LENGTH_LONG).show();
            return;
        }
        StockDataUtil.getWatchlistData().add(metainfo.get(0));
        Toast.makeText(this, metainfo.get(0) + " added!", Toast.LENGTH_LONG).show();
        /*if(StockDataUtil.getWatchlistData().contains(request)){
            Toast.makeText(this,request + " is already in watchlist!", Toast.LENGTH_LONG).show();
            return;
        }
        StockDataUtil.addWatchListData(request);
        Toast.makeText(this, request + " added", Toast.LENGTH_LONG).show();*/
    }

    public void updateData(){
        stockData = StockDataUtil.getStockData();
        dateKeys = StockDataUtil.getDateKeys2();
        metainfo = StockDataUtil.getMetadata();
        updateTextViews();
        setUpChart();
        chart.invalidate();
    }
    public void resetData() {
        chart.clearValues();
        StockDataUtil.clearDataSets();
        //StockDataUtil.resetBooleans();
    }

    private class FetchDataAsyncTask extends AsyncTask<String, Void, Void>{
        private Context context;
        private ProgressDialog pd;

        public FetchDataAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            //System.out.println("In onPreExecute...");
            pd = new ProgressDialog(context);
            pd.setTitle("Loading Data");
            pd.setMessage("Please Wait...");
            pd.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    cancel(true);
                }
            });
            pd.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            //System.out.println("In doInBackground...");
            String urlVersion = strings[0];
            String type = strings[1];
            System.out.println("The Request: " + urlVersion + "Type: " + type);
            if(type.equals(StockDataUtil.getTimeSeriesDaily())){
                //request = String.format(StockDataUtil.getURL_V1(), StockDataUtil.getMetadata().get(0));
                StockDataUtil.fetchRawData(urlVersion, type);
            }
            else if(type.equals(StockDataUtil.getTimeSeriesWeekly())){
                //request = String.format(StockDataUtil.getURL_V2(), StockDataUtil.getMetadata().get(0));
                StockDataUtil.fetchRawData(urlVersion, type);
            }
            else if(type.equals(StockDataUtil.getTimesSeriesMonthly())){
                //request = String.format(StockDataUtil.getURL_V3(), StockDataUtil.getMetadata().get(0));
                StockDataUtil.fetchRawData(urlVersion, type);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            //System.out.println("In onPostExecute...");
            while(true){
                if(StockDataUtil.getFetchDone()){
                    // set back to false
                    StockDataUtil.setFetchNotDone();
                    break;
                }
            }
            updateData();
            pd.dismiss();
        }
    }
}