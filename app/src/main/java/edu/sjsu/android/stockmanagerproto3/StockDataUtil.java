package edu.sjsu.android.stockmanagerproto3;

/**
 * Utility methods to fetch and process stock data from API
 */
public class StockDataUtil {
    private static final String KEY = "";
    private static final String DAILY = "TIME_SERIES_DAILY_ADJUSTED";
    private static final String WEEKLY = "TIME_SERIES_WEEKLY_ADJUSTED";
    private static final String MONTHLY = "TIME_SERIES_MONTHLY_ADJUSTED";
    private static String URL_V1 = "https://alpha-vantage.p.rapidapi.com/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=%s&outputsize=compact&datatype=json"; // for daily requests
    private static String URL_V2 = "https://alpha-vantage.p.rapidapi.com/query?function=TIME_SERIES_WEEKLY_ADJUSTED&symbol=%s&datatype=json"; // for weekly requests
    private static String URL_V3 = "https://alpha-vantage.p.rapidapi.com/query?symbol=%s&function=TIME_SERIES_MONTHLY_ADJUSTED&datatype=json"; // for weekly requests
    private static String SYMBOL = "";

    // initially called when use press find button
    public static String fetchRawDataInit(){

        return "";
    }

    // fetch raw data based on user input
    public static String fetchRawData(){

        return "";
    }

    public static String getURL_V1() {
        return URL_V1;
    }

    public static String getURL_V2() {
        return URL_V2;
    }

    public static String getURL_V3(){
        return URL_V3;
    }
}
