package com.example.curacap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.GridViewHolder> {


    private ArrayList<String> closetItemList;

    public ClosetAdapter(ArrayList<String> closetItemList){

        this.closetItemList = closetItemList;
    }

    public class GridViewHolder extends RecyclerView.ViewHolder{
        private ImageView closetItem;
        public GridViewHolder(@NonNull View itemView) {
            super(itemView);
            closetItem = itemView.findViewById(R.id.image_item);
        }
    }

    @NonNull
    @Override
    public ClosetAdapter.GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.closet_item, parent, false);
        return new GridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClosetAdapter.GridViewHolder holder, int position) {
        String imageURL = closetItemList.get(position);
        Picasso.get().load(imageURL).into(holder.closetItem);

    }

    @Override
    public int getItemCount() {
        return closetItemList.size();
    }
}

