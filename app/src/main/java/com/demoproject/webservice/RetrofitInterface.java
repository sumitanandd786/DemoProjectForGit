package com.demoproject.webservice;

import com.demoproject.pojo.ResponseJSON;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitInterface {
    @GET("json")
    Call<ResponseJSON> fetchData();
}
