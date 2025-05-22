package com.example.fitostory;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserInfo{
    //private int id;
    private String email;
    private String username;
    private String password;
    private int avatarIcon; // Store resource ID, not ImageView

    public UserInfo(String email, String username, String password, int avatarIcon){
        //this.id = 1;
        this.email = email;
        this.username = username;
        this.password = password;
        this.avatarIcon = avatarIcon; // Store the resource ID directly
    }

    public void setEmail(String newEmail){
        this.email = newEmail;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String newPassword){
        this.password = newPassword;
    }
    public void setAvatarIcon(int avatarIcon){
        this.avatarIcon = avatarIcon; // Set the resource ID
    }

    public String getEmail() {
        return email;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public int getAvatarIcon(){
        return avatarIcon; // Return the resource ID
    }
}
