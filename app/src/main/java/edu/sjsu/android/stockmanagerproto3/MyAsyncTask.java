package edu.sjsu.android.stockmanagerproto3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * This Async class is used to get the initial stock data
 */
public class MyAsyncTask extends AsyncTask<String, Void, Void> {
    private Context context;
    private ProgressDialog pd;

    public MyAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.setTitle("Loading Data");
        pd.setMessage("Please Wait...");
        pd.show();
    }

    @Override
    protected Void doInBackground(String... strings) {
        String request = strings[0];
        StockDataUtil.fetchRawDataInit(request);
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        while(true){
            if(StockDataUtil.getFetchDone()){
                StockDataUtil.setFetchNotDone();
                break;
            }
        }
        if(StockDataUtil.getFetchFailed()) {
            StockDataUtil.setFetchNotFailed();
            Toast.makeText(context, "Please enter a valid ticker symbol", Toast.LENGTH_SHORT).show();

        }
        else{
            Intent toDetailAct = new Intent(context, DetailActivity.class);
            context.startActivity(toDetailAct);
        }
        pd.dismiss();
    }

}
