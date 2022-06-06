package edu.sjsu.android.stockmanagerproto3;

import androidx.annotation.NonNull;

import org.json.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Utility methods to fetch and process stock data from AlphaVantage API
 */
public class StockDataUtil {
    private static final String SECRET_API_KEY;
    private static final String TIME_SERIES_DAILY = "Time Series (Daily)";
    private static final String TIME_SERIES_WEEKLY = "Weekly Adjusted Time Series";
    private static final String TIMES_SERIES_MONTHLY = "Monthly Adjusted Time Series";
    private static final String METADATA = "Meta Data";
    private static final String TICKER = "2. Symbol";
    private static final String LAST_ACCESSED = "3. Last Refreshed";
    private static final String OPEN = "1. open";
    private static final String HIGH = "2. high";
    private static final String LOW = "3. low";
    private static final String CLOSE = "4. close";
    private static String URL_V1 = "https://alpha-vantage.p.rapidapi.com/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=%s&outputsize=compact&datatype=json"; // for daily requests
    private static String URL_V2 = "https://alpha-vantage.p.rapidapi.com/query?function=TIME_SERIES_WEEKLY_ADJUSTED&symbol=%s&datatype=json"; // for weekly requests
    private static String URL_V3 = "https://alpha-vantage.p.rapidapi.com/query?symbol=%s&function=TIME_SERIES_MONTHLY_ADJUSTED&datatype=json"; // for weekly requests
    private static final String ERROR_MSG = "Error Message";
    private static boolean fetchDone = false;
    private static boolean fetchFailed = false;
    private static HashMap<String, Stock> stockData = new HashMap<>();
    private static ArrayList<String> dateKeys = new ArrayList<>();
    private static ArrayList<String> dateKeys2 = new ArrayList<>(); // same as dateKeys but reversed; used for x-axis
    private static ArrayList<String> metadata = new ArrayList<>();
    private static ArrayList<String> watchlistData = new ArrayList<>();

    /**
     * Fetch the stock data. Initially fetch Daily stock data
     * @param url
     */
    public static void fetchRawDataInit(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "alpha-vantage.p.rapidapi.com")
                .addHeader("x-rapidapi-key", KEY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        // check if ticker is valid
                        boolean valid = validateTicker(jsonObject);
                        if (valid) { ;
                            String rawData = jsonObject.getJSONObject(TIME_SERIES_DAILY).toString();
                            String meta = jsonObject.getJSONObject(METADATA).toString();
                            // process data after response
                            processData(rawData);
                            processMetaData(meta);
                            fetchDone();
                        } else {
                            fetchDone();
                            setFetchFailed();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * Fetch the stock data based on what radio button was checked from detail activity
     * @param url
     * @param timeSeriesType
     */
    public static void fetchRawData(String url, String timeSeriesType) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-rapidapi-host", "alpha-vantage.p.rapidapi.com")
                .addHeader("x-rapidapi-key", KEY)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        boolean valid = validateTicker(jsonObject);
                        if (valid) {
                            String rawData = jsonObject.getJSONObject(timeSeriesType).toString();
                            String meta = jsonObject.getJSONObject(METADATA).toString();
                            // process data after response
                            processData(rawData);
                            processMetaData(meta);
                            fetchDone();
                        } else {
                            setFetchFailed();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * Set fetchDone variable to true
     */
    public static void fetchDone() {
        fetchDone = true;
    }

    /**
     * Return fetchDone variable
     * @return
     */
    public static boolean getFetchDone() {
        return fetchDone;
    }

    /**
     * Set fetchFailed variable to true
     * Unable to get the stock data (typo ticker symbol or unsupported look up)
     */
    public static void setFetchFailed() {
        fetchFailed = true;
    }

    /**
     * Set fetchFailed variable to false
     * Use to reset the variable
     */
    public static void setFetchNotFailed() {
        fetchFailed = false;
    }

    /**
     * Set fetchDone variable to false
     * Use to reset the variable
     */
    public static void setFetchNotDone() {
        fetchDone = false;
    }

    /**
     * Return fetchFailed variable
     * @return
     */
    public static boolean getFetchFailed() {
        return fetchFailed;
    }

    /**
     * Check if the ticker symbol inputted from the user is valid
     * @param jsonObject
     * @return
     */
    public static boolean validateTicker(JSONObject jsonObject) {
        boolean valid = true;
        if (jsonObject.has(ERROR_MSG)) {
            valid = false;
        }
        return valid;
    }

    /**
     * Process the metadata information
     * @param mdata
     */
    public static void processMetaData(String mdata) {
        try {
            JSONObject meta = new JSONObject(mdata);
            ArrayList<String> temp = new ArrayList<>();
            temp.add(meta.getString(TICKER).replaceAll("\"", "")); // always first element
            temp.add(meta.getString(LAST_ACCESSED).replaceAll("\"", "")); // always second element
            metadata = temp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Process the stock data
     * @param rdata
     */
    public static void processData(String rdata) {
        String processed = prepareData(rdata);
        dateKeys = dateList(processed);
        try {
            JSONObject prices = new JSONObject(rdata);
            HashMap<String, Stock> temp = new HashMap<>();
            for (int i = 0; i < dateKeys.size(); i++) {
                temp.put(dateKeys.get(i), new Stock(
                        prices.getJSONObject(dateKeys.get(i)).getString(HIGH),
                        prices.getJSONObject(dateKeys.get(i)).getString(LOW),
                        prices.getJSONObject(dateKeys.get(i)).getString(OPEN),
                        prices.getJSONObject(dateKeys.get(i)).getString(CLOSE),
                        dateKeys.get(i)
                ));
            }
            stockData = temp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        reverseList(dateKeys);
    }

    /**
     * Prepares the data so it can be used in method processData
     * @param rawData
     * @return
     */
    public static String prepareData(String rawData) {
        String preProcess1 = removeOuterBracket(rawData);
        return removeQuotes(preProcess1);
    }

    /**
     * Removes the outer brackets of the JSONObject string
     * Method body is with assumption {, } are at the start and end of the string respectively
     * @param data
     * @return
     */
    public static String removeOuterBracket(String data) {
        return data.substring(1, data.length() - 1);
    }

    /**
     * Remove all the quotation marks
     * @param data
     * @return
     */
    public static String removeQuotes(String data) {
        return data.replaceAll("\"", "");
    }

    /**
     * Gets the list of the dates from the stock data
     * @param data
     * @return
     */
    public static ArrayList<String> dateList(String data) {
        ArrayList<String> list = new ArrayList<>();
        String[] item = data.split("\\},");
        int counter = 0;
        for (String val : item) {
            // only add the recent entries (100 max)
            if (counter >= 100) {
                break;
            }
            addDate(list, val);
            counter++;
        }
        return list;
    }

    /**
     * Helper method to add the date to the date list arraylist
     * @param list
     * @param date
     */
    public static void addDate(ArrayList<String> list, String date) {
        for (int i = 0; i < date.length(); i++) {
            if (date.substring(i, i + 1).equals(":")) {
                list.add(date.substring(0, i));
                return;
            }
        }
    }

    /**
     * Reverses the date list arraylist so that it is ordered from oldest to recent
     * @param list
     */
    public static void reverseList(ArrayList<String> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            dateKeys2.add(list.get(i));
        }
    }

    /**
     * Get the url for Daily data
     * @return
     */
    public static String getURL_V1() {
        return URL_V1;
    }

    /**
     * Get the url for Weekly data
     * @return
     */
    public static String getURL_V2() {
        return URL_V2;
    }

    /**
     * Get the url for Monthly data
     * @return
     */
    public static String getURL_V3() {
        return URL_V3;
    }

    /**
     * Return string Time Series (Daily)
     * @return
     */
    public static String getTimeSeriesDaily() {
        return TIME_SERIES_DAILY;
    }

    /**
     * Return string Weekly Adjusted Time Series
     * @return
     */
    public static String getTimeSeriesWeekly() {
        return TIME_SERIES_WEEKLY;
    }

    /**
     * Return string Monthly Adjusted Time Series
     * @return
     */
    public static String getTimesSeriesMonthly() {
        return TIMES_SERIES_MONTHLY;
    }

    /**
     * Returns the stock data as a HashMap with (Date, Stock) as key-value pair
     * @return
     */
    public static HashMap<String, Stock> getStockData() {
        return stockData;
    }

    /**
     * Returns the datelist arraylist that is reversed
     * @return
     */
    public static ArrayList<String> getDateKeys2() {
        return dateKeys2;
    }

    /**
     * Returns the arraylist of the metadata information
     * @return
     */
    public static ArrayList<String> getMetadata() {
        return metadata;
    }

    /**
     * Returns the watchlist arraylist
     * @return
     */
    public static ArrayList<String> getWatchlistData() {
        return watchlistData;
    }


    /**
     * Clear the datasets
     */
    public static void clearDataSets() {
        stockData.clear();
        dateKeys.clear();
        dateKeys2.clear();
        metadata.clear();
    }

}
