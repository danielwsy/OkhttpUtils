package com.wheel.daniel.okhttputils.bean;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author danielwang
 * @Description:
 * @date 2018/9/11 11:10
 */
public interface GankApi {
    @GET("/api/data/福利/50/1")
    Call<GankContentBean> getMovieContent();
}
