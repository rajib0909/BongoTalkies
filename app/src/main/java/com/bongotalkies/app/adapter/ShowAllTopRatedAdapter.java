package com.bongotalkies.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bongotalkies.app.R;
import com.bongotalkies.app.databinding.ItemAllTopRatedMovieBinding;
import com.bongotalkies.app.model.topRatedMovie.Result;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ShowAllTopRatedAdapter extends RecyclerView.Adapter<ShowAllTopRatedAdapter.ViewHolder> {
    private List<Result> allResultList;
    private Context context;

    public OnClickMovie onClickMovie;


    public void setOnClickMovie(OnClickMovie onClickMovie) {
        this.onClickMovie = onClickMovie;

    }


    public ShowAllTopRatedAdapter(List<Result> allResultList, Context context) {
        this.allResultList = allResultList;
        this.context = context;
    }


    public void clearAll() {
        this.allResultList.clear();
    }


    public void updateNotificationList(List<Result> allResultList) {
        this.allResultList.addAll(allResultList);
    }

    public interface OnClickMovie {
        void onClickMovie(int id);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAllTopRatedMovieBinding allTopRatedMovieBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_all_top_rated_movie, parent, false);

        return new ViewHolder(allTopRatedMovieBinding.getRoot(), allTopRatedMovieBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Result datum = allResultList.get(position);
        holder.bind(datum);
    }

    @Override
    public int getItemCount() {
        return allResultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAllTopRatedMovieBinding allTopRatedMovieBinding;

        public ViewHolder(@NonNull View itemView, ItemAllTopRatedMovieBinding allTopRatedMovieBinding) {
            super(itemView);
            this.allTopRatedMovieBinding = allTopRatedMovieBinding;
        }

        public void bind(Result datum) {

            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.bongo)
                    .error(R.drawable.bongo);

            Glide.with(context).load("https://image.tmdb.org/t/p/original" + datum.getPosterPath()).apply(options).into(allTopRatedMovieBinding.posterImage);

            allTopRatedMovieBinding.seeDetails.setOnClickListener(l -> onClickMovie.onClickMovie(datum.getId()));

            allTopRatedMovieBinding.executePendingBindings();

        }
    }
}
