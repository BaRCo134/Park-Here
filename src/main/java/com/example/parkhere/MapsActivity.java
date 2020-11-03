package com.example.parkhere;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.PermissionRequest;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;

import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UsersRepository usersRepository = new UsersRepository();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        EditText editText = (EditText) findViewById(R.id.search_location_eTxt);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        EditText.OnEditorActionListener actionListener = new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onSearchClicked(null);
                }
                return true;
            }
        };
        editText.setOnEditorActionListener(actionListener);
        final MarkersRepository markersRepository = new MarkersRepository();
        markersRepository.getMarkers(new MarkersRepository.GetMarkersListener() {
            @Override
            public void onChange(ArrayList<Marker> markers) {
                HashMap<com.google.android.gms.maps.model.Marker, String> creatorIdByMarker = new HashMap<>();
                for (Marker marker : markers){
                    LatLng latLngMarker = marker.getLocation();
                    com.google.android.gms.maps.model.Marker markerGoo = mMap.addMarker(new MarkerOptions().position(latLngMarker).title("Park Here!"));
                    creatorIdByMarker.put(markerGoo, marker.getCreatorId());
                }
                ParkingInfoWindowAdapter parkingInfoWindowAdapter = new ParkingInfoWindowAdapter(getLayoutInflater(), creatorIdByMarker);
                mMap.setInfoWindowAdapter(parkingInfoWindowAdapter);
                mMap.setOnMarkerClickListener(parkingInfoWindowAdapter);
                mMap.setOnInfoWindowClickListener(parkingInfoWindowAdapter);


            }

            @Override
            public void onCanceled(DatabaseError error) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            String permissionRequiredToast = (String) getString(R.string.Toast_permission_required);
            Toast.makeText(this, permissionRequiredToast, Toast.LENGTH_LONG).show();
        } else {
            if (mMap != null)
                mMap.setMyLocationEnabled(true);
        }

        LatLng israel = new LatLng(32.058, 34.824);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(israel));
        mMap.setBuildingsEnabled(true);
        mMap.setMinZoomPreference(17);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void onSearchClicked(View view){
        EditText editText = (EditText) findViewById(R.id.search_location_eTxt);
        String searchedLoc = editText.getText().toString();
        List <Address> addressList = null;
        if(searchedLoc != null || searchedLoc != "") {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(searchedLoc, 1);
            }catch (IOException e){
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }
}
