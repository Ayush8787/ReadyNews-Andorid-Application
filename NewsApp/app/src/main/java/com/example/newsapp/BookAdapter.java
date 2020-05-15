package com.example.newsapp;
import android.annotation.SuppressLint;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private final BookmarkFragment bookmark1;
    Context mcontext;
    List<output> mData;
    View view,view2;


    public BookAdapter(Context mcontext,  List<output> mData,BookmarkFragment bookmarkFragment ) {
        this.mcontext = mcontext;
        this.mData = mData;
        this.bookmark1 = bookmarkFragment;

    }




    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mcontext).inflate(R.layout.my_square,parent,false);
        BookAdapter.BookViewHolder viewHolder = new BookAdapter.BookViewHolder(view);

        return viewHolder;

    }



    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final BookViewHolder holder, final int position) {

        holder.title.setText(mData.get(position).getTitle());
        holder.tag .setText(mData.get(position).getTag());
        Glide.with(holder.img.getContext()).load(mData.get(position).getImg()).into(holder.img);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(mData.get(position).getDate());
        ZonedDateTime zdtAmericalLA1 = zonedDateTime.withZoneSameInstant( ZoneId.of( "America/Los_Angeles" ) );
        DateTimeFormatter dtff = DateTimeFormatter.ofPattern("dd MMM");
        holder.date.setText(zdtAmericalLA1.format(dtff));
        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output po = mData.get(position);
                String temp = mData.get(position).getTitle();
                SharedPreferences sharedPreferences = mcontext.getSharedPreferences("card",mcontext.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(mData.get(position).getId());
                editor.apply();
                mData.remove(po);
                notifyDataSetChanged();
                Toast.makeText(mcontext.getApplicationContext(), temp+" Was removed from Bookmarks", Toast.LENGTH_SHORT).show();
                Log.i("jj", String.valueOf(mData.size()));
                if(mData.size() == 0){
                    bookmark1.tt.setText("No Bookmarked Articles");
                }
                else{
                    bookmark1.tt.setText("");
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
//                            savedata(mData.get(position).getId(),mData.get(position).getTitle(),mData.get(position).getImg(),mData.get(position).getTag(),mData.get(position).getDate());
//                            holder.bookmark.setImageResource(R.drawable.baseline_bookmark_24);
//                            Toast.makeText(mcontext.getApplicationContext(), mData.get(position).getTitle()+" Was added to Bookmarks", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            String temp = mData.get(position).getTitle();
                            Toast.makeText(mcontext.getApplicationContext(), temp+" Was removed from Bookmarks", Toast.LENGTH_SHORT).show();
                            book.setImageResource(R.drawable.baseline_bookmark_border_24);
                            output poo = mData.get(position);
                            SharedPreferences sharedPreferences = mcontext.getSharedPreferences("card",mcontext.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove(mData.get(position).getId());
                            editor.apply();
                            mData.remove(poo);
                            notifyDataSetChanged();
                            dialog.dismiss();





                        }
                    }
                });


                dialog.show();

                return true;

            }
        });




    }



    @SuppressLint("ResourceAsColor")
    @Override
    public int getItemCount() {
        if(mData.size() == 0){
            bookmark1.tt.setText("No Bookmarked Articles");
        }
        else{
            bookmark1.tt.setText("");

        }
//        Toast.makeText(mcontext.getApplicationContext(), "From Item count " +mData.size(), Toast.LENGTH_SHORT).show();

        return mData.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        private ImageView img,img11;
        private TextView title,date;
        private TextView tag;
        private ImageView bookmark;

        LinearLayout linearLayout;
        ConstraintLayout con;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.bookimage);
            title = (TextView) itemView.findViewById(R.id.booktitle);
            tag = (TextView) itemView.findViewById(R.id.textView8);
            linearLayout = itemView.findViewById(R.id.lini2);
            bookmark = itemView.findViewById(R.id.imageView5);
            date = itemView.findViewById(R.id.bookdate);
            con = itemView.findViewById(R.id.con);



        }
    }
}
