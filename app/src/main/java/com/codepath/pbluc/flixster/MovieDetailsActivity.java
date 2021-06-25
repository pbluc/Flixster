package com.codepath.pbluc.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MovieDetailsActivity extends AppCompatActivity {

    ImageView ivPoster;
    RatingBar rbMovieRating;
    TextView tvTitle;
    TextView tvOverview;
    ImageView ivBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ivPoster = (ImageView) findViewById(R.id.ivPoster);
        rbMovieRating = (RatingBar) findViewById(R.id.rbMovieRating);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        ivBackHome = (ImageView) findViewById(R.id.ivBackHome);

        // Get values from previous activity
        Glide.with(this.getApplicationContext()).load(getIntent().getStringExtra("getImgPath")).into(ivPoster);
        tvTitle.setText(getIntent().getStringExtra("getTitle"));
        tvOverview.setText(getIntent().getStringExtra("getOverview"));
        rbMovieRating.setRating((float) (getIntent().getDoubleExtra("getRating", 0.0) / 2));


        ivBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MovieDetailsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}