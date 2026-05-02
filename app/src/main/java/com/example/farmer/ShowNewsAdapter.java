package com.example.farmer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aniketjain.weatherapp.R;

import java.util.List;

public class ShowNewsAdapter extends RecyclerView.Adapter<ShowNewsAdapter.NewsViewHolder> {

    private final List<News> newsList;
    private final Context context;

    public ShowNewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news_show, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.title.setText(news.title);
        holder.description.setText(news.description);
        holder.url.setText(news.url.isEmpty() ? "No URL" : news.url);

        if (!news.url.isEmpty()) {
            holder.url.setTextColor(Color.BLUE);
            holder.url.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.url));
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, url;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.newsTitle);
            description = itemView.findViewById(R.id.newsDescription);
            url = itemView.findViewById(R.id.newsUrl);
        }
    }
}

