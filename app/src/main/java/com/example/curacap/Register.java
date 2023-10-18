package com.example.curacap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    TextInputEditText firstName, lastName, username, editTextEmail, editTextPassword;
    Button registerButton, loginHereButton;

    FirebaseAuth mAuth;
    FirebaseDatabase userDatabase;
    DatabaseReference reference;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.editTextPassword);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        username = findViewById(R.id.username);
        userDatabase = FirebaseDatabase.getInstance();

        registerButton = findViewById(R.id.register_button);
        loginHereButton = findViewById(R.id.goto_login_button);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fName, lName, uName, email, password;
                Integer profilePic;
                fName = String.valueOf(firstName.getText());
                lName = String.valueOf(lastName.getText());
                uName = String.valueOf(username.getText());
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                profilePic = Integer.valueOf(R.drawable.blank_profile);

                if(TextUtils.isEmpty(fName)){
                    Toast.makeText(Register.this, "Enter first Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(lName)){
                    Toast.makeText(Register.this, "Enter last name", Toast.LENGTH_SHORT).show();
                    return;
                }

                //This needs to be checked if username is valid!!
                if(TextUtils.isEmpty(uName)){
                    Toast.makeText(Register.this, "Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                    if(firebaseUser != null){
                                        reference = userDatabase.getReference("users").child(firebaseUser.getUid());
                                        User user = new User(fName, lName, uName, profilePic, 0, 0, 0);
                                        reference.setValue(user);
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), LogIn.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "User with this email already exists.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });


            }
        });

        loginHereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
            }
        });
    }
}