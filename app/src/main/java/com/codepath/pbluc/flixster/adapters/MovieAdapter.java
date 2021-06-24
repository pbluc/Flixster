package com.codepath.pbluc.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.pbluc.flixster.R;
import com.codepath.pbluc.flixster.models.Movie;
import com.codepath.pbluc.flixster.MovieDetailsActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the movie at the passed in position
        Movie movie = movies.get(position);
        //Bind the movie data into the view holder
        holder.bind(movie);

        final String getTitle = movie.getTitle();
        final String getOverview = movie.getOverview();
        final double getRating = movie.getRating();
        final String getImgPath;

        // get either backdropPath or posterPath
        if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getImgPath = movie.getPosterPath();
        } else {
            getImgPath = movie.getBackdropPath();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MovieDetailsActivity.class);
                i.putExtra("getTitle", getTitle);
                i.putExtra("getOverview", getOverview);
                i.putExtra("getRating", getRating);
                i.putExtra("getImgPath", getImgPath);
                context.startActivity(i);
            }
        });
    }

    // Return the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvRating;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            ivPoster = (ImageView) itemView.findViewById(R.id.ivPoster);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvRating.setText(String.valueOf(movie.getRating()));

            int placeholder;
            String imageUrl;
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                placeholder = R.drawable.flicks_movie_placeholder;
                imageUrl = movie.getPosterPath();
            } else {
                placeholder = R.drawable.flicks_backdrop_placeholder;
                imageUrl = movie.getBackdropPath();
            }

            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(placeholder)
                    .into(ivPoster);
        }
    }
}
