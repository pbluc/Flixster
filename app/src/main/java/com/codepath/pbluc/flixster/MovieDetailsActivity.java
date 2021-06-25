package com.codepath.pbluc.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String TAG = "MovieDetailsActivity";
    String videoId;

    ImageView ivPoster;
    RatingBar rbMovieRating;
    TextView tvTitle;
    TextView tvOverview;
    ImageView ivBackHome;
    ImageView ivPlayTrailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ivPoster = (ImageView) findViewById(R.id.ivPoster);
        rbMovieRating = (RatingBar) findViewById(R.id.rbMovieRating);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        ivBackHome = (ImageView) findViewById(R.id.ivBackHome);
        ivPlayTrailer = (ImageView) findViewById(R.id.ivPlayTrailer);

        // Get values from previous activity
        Glide.with(this.getApplicationContext()).load(getIntent().getStringExtra("getImgPath")).into(ivPoster);
        tvTitle.setText(getIntent().getStringExtra("getTitle"));
        tvOverview.setText(getIntent().getStringExtra("getOverview"));
        rbMovieRating.setRating((float) (getIntent().getDoubleExtra("getRating", 0.0) / 2));

        final String GET_MOVIE_VIDEOS = "https://api.themoviedb.org/3/movie/" + getIntent().getIntExtra("getMovieId", 0) +
                "/videos?api_key=b29e48cf3c2f6318e18711f5cc1cade8&language=en-US";


        ivBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MovieDetailsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        ivPlayTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i("Trailer click", "works");
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(GET_MOVIE_VIDEOS, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        JSONObject jsonObject = json.jsonObject;
                        try {
                            JSONArray results = jsonObject.getJSONArray("results");
                            videoId = results.getJSONObject(0).getString("key");

                            Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
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