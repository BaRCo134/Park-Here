package com.example.parkhere;

import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;


public class UsersRepository {

    private DatabaseReference user = FirebaseDatabase.getInstance().getReference("users");

    interface GetUserListener{
        void onChange(User user);
        void onCanceled(DatabaseError error);
    }

    public void getCurrentUser(final GetUserListener getUserListener) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getUser(firebaseUser.getUid(), getUserListener);
    }

    public void getUser(final String firebaseId, final GetUserListener getUserListener){
        user.child(firebaseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap userValues = (HashMap) dataSnapshot.getValue();
                User user = new User(userValues, firebaseId);
                if(userValues == null){
                    changeUser(user);
                }
                getUserListener.onChange(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getUserListener.onCanceled(databaseError);
            }
        });
    }

    public void changeUser(User user){
        this.user.child(user.getFirebaseId()).updateChildren(user.getUserValues());
    }

}
