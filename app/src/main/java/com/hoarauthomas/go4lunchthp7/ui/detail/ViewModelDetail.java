package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

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
    private RestaurantsRepository myRestaurantRepository;
    private WorkMatesRepository myWorkMatesRepository;

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
                Log.i("[MONDETAIL]", "Event récupération de la liste restaurants : " + restaurantPojos.size());
                logicWork(restaurantPojos, myWorkMatesList.getValue(), myDetail.getValue());
            }
        });

        //ok
        myViewStateDetailMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.i("[MONDETAIL]", "Event récupération liste collègues : " + users.size());
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


        if (restaurants != null && workmates != null && detail != null && placeIdGen!=null) {

            for (int i = 0; i < restaurants.size(); i++) {

               if (restaurants.get(i).getPlaceId().equals(placeIdGen)) {
                    //on enregistre le restau
                    resultRestaurant = restaurants.get(i);

                    //on cherche les collegues qui sont enregistre sur ce restaurant
                    for (int z = 0; z < workmates.size(); z++) {

                        if (workmates.get(z).getFavoriteRestaurant().equals(placeIdGen)) {
                            resultWorkMAtes.add(workmates.get(z));
                        }

                    }

                    //on recupere le detail du restau
                    if (detail != null) {
                        resultDetailRestaurant = detail;
                    }


                    //end for

                }//end if


            }//end for

            if (resultRestaurant != null && resultDetailRestaurant != null) {
                //update viewstate
                myViewStateDetailMediator.setValue(new ViewStateDetail(resultRestaurant, resultDetailRestaurant, resultWorkMAtes));

            }

            //   }//fin si

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


    //public livedata to publish in viexwstate
    public LiveData<ViewStateDetail> getMediatorLiveData() {
        return myViewStateDetailMediator;
    }
}



