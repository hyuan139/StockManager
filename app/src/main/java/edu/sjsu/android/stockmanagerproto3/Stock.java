package edu.sjsu.android.stockmanagerproto3;

import com.github.mikephil.charting.charts.LineChart;

public class Stock {
    private String ticker;
    private String price;
    private LineChart chart;
    public Stock(String ticker, String price){
        this.ticker = ticker;
        this.price = price;
    }

    public String getTicker(){
        return this.ticker;
    }

    public String getPrice(){
        return this.price;
    }
}
