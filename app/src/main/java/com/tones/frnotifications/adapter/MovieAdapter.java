/**
 * MovieAdapter to list the movies
 */
package com.tones.frnotifications.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tones.frnotifications.R;
import com.tones.frnotifications.database.RoomModelData;

import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private Context mContext;
    private List<RoomModelData> mChannelList;

    public MovieAdapter(Context mContext, List<RoomModelData> mChannelList) {
        this.mContext = mContext;
        this.mChannelList = mChannelList;
    }

    @Override
    public MovieAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, null, false);
        return new MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.MyViewHolder holder, int position) {
        RoomModelData currentchannel = mChannelList.get(position);
        holder.title.setText(currentchannel.getTitle());
        holder.rating.setText(currentchannel.getDescription());
        holder.year.setText(currentchannel.getDatetime());

        Glide.with(mContext)
                .load(currentchannel.getThumbnailurl())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mChannelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView title, rating, year, genre;


        public MyViewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.ivThumbnail);
            title = itemView.findViewById(R.id.tvTitle);
            rating = itemView.findViewById(R.id.tvDescription);
            genre = itemView.findViewById(R.id.tvGenre);
            year = itemView.findViewById(R.id.tvReleaseYear);
        }
    }
}
