package com.example.curacap;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ViewHolder> {

    //Initialize variables
    ArrayList<Uri> arrayList;

    //create constructor
    public ImagePickerAdapter(ArrayList<Uri> arrayList){
        this.arrayList = arrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        // ViewHolder constructor and member variables
        public ViewHolder(View itemView) {
            super(itemView);
            // Initialize view references here
            imageView = itemView.findViewById(R.id.iv_image);
        }
    }

    @NonNull
    @Override
    public ImagePickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main,parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagePickerAdapter.ViewHolder holder, int position) {
            //print image using uri
        holder.imageView.setImageURI(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
