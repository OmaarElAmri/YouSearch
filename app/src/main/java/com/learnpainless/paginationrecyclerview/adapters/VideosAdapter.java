package com.learnpainless.paginationrecyclerview.adapters;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.learnpainless.paginationrecyclerview.MainActivity;
import com.learnpainless.paginationrecyclerview.R;
import com.learnpainless.paginationrecyclerview.models.YoutubeResponse;
import com.learnpainless.paginationrecyclerview.videoplayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by OmarElAmri on 25/01/18.
 * Adapter implementation for {@link RecyclerView}
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {
    private List<YoutubeResponse.Item> items = new ArrayList<>();




    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_item_video, parent, false);
        return new VideoViewHolder(view);
    }


    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        YoutubeResponse.Item.Snippet snippet = items.get(position).getSnippet();


        final String url = snippet.getThumbnails().getDefault().getUrl();
        Glide.with(holder.itemView.getContext()).load(url).crossFade().centerCrop().into(holder.logo);

        holder.title.setText(snippet.getTitle());
        holder.description.setText(snippet.getDescription());
        holder.date.setText(snippet.getPublishedAt());
        final String url2 = items.get(position).getSnippet().getChannelId();
       final Context context = holder.itemView.getContext();
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent (context,videoplayer.class);
                intent.putExtra("url",url2);
                context.startActivity(intent);

                //Log.d(url2,"hehe");
            }
        });



    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addAll(Collection<YoutubeResponse.Item> items) {
        int currentItemCount = this.items.size();
        this.items.addAll(items);
        notifyItemRangeInserted(currentItemCount, items.size());
    }

    public void replaceWith(Collection<YoutubeResponse.Item> items) {
        if (items != null) {
            int oldCount = this.items.size();
            int newCount = items.size();
            int delCount = oldCount - newCount;
            this.items.clear();
            this.items.addAll(items);
            if (delCount > 0) {
                notifyItemRangeChanged(0, newCount);
                notifyItemRangeRemoved(newCount, delCount);
            } else if (delCount < 0) {
                notifyItemRangeChanged(0, oldCount);
                notifyItemRangeInserted(oldCount, -delCount);
            } else {
                notifyItemRangeChanged(0, newCount);
            }
        }

    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView logo;
        TextView title, description, date;
        public LinearLayout linearLayout;

        VideoViewHolder(View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logo);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);
            linearLayout = itemView.findViewById(R.id.LinearLayout);
        }
    }
}
