package com.learnpainless.paginationrecyclerview;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.learnpainless.paginationrecyclerview.adapters.VideosAdapter;
import com.learnpainless.paginationrecyclerview.api.APIService;
import com.learnpainless.paginationrecyclerview.api.YoutubeApi;
import com.learnpainless.paginationrecyclerview.models.YoutubeResponse;
import com.learnpainless.paginationrecyclerview.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private VideosAdapter adapter;
    private String lastSearched = "", lastToken = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RecyclerView recyclerView = findViewById(R.id.video_list);
        Button more = findViewById(R.id.more);
        adapter = new VideosAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //load more data
                TextView txt = findViewById(R.id.editText) ;
                String x = txt.getText().toString();
                search(x, true);

            }
        });


    }



    /**
     * call this method to get response from youtube API.
     *
     * @param query String value to search on google, Empty string means get all videos.
     * @param more  if you want to load next page then pass true, this means add new items at bottom of RecyclerView.
     */
    private void search(String query, final boolean more) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, "Loading ...", true, false);
        String searchType = "video";
        if (query != null) {
            if (query.startsWith("playlist ")) {
                searchType = "playlist";
                query = query.substring(9);
            } else if (query.startsWith("channel ")) {
                searchType = "channel";
                query = query.substring(8);
            }else{
                searchType = "video";
            }
        }
        if (!more) {
            lastSearched = "" ;
            lastToken = "";
        }

        Call<YoutubeResponse> youtubeResponseCall = APIService.youtubeApi.searchVideo(query, searchType, Constants.YOUTUBE_API_KEY, "snippet,id", "10", lastToken);
        youtubeResponseCall.enqueue(new Callback<YoutubeResponse>() {
            @Override
            public void onResponse(@NonNull Call<YoutubeResponse> call, @NonNull Response<YoutubeResponse> response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                YoutubeResponse body = response.body();
                if (body != null) {
                    List<YoutubeResponse.Item> items = body.getItems();
                    lastToken = body.getNextPageToken();
                    if (more) {
                        adapter.addAll(items);
                    } else {
                        adapter.replaceWith(items);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<YoutubeResponse> call, @NonNull Throwable t) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Log.e(TAG, "onFailure: ", t);
            }
        });

    }
}
