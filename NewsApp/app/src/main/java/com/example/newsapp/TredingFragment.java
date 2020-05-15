package com.example.newsapp;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Entity;

import java.lang.reflect.Array;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Arrays;

public class TredingFragment extends Fragment {


    private View view;
    private LineChart mchart;
    private EditText et;
   private String url = "http://react-express-csci571.us-east-1.elasticbeanstalk.com/index1/trend?q=";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view =  inflater.inflate(R.layout.fragment_treding,container,false);
        mchart = (LineChart) view.findViewById(R.id.linechart);
        mchart.setDragEnabled(true);
        mchart.setScaleXEnabled(false);
        et = (EditText) view.findViewById(R.id.input);
        callthis("CoronaVirus");
        et.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    callthis(et.getText().toString());
                    return true;
                }
                return false;
            }
        });


        return view;
    }

    private void callthis(final String x) {
        final ArrayList<Entry> yValues = new ArrayList<>();

        Log.i("Line chart mate", "AVE che");

        JsonArrayRequest jooi = new JsonArrayRequest(Request.Method.GET, url+x, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i=0;i<response.length();i++)
                {
                    try {
                        yValues.add(new Entry(i,response.getInt(i)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.i("Line chart mate", String.valueOf(yValues));
                final LineDataSet set1 = new LineDataSet(yValues,"Trending Chart for "+x);
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                LineData data = new LineData(dataSets);
                Log.i("Line chart mate", "ahi bi ave che");
                set1.setColor(Color.parseColor("#3700B3"));
                set1.setCircleHoleColor(Color.parseColor("#3700B3"));
                set1.setCircleColor(Color.parseColor("#3700B3"));
                mchart.setData(data);
//                mchart.getLegend().setTextColor(Color.BLACK);
//                mchart.getLegend().setTextSize(15f);
                mchart.getXAxis().setDrawGridLines(false);
                mchart.getAxisLeft().setDrawGridLines(false);
                mchart.getAxisLeft().setDrawAxisLine(false);
                mchart.getAxisLeft().setDrawGridLines(false);
                mchart.getAxisRight().setDrawGridLines(false);
                LegendEntry leg = new LegendEntry();
                leg.formSize = 16f;
                leg.formColor = Color.parseColor("#3700B3");
                leg.label = "Trending Chart for "+x;
                Legend l = mchart.getLegend();
                l.setYOffset(16f);
                l.setTextSize(16f);
                l.setCustom(Arrays.asList(leg));
                mchart.invalidate();

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
