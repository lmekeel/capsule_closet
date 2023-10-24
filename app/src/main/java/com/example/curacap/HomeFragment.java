package com.example.curacap;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    //call post items from database... there should be a following and a for you (explore) type page.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //set variables and components
        recyclerView = view.findViewById(R.id.home_feed);
        ArrayList<PostData> feedData = new ArrayList<>();

        //layout manager
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        //set adapter
        MyAdapter myAdapter = new MyAdapter(feedData, this);
        recyclerView.setAdapter(myAdapter);


        //get firebase instances
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postRef;
        postRef = database.getReference("posts");


        postRef.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Iterate through post IDs
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String image = dataSnapshot.child("image").getValue(String.class);
                    String title = dataSnapshot.child("title").getValue(String.class);
                    String caption = dataSnapshot.child("caption").getValue(String.class);
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String userID = dataSnapshot.child("userID").getValue(String.class);
                    int likeCount = dataSnapshot.child("likeCount").getValue(Integer.class);


                    PostData post = new PostData();
                    post.setTitle(title);
                    post.setImage(image);
                    post.setLikeCount(likeCount);
                    post.setUserID(userID);
                    post.setUsername(username);
                    post.setCaption(caption);
                    //Add Url to images array
                    feedData.add(post);

                }

                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //handle error
            }
        });

        return view;
    }
}