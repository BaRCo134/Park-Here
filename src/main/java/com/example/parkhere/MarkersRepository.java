package com.example.parkhere;
import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MarkersRepository {

    private DatabaseReference marker = FirebaseDatabase.getInstance().getReference("markers");

    interface GetMarkersListener{
        void onChange(ArrayList<Marker> markers);
        void onCanceled(DatabaseError error);
    }

    public void createMarker(Marker marker) {
        this.marker.push().updateChildren(marker.getMarkerValues());
    }

    public void getMarkers(final GetMarkersListener getMarkersListener){
        marker.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap <String, HashMap> markerValues = (HashMap <String, HashMap>) dataSnapshot.getValue();
                ArrayList <Marker> markers = new ArrayList<>();
                if (markerValues != null) {
                    for(String markerId : markerValues.keySet()){

                        HashMap <String, Object> markerValue = (HashMap <String, Object>) markerValues.get(markerId);
                        Marker marker = new Marker(markerValue);

                        long currentTime = new Date().getTime();
                        if(marker.getCreationTime() + 60000 < currentTime){
                            MarkersRepository.this.marker.child(markerId).removeValue();
                        }
                        else
                            markers.add(marker);
                    }
                }
                getMarkersListener.onChange(markers);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                getMarkersListener.onCanceled(databaseError);
            }
        });
    }
}