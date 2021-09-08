package com.hoarauthomas.go4lunchthp7.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.R;
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
                logicWork(restaurantPojos,null,null,null);
            }
        });

        //observe detail of restaurant
        myScreenDetailMediator.addSource(myRestaurantDetail, new Observer<ResultDetailRestaurant>() {
            @Override
            public void onChanged(ResultDetailRestaurant resultDetailRestaurant) {
                logicWork(myRestaurantsList.getValue(), null, resultDetailRestaurant,null);
            }
        });

        //observe workmates list
        myScreenDetailMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> workmates) {
                logicWork(myRestaurantsList.getValue(), workmates, myRestaurantDetail.getValue(), null);
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

        if (restaurants != null){

            for (int x=0; x<restaurants.size();x++){

                //si la recherche dans la liste des restaurants correspond au restairant désiré alors...
                if (restaurants.get(x).getPlaceId().equals(placeIdGen)){

                    ScreenDetailModel myScreen;

                    myScreen = new ScreenDetailModel();

                    //get photo
                    try{
                        myScreen.setUrlPhoto(restaurants.get(x).getPhotos().get(0).getPhotoReference().toString());
                    } catch (Exception e){
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


                    //get liked

                    //si on a le detail alors...
                    if (detail != null){

                        //get phone number
                        myScreen.setCall(detail.getFormattedPhoneNumber());

                        //get url web
                        myScreen.setWebsite(detail.getUrl());
                    }



                    //si on a les collegues alors ...
                    List<User> myWorkMatesDetailList = new ArrayList<>();

                    if (workmates != null){

                        for (User myWorkMate : workmates){

                            myWorkMatesDetailList.add(myWorkMate);
                        }

                        myScreen.setListWorkMates(myWorkMatesDetailList);

                    }

                    myScreenDetailMediator.setValue(myScreen);

                }



            }



        }







     /*   RestaurantPojo resultRestaurant = null;
        SpecialWorkMates myWorkMates = new SpecialWorkMates();
        SpecialWorkMates myActualUser = new SpecialWorkMates();
        MyUser myUser = null;
        Boolean restauInFav = false;
        Boolean restauliked = false;

        if (restaurants != null && workmates != null && detail != null && placeIdGen != null) {

            mySpecialWorkMatesList.clear();

            for (int i = 0; i < restaurants.size(); i++) {

                if (restaurants.get(i).getPlaceId().equals(placeIdGen)) {

                    resultRestaurant = restaurants.get(i);

                    for (int z = 0; z < workmates.size(); z++) {
                        if (workmates.get(z).getFavoriteRestaurant().equals(resultRestaurant.getPlaceId())) {
                            myWorkMates.setAvatar(workmates.get(z).getUrlPicture());
                            myWorkMates.setNameOfWorkMates(workmates.get(z).getUsername());
                            myWorkMates.setNameOfRestaurant(resultRestaurant.getName());
                            myWorkMates.setPlaceId(resultRestaurant.getPlaceId());
                            mySpecialWorkMatesList.add(myWorkMates);

                        }//end if

                    }//end for


                }//end if

            }//end for


            //on test si l'utilisateur actuel a un favoris et des restaurant liked

            if (myUserBase != null) {

                for (int i = 0; i < workmates.size(); i++) {


                    if (workmates.get(i).getUid().equals(myUserBase.getUid())) {

                        //check favorite restaurant
                        if (workmates.get(i).getFavoriteRestaurant().equals(placeIdGen)) {
                            restauInFav = true;
                        } else {
                            restauInFav = false;
                        }

                        //check likedrestaurant


                        if (workmates.get(i).getRestaurant_liked() != null) {

                            for (int y = 0; y < workmates.get(i).getRestaurant_liked().size(); y++) {
                                if (placeIdGen.equals(workmates.get(i).getRestaurant_liked().get(y))) {
                                    restauliked = true;
                                } else {
                                    restauliked = false;
                                }

                            }


                        } else {
                            //liste liked null
                        }


                        List<String> z = null;
                        myUser = new MyUser("null", "null", workmates.get(i).getFavoriteRestaurant().toString(), z);
                        myUser.setMyFavoriteRestaurantId(restauInFav.toString());


                        //myUser.setMyFavoriteRestaurantId(workmates.get(i).getFavoriteRestaurant());
                    }


                }


            }

            myViewStateDetailMediator.setValue(new ViewStateDetail(resultRestaurant, detail, mySpecialWorkMatesList, restauInFav.booleanValue(), restauliked.booleanValue(), myUser));

        }

      */

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



