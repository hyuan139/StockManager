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
 * Utility methods to fetch and process stock data from API
 */
public class StockDataUtil {
    private static final String KEY = "5ce41460d7msha29ff32aead49d9p1f6cc1jsnbf8b68a43226";
    private static final String DAILY = "TIME_SERIES_DAILY_ADJUSTED"; // prob not needed, URL covers this
    private static final String WEEKLY = "TIME_SERIES_WEEKLY_ADJUSTED"; // prob not needed, URL covers this
    private static final String MONTHLY = "TIME_SERIES_MONTHLY_ADJUSTED"; // prob not needed, URL covers this
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
    private static boolean fetchDone = false;
    private static HashMap<String, Stock> stockData = new HashMap<>();
    private static ArrayList<String> dateKeys = new ArrayList<>();
    private static ArrayList<String> dateKeys2 = new ArrayList<>(); // same as dateKeys but reversed; used for x-axis
    private static ArrayList<String> metadata = new ArrayList<>();
    public static String ticker_test = "";

    // initially called when use press find button
    public static void fetchRawDataInit(String url){
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
                if(response.isSuccessful()){
                    String result = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String rawData = jsonObject.getJSONObject(TIME_SERIES_DAILY).toString();
                        String meta = jsonObject.getJSONObject(METADATA).toString();
                        // process data after response
                        processData(rawData);
                        processMetaData(meta);
                        fetchDone();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    // fetch raw data based on user input from Detail Fragment
    public static void fetchRawData(String url, String timeSeriesType){
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
                if(response.isSuccessful()){
                    String result = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String rawData = jsonObject.getJSONObject(timeSeriesType).toString();
                        String meta = jsonObject.getJSONObject(METADATA).toString();
                        // process data after response
                        processData(rawData);
                        processMetaData(meta);
                        fetchDone();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public static boolean fetchDone(){
        return fetchDone = true;
    }

    public static boolean setFetchNotDone(){
        return fetchDone = false;
    }

    public static boolean getFetchDone(){
        return fetchDone;
    }

    public static void processMetaData(String mdata){
        try {
            JSONObject meta = new JSONObject(mdata);
            ArrayList<String> temp = new ArrayList<>();
            temp.add(meta.getString(TICKER).replaceAll("\"","")); // always first element
            temp.add(meta.getString(LAST_ACCESSED).replaceAll("\"", "")); // always second element
            metadata = temp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void processData(String rdata){
        String processed = prepareData(rdata);
        dateKeys = dateList(processed);
        try {
            JSONObject prices = new JSONObject(rdata);
            HashMap<String, Stock> temp = new HashMap<>();
            for(int i = 0; i < dateKeys.size(); i++){
                // public Stock(String high, String low, String open, String close){
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

    public static String prepareData(String rawData){
        String preProcess1 = removeOuterBracket(rawData);
        return removeQuotes(preProcess1);
    }

    // Method body is with assumption {, } are at the start and end of the string respectively
    public static String removeOuterBracket(String data){
        return data.substring(1, data.length()-1);
    }

    public static String removeQuotes(String data){
        return data.replaceAll("\"", "");
    }

    public static ArrayList<String> dateList(String data){
        ArrayList<String> list = new ArrayList<>();
        String[] item = data.split("\\},");
        int counter = 0;
        for(String val: item){
            // only add the recent entries (100 max)
            if(counter >= 100){
                break;
            }
            addDate(list, val);
            counter++;
        }
        return list;
    }

    public static void addDate(ArrayList<String> list, String date){
        for(int i = 0; i < date.length(); i++){
            if(date.substring(i,i+1).equals(":")){
                list.add(date.substring(0, i));
                return;
            }
        }
    }

    public static void reverseList(ArrayList<String> list){
        for(int i = list.size()-1; i >= 0; i--){
            dateKeys2.add(list.get(i));
        }
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

    public static String getKey(){
        return KEY;
    }

    public static String getDAILY() {
        return DAILY;
    }
    public static String getMETADATA() {
        return METADATA;
    }


    public static String getMONTHLY() {
        return MONTHLY;
    }

    public static String getWEEKLY() {
        return WEEKLY;
    }

    public static String getTICKER() {
        return TICKER;
    }

    public static String getHIGH() {
        return HIGH;
    }

    public static String getOPEN() {
        return OPEN;
    }

    public static String getCLOSE() {
        return CLOSE;
    }

    public static String getLOW() {
        return LOW;
    }

    public static String getLastAccessed() {
        return LAST_ACCESSED;
    }

    public static String getTimeSeriesDaily(){
        return TIME_SERIES_DAILY;
    }

    public static String getTimeSeriesWeekly(){
        return TIME_SERIES_WEEKLY;
    }

    public static String getTimesSeriesMonthly(){
        return TIMES_SERIES_MONTHLY;
    }

    public static HashMap<String, Stock> getStockData() {
        return stockData;
    }

    public static ArrayList<String> getDateKeys(){
        return dateKeys;
    }

    public static ArrayList<String> getDateKeys2(){
        return dateKeys2;
    }

    public static ArrayList<String> getMetadata(){
        return metadata;
    }

    public static void clearDataSets(){
        stockData.clear();
        dateKeys.clear();
        dateKeys2.clear();
        metadata.clear();
    }


    public static void setTicker_test(String ticker){
        ticker_test = ticker;
    }

}
