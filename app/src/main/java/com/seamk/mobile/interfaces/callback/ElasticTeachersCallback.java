package com.seamk.mobile.interfaces.callback;

import com.seamk.mobile.elasticsearch.ElasticTeachers;

/**
 * Created by Juha Ala-Rantala on 17.3.2018.
 */

public interface ElasticTeachersCallback {
    void onSuccess(ElasticTeachers elasticTeachers);
    void onFailure(Throwable throwable);
}
