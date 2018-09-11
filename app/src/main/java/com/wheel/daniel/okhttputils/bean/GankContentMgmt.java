package com.wheel.daniel.okhttputils.bean;

import com.wheel.daniel.okhttputils.network.BaseHttpUtils;
import com.wheel.daniel.okhttputils.utils.UrlConfig;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author danielwang
 * @Description: 同步获取信息，需在子线程中调用
 * @date 2018/9/11 11:16
 */
public class GankContentMgmt {

    public static GankContentBean getMovieContents() {
        BaseHttpUtils baseHttpUtils = new BaseHttpUtils(UrlConfig.getUrl());
        Retrofit retrofit = baseHttpUtils.getRetrofit();
        GankApi api = retrofit.create(GankApi.class);
        Call<GankContentBean> call = api.getMovieContent();
        try {
            Response<GankContentBean> response = call.execute();
            GankContentBean responseBean = response.body();
            if (responseBean != null) {
                return responseBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
