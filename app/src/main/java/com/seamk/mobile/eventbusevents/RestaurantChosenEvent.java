package com.seamk.mobile.eventbusevents;

/**
 * Created by Juha Ala-Rantala on 9.8.2017.
 */

public class RestaurantChosenEvent {

    public final int restaurantCode;

    public RestaurantChosenEvent(int restaurantCode)
    {
        this.restaurantCode = restaurantCode;
    }
}
