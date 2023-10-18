package com.example.curacap;

import android.content.res.ColorStateList;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;

public class ClosetFragment extends Fragment {

    Button allItems, outfits, createOutfit;
    RecyclerView recyclerView;
    ArrayList<String> imagesArray = new ArrayList<>();
    StorageReference storageReference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_closet, container, false);

        allItems = view.findViewById(R.id.all_items);
        outfits = view.findViewById(R.id.outfits);
        recyclerView = view.findViewById(R.id.closet_recycler);
        createOutfit = view.findViewById(R.id.create_collection);

        storageReference = FirebaseStorage.getInstance().getReference("images/");

        allItems.setSelected(true);
        outfits.setSelected(false);
        displayClosetItems();
        recyclerView = view.findViewById(R.id.closet_recycler);



        allItems.setOnClickListener(v -> {
            // Button 1 is clicked
            allItems.setSelected(true);
            outfits.setSelected(false);

            //show create outfit button
            createOutfit.setVisibility(View.VISIBLE);

            // Update button colors based on selection state
            allItems.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.select_button)));
            outfits.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));

            // Handle other actions when button 1 is selected
            displayClosetItems();


        });

        outfits.setOnClickListener(v -> {
            // Button 2 is clicked
            allItems.setSelected(false);
            outfits.setSelected(true);

            //hide create outfit button
            createOutfit.setVisibility(View.GONE);
            // Update button colors based on selection state
            allItems.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            outfits.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.select_button)));

            // Handle other actions when button 2 is selected
            displayClosetItems();


        });



        return view;
    }


    private void deleteItems(){

    }


    private void displayClosetItems() {
        imagesArray.clear();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        ClosetAdapter adapter = new ClosetAdapter(imagesArray);
        recyclerView.setAdapter(adapter);

        //Get images from user's closet item URLs in database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            String userId = currentUser.getUid();
            userRef = database.getReference("users").child(userId);

            DatabaseReference closetItemURLs;
            if(allItems.isSelected()){
                closetItemURLs = userRef.child("closetItemUrls");
            }else{
                closetItemURLs = userRef.child("outfitUrls");
            }



           closetItemURLs.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Iterate through photo URLs
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String photoUrl = dataSnapshot.getValue(String.class);
                        //Add Url to images array
                        imagesArray.add(photoUrl);

                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //handle error
                }
            });
        }


    }

    /*-------------------NOT USING RIGHT NOW--------------------*/


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClosetFragment() {
        // Required empty public constructor
    }

    public static ClosetFragment newInstance(String param1, String param2) {
        ClosetFragment fragment = new ClosetFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


}