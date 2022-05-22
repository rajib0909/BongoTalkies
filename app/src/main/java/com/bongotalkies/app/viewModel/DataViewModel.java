package com.bongotalkies.app.viewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.bongotalkies.app.api.NetworkService;
import com.bongotalkies.app.model.movieDetails.MovieDetails;
import com.bongotalkies.app.model.topRatedMovie.TopRatedMovieResponse;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DataViewModel extends ViewModel {

    /**
     * only exposes immutable Auth LiveData objects to observe users
     */
    public MutableLiveData<TopRatedMovieResponse> topRatedMovie = new MutableLiveData<>();
    public MutableLiveData<MovieDetails> movieDetailsMutableLiveData = new MutableLiveData<>();

    /**
     * only exposes immutable Boolean LiveData objects to observe usersLoadError
     */
    public MutableLiveData<Boolean> topRatedMovieResponseError = new MutableLiveData<>();
    public MutableLiveData<Boolean> movieDetailsResponseError = new MutableLiveData<>();


    /**
     * only exposes immutable Boolen LiveData objects to observe loading
     */
    public MutableLiveData<Boolean> loading = new MutableLiveData<>();

    /**
     * Call network service
     */
    private final NetworkService networkService = NetworkService.getInstance();

    private final CompositeDisposable disposable = new CompositeDisposable();


    /**
     *  method Top rated movie API response
     */
    public void getTopRatedMovieResponse(String token, String language, String page) {
        disposable.add(
                networkService.topRatedMovieResponse(token, language, page)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<TopRatedMovieResponse>() {
                            @Override
                            public void onSuccess(@NonNull TopRatedMovieResponse topRatedMovieResponse) {
                                topRatedMovie.setValue(topRatedMovieResponse);
                                topRatedMovieResponseError.setValue(false);
                                loading.setValue(false);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                topRatedMovieResponseError.setValue(true);
                                loading.setValue(false);
                            }
                        })
        );
    }


    /**
     *  method movie details API response
     */
    public void getMovieDetailsResponse(String id, String token, String language) {
        disposable.add(
                networkService.movieDetailsResponse(id, token, language)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<MovieDetails>() {
                            @Override
                            public void onSuccess(@NonNull MovieDetails movieDetails) {
                                movieDetailsMutableLiveData.setValue(movieDetails);
                                movieDetailsResponseError.setValue(false);
                                loading.setValue(false);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                movieDetailsResponseError.setValue(true);
                                loading.setValue(false);
                            }
                        })
        );
    }


    /**
     * Using clear CompositeDisposable, but can accept new disposable
     */
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
