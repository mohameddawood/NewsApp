package com.dawood.newsapp.network;


import com.dawood.newsapp.models.NewsResponseModel;
import com.dawood.newsapp.models.SourcesModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("sources") Observable<SourcesModel> getSources(
            @Query("apiKey") String apiKey
    );

    @GET("top-headlines") Observable<NewsResponseModel> getFilterdNews(
            @Query("apiKey") String apiKey,
            @Query("country") String country,
            @Query("sources") String sources,
            @Query("page") String page
    );


    @GET("everything") Observable<NewsResponseModel> getAllNews(
            @Query("apiKey") String apiKey,
            @Query("q") String topic,
            @Query("page") String page
    );

}
