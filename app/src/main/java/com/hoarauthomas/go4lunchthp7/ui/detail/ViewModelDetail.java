package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.hoarauthomas.go4lunchthp7.api.UserHelper;
import com.hoarauthomas.go4lunchthp7.model.SpecialWorkMates;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.ResultDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelDetail extends ViewModel {

    //declare repo here...
    private final RestaurantsRepository myRestaurantRepository;
    private final WorkMatesRepository myWorkMatesRepository;
    private List<SpecialWorkMates> mySpecialWorkMatesList = new ArrayList<>();


    private final UserHelper myUserHelper = new UserHelper();


    //to publish data in ViewState
    private final MediatorLiveData<ViewStateDetail> myViewStateDetailMediator = new MediatorLiveData<>();

    private String placeIdGen = null;


    //constructor
    public ViewModelDetail(RestaurantsRepository myRestaurantRepository, WorkMatesRepository myWorkMatesRepository) {


        this.myRestaurantRepository = myRestaurantRepository;
        this.myWorkMatesRepository = myWorkMatesRepository;

        LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();
        LiveData<ResultDetailRestaurant> myDetail = this.myRestaurantRepository.getMyRestaurantDetail();
        LiveData<List<User>> myWorkMatesList = this.myWorkMatesRepository.getAllWorkMates();

        //ok
        myViewStateDetailMediator.addSource(myRestaurantsList, new Observer<List<RestaurantPojo>>() {
            @Override
            public void onChanged(List<RestaurantPojo> restaurantPojos) {
                // Log.i("[MONDETAIL]", "Event récupération de la liste restaurants : " + restaurantPojos.size());
                logicWork(restaurantPojos, myWorkMatesList.getValue(), myDetail.getValue());
            }
        });

        //ok
        myViewStateDetailMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                // Log.i("[MONDETAIL]", "Event récupération liste collègues : " + users.size());
                logicWork(myRestaurantsList.getValue(), users, myDetail.getValue());
            }
        });


        //ok
     /*   myViewStateDetailMediator.addSource(myPlaceId, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i("[MONDETAIL]", "Event récupération du restaurant à afficher : " + s);
                logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), s, myDetail.getValue());
            }
        });

      */

        //
        myViewStateDetailMediator.addSource(myDetail, new Observer<ResultDetailRestaurant>() {
            @Override
            public void onChanged(ResultDetailRestaurant resultDetailRestaurant) {

                if (resultDetailRestaurant == null) {
                    Log.i("[MONDETAIL]", "Event récupération détail retour null");
                } else {
                    Log.i("[MONDETAIL]", "Event récupération du détail du restaurant");
                    logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), resultDetailRestaurant);
                }

            }
        });


    }

    //**********************************************************************************************
    // Logic work
    //**********************************************************************************************
    private void logicWork(@Nullable List<RestaurantPojo> restaurants, @Nullable List<User> workmates, @Nullable ResultDetailRestaurant detail) {

        List<User> resultWorkMAtes = new ArrayList<>();
        RestaurantPojo resultRestaurant = null;
        ResultDetailRestaurant resultDetailRestaurant = null;


        SpecialWorkMates myWorkMates = new SpecialWorkMates();


        if (restaurants != null && workmates != null && detail != null && placeIdGen != null) {

            for (int i = 0; i < restaurants.size(); i++) {



                if (restaurants.get(i).getPlaceId().equals(placeIdGen)) {

                    resultRestaurant = restaurants.get(i);

                    for (int z = 0; z < workmates.size(); z++) {

                        myWorkMates.setAvatar(workmates.get(z).getUrlPicture());
                        myWorkMates.setNameOfWorkMates(workmates.get(z).getUsername());

                        if (workmates.get(z).getFavoriteRestaurant().equals(placeIdGen)) {
                            resultWorkMAtes.add(workmates.get(z));

                            myWorkMates.setNameOfRestaurant( restaurants.get(i).getName());
                            myWorkMates.setPlaceId(restaurants.get(i).getPlaceId());
                        }

                    }
                    mySpecialWorkMatesList.add(myWorkMates);



                }//end if


            }//end for

            myViewStateDetailMediator.setValue(new ViewStateDetail(resultRestaurant, detail, mySpecialWorkMatesList));


        }
        //**********************************************************************************


                /*for (int i = 0; i < restaurants.size(); i++) {

                    //restaurant trouvé
                    if (restaurants.get(i).getPlaceId().equals(placeId)) {
                        newRestau = restaurants.get(i);

                        if (workmates != null) {
                            for (int y = 0; y < workmates.size(); y++) {
                                if (workmates.get(y).getFavoriteRestaurant().equals(placeId)) {
                                    workMatesTag.add(workmates.get(y));
                                }
                            }

                        }


                        if (detail != null) {
                            newDetailRestau = detail;
                        }



                    }


                }

                 */


    }
//} else {
//  Log.i("[MONDETAIL]", "Pas de restaurant identifié donc en attente ...");
//return;
//}


//}
//**********************************************************************************************
// End of logic work
//**********************************************************************************************


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



