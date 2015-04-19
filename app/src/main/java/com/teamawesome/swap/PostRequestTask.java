package com.teamawesome.swap;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Ni on 2015-04-19.
 */

//onPreExecute and onPostExecute runs on UI thread, doInBackground does not
public class PostRequestTask extends AsyncTask <String, Void, String> { //no progress implemented

    Activity activity;

    public PostRequestTask(Activity activity) {
        this.activity = activity;
    }

//    private ProgressDialog progressDialog = new ProgressDialog(DebugActivity.this);
//
//    @Override
//    protected void onPreExecute() {
//        progressDialog.setMessage("Downloading your data...");
//        progressDialog.show();
//        progressDialog.setOnCancelListener(new OnCancelListener() {
//            public void onCancel(DialogInterface arg0) {
//                MyAsyncTask.this.cancel(true);
//            }
//        });
//    }

    @Override
    protected String doInBackground (String... params) {
        String message = params[0];
        String response = "";

        try {
            URL serverUri = new URL("http://69.158.14.155:8080/svop/test");
            HttpURLConnection connection = (HttpURLConnection) serverUri.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true); //set to POST
            connection.setChunkedStreamingMode(0);
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");

            try {
                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                out.write(message.getBytes());

                InputStream in = new BufferedInputStream(connection.getInputStream());
                StringWriter writer = new StringWriter();
                IOUtils.copy(in, writer, Charset.forName("utf-8"));
                response = writer.toString();
            } finally {
                connection.getInputStream().close();
                connection.getOutputStream().close();
                connection.disconnect();
            }
        } catch (IOException e) {
            //cannot connect to URL
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        //parse JSON data
        try {
                JSONObject jsonObject = new JSONObject(response);
                Toast.makeText(activity, jsonObject.toString(), Toast.LENGTH_LONG).show();

//            this.progressDialog.dismiss();
        } catch (JSONException e) {
            //could not deserialize
        }
    }
}
