package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.util.Log;

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
    private List<SpecialWorkMates> mySpecialWorkMatesList = new ArrayList<>();


    private MutableLiveData<MyUser> myUserMutable = new MutableLiveData<>();

    private final UserHelper myUserHelper = new UserHelper();


    //to publish data in ViewState
    private final MediatorLiveData<ViewStateDetail> myViewStateDetailMediator = new MediatorLiveData<>();

    private String placeIdGen = null;


    //constructor
    public ViewModelDetail(FirebaseAuthRepository myAuthRepository, RestaurantsRepository myRestaurantRepository, WorkMatesRepository myWorkMatesRepository) {

        this.myAuthRepository = myAuthRepository;
        this.myRestaurantRepository = myRestaurantRepository;
        this.myWorkMatesRepository = myWorkMatesRepository;

        LiveData<FirebaseUser> myUserFromRepo = myAuthRepository.getUserLiveData();
        LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();
        LiveData<ResultDetailRestaurant> myDetail = this.myRestaurantRepository.getMyRestaurantDetail();

        LiveData<List<User>> myWorkMatesList = this.myWorkMatesRepository.getAllWorkMatesList();

        //ok
        myViewStateDetailMediator.addSource(myRestaurantsList, new Observer<List<RestaurantPojo>>() {
            @Override
            public void onChanged(List<RestaurantPojo> restaurantPojos) {
                // Log.i("[MONDETAIL]", "Event récupération de la liste restaurants : " + restaurantPojos.size());
                logicWork(restaurantPojos, myWorkMatesList.getValue(), myDetail.getValue(), myUserFromRepo.getValue());
            }
        });

        //ok
        myViewStateDetailMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                // Log.i("[MONDETAIL]", "Event récupération liste collègues : " + users.size());
                logicWork(myRestaurantsList.getValue(), users, myDetail.getValue(), myUserFromRepo.getValue());
            }
        });

        myViewStateDetailMediator.addSource(myUserFromRepo, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                Log.i("[MONDETAIL]", "event sur user");
            }
        });

        myViewStateDetailMediator.addSource(myDetail, new Observer<ResultDetailRestaurant>() {
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
    }

    //**********************************************************************************************
    // Logic work
    //**********************************************************************************************
    private void logicWork(@Nullable List<RestaurantPojo> restaurants, @Nullable List<User> workmates, @Nullable ResultDetailRestaurant detail, @Nullable FirebaseUser myUserBase) {

        RestaurantPojo resultRestaurant = null;
        SpecialWorkMates myWorkMates = new SpecialWorkMates();
        SpecialWorkMates myActualUser = new SpecialWorkMates();
        MyUser myUser = null;
        Boolean restauInFav;
        Boolean restauliked;

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


            if (myUserBase != null) {

                for (int i = 0; i < workmates.size(); i++) {
                    if (workmates.get(i).getUid().equals(myUserBase.getUid())) {

                        List<String> z = null;
                       myUser = new MyUser("null","null", workmates.get(i).getFavoriteRestaurant().toString(),    z);


                       myUser.setMyFavoriteRestaurantId("toto");


                        //myUser.setMyFavoriteRestaurantId(workmates.get(i).getFavoriteRestaurant());
                    }
                }

            }

            myViewStateDetailMediator.setValue(new ViewStateDetail(resultRestaurant, detail, mySpecialWorkMatesList, true, true, myUser));

        }

    }
    //**********************************************************************************************
    // End of logic work
    //**********************************************************************************************


    public LiveData<MyUser> getMyUserLiveData() {
        return myUserMutable;
    }

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
}



