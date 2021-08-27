package com.example.youtubeapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.youtubeapi.Adapter.PageOneAdapter;
import com.example.youtubeapi.Model.PageOneModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;
    RecyclerView recyclerView;
    ArrayList<PageOneModel> list;
    ArrayList<PageOneModel> sendlist;
    SearchView searchView;
    PageOneAdapter adapter;
    int count=0;
    int call_count=0;
    ImageButton btn_prev,btn_next;
    boolean load_page=true;
    boolean second_load=false;
    String pagetoken;
    LinearLayout dotsLayout;
    TextView[] dots;
    String f_querey="cricket";
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        sendlist=new ArrayList<>();
        list=new ArrayList<>();
        recyclerView=findViewById(R.id.recycler);
        btn_prev=findViewById(R.id.pre_btn);
        dotsLayout=findViewById(R.id.dots_container);
        btn_next=findViewById(R.id.next_btn);
        progressBar=findViewById(R.id.prog);
        constraintLayout=findViewById(R.id.constraintLayout);
        constraintLayout.setVisibility(View.GONE);

        adapter=new PageOneAdapter(sendlist,MainActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        volleyCall(f_querey);

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count>0){
                    count--;
                    if (count<=0){
                        count=0;
                    }
                    load_page=true;
                }
                selectedIndicator(count);
                callBack();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count<10){
                    count++;
                    load_page=true;

                    if (count>=10){
                        count=9;
                    }
                }
                selectedIndicator(count);
                callBack();

            }
        });
        dots=new TextView[10];
        dotsIndicator();
    }
    private void selectedIndicator(int position) {
        for(int i=0;i<dots.length;i++){
            if(i==position){
                dots[i].setText(String.valueOf(position+1));
                dots[i].setTextSize(16);
                dots[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            else{
                dots[i].setText(Html.fromHtml("&#9679;"));
                dots[i].setTextSize(16);
                dots[i].setTextColor(getResources().getColor(R.color.colorGray));
            }
        }
    }

    private void dotsIndicator() {
        for(int i=0;i<dots.length;i++){
            dots[i]=new TextView(MainActivity.this);
            dots[i].setText(Html.fromHtml("&#9679;"));
            dots[i].setTextSize(16);
            dotsLayout.addView(dots[i]);
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.main, menu);

        final MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                count=0;
                call_count=0;
                list.clear();
                sendlist.clear();
                f_querey=query;
                load_page=true;
                second_load=false;
                adapter.notifyDataSetChanged();
                volleyCall(query);
                myActionMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    void volleyCall(String q){
        if (recyclerView.getVisibility()==View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

        }
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String url="";
        if (!second_load) {
             url= "https://youtube.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=50&q=" + q + "&key=AIzaSyCJnoIZlgFgI9xJ63hIJLzEqBaSkPXF6gg";
        }
        else {
            url="https://youtube.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=50&pageToken="+ pagetoken +"&q=" + q + "&key=AIzaSyCJnoIZlgFgI9xJ63hIJLzEqBaSkPXF6gg";
        }
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray=response.getJSONArray("items");
                    pagetoken=response.getString("nextPageToken");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject= (JSONObject) jsonArray.get(i);
                        JSONObject idd=jsonObject.getJSONObject("id");
                        JSONObject snippet=jsonObject.getJSONObject("snippet");
                        String videoId=idd.getString("videoId");
                        String title=snippet.getString("title");
                        String description=snippet.getString("description");
                        JSONObject thumbnails=snippet.getJSONObject("thumbnails");
                        JSONObject defaultt=thumbnails.getJSONObject("default");
                        String urll=defaultt.getString("url");

                        list.add(new PageOneModel(urll,title,description,videoId));
                    }
                   if (count<=4){
                       for(int i=0;i<list.size();i++){
                           statCounting(list.get(i));
                       }
                   }
                   else{
                       for(int i=50;i<list.size();i++){
                           statCounting(list.get(i));
                       }
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    void statCounting(final PageOneModel pageOneModel){
        String uid=pageOneModel.getVideoId();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        String url1="https://youtube.googleapis.com/youtube/v3/videos?part=statistics&id="+uid+"&key=AIzaSyCJnoIZlgFgI9xJ63hIJLzEqBaSkPXF6gg";

                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url1,null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray=response.getJSONArray("items");
                            JSONObject jsonObject= (JSONObject) jsonArray.get(0);
                            JSONObject snippet=jsonObject.getJSONObject("statistics");
                            String likes=snippet.getString("likeCount");
                            String views=snippet.getString("viewCount");
                            pageOneModel.setLike(likes);
                            pageOneModel.setViews(views);
                            call_count++;
                            load_Callback();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public void load_Callback(){
        if (call_count==40){
            second_load=true;
            volleyCall(f_querey);
        }
        if (call_count>=99){
            callBack();
            second_load=false;
        }
    }

    public void callBack(){
        int start=0;
        int end=0;
        if (count==0){
            start=0;
            end=10;
        }
        else if (count==1){
            start=10;
            end=18;
        }
        else if (count==2){
            start=20;
            end=30;
        }else if (count==3){
            start=30;
            end=40;
        }else if (count==4){
            start=40;
            end=50;
        }
        else if (count==5){
            start=50;
            end=60;
        }else if (count==6){
            start=60;
            end=70;
        }else if (count==7){
            start=70;
            end=80;
        }else if (count==8){
            start=80;
            end=90;
        }else if (count==9){
            start=90;
            end=99;
        }
        if (call_count>=end&&load_page){
            prepareData(start,end);
            load_page=false;
        }
    }
    public void prepareData(int start,int end){
        sendlist.clear();
        for (int i=start;i<end;i++){
            sendlist.add(list.get(i));
        }
        if (progressBar.getVisibility()==View.VISIBLE){
            progressBar.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }
}