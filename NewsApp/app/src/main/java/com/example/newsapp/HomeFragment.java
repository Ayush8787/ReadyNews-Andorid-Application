package com.example.newsapp;
import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.util.Log;
import android.location.Location;
import android.Manifest;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {


    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 100;
    private LocationManager locationmanager;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private OutputAdapter outputAdapter;


    TextView fetchto, cityy, statee, tempp, typee;
    ImageView image;
    RelativeLayout lini2;
    int tt = 2;
    private static final String URL = "http://react-express-csci571.us-east-1.elasticbeanstalk.com/index1/top";
    private RecyclerView myrecyleview;
    private List<output> last;
    String api = "9d7e823504d14e957d3f0dfc354ce4ac";
    String ll = "https://api.openweathermap.org/data/2.5/weather?q=los%20angeles&units=metric&appid=9d7e823504d14e957d3f0dfc354ce4ac";
    private FusedLocationProviderClient fusedLocationClient;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        cityy = (TextView) view.findViewById(R.id.citytoput);
        statee = (TextView) view.findViewById(R.id.statetoput);
        tempp = (TextView) view.findViewById(R.id.temptoput);
        typee = (TextView) view.findViewById(R.id.typetoput);
        image = (ImageView) view.findViewById(R.id.imagetoput);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_items);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        fetchto = (TextView) view.findViewById(R.id.fetch);
        image.setImageResource(0);
        myrecyleview = (RecyclerView) view.findViewById(R.id.rec1);
        lini2 = (RelativeLayout) view.findViewById(R.id.maindata);

        runthis();
//        trythis();
        callthis();
        myrecyleview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));



        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void callthis() {


        Log.i("Wolrd"," From World");
        JsonArrayRequest jooi = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("ERROR", response.toString());
              //  Toast.makeText(getActivity().getApplicationContext(), "Add", Toast.LENGTH_SHORT).show();
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
                        JSONObject fields = student.getJSONObject("fields");
                        try {
                            String img = fields.getString("thumbnail");
                            use = img;
                            if(img == "")
                            {
                                use = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                            }
                        }
                        catch (Exception e)
                        {
                            use = "https://assets.guim.co.uk/images/eada8aa27c12fe2d5afa3a89d3fbae0d/fallback-logo.png";
                        }


                        last.add(new output(title1, sectionName1, use,id1,weburl,date1));
                    }
                    catch (JSONException e) {
                        Log.d("ahi aghe che",String.valueOf(last));
                        e.printStackTrace();
                    }
                }

                if(last != null)
                {
//                    mProgressBar.setVisibility(View.GONE);
//                    fetchto.setVisibility(View.GONE);
                    try {
                        mProgressBar.setVisibility(View.GONE);
                        fetchto.setVisibility(View.GONE);
                        lini2.setVisibility(View.VISIBLE);
                        Log.d("hhhhh",String.valueOf(last));
                        outputAdapter = new OutputAdapter(getContext(),last);
                        myrecyleview.setLayoutManager(new LinearLayoutManager(getActivity()));
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

    private void runthis() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Log.i("TAGggggggggggggggggggg", "GPS Lat = ");

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//                Toast.makeText(getActivity().getApplicationContext(),"GPS unable to get Value",Toast.LENGTH_SHORT).show();
                Log.i("TAG", "GPS Lat = ");
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            GPSTracker gt = new GPSTracker(getActivity().getApplicationContext());
            Location l = gt.getLocation();
            if (l == null) {
               // Toast.makeText(getActivity().getApplicationContext(), "GPS unable to get Value", Toast.LENGTH_SHORT).show();
            } else {
                double lat = l.getLatitude();
                double lon = l.getLongitude();
             //   Toast.makeText(getActivity().getApplicationContext(), "GPS Lat = " + lat + "\n lon = " + lon, Toast.LENGTH_SHORT).show();
                Log.i("TAG", "GPS Lat = " + lat + "\n lon = " + lon);
                try {
                    getcityname(lat, lon);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            // Permission has already been granted
        }


    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                callthis();
//                Toast.makeText(getActivity().getApplicationContext(), "Ama su che", Toast.LENGTH_SHORT).show();
////                last.add(new output("title1", "sectionName1", "img","id1","weburl"));
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(mSwipeRefreshLayout.isRefreshing()) {
//                            mSwipeRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                }, 1000);
//            }
//        });
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            GPSTracker gt = new GPSTracker(getActivity().getApplicationContext());
            Location l = gt.getLocation();
            if (l == null) {

                Toast.makeText(getActivity().getApplicationContext(), "GPS unable to get Value", Toast.LENGTH_SHORT).show();
            } else {
                double lat = l.getLatitude();
                double lon = l.getLongitude();
//                Toast.makeText(getActivity().getApplicationContext(),"GPS Lat = "+lat+"\n lon = "+lon,Toast.LENGTH_SHORT).show();
                Log.i("TAG", "GPS Lat from permissionnnnnnnnnnnnnnnn = " + lat + "\n lon = " + lon);
                try {
                    getcityname(lat, lon);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                callthis();
            //    Toast.makeText(getActivity().getApplicationContext(), "Ama su che", Toast.LENGTH_SHORT).show();
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

    private void getcityname(double lat, double lon) throws IOException {
        Log.i("TAG", "GPS Lat from permissionnnnnnnnnnnnnnnn from geocode = " + lat + "\n lon = " + lon);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        cityy.setText(city);
        statee.setText(state);
        Log.i("TAG", "GPS Lat from permissionnnnnnnnnnnnnnnn = " + city + "\n lon = " + state);
        findweather(city);

    }

    private void findweather(String city) {
      //  Toast.makeText(getActivity().getApplicationContext(), city, Toast.LENGTH_SHORT).show();
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=9d7e823504d14e957d3f0dfc354ce4ac";
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String type = object.getString("main");
                    Double temp_1 = Double.parseDouble(temp);
                    String tt = String.valueOf((int) Math.round(temp_1));
                    tempp.setText(tt + " \u2103");
                    typee.setText(type);

                    switch (type) {
                        case "Clear":
                            image.setImageResource(R.drawable.clear_weather);
                            break;
                        case "Clouds":
                            image.setImageResource(R.drawable.cloudy_weather);
                            break;
                        case "Snow":
                            image.setImageResource(R.drawable.snowy_weather);
                            break;
                        case "Rain":
                            image.setImageResource(R.drawable.rainy_weather);
                            break;
                        case "Drizzle":
                            image.setImageResource(R.drawable.rainy_weather);
                            break;
                        case "Thunderstorm":
                            image.setImageResource(R.drawable.thunder_weather);
                            break;
                        default:
                            image.setImageResource(R.drawable.sunny_weather);
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jor);


    }


}