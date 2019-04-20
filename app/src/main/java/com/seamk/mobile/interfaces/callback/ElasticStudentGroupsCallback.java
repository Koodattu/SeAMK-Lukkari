package com.seamk.mobile.interfaces.callback;

import com.seamk.mobile.elasticsearch.ElasticStudentGroups;

/**
 * Created by Juha Ala-Rantala on 17.3.2018.
 */

public interface ElasticStudentGroupsCallback {
    void onSuccess(ElasticStudentGroups elasticStudentGroups);
    void onFailure(Throwable throwable);
}
