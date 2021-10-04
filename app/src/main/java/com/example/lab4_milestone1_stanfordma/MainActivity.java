package com.example.lab4_milestone1_stanfordma;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button startButton;
    private TextView downloadProg;
    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        downloadProg = findViewById(R.id.textView);
    }

    public void mockFileDownloader(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText(R.string.download);
                downloadProg.setText("Download Progress: 0%");
            }
        });

        for (int downloadProgress = 0; downloadProgress <= 100; downloadProgress = downloadProgress + 10){
            if(stopThread) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setText(R.string.start);
                        downloadProg.setText("");
                    }
                });
                return;
            }

            Log.d(TAG, "Download Progress: " + downloadProgress + "%");
            try{
                Thread.sleep(1000);
                int finalDownloadProgress = downloadProgress;
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        downloadProg.setText("Download Progress: " + finalDownloadProgress + "%");
                    }
                });
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText(R.string.start);
                downloadProg.setText("");
            }
        });
    }

    public class ExampleRunnable implements Runnable{

        @Override
        public void run(){
            mockFileDownloader();
        }
    }


    public void startDownload(View view){
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view){
        stopThread = true;
    }

}