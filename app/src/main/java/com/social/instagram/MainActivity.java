package com.social.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.squareup.picasso.Picasso;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Target;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

public class MainActivity extends AppCompatActivity {
    String downloadlink="";String str="";
    ArrayList<String> listimage = new ArrayList<String>();
    ArrayList<String> listvideo = new ArrayList<String>();
    ArrayList<String> urllink = new ArrayList<String>();
    ArrayList<String> videoimage = new ArrayList<String>();
    ArrayList<String> imageurllink = new ArrayList<String>();
    ArrayList<String> videourllink = new ArrayList<String>();



    String urlimage="",urlvideo="",description ="";
    VideoView videoView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        haveStoragePermission();
        AdView adView;
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        final EditText link = (EditText) findViewById(R.id.link);

        link.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                downloadlink = s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
                downloadlink = s.toString();
            }
        });

        //videoView.setVisibility(View.INVISIBLE);
        Button button = (Button) findViewById(R.id.button);
        findViewById(R.id.pastelink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  videoView.setVisibility(View.INVISIBLE);
                str ="";
                urlimage="";
                urlvideo="";
                listimage.clear();
                listvideo.clear();
                urllink.clear();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                try {
                    CharSequence textToPaste = clipboard.getPrimaryClip().getItemAt(0).getText();
                    link.setText(textToPaste.toString());
                } catch (Exception e) {

                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final TextView textView = (TextView) findViewById(R.id.text);
               listimage.clear();
               listvideo.clear();
               urllink.clear();
               videoimage.clear();
               imageurllink.clear();
               videourllink.clear();
               downloadlink = link.getText().toString();
               if(downloadlink.isEmpty());
               else
               Ion.with(getApplicationContext()).load(downloadlink).asString().setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        downloadlink = result;
                        String str1 = "",str2="";
                        for (int j=0;j<downloadlink.length();j++){
                            char ch = downloadlink.charAt(j);
                            char ch1=' ';
                            str2+=ch;
                            if(ch=='<'||ch=='>')
                            {
                                if(str2.contains("meta content"))
                                description = str2.substring(str2.indexOf('-')+1).substring(0,str2.substring(str2.indexOf('-')+1).indexOf(')')+1);
                                str2="";

                            }
                            if(j<downloadlink.length()-1)
                                 ch1 = downloadlink.charAt(j+1);
                            {
                                if (ch == '\\') {
                                    if(ch1=='u'){
                                        j+=6;
                                        str1+='&';
                                        str+='&';
                                    }
                                    else{
                                        str1+="&_";
                                        str+="&_";
                                        j++;}
                                }
                            }
                            str1 +=downloadlink.charAt(j);
                            if(downloadlink.charAt(j)=='\"'){
                                if(str1.contains("http")){
                                    if(str1.contains("mp4")){
                                        urlvideo=str1.substring(0,str1.length()-1);
                                        if(listvideo.size()==listimage.size()){
                                            listvideo.remove(listvideo.get(listvideo.size()-1));
                                            listvideo.add(urlvideo);
                                        }
                                      }



                                }str1="";
                            }
                            if(downloadlink.charAt(j)== '\\'){
                                j+=6;
                                str+='&';
                            } str +=downloadlink.charAt(j);
                            if(downloadlink.charAt(j)==' '||downloadlink.charAt(j)=='{')
                            {
                                if(str.startsWith("\"src")){






                                    str = str.substring(7);
                                    str = str.substring(0,str.indexOf('\"'));
                                    urlimage = str;
                                    String w = str.substring(0,str.indexOf('?'));
                                    w = w.substring(w.lastIndexOf('/'));

                                    int n = listimage.size();

                                    if(n>0&&listimage.get(n-1).contains(w)){
                                        listimage.remove(listimage.get(listimage.size()-1));
                                        listimage.add(urlimage);
                                        listvideo.remove(listvideo.get(listvideo.size()-1));
                                        listvideo.add(urlimage);

                                    }
                                    else{
                                    listimage.add(str);
                                        listvideo.add(urlimage);
                                    }
          /*                          ImageView imageView = (ImageView) findViewById(R.id.image);
                                    Ion.with(imageView)
                                            .placeholder(R.drawable.ic_launcher_background)
                                            .error(R.drawable.ic_launcher_foreground)
                                            .load(urlimage);
            */                    }
                                str ="";
                            }
                        }



                        ListView listView = (ListView) findViewById(R.id.listview);
                        Adapter adapter = new Adapter(MainActivity.this,listimage,listvideo);
                        listView.setAdapter(adapter);


                        for(int j=0;j<listvideo.size();j++){
                            Log.i("info",listvideo.get(j));
                        }
                        /*          if(urlvideo.contains("mp4")){
                            videoView.setVisibility(View.VISIBLE);
                            videoView.setVideoURI(Uri.parse(urlvideo));
                            MediaController mediaController = new MediaController(MainActivity.this);
                            mediaController.setAnchorView(videoView);
                            videoView.setMediaController(mediaController);
                            videoView.start();

                        }
                */    }

                });

//               findViewById(R.id.downloadbutton).setOnClickListener(new View.OnClickListener() {
//                   @Override
//                   public void onClick(View v) {
//                       if(urlvideo.contains("mp4")){
//                           downloadvideo(urlvideo);
//                           //downloadimage(urlvideo,"jpg");
//                       }
//                       else{
//                           Log.i("info","info");
//                           downloadimage(urlimage,"jpg");
//}
//                   }
//               });
//

            }
        });




    }


    public void downloadvideo(final String url){
      DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        request.allowScanningByMediaScanner();
        Date date = new Date();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, date.getTime()+"");
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);
    }
    public void downloadimage(final String url, final String type)
    {
        haveStoragePermission();
                String filename = "filename";
                String downloadUrlOfImage = url;
                File direct =
                        new File(Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                .getAbsolutePath() + "/" + "instagramapp" + "/");


                if (!direct.exists()) {
                    direct.mkdir();
                    Log.d("info", "dir created for first time");
                }

                DownloadManager dm = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadUri = Uri.parse(downloadUrlOfImage);
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false)
                        .setTitle(filename)
                        .setMimeType("image/")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                                File.separator + "instagramapp" + File.separator + filename);

                dm.enqueue(request);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)        ;
    }
    public  boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                return true;
            } else {

                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            return true;
        }
    }

    class Adapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> linkimage,linkvideo;
        Adapter (Context c, ArrayList<String> linkimage,ArrayList<String> linkvideo){


            super(c,R.layout.menu,linkimage);
            this.context=c;
            this.linkimage = linkimage;
            this.linkvideo = linkvideo;

        }
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.menu,parent,false);
            ImageView imageicon = (ImageView) row.findViewById(R.id.imageicon);
            TextView texticon = (TextView) row.findViewById(R.id.type);
            TextView textView = (TextView) row.findViewById(R.id.texticon);

            Ion.with(imageicon)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .load(linkimage.get(position));
            textView.setText(description);
            if(linkvideo.get(position).contains("mp4"))
            {
                texticon.setText("  Video Link  ");
               row.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                       Intent intent = new Intent(MainActivity.this,ImageActivity.class);
                       intent.putExtra("url",linkvideo.get(position));
                       intent.putExtra("type","videolink");
                       startActivity(intent);

                   }
               });


            }
            else
                {
                Ion.with(imageicon)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .load(linkvideo.get(position));

                texticon.setText("  Image Link  ");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,ImageActivity.class);
                        intent.putExtra("url",linkvideo.get(position));
                        intent.putExtra("type","imagelink");
                        startActivity(intent);
                    }
                });

            }
            row.findViewById(R.id.downloadlist).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String type ="";
                    if(linkvideo.get(position).contains("mp4"))
                        type = ".mp4";
                    else
                        type = ".jpg";
                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(linkvideo.get(position)));
                    request.allowScanningByMediaScanner();
                    Date date = new Date();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, date.getTime()+type);
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                }
            });

            return row;
        }
    }


}


