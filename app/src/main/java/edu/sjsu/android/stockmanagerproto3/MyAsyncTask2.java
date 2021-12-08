package edu.sjsu.android.stockmanagerproto3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

// TODO : NOT NEEDED ANYMORE; Detail Actiivity has its own private async class
public class MyAsyncTask2 extends AsyncTask<String, Void, Void> {
    private Context context;
    private ProgressDialog pd;

    public MyAsyncTask2(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        System.out.println("In onPreExecute...");
        pd = new ProgressDialog(context);
        pd.setTitle("Loading Data");
        pd.setMessage("Please Wait...");
        pd.show();
    }

    @Override
    protected Void doInBackground(String... strings) {
        System.out.println("In doInBackground...");
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
        System.out.println("In onPostExecute...");
        while(true){
            if(StockDataUtil.getFetchDone()){
                // set back to false
                StockDataUtil.setFetchNotDone();
                break;
            }
        }
    }

}
