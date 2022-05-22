package com.bongotalkies.app.api;


import com.bongotalkies.app.model.movieDetails.MovieDetails;
import com.bongotalkies.app.model.topRatedMovie.TopRatedMovieResponse;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static NetworkService instance;

    private BongoTalkiesApi api = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(BongoTalkiesApi.class);


    private NetworkService() {
    }

    public static NetworkService getInstance() {
        if (instance == null) {
            instance = new NetworkService();
        }
        return instance;
    }


    public Single<TopRatedMovieResponse> topRatedMovieResponse(String key, String language, String page) {
        return api.topRatedMovieResponse(key, language, page);
    }

    public Single<MovieDetails> movieDetailsResponse(String id, String key, String language) {
        return api.movieDetailsResponse(id, key, language);
    }



}
