package com.example.newsapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class Sports extends Fragment {
    private static final String URL = "http://react-express-csci571.us-east-1.elasticbeanstalk.com/index1/sports";
    private RecyclerView myrecyleview;
    private List<output> last;
    private ProgressBar mProgressBar;
    private TextView fetchto;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View view;
    private OutputAdapter outputAdapter;

    public Sports() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            outputAdapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Wolrd","World");
        view = inflater.inflate(R.layout.fragment_sports, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        fetchto = (TextView) view.findViewById(R.id.fetch);
        myrecyleview = (RecyclerView) view.findViewById(R.id.userlist2);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_items);
        myrecyleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        callthis();
        myrecyleview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));





        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                callthis();
              //  Toast.makeText(getActivity().getApplicationContext(), "Ama su che", Toast.LENGTH_SHORT).show();
//                last.add(new output("title1", "sectionName1", "img","id1","weburl"));
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


    private void callthis() {

        JsonArrayRequest jooi = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
          //      Log.d("ERROR", response.toString());
              //  Toast.makeText(getActivity().getApplicationContext(), "Add", Toast.LENGTH_SHORT).show();
                last = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {

                        String use = null;
                        JSONObject student = response.getJSONObject(i);
                        String id1 = student.getString("id");
                        String title1 = student.getString("webTitle");
                        String date1 = student.getString("webPublicationDate");
                        String weburl = student.getString("webUrl");
                        String sectionName1 = student.getString("sectionName");
                        JSONObject blocks = student.getJSONObject("blocks");
                        try{
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
                        }
                        catch (Exception e)
                        {
                            use = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";

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
                    try
                    {
                        mProgressBar.setVisibility(View.GONE);
                        fetchto.setVisibility(View.GONE);
                        Log.d("hhhhh",String.valueOf(last));
                        outputAdapter = new OutputAdapter(getContext(),last);

                        myrecyleview.setAdapter(outputAdapter);
                    }
                    catch (Exception e)
                    {

                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", error.toString());
                Toast.makeText(getActivity().getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jooi);

    }
}
