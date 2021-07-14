package com.social.instagram;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.koushikdutta.ion.Ion;

import java.util.Date;

public class ImageActivity extends AppCompatActivity {

    String url;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_image);

        url = getIntent().getStringExtra("url");
        type = getIntent().getStringExtra("type");
        ImageView imageicon = (ImageView) findViewById(R.id.imageactivity);
        VideoView videoView = (VideoView) findViewById(R.id.videoactivity);

        if(type.equals("imagelink"))
        {
            type = ".jpg";
            videoView.setVisibility(View.INVISIBLE);
            Ion.with(imageicon).placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_foreground).load(url);
        }else
        {
            type = ".mp4";
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(Uri.parse(url));
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.start();
        }
        findViewById(R.id.downloadimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));
                request.allowScanningByMediaScanner();
                Date date = new Date();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, date.getTime()+type);
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
            }
        });

    }
}