package com.teamawesome.geese.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.amazonaws.auth.BasicAWSCredentials;

import java.io.File;
import java.io.FileOutputStream;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.teamawesome.geese.R;
import com.teamawesome.geese.adapter.FlockAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * Created by LocalEvan on 2016-02-25.
 */
public class CreateFlockFragment extends GeeseFragment {
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_flock, container, false);

        listView = (ListView) view.findViewById(R.id.create_flock_listview);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void update() {
        Log.i("CreateFlockFragment", "update");
    }

    public void createFlock() {
        // TODO: Make this actually upload an image
        Context context = getContext();
        String filename = "localfile";
        String string = "Hello world 5!";
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File toUpload = new File(context.getApplicationContext().getFilesDir(), "localfile");
        new UploadToS3().execute(toUpload);
    }

    private class UploadToS3 extends AsyncTask<File, Integer, Long> {
        protected Long doInBackground(File... files) {
            // TODO: This shouldn't actually be here.
            // Configure AWS S3 Access
            // Create an S3 client

            String accessKey, secretKey;
            BasicAWSCredentials bac = new BasicAWSCredentials("AKIAI67CPRBHXAGTO33A",
                    "wxETPVLbptOst5dn4C9MBHnrJuc5vb+scnOE7fBd");
            AmazonS3 s3 = new AmazonS3Client(bac);

            // Set the region of your S3 bucket
            s3.setRegion(Region.getRegion(Regions.US_EAST_1));

            String fname = "uploaded";
            fname += (int)(Math.random()*100000);
            fname += ".txt";

            TransferUtility transferUtility = new TransferUtility(s3, getContext().getApplicationContext());
            TransferObserver observer = transferUtility.upload(
                    "geese-images",     /* The bucket to upload to */
                    fname,      /* The key for the uploaded object */
                    files[0]        /* The file where the data to upload exists */
            );

            long result = 0;

            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //showDialog("Uploaded " + result + " bytes");
        }
    }
}
