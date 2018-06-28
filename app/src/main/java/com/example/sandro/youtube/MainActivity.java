package com.example.sandro.youtube;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends YouTubeBaseActivity {


             YouTubePlayerView view;
             YouTubePlayer.OnInitializedListener onInitializedListener;
    ArrayList<String> title_arr=new ArrayList<>();
    ArrayList<String> link_arr=new ArrayList<>();
    ArrayList<String> linkdurate=new ArrayList<>();
    ArrayList<String> durate=new ArrayList<>();
    String link_play="mFQukpkgoSI";
    String title_play="mFQukpkgoSI";
    YouTubePlayer player;
    PopupWindow popupWindow;
    boolean finito=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view=(YouTubePlayerView) findViewById(R.id.view2);
        onInitializedListener= new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setFullscreen(false);
                youTubePlayer.setShowFullscreenButton(false);
                youTubePlayer.loadVideo(link_play);
                player=youTubePlayer;
                findViewById(R.id.button2).setVisibility(View.VISIBLE);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };



        final SearchView cerca= (SearchView)findViewById(R.id.cerca);
        cerca.setFocusable(false);
        cerca.clearFocus();
        cerca.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    findViewById(R.id.iutube).setVisibility(View.INVISIBLE);
                }
                else{
                    findViewById(R.id.iutube).setVisibility(View.VISIBLE);}

            }
        });
        cerca.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                         @Override
                                         public boolean onQueryTextSubmit(String query) {
                                             RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
                                             String url2 = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&q=" + cerca.getQuery().toString().replace(" ","+") + "&type=video&key=AIzaSyB2RxuaZC619TspCmvhOq5cS7aTcjTUZMA";

// Request a string response from the provided URL.
                                             StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                                                     new Response.Listener<String>() {
                                                         @Override
                                                         public void onResponse(String response) {


                                                             JSONObject expl = null;
                                                             String videoId = null;
                                                             String title = null;
                                                             try {
                                                                 title_arr.clear();
                                                                 link_arr.clear();
                                                                 durate.clear();
                                                                 linkdurate.clear();
                                                                 JSONObject c = new JSONObject(response);
                                                                 JSONArray a = c.getJSONArray("items");
                                                                 for (int i = 0; i < a.length(); i++) {
                                                                     expl = a.getJSONObject(i);
                                                                     title = expl.getJSONObject("snippet").getString("title");
                                                                     title_arr.add(title);
                                                                     videoId = expl.getJSONObject("id").getString("videoId");
                                                                     link_arr.add(videoId);

                                                                 }

                                                                 prenditempo();

                                                                 finito = true;

                                                             } catch (JSONException e) {
                                                                 e.printStackTrace();
                                                             }

                                                         }
                                                     }, new Response.ErrorListener() {
                                                 @Override
                                                 public void onErrorResponse(VolleyError error) {

                                                 }


                                             });

// Add the request to the RequestQueue.
                                             stringRequest2.setRetryPolicy(new DefaultRetryPolicy(
                                                     30000,
                                                     DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                     DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                             queue2.add(stringRequest2);
                                             queue2.getCache().clear();




                                             return false;
                                         }


                                         @Override
                                         public boolean onQueryTextChange(String newText) {

                                             return false;
                                         }

                                     });


            }

            public String convertdate (String s){
        String finale="";
                String diviso="";
                if(s.startsWith("P1")||s.startsWith("P2")||s.startsWith("P3")){diviso=s.substring(1);}
                else{diviso=s.substring(2);}
                //Log.d("diviso",diviso);

                String[] divisoDay=diviso.split("DT");
                if(divisoDay.length==1){}
                else{finale+=divisoDay[0]+":";divisoDay[0]=divisoDay[1];}
                String[] diviso2=divisoDay[0].split("H");
                //Log.d("diviso2[0]",diviso2[0]);
                if(diviso2.length==1){ //non c'è H
                    if(divisoDay.length==1){
                        }
                    else{finale+="00:";}
                }else{
                    finale+=diviso2[0]+":";
                    diviso2[0]=diviso2[1];
                    //Log.d("diviso2[1]",diviso2[1]);
                }
                String[] diviso3=diviso2[0].split("M");
                if(diviso3.length==1){//non c'è M
                    if(!diviso3[0].endsWith("S")){finale+=diviso3[0]+":";}
                    if(diviso2.length==1 && diviso3[0].endsWith("S")){
                    finale+="00:";}
                    if(diviso2.length!=1 && diviso3[0].endsWith("S")){finale+="00:";}
                }else{
                    if(diviso3[0].length()==1 && diviso2.length!=1){
                    finale+="0"+diviso3[0]+":";}
                    else{finale+=diviso3[0]+":";}
                    diviso3[0]=diviso3[1];
                }
                String[] diviso4=diviso3[0].split("S");
                if(!diviso3[0].endsWith("S")){ //non c'è S
                   finale+="00";
                }else {
                    if (diviso4[0].length() == 1) {
                        finale += "0" + diviso4[0];
                    } else {
                        finale += diviso4[0];
                    }
                }
                return finale;
            }
public void prenditempo(){
                for(int i=0;i<link_arr.size();i++) {

                    RequestQueue queue3 = Volley.newRequestQueue(getApplicationContext());
                    String url3 = "https://www.googleapis.com/youtube/v3/videos?id=" + link_arr.get(i) + "&part=contentDetails&key=AIzaSyB2RxuaZC619TspCmvhOq5cS7aTcjTUZMA";

// Request a string response from the provided URL.

                    final int finalI = i;
                    StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {


                                    JSONObject expl = null;

                                    try {
                                        JSONObject c = new JSONObject(response);
                                        JSONArray a = c.getJSONArray("items");

                                        expl = a.getJSONObject(0);

                                        linkdurate.add(link_arr.get(finalI));
                                        durate.add(convertdate(expl.getJSONObject("contentDetails").getString("duration")));
                                        //Log.d("durate", link_arr.get(finalI) + "   " +convertdate(expl.getJSONObject("contentDetails").getString("duration")));


                                        if(finalI==link_arr.size()-1){

                                            findViewById(R.id.view2).setVisibility(View.VISIBLE);
                                            findViewById(R.id.inizio).setVisibility(View.GONE);
                                            ArrayList<String> t2=new ArrayList<>();
                                            for (int w=0;w<link_arr.size();w++){
                                                int numero=-1;
                                                cacca: for (int j=0;j<linkdurate.size();j++){
                                                    if(linkdurate.get(j)==link_arr.get(w)){numero=j;break cacca;}
                                                }
                                                if(numero!=-1){
                                                    t2.add(title_arr.get(w)+"  "+durate.get(numero));
                                                }
                                                if(numero==-1){
                                                    t2.add(title_arr.get(w));
                                                }
                                            }

                                            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(MainActivity.this,
                                                android.R.layout.simple_list_item_1,
                                                t2);
                                            ListView lista = (ListView) findViewById(R.id.lista);
                                            lista.setAdapter(adapter2);
                                            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view1, int position, long id) {
                                                    if (player != null) {
                                                        link_play = link_arr.get(position);
                                                        title_play=title_arr.get(position);
                                                        player.release();
                                                        view.initialize("AIzaSyB2RxuaZC619TspCmvhOq5cS7aTcjTUZMA", onInitializedListener);
                                                    } else {
                                                        link_play = link_arr.get(position);
                                                        title_play=title_arr.get(position);
                                                        view.initialize("AIzaSyB2RxuaZC619TspCmvhOq5cS7aTcjTUZMA", onInitializedListener);
                                                    }

                                                }
                                            });

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }


                    });

// Add the request to the RequestQueue.
                    stringRequest3.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue3.add(stringRequest3);
                    queue3.getCache().clear();
                }

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
    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    public void download(String sito,String est) {
        if(haveStoragePermission()==true) {
            if(isNetworkAvailable()==true) {
/*
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("http://fire94.altervista.org/Images/download.php");
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.allowScanningByMediaScanner();
                request.setMimeType("application/jpeg");
                request.setDestinationInExternalPublicDir("/Download", "giro.jpeg");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = downloadManager.enqueue(request);*/

                String url=sito;
                Uri uri = Uri.parse(url);
                DownloadManager.Request r = new DownloadManager.Request(uri);

                String fileName = url.substring( url.lastIndexOf('/')+ 1, url.length() );

                // This put the download in the same Download dir the browser uses
                r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title_play+est);
                r.allowScanningByMediaScanner();

                // Notify user when download is completed
                // (Seems to be available since Honeycomb only)
                r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                // Start download
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(r);


                BroadcastReceiver onComplete=new BroadcastReceiver() {
                    public void onReceive(Context ctxt, Intent intent) {

                        Toast.makeText(MainActivity.this, "Music Downloaded", Toast.LENGTH_SHORT).show();
                    }
                };
                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


                Toast.makeText(MainActivity.this, "Download Started...", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity.this, "Connessione Internet Assente", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void apri_popup(View view) {
        LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.new_popup_layout, null);
        popupWindow = new PopupWindow(
                layout,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        final WebView web = (WebView)layout.findViewById(R.id.webView1);
        web.setSelected(true);
        web.getSettings().setJavaScriptEnabled(true);
        String html="<iframe class=\"button-api-frame\" src=\"https://youtubemp3api.com/@api/button/mp3/"+link_play+"\" width=\"100%\" height=\"100%\" allowtransparency=\"true\" scrolling=\"no\" style=\"border:none\"></iframe>\n" +
                "\n" +
                "<!-- Optional script that automatically makes iframe content responsive. -->\n" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/iframe-resizer/3.5.14/iframeResizer.min.js\"></script>\n" +
                "<script>iFrameResize({}, '.button-api-frame');</script>";
        web.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url) {
                Log.d("url",url);

                download(url,".mp3");

                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }});
        web.loadData(html, "text/html", null);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    public void apri_popup_video(View view) {
        LayoutInflater layoutInflater2 = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout2 = layoutInflater2.inflate(R.layout.new_popup_layout, null);
        popupWindow = new PopupWindow(
                layout2,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        final WebView web2 = (WebView)layout2.findViewById(R.id.webView1);
        web2.setSelected(true);
        web2.getSettings().setJavaScriptEnabled(true);
        String html2="<iframe class=\"button-api-frame\" src=\"https://youtubemp3api.com/@api/button/videos/"+link_play+"\" width=\"100%\" height=\"100%\" allowtransparency=\"true\" scrolling=\"no\" style=\"border:none\"></iframe>\n" +
                "\n" +
                "<!-- Optional script that automatically makes iframe content responsive. -->\n" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/iframe-resizer/3.5.14/iframeResizer.min.js\"></script>\n" +
                "<script>iFrameResize({}, '.button-api-frame');</script>";
        web2.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url) {
                Log.d("url",url);

                if(url.startsWith("https://youtubemp3api.com/@download/"))download(url,".mp4");

                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }});
        web2.loadData(html2, "text/html", null);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Conferma")
                .setMessage("Sei sicuro di voler uscire?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            finishAffinity();
                        }
                        int pid = Process.myPid();
                        Process.killProcess(pid);
                        MainActivity.super.onBackPressed();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void close(View view) {
        popupWindow.dismiss();
    }
}
