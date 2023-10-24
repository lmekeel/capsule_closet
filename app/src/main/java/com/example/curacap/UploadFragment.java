package com.example.curacap;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class UploadFragment extends Fragment implements EasyPermissions.PermissionCallbacks{

    ArrayList<Uri> imagesSelected = new ArrayList<>();
    Button createPost, uploadItem, saveOutfit;

    int buttonSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        //Assign variables
        createPost = view.findViewById(R.id.create_post);
        uploadItem = view.findViewById(R.id.upload_to_closet);
        saveOutfit = view.findViewById(R.id.save_to_outfits);

        //Define camera $ storage permissions
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

        createPost.setOnClickListener(v ->{
            if(EasyPermissions.hasPermissions(requireActivity(), permissions)){
                startActivity(new Intent(getActivity(), CreatePostActivity.class));
            }else{
                //permission not granted, request it
                EasyPermissions.requestPermissions(requireActivity(),
                        "App needs access to your camera & storage",
                        123,
                        permissions);
            }
        });

        uploadItem.setOnClickListener(v -> {
            if(EasyPermissions.hasPermissions(requireActivity(), permissions)){
                buttonSelected = 2;
                imagePicker();
            }else{
                //permission not granted, request it
                EasyPermissions.requestPermissions(requireActivity(),
                        "App needs access to your camera & storage",
                        123,
                        permissions);
            }

        });

        saveOutfit.setOnClickListener(v -> {
            if(EasyPermissions.hasPermissions(requireActivity(), permissions)){
                buttonSelected = 3;
                imagePicker();
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Handles request result
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, getActivity());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){

            if(requestCode == FilePickerConst.REQUEST_CODE_PHOTO){
                imagesSelected = data.getParcelableArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();

                // Loop through the selected image URIs and upload each one
                for (Uri imageUri : imagesSelected) {
                    // Create a reference to the location in Firebase Storage where you want to store the image
                    StorageReference imageRef = storageRef.child("images/" + imageUri.getLastPathSegment());

                    // Upload the image using putFile
                    UploadTask uploadTask = imageRef.putFile(imageUri);
                    Toast.makeText(getActivity(), "Image uploaded", Toast.LENGTH_SHORT).show();
                    // Monitor the upload process and handle success/failure
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        // Image uploaded successfully, you can get the download URL if needed
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            //store the URL in current user's images database
                            switch (buttonSelected){
                                case 2:
                                    uploadToCloset(downloadUrl);
                                case 3:
                                    saveToOutfits(downloadUrl);
                                default:
                                    break;
                            }


                            // Now, store the download URL in the user's database

                    }).addOnFailureListener(e -> {
                        // Handle the error
                        Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                    });

                    });

                }

            }
        }
    }

    private void saveToOutfits(String downloadUrl){
        DatabaseReference userImagesRef;
        FirebaseUser UserId = FirebaseAuth.getInstance().getCurrentUser();

        assert UserId != null;
        userImagesRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(UserId.getUid())
                .child("outfitUrls")
                .push(); // Push generates a unique key for each image

        userImagesRef.setValue(downloadUrl)
                .addOnSuccessListener(aVoid -> {
                    // Image URL saved in the user's database successfully
                    Toast.makeText(getActivity(), "Added to Outfits!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Toast.makeText(getActivity(), "Couldn't add to closet"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void uploadToCloset(String downloadUrl){
        DatabaseReference userImagesRef;
        FirebaseUser UserId = FirebaseAuth.getInstance().getCurrentUser();

        assert UserId != null;
        userImagesRef = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(UserId.getUid())
                .child("closetItemUrls")
                .push(); // Push generates a unique key for each image

        userImagesRef.setValue(downloadUrl)
                .addOnSuccessListener(aVoid -> {
                    // Image URL saved in the user's database successfully
                    Toast.makeText(getActivity(), "Added to Closet!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Toast.makeText(getActivity(), "Couldn't add to closet"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void imagePicker() {

        //open picker
        FilePickerBuilder.getInstance()
                .setActivityTitle("Select Images to Upload")
                .setSpan(FilePickerConst.SPAN_TYPE.FOLDER_SPAN, 3)
                .setSpan(FilePickerConst.SPAN_TYPE.DETAIL_SPAN,4)
                .setMaxCount(4)
                .setSelectedFiles(imagesSelected)
                .setActivityTheme(R.style.CustomTheme)
                .pickPhoto(this);


    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == 123 && perms.size() == 2){
            imagePicker();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)){
            new AppSettingsDialog.Builder(requireActivity()).build().show();
        }else{
            //when permissions deny once
            //display toast
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }








    /* ----------------------------NOT USING RIGHT NOW----------------------------*/

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public UploadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadFragment newInstance(String param1, String param2) {
        UploadFragment fragment = new UploadFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


}