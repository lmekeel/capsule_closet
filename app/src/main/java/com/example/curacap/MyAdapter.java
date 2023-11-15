package com.example.curacap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    ArrayList<PostData> feedData;
    Context context;
    public MyAdapter(ArrayList<PostData> feedData, HomeFragment homeFragment) {
        this.feedData = feedData;
        this.context = homeFragment.getActivity();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.feed_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PostData feedDataList = feedData.get(position);
        String imageUrl = feedDataList.getImage();
        String postID = feedDataList.getPostId();
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image) // Placeholder image
                .error(R.drawable.placeholder_image)// Error image
                .into(holder.feedImage);
        holder.imageTitle.setText(feedDataList.getTitle());
        holder.imageCreator.setText(feedDataList.getUsername());
        holder.likeCount.setText(String.valueOf(feedDataList.getLikeCount()));

        fetchLikedStatusForPosts();

        // Set the initial like button state
        updateLikeButtonState(holder.likeButton, feedDataList.isLiked());



        holder.likeButton.setOnClickListener(v -> {
            // Toggle the liked state
            boolean isLiked = !feedDataList.isLiked();

            // Update the like button state
            updateLikeButtonState(holder.likeButton, isLiked);


            // Update the like count UI
            int newLikeCount = isLiked ? feedDataList.getLikeCount() + 1 : feedDataList.getLikeCount() - 1;
            feedDataList.setLikeCount(newLikeCount);
            holder.likeCount.setText(String.valueOf(newLikeCount));

            // Update the liked state in the data model
            feedDataList.setLiked(isLiked);

            // Update the like count in the Firebase database
            updateLikeCountInDatabase(feedDataList, isLiked);
            updateLikedByUsersState(postID,isLiked);

        });

        holder.itemView.setOnClickListener(v -> {
            //Toast.makeText(context, "Take to new fragment or activity", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return feedData.size();
    }


    //added
    private void updateLikedByUsersState(String postId, boolean isLiked) {
        if(postId != null) {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();
            assert user != null;
            String userID = user.getUid();
            DatabaseReference likedByUsersRef = FirebaseDatabase.getInstance().getReference("posts").child(postId)
                    .child("likedByUsers")
                    .child(userID); // Reference to the current user's liked state
            if (isLiked) {
                // User liked the post, set the liked state to true
                likedByUsersRef.setValue(true);
            } else {
                // User unliked the post, remove the liked state
                likedByUsersRef.removeValue();
            }
        }else{
            Toast.makeText(context, "postID is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateLikeButtonState(ImageView likeButton, boolean isLiked) {
        if (isLiked) {
            // Change the vector image to the liked state (e.g., red)
            likeButton.setImageResource(R.drawable.baseline_favorite_liked);
            likeButton.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

        } else {
            // Change the vector image to the unliked state (e.g., gray or transparent)
            likeButton.setImageResource(R.drawable.baseline_favorite_border_24);
            likeButton.setColorFilter(null); // Remove the color filter
        }
    }


    private void updateLikeCountInDatabase(PostData postData, boolean isLiked) {
        String postID = postData.getPostId();
        if(postID != null){
            // Get a reference to the specific post in the Firebase database
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts").child(postID);

            if (isLiked) {
                // If liked, increment the like count
                databaseReference.child("likeCount").setValue(postData.getLikeCount());
            } else {
                // If unliked, decrement the like count
                databaseReference.child("likeCount").setValue(postData.getLikeCount());
            }

        }
    }

    private void fetchLikedStatusForPosts() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String currentUserID = user.getUid();

            // Loop through the feedData list and fetch liked status for each post
            for (PostData postData : feedData) {
                String postID = postData.getPostId();
                if (postID != null) {
                    DatabaseReference likedByUsersRef = FirebaseDatabase.getInstance()
                            .getReference("posts")
                            .child(postID)
                            .child("likedByUsers")
                            .child(currentUserID);

                    likedByUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean isLiked = dataSnapshot.exists() && dataSnapshot.getValue(Boolean.class) != null;

                            // Set the isLiked property in the PostData object
                            postData.setLiked(isLiked);

                            // Notify the adapter that the data has changed
                            notifyItemChanged(feedData.indexOf(postData));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle any errors
                        }
                    });
                }
            }
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView feedImage;
        TextView imageTitle;
        TextView imageCreator;
        TextView likeCount;
        ImageButton likeButton;
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            feedImage = itemView.findViewById(R.id.image);
            imageTitle = itemView.findViewById(R.id.image_title);
            imageCreator = itemView.findViewById(R.id.profile_name);
            likeCount = itemView.findViewById(R.id.like_count);
            likeButton = itemView.findViewById(R.id.like_button);
        }
    }
}

