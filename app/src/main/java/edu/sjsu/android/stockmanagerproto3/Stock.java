package edu.sjsu.android.stockmanagerproto3;

public class Stock {
    private String ticker;
    private String price;

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
