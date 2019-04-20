package com.seamk.mobile.eventbusevents;

/**
 * Created by Juha Ala-Rantala on 19.9.2017.
 */

public class RestaurantSetupChosenEvent {
    public final String restaurantCode;

    public RestaurantSetupChosenEvent(String restaurantCode) {
        this.restaurantCode = restaurantCode;
    }
}
