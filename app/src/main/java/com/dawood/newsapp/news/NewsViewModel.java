package com.dawood.newsapp.news;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dawood.newsapp.Utils;
import com.dawood.newsapp.models.NewsResponseModel;
import com.dawood.newsapp.models.SourcesModel;


public class NewsViewModel extends ViewModel {

    private MutableLiveData<NewsResponseModel> mutableLiveData;
    private MutableLiveData<NewsResponseModel> filterdNewsLiveData;
    private MutableLiveData<SourcesModel> sourcesLiveData;
    private NewsRepo newsRepository = NewsRepo.getInstance();
    String page;

    public NewsViewModel() {
    }

    void init(String page, String topic) {
        mutableLiveData = newsRepository.getNews(Utils.ApiKey, topic, page + "");
        sourcesLiveData = newsRepository.getNewsSources(Utils.ApiKey);
    }


    LiveData<NewsResponseModel> getNewsRepository() {
        return mutableLiveData;
    }

    LiveData<NewsResponseModel> getFilterdNewsRepository(String country, String source, String page) {
        filterdNewsLiveData = newsRepository.getFilterdNews(Utils.ApiKey, country, source, page);
        return filterdNewsLiveData;
    }


    LiveData<SourcesModel> getSources() {
        return sourcesLiveData;
    }
}
