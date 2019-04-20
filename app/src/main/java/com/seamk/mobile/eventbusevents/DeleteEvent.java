package com.seamk.mobile.eventbusevents;

/**
 * Created by Juha Ala-Rantala on 3.10.2017.
 */

public class DeleteEvent {
    private Object object;

    public DeleteEvent(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
