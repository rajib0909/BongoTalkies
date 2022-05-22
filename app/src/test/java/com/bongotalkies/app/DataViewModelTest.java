package com.bongotalkies.app;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.bongotalkies.app.api.NetworkService;
import com.bongotalkies.app.model.movieDetails.MovieDetails;
import com.bongotalkies.app.model.topRatedMovie.TopRatedMovieResponse;
import com.bongotalkies.app.viewModel.DataViewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.internal.schedulers.ExecutorScheduler;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;


public class DataViewModelTest {
    private static String token = "c37d3b40004717511adb2c1fbb15eda4";
    private static String language = "en-US";
    private static String page = "1";
    private static String movieId = "19404";

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    NetworkService networkService;

    @InjectMocks
    DataViewModel dataViewModel = new DataViewModel();

    private Single<TopRatedMovieResponse> testTopRatedMovieResponse;
    private Single<MovieDetails> testMovieDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getTopRatedMovieSuccess() {
        TopRatedMovieResponse topRatedMovieResponse = new TopRatedMovieResponse();
        testTopRatedMovieResponse = Single.just(topRatedMovieResponse);
        Mockito.when(networkService.topRatedMovieResponse(token, language, page)).thenReturn(testTopRatedMovieResponse);
        dataViewModel.getTopRatedMovieResponse(token, language, page);

        Assert.assertEquals(1, dataViewModel.topRatedMovie.getValue().getPage().intValue());
        Assert.assertEquals(false, dataViewModel.topRatedMovieResponseError.getValue());
    }

    @Test
    public void getMovieDetailsResponse() {
        MovieDetails movieDetails = new MovieDetails();
        testMovieDetails = Single.just(movieDetails);
        Mockito.when(networkService.movieDetailsResponse(token, language, page)).thenReturn(testMovieDetails);
        dataViewModel.getMovieDetailsResponse(movieId, token, language);

        Assert.assertEquals(false, dataViewModel.movieDetailsMutableLiveData.getValue().getAdult());
        Assert.assertEquals(false, dataViewModel.movieDetailsResponseError.getValue());
    }


    @Before
    public void setupRxSchedulers() {
        Scheduler immediate = new Scheduler() {

            @Override
            public @NonNull Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(runnable -> runnable.run(), true, true);
            }
        };

        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);

        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);

    }
}
