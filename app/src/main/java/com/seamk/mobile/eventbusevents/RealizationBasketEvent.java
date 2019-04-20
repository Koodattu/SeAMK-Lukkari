package com.seamk.mobile.eventbusevents;

import com.seamk.mobile.objects.BasketRealization;

/**
 * Created by Juha Ala-Rantala on 1.10.2017.
 */

public class RealizationBasketEvent {
    BasketRealization basketRealization;

    public RealizationBasketEvent(BasketRealization basketRealization) {
        this.basketRealization = basketRealization;
    }

    public BasketRealization getBasketRealization() {
        return basketRealization;
    }
}
