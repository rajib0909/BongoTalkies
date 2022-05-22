package com.bongotalkies.app.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.bongotalkies.app.R;
import com.bongotalkies.app.adapter.ShowAllTopRatedAdapter;
import com.bongotalkies.app.databinding.ActivityMainBinding;
import com.bongotalkies.app.viewModel.DataViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    private DataViewModel viewModel;
    private static String token = "c37d3b40004717511adb2c1fbb15eda4";
    private static String language = "en-US";
    private ShowAllTopRatedAdapter allTopRatedAdapter;
    private int page, totalPage;
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(DataViewModel.class);
        page = 1;
        totalPage = 1;
        allTopRatedAdapter = new ShowAllTopRatedAdapter(new ArrayList<>(), this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mainBinding.movieList.setLayoutManager(layoutManager);
        mainBinding.movieList.setAdapter(allTopRatedAdapter);

        mainBinding.loading.setVisibility(View.VISIBLE);
        viewModel.getTopRatedMovieResponse(token, language, String.valueOf(page));
        observerTopRatedMovieViewModel();


        allTopRatedAdapter.setOnClickMovie(id -> {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra("movieId", id);
            startActivity(intent);
        });


        mainBinding.movieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = layoutManager.getChildCount();
                totalItems = layoutManager.getItemCount();
                scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    loadData();
                }

            }
        });


    }

    private void loadData() {
        if (page != totalPage){
            mainBinding.loading.setVisibility(View.VISIBLE);
            page++;
            viewModel.getTopRatedMovieResponse(token, language, String.valueOf(page));
            observerTopRatedMovieViewModel();
        }
    }

    private void observerTopRatedMovieViewModel() {

        viewModel.topRatedMovie.observe(
                this,
                topRatedMovieResponse -> {
                    totalPage = topRatedMovieResponse.getTotalPages();
                    if (page == 1)
                        allTopRatedAdapter.clearAll();
                    allTopRatedAdapter.updateNotificationList(topRatedMovieResponse.getResults());
                    allTopRatedAdapter.notifyDataSetChanged();
                    viewModel.topRatedMovie = new MutableLiveData<>();
                }
        );
        viewModel.topRatedMovieResponseError.observe(
                this, isError -> {
                    if (isError != null) {
                        if (isError)
                            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                        viewModel.topRatedMovieResponseError = new MutableLiveData<>();
                    }

                }
        );

        viewModel.loading.observe(
                this, isloading -> {
                    if (!isloading)
                        mainBinding.loading.setVisibility(View.GONE);
                    viewModel.loading = new MutableLiveData<>();
                }
        );
    }
}