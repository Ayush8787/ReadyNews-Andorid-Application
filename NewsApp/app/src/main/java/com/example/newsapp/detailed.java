package com.example.newsapp;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TimeZone;


public class detailed extends AppCompatActivity {
    String title1;
    String id;
    TextView dd,titleto,dateto,sectionto,showto,fetchto;
    ImageView imageto,bookmarkto,twitter;
    LinearLayout linito;
    Context con;
    private ProgressBar mProgressBar;





    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        linito = (LinearLayout) findViewById(R.id.lini);
        dd = (TextView) findViewById(R.id.des);
        titleto = (TextView) findViewById(R.id.tile);
        dateto = (TextView) findViewById(R.id.date);
        imageto = (ImageView) findViewById(R.id.imageto);
        sectionto = (TextView) findViewById(R.id.section);
        showto = (TextView) findViewById(R.id.show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_row);
        imageto.setImageResource(0);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        bookmarkto = (ImageView) findViewById(R.id.image1);
        twitter = (ImageView) findViewById(R.id.image);
        fetchto = (TextView) findViewById(R.id.fetch);
        title1 = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");
        toolbar.setTitle(title1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String url = "http://react-express-csci571.us-east-1.elasticbeanstalk.com/index1/details?id="+id;

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String use = null;
                    final String title1 = response.getString("webTitle");
                    final String id = response.getString("id");
                    final String sectionName1 = response.getString("sectionName");
                    final String date1 = response.getString("webPublicationDate");
                    final String weburl = response.getString("webUrl");
                    JSONObject blocks = response.getJSONObject("blocks");
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


                    JSONArray body = blocks.getJSONArray("body");
                    JSONObject predes = body.getJSONObject(0);
                    String description = predes.getString("bodyHtml");
                    Log.d("Description",description);
                    mProgressBar.setVisibility(View.GONE);
                    fetchto.setVisibility(View.GONE);
                    linito.setVisibility(View.VISIBLE);

                    LocalDateTime ldt = LocalDateTime.now();
                    ZoneId zoneId = ZoneId.of( "America/Los_Angeles" );
                    ZonedDateTime zdtAmericaLA = ldt.atZone( zoneId );
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(date1);
                    ZonedDateTime zdtAmericalLA1 = zonedDateTime.withZoneSameInstant( ZoneId.of( "America/Los_Angeles" ) );
                    DateTimeFormatter dtff = DateTimeFormatter.ofPattern("dd MMM yyyy");


                    Glide.with(imageto).load(use).into(imageto);
                    dd.setText(Html.fromHtml(description));
                    titleto.setText(title1);
                    dateto.setText(zdtAmericalLA1.format(dtff));
                    sectionto.setText(sectionName1);
                    sectionto.setLinksClickable(true);
                    showto.setText(Html.fromHtml("<p><u>View Full Article</u></p>"));
                    showto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(weburl));
                            startActivity(intent);


                        }
                    });

                    SharedPreferences sharedPreferences = getSharedPreferences("card",MODE_PRIVATE);
                    String saved;
                    saved = sharedPreferences.getString(id,"false");
                    if(saved != "false")
                    {
//            Toast.makeText(mcontext.getApplicationContext(), "Ahi ave che che che che che", Toast.LENGTH_SHORT).show();
                        bookmarkto.setImageResource(R.drawable.baseline_bookmark_24);
                    }
                    else{
                        bookmarkto.setImageResource(R.drawable.baseline_bookmark_border_24);
                    }
                    final String finalUse = use;
                    bookmarkto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Drawable.ConstantState cs1 = bookmarkto.getDrawable().getConstantState();
                            Drawable.ConstantState cs2 = getResources().getDrawable(R.drawable.baseline_bookmark_border_24).getConstantState();
                            if ( cs1 == cs2) {
                                bookmarkto.setImageResource(R.drawable.baseline_bookmark_24);
                                Toast.makeText(getApplicationContext(), title1+" was added to favorites", Toast.LENGTH_SHORT).show();
                                savedata(id,title1, finalUse,sectionName1,date1);


                            }
                            else{
                                bookmarkto.setImageResource(R.drawable.baseline_bookmark_border_24);
                                Toast.makeText(getApplicationContext(), title1+" was removed from favorites", Toast.LENGTH_SHORT).show();
                                delete(id);


                            }

                        }

                    });
                    twitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://twitter.com/intent/tweet?text=Check%20out%20this%20Link:&url="+weburl+"&hashtags=CSCI571NewsSearch"));
                            startActivity(intent);
                        }
                    });




                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);

    }

    public void savedata(String ii,String title,String img,String section,String date){
        SharedPreferences sharedPreferences = getSharedPreferences("card",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear();
//        editor.apply();
        ArrayList<String> set = new ArrayList<String>();
        set.add(ii);
        set.add(title);
        set.add(img);
        set.add(section);
        set.add(date);
        Gson gson = new Gson();
        String json = gson.toJson(set);
        editor.putString(ii,json);
        editor.apply();

    }


    public void delete(String jj)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("card",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(jj);
        editor.apply();

    }

//    private String getFormattedDate(String OurDate) {
//        try {
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // According to your Server TimeStamp
//            formatter.setTimeZone(TimeZone.getTimeZone("UTC")); //your Server Time Zone
//            Date value = formatter.parse(OurDate); // Parse your date
//
//            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy"); //this format changeable according to your choice
//            dateFormatter.setTimeZone(TimeZone.getDefault());
//            OurDate = dateFormatter.format(value);
//
//
//        } catch (Exception e) {
//            OurDate = "00-00-0000 00:00";
//
//        }
//        return OurDate;
//    }


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
