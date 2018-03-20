package com.learnpainless.paginationrecyclerview;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import static android.content.ContentValues.TAG;

public class videoplayer extends YouTubeBaseActivity {

    private YouTubePlayerView youTubePlayerView ;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    Button b ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoplayer);
        youTubePlayerView = findViewById(R.id.youtube_view);




        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                String url = getIntent().getStringExtra("url");
                Log.d(url, "url");

                youTubePlayer.loadVideo("O17OWyx08Cg");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        b = findViewById(R.id.playbutton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                youTubePlayerView.initialize("AIzaSyCjg1i4SgosNsFdODi-IVJmJ5DGuP0KS_k", onInitializedListener);
            }
        });
    }










}

