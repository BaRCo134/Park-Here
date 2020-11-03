package com.example.parkhere;

import android.view.LayoutInflater;
import android.view.View;

import com.example.parkhere.databinding.MarkerWindowContentViewBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DatabaseError;

import java.util.HashMap;

class ParkingInfoWindowAdapter implements GoogleMap.OnMarkerClickListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    private HashMap<Marker, String> creatorIdByMarker;
    private HashMap<Marker, User> creatorByMarker = new HashMap<>();
    private UsersRepository usersRepository = new UsersRepository();
    private LayoutInflater layoutInflater;

    public ParkingInfoWindowAdapter(LayoutInflater layoutInflater, HashMap<Marker, String> creatorIdByMarker) {

        this.layoutInflater = layoutInflater;
        this.creatorIdByMarker = creatorIdByMarker;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        final User user = this.creatorByMarker.get(marker);
        final MarkerWindowContentViewBinding binding = MarkerWindowContentViewBinding.inflate(layoutInflater);
        /*this.usersRepository.getUser(creatorId, new UsersRepository.GetUserListener() {
            @Override
            public void onChange(final User user) {

                binding.markerNicknameTxtV.setText("WOW" + user.getNickName());
                binding.markerLikesTxtV.setText("Likes: " + user.getLikes());
                binding.markerReportsTxtV.setText("Report: " + user.getReportNum());
                binding.getRoot().invalidate();
                binding.markerAddLikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.addLike();
                        binding.markerLikesTxtV.setText("" + user.getLikes());
                        binding.markerAddLikeBtn.setVisibility(View.GONE);
                        usersRepository.changeUser(user);
                    }
                });
            }

            @Override
            public void onCanceled(DatabaseError error) {

            }
        });*/
        binding.markerNicknameTxtV.setText(user.getNickName());
        binding.markerLikesTxtV.setText("Likes: " + user.getLikes());
        binding.markerReportsTxtV.setText("Report: " + user.getReportNum());
        binding.getRoot().invalidate();
        binding.markerAddLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.addLike();
                binding.markerLikesTxtV.setText("" + user.getLikes());
                binding.markerAddLikeBtn.setVisibility(View.GONE);
                usersRepository.changeUser(user);
            }
        });
        return binding.getRoot();
    }

    @Override
    public View getInfoContents(final Marker marker) {
        return null;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        String creatorId = this.creatorIdByMarker.get(marker);
        this.usersRepository.getUser(creatorId, new UsersRepository.GetUserListener() {
            @Override
            public void onChange(User user) {
                creatorByMarker.put(marker, user);
                marker.showInfoWindow();
            }

            @Override
            public void onCanceled(DatabaseError error) {

            }
        });
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        final User user = this.creatorByMarker.get(marker);
        user.addLike();
        usersRepository.changeUser(user);
        marker.hideInfoWindow();
    }
}

