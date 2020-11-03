package com.example.parkhere;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    
    private String nickname = "";
    private long likes = 0;
    private long reportCount = 0;
    private String location;
    private String firebaseId;

    public User(String firebaseId){

        this.firebaseId = firebaseId;
    }
    public User(HashMap <String, Object> userValues, String firebaseId){

        this.firebaseId = firebaseId;

        if(userValues == null) return;

        this.nickname = (String) userValues.get("nickname");
        this.location = (String) userValues.get("location"); 
        this.likes = (long) userValues.get("likes");
        this.reportCount = (long) userValues.get("reportCount");
    }

    public HashMap <String, Object> getUserValues (){
        HashMap <String, Object> userValue = new HashMap<>();
        userValue.put("nickname", nickname);
        userValue.put("location", location);
        userValue.put("reportCount", reportCount);
        userValue.put("likes", likes);
        return userValue;
    }

    public String getFirebaseId() { return firebaseId; }

    public String getNickName() { return nickname;    }

    public void setNickName(String nickname) {  this.nickname = nickname;  }

    public long getLikes() {  return likes;  }

    public void addLike() {  this.likes++;   }

    public long getReportNum() { return reportCount;  }

    public void addReportNum() {  this.reportCount++;  }

    public String getLocation() {  return location;  }

    public void setLocation(String location) {  this.location = location;  }

}
