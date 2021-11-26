package com.example.allnews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NewsItemClicked {

    RecyclerView recyclerView;
    NewsListAdapter adapter=new NewsListAdapter(this);
    String apiKey;

    SwipeRefreshLayout swipeRefreshLayout;

    String url =" https://newsdata.io/api/1/news?apikey=pub_22836518b766b0073dbc160e9d0d03788076&q=India&country=in&language=en&category=";
    String category="Top";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView =findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        try {
            ApplicationInfo applicationInfo = getApplicationContext().getPackageManager()
                    .getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle=applicationInfo.metaData;
            apiKey=bundle.getString("keyValue");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        
        String catUrl=url+category;
        fetchData(catUrl);
        adapter =  new NewsListAdapter(this);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData(url+category);
                Toast.makeText(MainActivity.this, "Refreshed", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


    }

    public void isEmpty(){
        if(adapter.getItemCount()==0)
            Toast.makeText(this, "This category news not available, Please switch to other category ", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.categories,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String categoryUrl=" https://newsdata.io/api/1/news?apikey=pub_22836518b766b0073dbc160e9d0d03788076&q=India&country=us&language=en&category=";
        switch (item.getItemId()){
            case R.id.top:
                fetchData(categoryUrl+"top");
                adapter.notifyDataSetChanged();
                category="top";
                isEmpty();
                return true;
            case R.id.business:
                fetchData(categoryUrl+"business");
                adapter.notifyDataSetChanged();
                category="business";
                isEmpty();
                return true;
            case R.id.entertainment:
                fetchData(categoryUrl+"entertainment");
                adapter.notifyDataSetChanged();
                category="entertainment";
                isEmpty();
                return true;
            case R.id.food:
                fetchData(categoryUrl+"food");
                adapter.notifyDataSetChanged();
                category="food";
                isEmpty();
                return true;
            case R.id.health:
                fetchData(categoryUrl+"health");
                adapter.notifyDataSetChanged();
                category="health";
                isEmpty();
                return true;
                case R.id.politics:
                    fetchData(categoryUrl+"politics");
                    adapter.notifyDataSetChanged();
                    category="politics";
                    isEmpty();
                    return true;
            case R.id.science:
                        fetchData(categoryUrl+"science");
                        adapter.notifyDataSetChanged();
                        category="science";
                        isEmpty();
                        return true;
            case R.id.sports:
                fetchData(categoryUrl+"sports");
                adapter.notifyDataSetChanged();
                category="sports";
                isEmpty();
                return true;
            case R.id.technology:
                fetchData(categoryUrl+"technology");
                adapter.notifyDataSetChanged();
                category="technology";
                isEmpty();
                return true;
                case R.id.world:
                    fetchData(categoryUrl+"world");
                    adapter.notifyDataSetChanged();
                    category="world";
                    isEmpty();
                    return true;
            case R.id.environment:
                fetchData(categoryUrl+"environment");
                adapter.notifyDataSetChanged();
                category="environment";
                isEmpty();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchData(String url){

   //     https://newsdata.io/api/1/news?apikey=pub_22836518b766b0073dbc160e9d0d03788076&country=in
//        /String url = "https://google-news1.p.rapidapi.com/geolocation?geo=Alabama&country=US&lang=en).get()
//                .addHeader(\"x-rapidapi-key\", "f265b67ed6msh991dff94087349dp16dbcfjsn15f58ee0cdeb")
//                .addHeader("x-rapidapi-host", "google-news1.p.rapidapi.com")
//                .build()";
;
        // String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=ccc13919635c4048b672abd985dda259";
       //{headers: new Headers({"X-Requested-With":"bdbrtvgesgesesrg"})}

       // String url = "https://newsdata.io/api/1/news?apikey=pub_22836518b766b0073dbc160e9d0d03788076&country=in";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest (Request.Method.GET, url, null, response -> {
            try {
                JSONArray newsJsonArray = response.getJSONArray("results");
                 ArrayList<News> newsArray= new ArrayList<>();
                 for (int i=0;i<newsJsonArray.length();i++){
                     JSONObject newsJsonObject = newsJsonArray.getJSONObject(i);
                     News news = new News(newsJsonObject.getString("title"),newsJsonObject.getString("source_id"),newsJsonObject.getString("link"),newsJsonObject.getString("image_url"));
                     newsArray.add(news);
                 }
                 adapter.updateNews(newsArray);

            }
            catch (JSONException e) {
                e.printStackTrace();
               // Toast.makeText(MainActivity.this, "JSON ERROR", Toast.LENGTH_SHORT).show();
            }

        }, error -> {


        }) ;



        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);


    }




    @Override
    public void onItemClicked(News item) {

        // and convert it to string
        String str = item.url;

        // Create the Intent object of this class Context() to Second_activity class
        Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);

        // now by putExtra method put the value in key, value pair
        // key is message_key by this key we will receive the value, and put the string

        intent.putExtra("urlLink", str);

        // start the Intent
        startActivity(intent);
//        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//        CustomTabsIntent customTabsIntent = builder.build();
//        customTabsIntent.launchUrl(this, Uri.parse(item.url));


    }
}

