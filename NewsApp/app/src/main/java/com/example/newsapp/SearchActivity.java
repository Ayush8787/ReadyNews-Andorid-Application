package com.example.newsapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final String URL = "http://react-express-csci571.us-east-1.elasticbeanstalk.com/index1/search?q=";
    private RecyclerView myrecyleview;
    private List<output> last;
    private ProgressBar mProgressBar;
    private TextView fetchto;
    private String qq;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private OutputAdapter outputAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        try {
            outputAdapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        qq = getIntent().getStringExtra("query");
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_use);
        toolbar.setTitle("Search Results for "+qq);
        myrecyleview = (RecyclerView) findViewById(R.id.userlist6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh_items);
        fetchto = (TextView) findViewById(R.id.fetch);
        myrecyleview.setLayoutManager(new LinearLayoutManager(this));
        callthis();
        myrecyleview.addItemDecoration(new DividerItemDecoration(SearchActivity.this, DividerItemDecoration.VERTICAL));
        try {
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    callthis();
                   // Toast.makeText(getApplicationContext(), "Ama su che", Toast.LENGTH_SHORT).show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    }, 1000);
                }
            });
        }
        catch (Exception e)
        {

        }

    }

    private void callthis() {

        JsonArrayRequest jooi = new JsonArrayRequest(Request.Method.GET, URL+qq, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
            //    Log.d("ERROR", response.toString());
             //   Toast.makeText(getApplicationContext(), "Add", Toast.LENGTH_SHORT).show();
                last = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {

                        String use = null;
                        JSONObject student = response.getJSONObject(i);
                        String id1 = student.getString("id");
                        String title1 = student.getString("webTitle");
                        String weburl = student.getString("webUrl");
                        String date1 = student.getString("webPublicationDate");
                        String sectionName1 = student.getString("sectionName");
                        JSONObject blocks = student.getJSONObject("blocks");
                        JSONObject main = blocks.getJSONObject("main");
                        JSONArray elements = main.getJSONArray("elements");
                        JSONObject preassests = elements.getJSONObject(0);
                        JSONArray assets = preassests.getJSONArray("assets");
                        if(assets.length()==0)
                        {

                            Log.i("Ahi ave che","JOjojojj");
                            use = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";

                        }
                        else
                        {
                            JSONObject preimg = assets.getJSONObject(0);
                            String img = preimg.getString("file");
                            use = img;
                        }

                        last.add(new output(title1, sectionName1, use,id1,weburl,date1));
//                        last.add(new output("title1", "sectionName1", "img","id1","weburl"));
                    }
                    catch (JSONException e) {
                        Log.d("ahi aghe che",String.valueOf(last));
                        e.printStackTrace();
                    }
                }

                if(last != null)
                {
                    mProgressBar.setVisibility(View.GONE);
                    fetchto.setVisibility(View.GONE);
                    Log.d("hhhhh",String.valueOf(last));
                    outputAdapter = new OutputAdapter(SearchActivity.this,last);

                    myrecyleview.setAdapter(outputAdapter);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
                Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jooi);
    }


    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
