package com.dawood.newsapp.news;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.dawood.newsapp.Utils;
import com.dawood.newsapp.models.NewsResponseModel;
import com.dawood.newsapp.models.SourcesModel;
import com.dawood.newsapp.network.ApiServices;
import com.dawood.newsapp.network.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NewsRepo {
    private static NewsRepo newsRepository;
    CompositeDisposable disposable;

    public static NewsRepo getInstance() {
        if (newsRepository == null) {
            newsRepository = new NewsRepo();
        }
        return newsRepository;
    }

    private ApiServices newsApi;

    NewsRepo() {
        newsApi = RetrofitClient.getInterfaceInstance(Utils.BaseUrl);
        disposable = new CompositeDisposable();
    }


    MutableLiveData<NewsResponseModel> getNews(String apiKey, String topic, String page) {
        MutableLiveData<NewsResponseModel> newsData = new MutableLiveData<>();
        disposable.add(newsApi.getAllNews(apiKey, topic, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsData::setValue, throwable -> {
                    //disposable.dispose();
                    Log.d(TAG, "getNews: " + throwable.getMessage());
                }));
        return newsData;
    }

    MutableLiveData<NewsResponseModel> getFilterdNews(String apiKey, String country, String source, String page) {
        MutableLiveData<NewsResponseModel> newsData = new MutableLiveData<>();
        disposable.add(newsApi.getFilterdNews(apiKey, country, source, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsData::setValue, throwable -> {
                    //disposable.dispose();
                    Log.d(TAG, "getNews: " + throwable.getMessage());
                }));
        return newsData;
    }

    MutableLiveData<SourcesModel> getNewsSources(String apiKey) {
        MutableLiveData<SourcesModel> newsSources = new MutableLiveData<>();
        disposable.add(newsApi.getSources(apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newsSources::setValue, throwable -> {
                    //disposable.dispose();
                    Log.d(TAG, "sources: " + throwable.getMessage());
                }));
        return newsSources;
    }


}
