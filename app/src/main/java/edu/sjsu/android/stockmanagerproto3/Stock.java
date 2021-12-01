package edu.sjsu.android.stockmanagerproto3;

import com.github.mikephil.charting.charts.LineChart;

import java.util.HashMap;

public class Stock {
    //private String ticker;
    private String high;
    private String open;
    private String close;
    private String low;
    private HashMap<String, Stock> stockData;

    public Stock(String high, String low, String open, String close){
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
    }

    public Stock(HashMap<String, Stock> stockData){
        this.stockData = stockData;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getOpen() {
        return open;
    }

    public String getClose() {
        return close;
    }

    public HashMap<String, Stock> getStockData(){
        return stockData;
    }
}
