package com.example.newsapp;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
public class OutputAdapter extends RecyclerView.Adapter<OutputAdapter.OutputViewHolder> {

     Context mcontext;

     List<output> mData;
    View view;

    public OutputAdapter(Context mcontext,  List<output> mData ) {
        this.mcontext = mcontext;
        this.mData = mData;

    }

    @NonNull
    @Override
    public OutputViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(mcontext).inflate(R.layout.my_row,parent,false);
        OutputViewHolder viewHolder = new OutputViewHolder(view);


        return viewHolder;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final OutputViewHolder holder, final int position) {

        LocalDateTime ldt = LocalDateTime.now();
        ZoneId zoneId = ZoneId.of( "America/Los_Angeles" );
        ZonedDateTime zdtAmericaLA = ldt.atZone( zoneId );
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(mData.get(position).getDate());
        ZonedDateTime zdtAmericalLA1 = zonedDateTime.withZoneSameInstant( ZoneId.of( "America/Los_Angeles" ) );

        Duration diff = Duration.between(zdtAmericalLA1.toInstant(), zdtAmericaLA.toInstant());

        long days = diff.toDays();
        diff = diff.minusDays(days);
        long hours = diff.toHours();
        diff = diff.minusHours(hours);
        long minutes = diff.toMinutes();
        diff = diff.minusMinutes(minutes);
        long seconds = diff.toMillis();
        String temp = "3m";


        if(seconds/1000!=0)
        {
            temp = String.valueOf(seconds/1000)+"s";
            if(minutes != 0)
            {
                temp = String.valueOf(minutes)+"m";
                if(hours!=0)
                {
                    temp = String.valueOf(hours)+"h";
                    if(days!=0)
                    {
                        temp = String.valueOf(days)+"d";
                    }
                }
            }
            else
            {
                if(hours!=0)
                {
                    temp = String.valueOf(hours)+"h";
                    if(days!=0)
                    {
                        temp = String.valueOf(days)+"d";
                    }
                }
            }

        }
        else
        {
            if(seconds/100!=0);
            {
                temp = "0s";
            }

        }
        Log.i("Given time ", temp);

        StringBuilder formattedDiff = new StringBuilder();
        if(days!=0){
            if(days==1){
                formattedDiff.append(days + " Day ");

            }else {
                formattedDiff.append(days + " Days ");
            }
        }if(hours!=0){
            if(hours==1){
                formattedDiff.append(hours + " hour ");
            }else{
                formattedDiff.append(hours + " hours ");
            }
        }if(minutes!=0){
            if(minutes==1){
                formattedDiff.append(minutes + " minute ");
            }else{
                formattedDiff.append(minutes + " minutes ");
            }
        }if(seconds!=0){
            if(seconds==1){
                formattedDiff.append(seconds + " second ");
            }else{
                formattedDiff.append(seconds + " seconds ");
            }
        }
        Log.i("Given time ", formattedDiff.toString());
        Log.i("Given time -- -- -- -- ", String.valueOf(diff.toHours()));


        holder.time.setText(temp+" ago");
        holder.title.setText(mData.get(position).getTitle());
        holder.tag .setText(mData.get(position).getTag());
        Glide.with(holder.img.getContext()).load(mData.get(position).getImg()).into(holder.img);
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("card",mcontext.MODE_PRIVATE);
        String saved;
        saved = sharedPreferences.getString(mData.get(position).getId(),"false");

        if(saved != "false")
        {
//            Toast.makeText(mcontext.getApplicationContext(), "Ahi ave che che che che che", Toast.LENGTH_SHORT).show();
            holder.bookmark.setImageResource(R.drawable.baseline_bookmark_24);
        }
        else{
            holder.bookmark.setImageResource(R.drawable.baseline_bookmark_border_24);
        }

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable.ConstantState cs1 = holder.bookmark.getDrawable().getConstantState();
                Drawable.ConstantState cs2 = mcontext.getResources().getDrawable(R.drawable.baseline_bookmark_border_24).getConstantState();
                if ( cs1 == cs2) {
                    holder.bookmark.setImageResource(R.drawable.baseline_bookmark_24);
//                    Toast.makeText(mcontext.getApplicationContext(), mData.get(position).getId(), Toast.LENGTH_SHORT).show();
                    savedata(mData.get(position).getId(),mData.get(position).getTitle(),mData.get(position).getImg(),mData.get(position).getTag(),mData.get(position).getDate());
                    Toast.makeText(mcontext.getApplicationContext(), mData.get(position).getTitle()+" was added to bookmarks", Toast.LENGTH_SHORT).show();

                }
                else{
                    holder.bookmark.setImageResource(R.drawable.baseline_bookmark_border_24);
//                    Toast.makeText(mcontext.getApplicationContext(), mData.get(position).getId(), Toast.LENGTH_SHORT).show();
                    deletedata(mData.get(position).getId());
                    Toast.makeText(mcontext.getApplicationContext(), mData.get(position).getTitle()+" was removed from bookmarks", Toast.LENGTH_SHORT).show();



                }

            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,detailed.class);
                intent.putExtra("title",mData.get(position).getTitle());
                intent.putExtra("id",mData.get(position).getId());
                mcontext.startActivities(new Intent[]{intent});
            }
        });
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("from","adapter");
                final Dialog dialog = new Dialog(mcontext);
                dialog.setContentView(R.layout.custom);
                final TextView title11 = (TextView) dialog.findViewById(R.id.textTD);
                ImageView img11 = (ImageView) dialog.findViewById(R.id.imageD);
                ImageView img22 = (ImageView) dialog.findViewById(R.id.imageView2);
                final ImageView book = (ImageView) dialog.findViewById(R.id.imageView3);
                SharedPreferences sharedPreferences = mcontext.getSharedPreferences("card",mcontext.MODE_PRIVATE);
                String saved;
                saved = sharedPreferences.getString(mData.get(position).getId(),"false");
                if(saved != "false")
                {
//                    Toast.makeText(mcontext.getApplicationContext(), "Ahi ave che che che che che", Toast.LENGTH_SHORT).show();
                    book.setImageResource(R.drawable.baseline_bookmark_24);

                }
                else{
                    holder.bookmark.setImageResource(R.drawable.baseline_bookmark_border_24);

                }


                title11.setText(mData.get(position).getTitle());
                Glide.with(img11).load(mData.get(position).getImg()).into(img11);
                final String temp = mData.get(position).getWeb();
                img22.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://twitter.com/intent/tweet?text=Check%20out%20this%20Link:&url="+temp+"&hashtags=CSCI571NewsSearch"));
                        mcontext.startActivity(intent);
                    }
                });
                book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Drawable.ConstantState cs1 = book.getDrawable().getConstantState();
                        Drawable.ConstantState cs2 = mcontext.getResources().getDrawable(R.drawable.baseline_bookmark_border_24).getConstantState();
                        if ( cs1 == cs2) {
                            book.setImageResource(R.drawable.baseline_bookmark_24);
//                            Toast.makeText(mcontext.getApplicationContext(), "Add", Toast.LENGTH_SHORT).show();
                            savedata(mData.get(position).getId(),mData.get(position).getTitle(),mData.get(position).getImg(),mData.get(position).getTag(),mData.get(position).getDate());
                            holder.bookmark.setImageResource(R.drawable.baseline_bookmark_24);
                            Toast.makeText(mcontext.getApplicationContext(), mData.get(position).getTitle()+" was added to bookmarks", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            book.setImageResource(R.drawable.baseline_bookmark_border_24);
//                            Toast.makeText(mcontext.getApplicationContext(), "Remove", Toast.LENGTH_SHORT).show();
                            deletedata(mData.get(position).getId());
                            holder.bookmark.setImageResource(R.drawable.baseline_bookmark_border_24);
                            Toast.makeText(mcontext.getApplicationContext(), mData.get(position).getTitle()+" was removed from bookmarks", Toast.LENGTH_SHORT).show();



                        }
                    }
                });

                dialog.show();

                return true;
            }
        });



    }

    public void savedata(String ii,String title,String img,String section,String date){
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("card",mcontext.MODE_PRIVATE);
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

    public void deletedata(String jj){
        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("card",mcontext.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(jj);
        editor.apply();
    }

    @Override
    public int getItemCount() {

        return mData.size();
    }

    public class OutputViewHolder extends RecyclerView.ViewHolder {
        private ImageView img,img11;
        private TextView title,time;
        private TextView tag;
        private ImageView bookmark;
        LinearLayout linearLayout;

        public OutputViewHolder(@NonNull View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.myimage);
            title = (TextView) itemView.findViewById(R.id.mytitle);
            tag = (TextView) itemView.findViewById(R.id.mytag);
            linearLayout = itemView.findViewById(R.id.lini);
            bookmark = itemView.findViewById(R.id.Bookimage);
            time = itemView.findViewById(R.id.mytime);


        }
    }
}
