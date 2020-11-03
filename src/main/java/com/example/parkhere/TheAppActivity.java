package com.example.parkhere;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseError;

public class TheAppActivity extends AppCompatActivity {

    private UsersRepository usersRepository = new UsersRepository();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_app);

        usersRepository.getCurrentUser(new UsersRepository.GetUserListener() {

            @Override
            public void onChange(final User user) {

                TheAppActivity.this.user = user;
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(TheAppActivity.this);
                TextView nickname = findViewById(R.id.nick_name_txtV);
                TextView likes = findViewById(R.id.likes_txtV);
                TextView reports = findViewById(R.id.reports_txtV);
                if (acct != null) {
                    String acctName = acct.getDisplayName();
                    String nicknameStr = getString(R.string.marker_nickname_txtV);
                    nickname.setText(nicknameStr + acctName);
                    user.setNickName(acctName);
                    usersRepository.changeUser(user);

                    String likesStr = getString(R.string.marker_likes_txtV);
                    likes.setText(likesStr + user.getLikes());

                    String reportsStr = getString(R.string.marker_reports_txtV);
                    reports.setText(reportsStr + user.getReportNum());
                }
            }

            @Override
            public void onCanceled(DatabaseError error) {

            }
        });
    }

    public void onLookForParkingsClicked(View view) {
        finish();
        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
    }

    public void onImOutClicked(View view) {

        user.addReportNum();

        usersRepository.changeUser(user);

        final MarkersRepository markersRepository = new MarkersRepository();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    markersRepository.createMarker(new Marker(latLng, user));
                }
            } catch (SecurityException e) {
                String permissionRequestToast = getString(R.string.Toast_permission_required);
                Toast.makeText(TheAppActivity.this, permissionRequestToast, Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }
}