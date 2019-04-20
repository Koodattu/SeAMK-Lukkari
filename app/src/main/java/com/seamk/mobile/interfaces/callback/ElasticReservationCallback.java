package com.seamk.mobile.interfaces.callback;

import com.seamk.mobile.elasticsearch.ElasticReservation;

/**
 * Created by Juha Ala-Rantala on 17.3.2018.
 */

public interface ElasticReservationCallback {
    void onSuccess(ElasticReservation elasticReservation);
    void onFailure(Throwable throwable);
}
