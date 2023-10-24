package com.example.curacap;
//This activity allows the user to upload a post
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class CreatePostActivity extends AppCompatActivity {

    ArrayList<Uri> selectedImage = new ArrayList<>();
    ImageView uploadImage;
    EditText title, caption;

    Button postButton, saveDraft;

    String postTitle, postCaption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        uploadImage = findViewById(R.id.upload_image);
        title = findViewById(R.id.post_title);
        caption = findViewById(R.id.post_caption);
        postButton = findViewById(R.id.post_button);
        saveDraft = findViewById(R.id.save_draft_button);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Show the back button
            actionBar.setDisplayShowHomeEnabled(true); // Show the app icon in the ActionBar
        }

        imagePicker();

        //Capture title
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                postTitle = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //capture caption
        caption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                postCaption = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        postButton.setOnClickListener(v -> {
            addImageToStorage();
            Intent intent = new Intent(CreatePostActivity.this, MainActivity.class);
            startActivity(intent);
            //UPDATE CARRY DATA TO NEXT INTENT TO SHOW THE UPLOAD PROGRESS OF THE POST
        });

    }

    private void displayImage(ArrayList<Uri> arr){
        if(arr != null){
            Uri image = arr.get(0);
            uploadImage.setImageURI(image);
        }

    }
    private void imagePicker() {

        //open picker
        FilePickerBuilder.getInstance()
                .setActivityTitle("Select Image to Upload")
                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN, 3)
                .setSpan(FilePickerConst.SPAN_TYPE.DETAIL_SPAN,4)
                .setMaxCount(1)
                .setSelectedFiles(selectedImage)
                .setActivityTheme(R.style.CustomTheme)
                .pickPhoto(this);


    }

    //Gets image data selected in FilePicker for further actions
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {
                selectedImage = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
                //Display selected image in ImageView
                displayImage(selectedImage);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed(); // Handle the back button click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addPostToDatabase(String url){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String username = snapshot.child("username").getValue(String.class);

                if(username != null ){
                    PostData post = new PostData(postTitle, postCaption, "@"+username, user.getUid(), url, 0);

                    DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts")
                            .push(); // Push generates a unique key for each post

                    postsRef.setValue(post)
                            .addOnSuccessListener(aVoid -> {
                                // Image URL saved in the user's database successfully
                                Toast.makeText(CreatePostActivity.this, "Posted!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Handle the error
                                Toast.makeText(CreatePostActivity.this, "Couldn't post"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                     }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CreatePostActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void addImageToStorage(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Loop through the selected image URIs and upload each one
        for (Uri imageUri : selectedImage) {
            // Create a reference to the location in Firebase Storage where you want to store the image
            StorageReference imageRef = storageRef.child("posts/" + imageUri.getLastPathSegment());

            // Upload the image using putFile
            UploadTask uploadTask = imageRef.putFile(imageUri);
            Toast.makeText(CreatePostActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();
            // Monitor the upload process and handle success/failure
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Image uploaded successfully, you can get the download URL if needed
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    //store the URL in posts table
                    addPostToDatabase(downloadUrl);


                }).addOnFailureListener(e -> {
                    // Handle the error
                    Toast.makeText(CreatePostActivity.this, "", Toast.LENGTH_SHORT).show();
                });

            });

        }
    }
}