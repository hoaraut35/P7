package com.hoarauthomas.go4lunchthp7.ui.detail;

import com.hoarauthomas.go4lunchthp7.model.firestore.User;

import java.util.List;

import javax.annotation.Nullable;

public class ViewStateDetail {

    String urlPhoto;

    String title;
    String address;
    int rating;

    String call;
    Boolean liked;
    String website;

    Boolean favorite;

    List<User> listWorkMates;

    public ViewStateDetail(@Nullable String urlPhoto, @Nullable String title, @Nullable String address, @Nullable int rating, @Nullable String call, @Nullable Boolean liked, @Nullable String website, @Nullable Boolean favorite, @Nullable List<User> listWorkMates) {
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

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public List<User> getListWorkMates() {
        return listWorkMates;
    }

    public void setListWorkMates(List<User> listWorkMates) {
        this.listWorkMates = listWorkMates;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}
