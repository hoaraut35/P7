package com.hoarauthomas.go4lunchthp7.ui;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.repository.AlarmRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class ViewModelMain extends ViewModel {

    //repository...
    private FirebaseAuthRepository myFirebaseAuthRepoVM;
    private FirestoreRepository myFirestoreRepo;
    private PlaceAutocompleteRepository myPlaceAutocompleteRepoVM;
    private PositionRepository myPositionRepoVM;
    private AlarmRepository myAlarmRepoVM;
    String myPlaceForOpenFav = null;
    private Application application;



    //for notification work
    private WorkManager myWorkManager;
    private LiveData<List<WorkInfo>> mySavedInfo;



    //shared beetween multiple view
    private SharedRepository mySharedRepoVM;

    //livedata...
    private  LiveData<FirebaseUser> myUserLiveData;
    private final LiveData<FirestoreUser> myWorkmate;
    private final LiveData<Boolean> myUserStateNew;
    private LiveData<List<FirestoreUser>> myWorkMatesListLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> myUserRestaurantId = new MutableLiveData<>();

    private MutableLiveData<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> myPlaceAutocompleteList = new MutableLiveData<>();



    //to update ViewState...
    MediatorLiveData<ViewMainState> myAppMapMediator = new MediatorLiveData<>();

    /**
     * constructor called by viewmodelfactory
     *
     * @param firebaseAuthRepository
     * @param firestoreRepository
     * @param placeAutocompleteRepository
     * @param myPositionRepoVM
     * @param myAlarmRepoVM
     * @param mySharedRepoVM
     */
    public ViewModelMain(
            FirebaseAuthRepository firebaseAuthRepository,
            FirestoreRepository firestoreRepository,
            PlaceAutocompleteRepository placeAutocompleteRepository,
            PositionRepository myPositionRepoVM,
            AlarmRepository myAlarmRepoVM,
            SharedRepository mySharedRepoVM
            ) {

        //get data from Auth repository...
        this.myFirebaseAuthRepoVM = firebaseAuthRepository;
        myUserLiveData = myFirebaseAuthRepoVM.getFirebaseAuthUserFromRepo();
        myUserStateNew = myFirebaseAuthRepoVM.getFirebaseAuthUserStateFromRepo();

        //get data from workmates repository...
        this.myFirestoreRepo = firestoreRepository;
        myWorkMatesListLiveData = myFirestoreRepo.getFirestoreWorkmates();
        myWorkmate = myFirestoreRepo.getWorkmateFromRepo();


        //get data from place autocomplete repository...
        this.myPlaceAutocompleteRepoVM = placeAutocompleteRepository;
        //get position for autocomplete request
        this.myPositionRepoVM = myPositionRepoVM;
        //get alarm data
        this.myAlarmRepoVM = myAlarmRepoVM;
        this.mySharedRepoVM = mySharedRepoVM;

        //add source on onPlacesAutocomplete
        myAppMapMediator.addSource(myPlaceAutocompleteList, placeAutocomplete -> {
            if (myPlaceAutocompleteList == null) return;
            logicWork(myUserLiveData.getValue(),
                    myWorkMatesListLiveData.getValue(),
                    myUserStateNew.getValue(),
                    myWorkmate.getValue(),
                    placeAutocomplete);
        });


        //add source
        myAppMapMediator.addSource(myUserLiveData, firebaseUser -> {
            if (firebaseUser != null) {
                if (!firebaseUser.getUid().isEmpty()) {
                    logicWork(firebaseUser, myWorkMatesListLiveData.getValue(),
                            myUserStateNew.getValue(),
                            myWorkmate.getValue(),
                            myPlaceAutocompleteList.getValue());
                }
            }
        });

        //add source
        myAppMapMediator.addSource(myWorkMatesListLiveData, users -> {
            if (users == null) return;
            if (myWorkMatesListLiveData != null) {
                if (!myWorkMatesListLiveData.getValue().isEmpty()) {
                    logicWork(myUserLiveData.getValue(),
                            users,
                            myUserStateNew.getValue(),
                            myWorkmate.getValue(),
                            myPlaceAutocompleteList.getValue());
                }
            }
        });

        myAppMapMediator.addSource(myWorkmate, firestoreUser -> {
            if (firestoreUser == null) return;
            logicWork(myUserLiveData.getValue(),
                    myWorkMatesListLiveData.getValue(),
                    myUserStateNew.getValue(),
                    firestoreUser,
                    myPlaceAutocompleteList.getValue());
        });

    }

    /**
     * logic work here
     *
     * @param myUser
     * @param workmates
     * @param bool
     */
    // Logic work
    private void logicWork(
            @Nullable FirebaseUser myUser,
            @Nullable List<FirestoreUser> workmates,
            Boolean bool,
            FirestoreUser myFirestoreUserData,
            PlaceAutocomplete myPlacesAuto) {

        createUser();

        if (myUser != null && workmates != null && myFirestoreUserData != null) {

            //placeautocomplete à traduire en placedetails




            //get data here for prepare alarm
            if (myFirestoreUserData.getFavoriteRestaurant() != null){
                Log.i("[ALARME]","User name " + myFirestoreUserData.getUsername());
            }


            Log.i("[NOTIFICATION]","My user favorite restaurant : " + myFirestoreUserData.getFavoriteRestaurant());


            if (myPlacesAuto != null) {


                List<String> myPlacesIdList = new ArrayList<>();

                for (int z = 0; z < myPlacesAuto.getPredictions().size(); z++) {
                    myPlacesIdList.add(myPlacesAuto.getPredictions().get(z).getPlaceId());

                    //     Log.i("[SEARCH]", "predic" + myPlacesAuto.getPredictions().get(z).getDescription().toString() + myPlacesAuto.getPredictions().get(z).getPlaceId());
                }

                mySharedRepoVM.setMyRestaurantList(myPlacesIdList);

            } else {

                if (!myUser.getUid().isEmpty()) {

                    if (!myFirestoreUserData.getFavoriteRestaurant().isEmpty()) {
                        myUserRestaurantId.setValue(myFirestoreUserData.getFavoriteRestaurant());
                    } else {
                        myUserRestaurantId.setValue(null);
                    }

                } else {
                    myAppMapMediator.setValue(new ViewMainState(true, "pas de restau", myUser));
                }
                myAppMapMediator.setValue(new ViewMainState(true, "liste restaur non chargée", myUser));


            }


        }
    }

    public void setMyUserRestaurantId(String myUserRestaurantId) {
        this.myUserRestaurantId.setValue(myUserRestaurantId);
    }

    public LiveData<Boolean> getLoginState() {
        return myFirebaseAuthRepoVM.getFirebaseAuthUserStateFromRepo();
    }


    public LiveData<Boolean> getMyLogin() {
        return myUserStateNew;
    }

    public void LogOut() {
        myFirebaseAuthRepoVM.logOut();
    }

    public LiveData<String> getMyUserRestaurant() {
        return myFirestoreRepo.getMyUser();
    }

    //Create user to Firestore
    public void createUser() {
        this.myFirestoreRepo.createUser();
    }

    //to publish mediatorlivedata to mainactivity
    public LiveData<ViewMainState> getMediatorLiveData() {
        return myAppMapMediator;
    }

    public void getResultAutocomplete(String query, Location location) {
        myPlaceAutocompleteRepoVM.getPlaceAutocompleteSingle(query, location);
    }

    public Location getMyPosition() {
        return myPositionRepoVM.getLocationLiveData().getValue();
    }

    public void setNotification(String uid, Boolean state) {
        if (state){
            myAlarmRepoVM.setAlarm(uid);

        }else
        {
            myAlarmRepoVM.removeAlarm();
        }
    }


    private Data notificationBuilderForWorkRequest() {

       // MyNotification montest = myViewModel.getDataForNotification(myViewModel.getMyUserRestaurant().getValue());

        String[] myWorkmates = {"un","deux","trois"};
        //new String [montest.getMyWorkmateList().size()];
//        myWorkmates = montest.getMyWorkmateList().toArray(myWorkmates);


        Data.Builder builder = new Data.Builder();

        builder.putString("restaurant_title", "Pizza del arte");

        builder.putString("restaurant_address", "12 rue du vieux moulin");

        builder.putStringArray("workmates", myWorkmates);

        return builder.build();

    }

    public void updataApp() {
        myFirestoreRepo.getFirestoreWorkmates();
        //WorkMatesListFromRepo();
    }

    public void setUser() {
        myFirestoreRepo.setupListeners();
    }

    public FirebaseUser getUser() {
        return myUserLiveData.getValue();
    }

    public void setZoom(int myZoom) {
        mySharedRepoVM.setZoom(myZoom);
    }

    public void stopPositionListener() {
        myPositionRepoVM.stopLocationRequest();
    }

    public void startPositionListener() {
        myPositionRepoVM.startLocationRequest();
    }

    public void reloadDataAfterQuery(Boolean bool) {
        mySharedRepoVM.setReloadMap(bool);
    }


    public FirebaseUser getMyUserFromFirestore(){
      return myFirestoreRepo.getCurrentUser();
    }





    public LiveData<List<WorkInfo>> getWorkInfos(){
        return mySavedInfo;
    }

    public MyNotification getDataForNotification(String placeId) {
        Log.i("[NOTIFICATION]","Liste des restaurants  : " + mySharedRepoVM.getMyRestaurantList().getValue());
//        myWorkMatesDetailList.addAll(this.myWorkMatesRepoVM.getAllWorkmatesForAnRestaurant(myUserRestaurantId.getValue()).getValue());
        Log.i("[NOTIFICATION]","My pace id " + placeId);
        List<String> teszt = new ArrayList<>();
        //teszt.addAll(myWorkMatesRepoVM.getAllWorkmatesForAnRestaurant("ChIJO5NxcizVDkgRfPGwfbKFK9I"));
        return null;
    }


    public void setupSP(Context applicationContext) {


    }

    public LiveData<List<String>> getAllWorkmatesByPlaceId() {
        return myFirestoreRepo.getAllWorkmatesForAnRestaurant("ChIJy9WiwEzVDkgRxxG08dkPb-0");
    }


/*    public MutableLiveData<String> getMyUserRestaurantId() {
        return myFirestoreRepo.getCurrentUser(). myUserRestaurantId;
    }

 */
}


