package com.example.allnews;

import android.annotation.SuppressLint;
import android.net.sip.SipSession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsViewHolder> {
    ArrayList<News> items = new ArrayList<News>();
      NewsItemClicked listener;
    public NewsListAdapter( NewsItemClicked listener){

        this.listener=listener;
    }
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
        NewsViewHolder viewHolder = new NewsViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(items.get(viewHolder.getAbsoluteAdapterPosition()));

            }
        });

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News currentItem = items.get(position);
        holder.titleView.setText(currentItem.title);
        String nameOfAuthor =currentItem.author;
//        if(nameOfAuthor.equals(null)) holder.author.setText("Author Name Unavailable");
//        else
        holder.author.setText(currentItem.author);

        Glide.with(holder.itemView.getContext()).load(currentItem.imageURL).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void updateNews(ArrayList<News> updatedNews){
        items.clear();
        items.addAll(updatedNews);
        notifyDataSetChanged();
    }
}

class NewsViewHolder extends RecyclerView.ViewHolder{
    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
    }
    TextView titleView =  itemView.findViewById(R.id.title);
    ImageView image =  itemView.findViewById(R.id.image);
    TextView author = itemView.findViewById(R.id.author);

}

interface NewsItemClicked{
    void onItemClicked(News item);


}
//ccc13919635c4048b672abd985dda259