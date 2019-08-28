package com.dawood.newsapp.news;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.dawood.newsapp.FilterDialogFragment;
import com.dawood.newsapp.MainActivity;
import com.dawood.newsapp.R;
import com.dawood.newsapp.models.NewsResponseModel;
import com.dawood.newsapp.models.SourcesModel;
import com.dawood.newsapp.network.ConnectivityReceiver;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends Fragment implements FilterDialogFragment.FilterAction {

    private static final String TAG = "NewsFragment";
    @BindView(R.id.progress) ProgressBar progress;
    @BindView(R.id.newsRecycleView) RecyclerView newsRecycleView;
    @BindView(R.id.chooseAgainBtn) Button chooseAgainBtn;
    @BindView(R.id.EmptyRow) LinearLayout emptyRow;
    private ArrayList<NewsResponseModel.Article> articleArrayList = new ArrayList<>();
    private List<String> sourcesNames = new ArrayList<>();
    private List<String> sourcesIds = new ArrayList<>();
    private NewsAdapter newsAdapter;
    private NewsViewModel newsViewModel;
    private static int pageCounter = 1;
    private String topic = "bitcoin", country, source;

    public NewsFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setActionBarTitle("News App");

        return view;
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        if (ConnectivityReceiver.isConnected()) {
            progress.setVisibility(View.VISIBLE);
            getCurrentNews(topic);
            setupRecyclerView();
            getLastPosition();
        } else
            Toast.makeText(getContext(), "Please Connect Your WIFI/Data", Toast.LENGTH_SHORT).show();
    }

    private void getCurrentNews(String topic) {
        newsViewModel.init(pageCounter + "", topic + "");
        newsViewModel.getNewsRepository().observe(this, newsResponse -> {
            List<NewsResponseModel.Article> newsArticles = newsResponse.getArticles();
            if (newsArticles == null || newsArticles.size() == 0)
                Toast.makeText(getContext(), "No More News", Toast.LENGTH_SHORT).show();
            else {
                articleArrayList.addAll(newsArticles);
                newsAdapter.notifyDataSetChanged();
            }
            progress.setVisibility(View.GONE);
        });
    }

    private void getFilteredNews(String country, String source) {
        this.country = country;
        this.source = source;
        newsViewModel.getFilterdNewsRepository(country, source, pageCounter + "").observe(this, newsResponse -> {
            List<NewsResponseModel.Article> newsArticles = newsResponse.getArticles();
            if (newsArticles == null || newsArticles.size() == 0)
                Toast.makeText(getContext(), "No More News", Toast.LENGTH_SHORT).show();
            else {
                articleArrayList.addAll(newsArticles);
                newsAdapter.notifyDataSetChanged();
            }
            progress.setVisibility(View.GONE);
        });
    }


    private void getNewsSources() {
        newsViewModel.getSources().observe(this, newsResponse -> {
            List<SourcesModel.Source> sources = newsResponse.getSources();
            sourcesIds.add("-1");
            sourcesNames.add("Select News Source");
            for (int i = 0; i < sources.size(); i++) {
                sourcesNames.add(sources.get(i).getName());
                sourcesIds.add(sources.get(i).getId());
            }
        });
    }

    private void setupRecyclerView() {
        newsAdapter = new NewsAdapter();
        newsAdapter.setNewsList(articleArrayList);
        newsRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsRecycleView.setAdapter(newsAdapter);
        newsRecycleView.setItemAnimator(new DefaultItemAnimator());
        newsRecycleView.setNestedScrollingEnabled(true);
    }

    private void getLastPosition() {
        if (newsAdapter != null)
            newsRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1)) {
                        pageCounter++;
                        if (country == null && source == null) {
                            newsViewModel.init(pageCounter + "", topic + "");
                            newsViewModel.getNewsRepository().observe(getActivity(), newsResponse -> {
                                progress.setVisibility(View.GONE);
                                List<NewsResponseModel.Article> newsArticles = newsResponse.getArticles();
                                if (newsArticles != null)
                                    if (newsArticles.size() != 0)
                                        newsAdapter.getMoreNews((ArrayList<NewsResponseModel.Article>) newsArticles);
                                    else {
                                        Toast.makeText(getContext(), "No More News", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                            });
                        } else {
                            newsViewModel.getFilterdNewsRepository(country, source, pageCounter + "").observe(getActivity(), newsResponse -> {
                                List<NewsResponseModel.Article> newsArticles = newsResponse.getArticles();
                                progress.setVisibility(View.GONE);
                                if (newsArticles == null || newsArticles.size() == 0) {
                                    Toast.makeText(getContext(), "No More News", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    articleArrayList.addAll(newsArticles);
                                    newsAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }
            });
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filterDialogFragment:
                showDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        getNewsSources();
        DialogFragment dialogFragment = new FilterDialogFragment(sourcesNames, sourcesIds, this);
        dialogFragment.show(ft, "dialog");
    }

    @Override public void onCountrySelected(String country) {
        pageCounter = 1;
        if (country != null && !country.equals("")) {
            newsAdapter.clear();
            getFilteredNews(country + "", "");
        }
    }

    @Override public void onNewsSelected(String sourceId) {
        pageCounter = 1;
        if (sourceId != null && !sourceId.equals("-1") && !sourceId.equals("")) {
            newsAdapter.clear();
            getFilteredNews("", sourceId + "");
        }

    }
}
