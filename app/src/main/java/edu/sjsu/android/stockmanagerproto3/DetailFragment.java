package edu.sjsu.android.stockmanagerproto3;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import edu.sjsu.android.stockmanagerproto3.databinding.FragmentDetailBinding;


public class DetailFragment extends Fragment {
    private CandleStickChart chart;
    private TextView date;
    private RadioGroup theRange;
    private ArrayList<CandleEntry> yValsCandleStick;
    private CandleData data;

    public DetailFragment() {
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
        FragmentDetailBinding binding = FragmentDetailBinding.inflate(getLayoutInflater());
        chart = binding.stockChart;
        date = binding.theDate;
        theRange = binding.range;

        setUpChart();
        chart.invalidate();
        return binding.getRoot();
    }

    /**
     * Configure the candlestick chart
     */
    public void setUpChart(){
        chart.setDoubleTapToZoomEnabled(false);
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

        Legend l = chart.getLegend();
        l.setEnabled(false);
        generateData();
        chart.setData(data);
    }

    public void generateData(){
        yValsCandleStick= new ArrayList<CandleEntry>();
        yValsCandleStick.add(new CandleEntry(0, 225.0f, 219.84f, 224.94f, 221.07f));
        yValsCandleStick.add(new CandleEntry(1, 228.35f, 222.57f, 223.52f, 226.41f));
        yValsCandleStick.add(new CandleEntry(2, 226.84f,  222.52f, 225.75f, 223.84f));
        yValsCandleStick.add(new CandleEntry(3, 222.95f, 217.27f, 222.15f, 217.88f));
        yValsCandleStick.add(new CandleEntry(4, 225.0f, 219.84f, 224.94f, 221.07f));
        yValsCandleStick.add(new CandleEntry(5, 228.35f, 222.57f, 223.52f, 226.41f));
        yValsCandleStick.add(new CandleEntry(6, 226.84f,  222.52f, 225.75f, 223.84f));
        yValsCandleStick.add(new CandleEntry(7, 222.95f, 217.27f, 222.15f, 217.88f));
        yValsCandleStick.add(new CandleEntry(8, 225.0f, 219.84f, 224.94f, 221.07f));
        yValsCandleStick.add(new CandleEntry(9, 228.35f, 222.57f, 223.52f, 226.41f));
        yValsCandleStick.add(new CandleEntry(10, 226.84f,  222.52f, 225.75f, 223.84f));
        yValsCandleStick.add(new CandleEntry(11, 222.95f, 217.27f, 222.15f, 217.88f));

        CandleDataSet set1 = new CandleDataSet(yValsCandleStick, "DataSet 1");
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
}
