package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.RestaurantDetailPojo;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.ResultDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewStateMap;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelDetail extends ViewModel {

    //declare repo here...
    private RestaurantsRepository myRestaurantRepository;
    private WorkMatesRepository myWorkMatesRepository;

    //to publish data in ViewState
    private final MediatorLiveData<ViewStateDetail> myViewStateDetailMediator = new MediatorLiveData<>();

    private MutableLiveData<String> myPlaceId =  new MutableLiveData<>();

    //constructor
    public ViewModelDetail( RestaurantsRepository myRestaurantRepository, WorkMatesRepository myWorkMatesRepository){


        this.myRestaurantRepository = myRestaurantRepository;
        this.myWorkMatesRepository = myWorkMatesRepository;

        LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();
        LiveData<ResultDetailRestaurant> myDetail = this.myRestaurantRepository.getRestaurantById2(myPlaceId.getValue());
        LiveData<List<User>> myWorkMatesList = this.myWorkMatesRepository.getAllWorkMates();

        //ok
        myViewStateDetailMediator.addSource(myRestaurantsList, new Observer<List<RestaurantPojo>>() {
            @Override
            public void onChanged(List<RestaurantPojo> restaurantPojos) {
                Log.i("[MONDETAIL]", "Event récupération de la liste restaurants : " + restaurantPojos.size());
                logicWork( restaurantPojos,myWorkMatesList.getValue(),myPlaceId.getValue(),myDetail.getValue());
            }
        });

        //ok
        myViewStateDetailMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.i("[MONDETAIL]", "Event récupération liste collègues : "+ users.size());
                logicWork(myRestaurantsList.getValue(),users,myPlaceId.getValue(),myDetail.getValue());
            }
        });


        //ok
        myViewStateDetailMediator.addSource(myPlaceId, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (myPlaceId != null){
                    //myRestaurantRepository.setupPlaceId(myPlaceId.getValue());
                }

                Log.i("[MONDETAIL]", "Event récupération du restaurant à afficher : "+ s);
                logicWork(myRestaurantsList.getValue(),myWorkMatesList.getValue(),s,myDetail.getValue());
            }
        });

        //
        myViewStateDetailMediator.addSource(myDetail, new Observer<ResultDetailRestaurant>() {
            @Override
            public void onChanged(ResultDetailRestaurant resultDetailRestaurant) {

                if (resultDetailRestaurant == null){
                    Log.i("[MONDETAIL]", "Event récupération détail retour null");
                }else
                {
                    Log.i("[MONDETAIL]", "Event récupération du détail du restaurant");
                    logicWork(myRestaurantsList.getValue(),myWorkMatesList.getValue(),myPlaceId.getValue(), resultDetailRestaurant);
                }

            }
        });




    }

    //**********************************************************************************************
    // Logic work
    //**********************************************************************************************
    private void logicWork(@Nullable List<RestaurantPojo> restaurants, @Nullable List<User> workmates, @Nullable String placeId, @Nullable ResultDetailRestaurant detail) {
        List<User> workMatesTag = new ArrayList<>();

        RestaurantPojo newRestau;
        ResultDetailRestaurant newDetailRestau = null;





     /*   if (restaurants == null || workmates == null || placeId == null){
            return;
        }

      */



        //l'objet placeId existe
        if (placeId != null){

            Log.i("[MONDETAIL]","Un identifiant restaurant est créé ...");



            //un restaurant est choisi
            if (!placeId.isEmpty()){
                Log.i("[MONDETAIL]","Un restaurant est sélectionné ...");

                if (detail == null){
                    Log.i("[MONDETAIL]","demande detail en asynchrone..." + placeId);
                  //  setPlaceId(placeId);
                    return;
                }


                //si on a la liste des restaurant on peut récupéré le début des infos
                if (restaurants != null && !restaurants.isEmpty()){

                    for (int i =0; i<restaurants.size();i++){

                        //restaurant trouvé
                        if (restaurants.get(i).getPlaceId().equals(placeId)){
                            newRestau = restaurants.get(i);

                            if (workmates != null){
                                for (int y=0; y<workmates.size();y++){
                                    if (workmates.get(y).getFavoriteRestaurant().equals(placeId)){
                                        workMatesTag.add(workmates.get(y));
                                    }
                                }

                            }


                            if (detail != null){
                                newDetailRestau  = detail;
                            }


                            myViewStateDetailMediator.setValue(new ViewStateDetail(newRestau,newDetailRestau ,true,true,workMatesTag));
                        }

                    /*for (int z=0;z<workmates.size();z++){

                        if (restaurants.get(i).getPlaceId().equals(workmates.get(z).getFavoriteRestaurant())){
                            workMatesTag.add(workmates.get(z));

                        }
                    }

                     */

                    }
                }



            }
        }else
        {
            Log.i("[MONDETAIL]","Pas de restaurant identifié donc en attente ...");
            return;
        }








    }
    //**********************************************************************************************
    // End of logic work
    //**********************************************************************************************




    public void setPlaceId(String placeId){
        myPlaceId.setValue(placeId);
    }




    //public livedata to publish in viexwstate
    public LiveData<ViewStateDetail> getMediatorLiveData() {
        return myViewStateDetailMediator;
    }
}



