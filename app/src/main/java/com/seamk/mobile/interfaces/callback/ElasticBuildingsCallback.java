package com.seamk.mobile.interfaces.callback;

import com.seamk.mobile.elasticsearch.ElasticBuildings;

/**
 * Created by Juha Ala-Rantala on 17.3.2018.
 */

public interface ElasticBuildingsCallback {
    void onSuccess(ElasticBuildings elasticBuildings);
    void onFailure(Throwable throwable);
}
