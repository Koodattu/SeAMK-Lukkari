package com.seamk.mobile.eventbusevents;

/**
 * Created by Juha Ala-Rantala on 25.3.2018.
 */

public class ItemAddedEvent {
    private Object object;

    public ItemAddedEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
