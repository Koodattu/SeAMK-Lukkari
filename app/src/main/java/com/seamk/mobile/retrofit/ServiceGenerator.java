package com.seamk.mobile.retrofit;

import android.text.TextUtils;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by Juha Ala-Rantala on 17.11.2017.
 */

public class ServiceGenerator {

    public static final String API_BASE_URL = "https://opendata.seamk.fi:443";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL);
                    //.addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass, String username, String password) {
        if (!TextUtils.isEmpty(username)) {
            String authToken = Credentials.basic(username, password);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null, null);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }

        return retrofit.create(serviceClass);
    }
}


