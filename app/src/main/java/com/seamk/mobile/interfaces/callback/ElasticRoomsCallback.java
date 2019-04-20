package com.seamk.mobile.interfaces.callback;

import com.seamk.mobile.elasticsearch.ElasticRooms;

/**
 * Created by Juha Ala-Rantala on 17.3.2018.
 */

public interface ElasticRoomsCallback {
    void onSuccess(ElasticRooms elasticRooms);
    void onFailure(Throwable throwable);
}
