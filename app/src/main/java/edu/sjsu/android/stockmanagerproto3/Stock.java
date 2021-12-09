package edu.sjsu.android.stockmanagerproto3;

/**
 * Class to construct a Stock object
 */
public class Stock {
    private String high;
    private String open;
    private String close;
    private String low;
    private String date;

    /**
     * Creates a stock
     * @param high the high price of the stock
     * @param low the low price of the stock
     * @param open the open price of the stock
     * @param close the close price of the stock
     * @param date the date of the stock
     */
    public Stock(String high, String low, String open, String close, String date){
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.date = date;
    }

    /**
     * Return the high price
     * @return
     */
    public String getHigh() {
        return high;
    }

    /**
     * Return the low price
     * @return
     */
    public String getLow() {
        return low;
    }

    /**
     * Return the open price
     * @return
     */
    public String getOpen() {
        return open;
    }

    /**
     * Return the close price
     * @return
     */
    public String getClose() {
        return close;
    }

    /**
     * Return the date of the stock
     * @return
     */
    public String getDate() {
        return date;
    }

}
