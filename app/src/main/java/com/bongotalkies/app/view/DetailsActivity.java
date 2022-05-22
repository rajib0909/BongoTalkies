package com.bongotalkies.app.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.bongotalkies.app.R;
import com.bongotalkies.app.databinding.ActivityDetailsBinding;
import com.bongotalkies.app.model.movieDetails.MovieDetails;
import com.bongotalkies.app.viewModel.DataViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DetailsActivity extends AppCompatActivity {

    ActivityDetailsBinding detailsBinding;
    private DataViewModel viewModel;
    private static String token = "c37d3b40004717511adb2c1fbb15eda4";
    private static String language = "en-US";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        viewModel = ViewModelProviders.of(this).get(DataViewModel.class);

        Intent intent = getIntent();
        int movieId = intent.getIntExtra("movieId", 0);
        setTitle(getString(R.string.details));


        viewModel.getMovieDetailsResponse(String.valueOf(movieId), token, language);
        observerMovieDetailsViewModel();


    }

    private void observerMovieDetailsViewModel() {

        viewModel.movieDetailsMutableLiveData.observe(
                this,
                movieDetails -> {
                    viewModel.movieDetailsMutableLiveData = new MutableLiveData<>();
                    loadData(movieDetails);
                }
        );
        viewModel.movieDetailsResponseError.observe(
                this, isError -> {
                    if (isError != null) {
                        if (isError)
                            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        viewModel.movieDetailsResponseError = new MutableLiveData<>();
                    }

                }
        );

        viewModel.loading.observe(
                this, isLoading -> {
                    if (!isLoading)
                        detailsBinding.loading.setVisibility(View.GONE);
                    viewModel.loading = new MutableLiveData<>();
                }
        );
    }

    private void loadData(MovieDetails movieDetails) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.bongo)
                .error(R.drawable.bongo);

        Glide.with(this).load("https://image.tmdb.org/t/p/original" + movieDetails.getBackdropPath()).apply(options).into(detailsBinding.posterImage);
        String[] release = movieDetails.getReleaseDate().split("-");
        detailsBinding.movieName.setText(movieDetails.getTitle() + " (" + release[0] + ")");
        String movieType = "";
        if (movieDetails.getGenres().size() != 0) {
            for (int i = 0; i < movieDetails.getGenres().size(); i++) {
                movieType = movieType + movieDetails.getGenres().get(i).getName();
                if (i != movieDetails.getGenres().size() - 1)
                    movieType = movieType + ", ";
            }
        }
        detailsBinding.movieType.setText(movieType);

        detailsBinding.movieYear.setText(movieDetails.getReleaseDate());
        detailsBinding.movieDuration.setText(String.valueOf(movieDetails.getRuntime() / 60) + "h " + String.valueOf(movieDetails.getRuntime() % 60) + "m");
        detailsBinding.movieTagline.setText(movieDetails.getTagline());
        detailsBinding.movieOverview.setText(movieDetails.getOverview());
        detailsBinding.movieRating.setText(String.valueOf(movieDetails.getVoteAverage()) + " (" + String.valueOf(movieDetails.getVoteCount()) + ")");


    }
}