package com.hoarauthomas.go4lunchthp7.ui.detail;

import androidx.annotation.NonNull;

import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;

import java.util.List;

import javax.annotation.Nullable;

public class ViewStateDetail {

    @NonNull
    public String getWorkmate() {
        return workmate;
    }

    public void setWorkmate(@NonNull String workmate) {
        this.workmate = workmate;
    }

    @NonNull  String workmate;

    String placeId;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    String urlPhoto;

    String title;
    String address;
    int rating;

    String call;
    Boolean liked;
    String website;

    Boolean favorite;

    List<FirestoreUser> listWorkMates;

    public ViewStateDetail(@Nullable String placeID, @Nullable String urlPhoto, @Nullable String title, @Nullable String address, int rating, @Nullable String call, @Nullable Boolean liked, @Nullable String website, @Nullable Boolean favorite, @Nullable List<FirestoreUser> listWorkMates, @NonNull String workmate) {
        this.workmate = workmate;
        this.placeId = placeID;
        this.urlPhoto = urlPhoto;
        this.title = title;
        this.address = address;
        this.rating = rating;
        this.call = call;
        this.liked = liked;
        this.website = website;
        this.favorite = favorite;
        this.listWorkMates = listWorkMates;
    }

    public ViewStateDetail() {
//        workmate = null;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String PhoneNumber() {
        return call;
    }

    public void setPhoneNumber(String call) {
        this.call = call;
    }

    public Boolean getLike() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public String getWebSite() {
        return website;
    }

    public void setWebSite(String website) {
        this.website = website;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public List<FirestoreUser> getListWorkMates() {
        return listWorkMates;
    }

    public void setListWorkMates(List<FirestoreUser> listWorkMates) {
        this.listWorkMates = listWorkMates;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}
