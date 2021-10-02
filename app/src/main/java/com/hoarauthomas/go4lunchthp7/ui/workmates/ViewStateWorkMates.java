package com.hoarauthomas.go4lunchthp7.ui.workmates;

import com.hoarauthomas.go4lunchthp7.model.WorkmatesPojoForUI;

import java.util.List;

public class ViewStateWorkMates {

    List<WorkmatesPojoForUI> mySpecialWorkMAtes;
   // List<User> myWorkMatesList;
   // List<RestaurantPojo> myRestaurantList;
    //RestaurantPojo myRestaurantResult;




    public ViewStateWorkMates(List<WorkmatesPojoForUI> mySpecial) {
        this.mySpecialWorkMAtes = mySpecial;
    }

    public List<WorkmatesPojoForUI> getMySpecialWorkMAtes() {
        return mySpecialWorkMAtes;
    }


}
