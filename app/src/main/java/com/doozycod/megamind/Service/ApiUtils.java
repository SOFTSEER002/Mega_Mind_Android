package com.doozycod.megamind.Service;

import com.doozycod.megamind.Interface.ApiService;

public class ApiUtils {
    //      api service for webservices
    private ApiUtils() {}
    public static final String BASE_URL = "https://megamindabacus.tdqlabs.com/";
//    public static final String BASE_URL = "https://glneayr1ic.execute-api.ca-central-1.amazonaws.com/Development/";

    public static ApiService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
