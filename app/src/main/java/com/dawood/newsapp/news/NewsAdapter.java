package com.dawood.newsapp.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dawood.newsapp.R;
import com.dawood.newsapp.models.NewsResponseModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewViewHolder> {

    private ArrayList<NewsResponseModel.Article> newsList;
    private Context context;

    @NonNull @Override public NewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row_item, parent, false);
        return new NewViewHolder(view);
    }

    void setNewsList(ArrayList<NewsResponseModel.Article> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    void getMoreNews(ArrayList<NewsResponseModel.Article> newsList) {
        this.newsList.addAll(newsList);
        notifyDataSetChanged();
    }

    void clear() {
        newsList.clear();
        notifyDataSetChanged();
    }

    @Override public void onBindViewHolder(@NonNull NewViewHolder holder, int position) {
        holder.bind(newsList.get(position));
    }

    @Override public int getItemCount() {
        return newsList.size();
    }

    class NewViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        @BindView(R.id.logoImage) ImageView logoImage;
        @BindView(R.id.textView) TextView title;
        @BindView(R.id.newsDate) TextView newsDate;
        @BindView(R.id.newsItemContainer) ConstraintLayout newsItemContainer;
        NewsResponseModel.Article article;

        NewViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(NewsResponseModel.Article article) {
            this.article = article;
            newsDate.setText(article.getPublishedAt());
            title.setText(article.getTitle());
            Glide.with(context).load(article.getUrlToImage()).into(logoImage);
            newsItemContainer.setOnClickListener(this::onClick);
        }

        @Override public void onClick(View v) {
            Navigation.findNavController(v).navigate(NewsFragmentDirections.actionNewsFragmentToNewsDetailsFragment(article));
        }
    }
}
