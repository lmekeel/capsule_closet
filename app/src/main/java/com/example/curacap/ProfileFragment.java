package com.example.curacap;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView name, username, bio;
    private Button followers, following, totalLikes, editProfile;
    private ImageButton feed, saves, myLikes;
    private ImageView profilePic;
    private FirebaseAuth mAuth;
    private FirebaseDatabase userDatabase;
    private DatabaseReference reference;

    //UPDATE: The following/follower count needs to be double? some how truncate large numbers



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setHasOptionsMenu(true);

        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.usernameTextView);
        followers = view.findViewById(R.id.follower_count);
        following = view.findViewById(R.id.following_count);
        totalLikes = view.findViewById(R.id.likes_count);
        editProfile = view.findViewById(R.id.edit_profile_button);

        editProfile.setOnClickListener(v ->{
            Intent intent = new Intent(getActivity(), EditProfile.class);
            startActivity(intent);
        });

        reference = FirebaseDatabase.getInstance().getReference();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //This is getting the user's information to display on their profile. Could make own function?
        DatabaseReference userReference = reference.child("users").child(currentUserId);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Retrieve the user's information
                    String firstName = dataSnapshot.child("firstName").getValue(String.class);
                    String lastName = dataSnapshot.child("lastName").getValue(String.class);
                    String usernameRef = dataSnapshot.child("username").getValue(String.class);
                    int followerCount = dataSnapshot.child("followers").getValue(Integer.class);
                    int followingCount = dataSnapshot.child("following").getValue(Integer.class);
                    int likesCount = dataSnapshot.child("likesSaves").getValue(Integer.class);
                    // Now you have the user's information, you can use it to display on profile
                    name.setText(firstName + " " + lastName);
                    username.setText( getString(R.string.username_display) + usernameRef);
                    followers.setText(String.valueOf(followerCount));
                    following.setText(String.valueOf(followingCount));
                    totalLikes.setText(String.valueOf(likesCount));


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occur during the retrieval
            }
        });

        return view;
    }



    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        menuInflater.inflate(R.menu.settings_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.logout_button:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LogIn.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}