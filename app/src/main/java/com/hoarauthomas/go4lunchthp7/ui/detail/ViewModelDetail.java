package com.hoarauthomas.go4lunchthp7.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.api.UserHelper;
import com.hoarauthomas.go4lunchthp7.model.MyUser;
import com.hoarauthomas.go4lunchthp7.model.SpecialWorkMates;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.ResultDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelDetail extends ViewModel {

    //declare repo here...
    private final RestaurantsRepository myRestaurantRepository;
    private final WorkMatesRepository myWorkMatesRepository;
    private final FirebaseAuthRepository myAuthRepository;

    //livedata...
    private List<SpecialWorkMates> mySpecialWorkMatesList = new ArrayList<>();
    private MutableLiveData<MyUser> myUserMutable = new MutableLiveData<>();

    //to publish data in ViewState
    private final MediatorLiveData<ViewStateDetail> myViewStateDetailMediator = new MediatorLiveData<>();

    private final MediatorLiveData<ScreenDetailModel> myScreenDetailMediator = new MediatorLiveData<>();

    //others
    private String placeIdGen = null;

    //constructor
    public ViewModelDetail(FirebaseAuthRepository myAuthRepository, RestaurantsRepository myRestaurantRepository, WorkMatesRepository myWorkMatesRepository) {

        this.myAuthRepository = myAuthRepository;
        //get actual user authentification
        LiveData<FirebaseUser> myUserFromRepo = myAuthRepository.getUserLiveData();

        this.myRestaurantRepository = myRestaurantRepository;
        //get restaurant list
        LiveData<List<RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();
        //get restaurant detail
        LiveData<ResultDetailRestaurant> myRestaurantDetail = this.myRestaurantRepository.getMyRestaurantDetail();

        this.myWorkMatesRepository = myWorkMatesRepository;
        //get workmates list
        LiveData<List<User>> myWorkMatesList = this.myWorkMatesRepository.getAllWorkMatesList();

        //observe for list of restaurant
        myScreenDetailMediator.addSource(myRestaurantsList, new Observer<List<RestaurantPojo>>() {
            @Override
            public void onChanged(List<RestaurantPojo> restaurantPojos) {
                logicWork(restaurantPojos, myWorkMatesList.getValue(), myRestaurantDetail.getValue(), myUserFromRepo.getValue());
            }
        });

        //observe detail of restaurant
        myScreenDetailMediator.addSource(myRestaurantDetail, new Observer<ResultDetailRestaurant>() {
            @Override
            public void onChanged(ResultDetailRestaurant resultDetailRestaurant) {
                logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), resultDetailRestaurant, myUserFromRepo.getValue());
            }
        });

        //observe workmates list
        myScreenDetailMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> workmates) {
                logicWork(myRestaurantsList.getValue(), workmates, myRestaurantDetail.getValue(), myUserFromRepo.getValue());
            }
        });

        myScreenDetailMediator.addSource(myUserFromRepo, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), myRestaurantDetail.getValue(), firebaseUser);
            }
        });

        /*
        myViewStateDetailMediator.addSource(myRestaurantsList, new Observer<List<RestaurantPojo>>() {
            @Override
            public void onChanged(List<RestaurantPojo> restaurantPojos) {
                logicWork(restaurantPojos, myWorkMatesList.getValue(), myRestaurantDetail.getValue(), myUserFromRepo.getValue());
            }
        });

        myViewStateDetailMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                logicWork(myRestaurantsList.getValue(), users, myRestaurantDetail.getValue(), myUserFromRepo.getValue());
            }
        });

        myViewStateDetailMediator.addSource(myUserFromRepo, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                Log.i("[MONDETAIL]", "event sur user");
            }
        });

        myViewStateDetailMediator.addSource(myRestaurantDetail, new Observer<ResultDetailRestaurant>() {
            @Override
            public void onChanged(ResultDetailRestaurant resultDetailRestaurant) {
                if (resultDetailRestaurant == null) {
                    Log.i("[MONDETAIL]", "Event récupération détail retour null");
                } else {
                    Log.i("[MONDETAIL]", "Event récupération du détail du restaurant");
                    logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), resultDetailRestaurant, myUserFromRepo.getValue());
                }
            }
        });

         */
    }

    //**********************************************************************************************
    // Logic work
    //**********************************************************************************************
    private void logicWork(@Nullable List<RestaurantPojo> restaurants, @Nullable List<User> workmates, @Nullable ResultDetailRestaurant detail, @Nullable FirebaseUser myUserBase) {

        //retourner
        //un objet restaurant ok
        //un objet detail restaurant ok
        //la liste des workmates
        // l'état du bouton favoris
        // l'état du bouton like

        if (restaurants != null) {

            for (int x = 0; x < restaurants.size(); x++) {

                //si la recherche dans la liste des restaurants correspond au restairant désiré alors...
                if (restaurants.get(x).getPlaceId().equals(placeIdGen)) {

                    ScreenDetailModel myScreen;

                    myScreen = new ScreenDetailModel();

                    //get photo
                    try {
                        myScreen.setUrlPhoto(restaurants.get(x).getPhotos().get(0).getPhotoReference().toString());
                    } catch (Exception e) {
                        //charger une photo de substitution
                    }

                    //get titre restaurant
                    myScreen.setTitle(restaurants.get(x).getName());

                    //get address
                    myScreen.setAddress(restaurants.get(x).getVicinity());

                    //get rating
                    try {
                        //on google we have a rating from 1 to 5 but we want 1 to 3...
                        Double ratingDouble = map(restaurants.get(x).getRating(), 1.0, 5.0, 1.0, 3.0);
                        int ratingInt = (int) Math.round(ratingDouble);

                        if (ratingInt == 1) {
                            myScreen.setRating(1);
                        } else if (ratingInt == 2) {
                            myScreen.setRating(2);
                        } else if (ratingInt == 3) {
                            myScreen.setRating(3);
                        }
                    } catch (Exception e) {
                        myScreen.setRating(0);
                    }

                    //get favorite
                    if (workmates != null) {
                        if (workmates.size() > 0) {

                            for (int i = 0; i < workmates.size(); i++) {

                                //restaurant is favorite, update button
                                if (workmates.get(i).getUid().equals(myUserBase.getUid())) {

                                    if (workmates.get(i).getFavoriteRestaurant().equals(placeIdGen)) {
                                        myScreen.setFavorite(true);
                                        break;
                                    } else {
                                        myScreen.setFavorite(false);
                                    }

                                }

                                //get list of liked restaurants

                                List<String> myTempRestaurant = new ArrayList<>();

                               // myTempRestaurant.addAll(workmates.get(i).getRestaurant_liked());

                                if (workmates.get(i).getRestaurant_liked().size() > 0) {

                                  //  for (int z = 0; z < workmates.get(i).getRestaurant_liked().size(); z++) {

                                        //set liked restaurant bool
                                    //    if (workmates.get(i).getRestaurant_liked().get(z).equals(placeIdGen)) {
                                      //      myScreen.setLiked(true);
                                        //} else {
                                           myScreen.setLiked(false);
                                       // }

                                    //}



                                }else
                                {
                                    myScreen.setLiked(false);
                                }




                            }
                        } else {
                            myScreen.setFavorite(false);
                            myScreen.setLiked(false);

                        }


                    } else {
                        myScreen.setFavorite(false);
                        myScreen.setLiked(false);

                    }


                    //get liked

                    //si on a le detail alors...
                    if (detail != null) {

                        //get phone number
                        myScreen.setCall(detail.getFormattedPhoneNumber());

                        //get url web
                        myScreen.setWebsite(detail.getUrl());
                    }

                    //si on a les collegues alors ...
                    List<User> myWorkMatesDetailList = new ArrayList<>();

                    if (workmates != null) {

                        for (User myWorkMate : workmates) {
                            //if detail have a workmate
                            if (myWorkMate.getFavoriteRestaurant().equals(placeIdGen)) {
                                myWorkMatesDetailList.add(myWorkMate);
                            }

                        }

                        myScreen.setListWorkMates(myWorkMatesDetailList);

                    }

                    myScreenDetailMediator.setValue(myScreen);

                }

            }

        }


    }
    //**********************************************************************************************
    // End of logic work
    //**********************************************************************************************


    //to map a range to another range ... from arduino library
    private Double map(double value, double in_min, double in_max, double out_min, double out_max) {
        return (value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public LiveData<MyUser> getMyUserLiveData() {
        return myUserMutable;
    }


    //
    public void setPlaceId(String placeId) {
        placeIdGen = myRestaurantRepository.setPlaceId(placeId);
    }

    public String getPlaceId() {
        return placeIdGen;
    }


    public void setFavRestaurant(String uid, String placeId) {

        UserHelper.updateFavRestaurant(uid, placeId);
        // myWorkMatesRepository.getCurrentUser().
    }


    public void adLikedRestaurant(String uid, String myPlaces) {

        //UserHelper.getUser(uid);

        //  DocumentReference mySnapShot = UserHelper.getUsersCollection().document();

        //List<String> myTab = (List<String>) mySnapShot.get("restaurant_liked");
        //  DocumentSnapshot document = mySnapShot.get();


        UserHelper.addLikedRestaurant(uid, myPlaces);
    }

    public FirebaseUser getCurrentUser() {


        return myWorkMatesRepository.getCurrentUser();

    }

    //public livedata to publish in viexwstate
    public LiveData<ViewStateDetail> getMediatorLiveData() {
        return myViewStateDetailMediator;
    }

    //test2
    public LiveData<ScreenDetailModel> getMediatorScreen() {
        return myScreenDetailMediator;
    }
}



