package com.example.curacap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    PostData[] feedData;
    Context context;
    public MyAdapter(PostData[] feedData, HomeFragment homeFragment) {
        this.feedData = feedData;
        this.context = homeFragment.getActivity();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.feed_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PostData feedDataList = feedData[position];
        holder.feedImage.setImageResource(feedDataList.getImage());
        holder.imageTitle.setText(feedDataList.getTitle());
        holder.imageCreator.setText(feedDataList.getUsername());
        holder.likeCount.setText(String.valueOf(feedDataList.getLikeCount()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "Take to new fragment or activity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView feedImage;
        TextView imageTitle;
        TextView imageCreator;
        TextView likeCount;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            feedImage = itemView.findViewById(R.id.image);
            imageTitle = itemView.findViewById(R.id.image_title);
            imageCreator = itemView.findViewById(R.id.profile_name);
            likeCount = itemView.findViewById(R.id.like_count);
        }
    }
}
