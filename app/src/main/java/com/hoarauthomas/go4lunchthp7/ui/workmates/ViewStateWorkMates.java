package com.hoarauthomas.go4lunchthp7.ui.workmates;

import com.hoarauthomas.go4lunchthp7.model.SpecialWorkMates;

import java.util.List;

public class ViewStateWorkMates {

    List<SpecialWorkMates> mySpecialWorkMAtes;
   // List<User> myWorkMatesList;
   // List<RestaurantPojo> myRestaurantList;
    //RestaurantPojo myRestaurantResult;




    public ViewStateWorkMates(List<SpecialWorkMates> mySpecial) {
        this.mySpecialWorkMAtes = mySpecial;
    }

    public List<SpecialWorkMates> getMySpecialWorkMAtes() {
        return mySpecialWorkMAtes;
    }


}
