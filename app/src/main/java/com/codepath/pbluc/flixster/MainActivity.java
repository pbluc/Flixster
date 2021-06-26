package com.codepath.pbluc.flixster;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.pbluc.flixster.adapters.MovieAdapter;
import com.codepath.pbluc.flixster.models.Movie;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    public final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=b29e48cf3c2f6318e18711f5cc1cade8";
    public static final String TAG = "MainActivity";

    RecyclerView rvMovies;
    ImageView ivTopMovie;
    ImageView ivPlayTrailer;
    ImageView ivMovieInfo;

    List<Movie> movies;
    Movie topMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>();

        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        ivTopMovie = (ImageView) findViewById(R.id.ivTopMovie);
        ivPlayTrailer = (ImageView) findViewById(R.id.ivPlayTrailer);
        ivMovieInfo = (ImageView) findViewById(R.id.ivMovieInfo);

        // Create the adapter
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);

        // Set a Layout Manager on the recycler view
        rvMovies.setLayoutManager(new GridLayoutManager(this, 3));

        // Create an instance of our async HTTP client
        AsyncHttpClient client = new AsyncHttpClient();
        // Making a get request on that URL and passing a callback here
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                //Log.d(TAG, "onSuccess");
                // Returns to us the actual JSON object
                JSONObject jsonObject = json.jsonObject;
                try {
                    // Within that JSON object, we get a JSON array
                    // As we're trying to parse our JSON, this key may not exist or there might be some other isse as we
                    // parse the data in the JSON
                    JSONArray results = jsonObject.getJSONArray("results");
                    //Log.i(TAG, "Results: " + results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    topMovie = movies.get(0);
                    movieAdapter.notifyDataSetChanged();
                    //Log.i(TAG, "Movies: " + movies.toString());
                } catch (JSONException e) {
                    Log.e(TAG, "Hit json exception", e);
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        ivMovieInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MovieDetailsActivity.class);
                i.putExtra("getTitle", topMovie.getTitle());
                i.putExtra("getOverview", topMovie.getOverview());
                i.putExtra("getRating", topMovie.getRating());

                if(getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    i.putExtra("getImgPath", topMovie.getBackdropPath());
                } else {
                    i.putExtra("getImgPath", topMovie.getPosterPath());
                }
                startActivity(i);
            }
        });

        ivPlayTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String GET_MOVIE_VIDEOS = "https://api.themoviedb.org/3/movie/" + topMovie.getId() +
                        "/videos?api_key=b29e48cf3c2f6318e18711f5cc1cade8&language=en-US";
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(GET_MOVIE_VIDEOS, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONArray results = jsonObject.getJSONArray("results");
                            String videoId = results.getJSONObject(0).getString("key");

                            Intent intent = new Intent(MainActivity.this, MovieTrailerActivity.class);
                            if(!videoId.equals(null) || !videoId.isEmpty() || !videoId.equals("")) {
                                intent.putExtra("getVideoId", videoId);
                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            Log.e(TAG, "Hit json exception", e);
                        }
                    }

                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                        Log.d(TAG, "onFailure");
                    }
                });
            }
        });

    }

}