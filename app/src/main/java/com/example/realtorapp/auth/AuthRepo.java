package com.example.realtorapp.auth;

import com.example.realtorapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthRepo {

    // Handles firebase auth
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    // creating fire store to manage data there in folders and adding data
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public void login(String email, String password, OnCompleteListener<AuthResult> listener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void signup(String name, String email, String password, String type, OnCompleteListener<AuthResult> listener) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {

            listener.onComplete(task); // activity signup completed

            if(task.isSuccessful()){
                String userId = auth.getCurrentUser().getUid(); // this creates a new UserId for the user

                User user = new User(userId, name, email, type, false); // this creates a new user object

                FirebaseFirestore.getInstance().collection("users").document(userId).set(user);// this is to manage the data in the database
                    // it creates a collection User under there it will store User accordingt to their user id.
            }

    });
    }

    public void logout() {
        auth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public boolean isLoggedIn() {

        return false;
    }
}