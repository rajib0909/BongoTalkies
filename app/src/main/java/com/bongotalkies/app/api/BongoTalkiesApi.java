package com.bongotalkies.app.api;
import com.bongotalkies.app.model.movieDetails.MovieDetails;
import com.bongotalkies.app.model.topRatedMovie.TopRatedMovieResponse;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BongoTalkiesApi {

    @GET("movie/top_rated")
    Single<TopRatedMovieResponse> topRatedMovieResponse(
            @Query("api_key") String key,
            @Query("language") String language,
            @Query("page") String page
    );

    @GET("movie/{id}")
    Single<MovieDetails> movieDetailsResponse(
            @Path("id") String id,
            @Query("api_key") String key,
            @Query("language") String language
    );


}
