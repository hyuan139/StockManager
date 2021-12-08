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
        //System.out.println("In onPreExecute...");
        pd = new ProgressDialog(context);
        pd.setTitle("Loading Data");
        pd.setMessage("Please Wait...");
        pd.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                cancel(true);
            }
        });
        pd.show();
    }

    @Override
    protected Void doInBackground(String... strings) {
        //System.out.println("In doInBackground...");
        String request = strings[0];
        //System.out.println("The Request: " + request);
        StockDataUtil.fetchRawDataInit(request);
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        super.onPostExecute(v);
        //System.out.println("In onPostExecute...");
        //System.out.println("In onPostExecute... Operation done? : " + StockDataUtil.getFetchDone());
        while(true){
            if(StockDataUtil.getFetchDone()){
                // set back to false
                //System.out.println("In lopp of onPostExecute... Operation done? : " + StockDataUtil.getFetchDone());
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
