package com.example.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BookmarkFragment extends Fragment {

    public TextView tt;
    private View view;
    private RecyclerView myrecyleview;
    private List<output> last;
//    private OutputAdapter outputAdapter;
    private BookAdapter bookAdapter;


    @Override
    public void onResume() {
        super.onResume();
        try {
            callthis();
        }
        catch (Exception e)
        {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_bookmark,container,false);
        myrecyleview = (RecyclerView) view.findViewById(R.id.recbook);
        myrecyleview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        tt = (TextView) view.findViewById(R.id.texti);
        callthis();


        return view;
    }

    private void callthis() {
        SharedPreferences prefs = getActivity().getSharedPreferences("card",getActivity().MODE_PRIVATE);
        Map<String, ?> allPrefs = prefs.getAll(); //your sharedPreference
        Set<String> set = allPrefs.keySet();
        ArrayList<String> set1 = new ArrayList<String>();
        Gson gson = new Gson();
        last = new ArrayList<>();
        for(String s : set){
            String json = prefs.getString(s, null);
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            Log.i("hhhhhhhhhhhhh",json);
            try{
                set1 = gson.fromJson(json, type);
            }
            catch (Exception e)
            {

            }
            Log.i("hh",set1.get(0));
            last.add(new output(set1.get(1), set1.get(3), set1.get(2),set1.get(0),"web url",set1.get(4)));

        }
        bookAdapter = new BookAdapter(getContext(),last,this);
        myrecyleview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        myrecyleview.setAdapter(bookAdapter);

    }
}
