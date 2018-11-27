package com.wheel.daniel.okhttputils.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;

import com.wheel.daniel.okhttputils.bean.GankContentBean;
import com.wheel.daniel.okhttputils.bean.GankContentMgmt;
import com.wheel.daniel.okhttputils.utils.ThreadMgrUtils;


/**
 * @author danielwang
 * @Description: 获取图片信息，装载到LivaData中
 * @date 2018/9/11 14:07
 */
public class GankModel extends ViewModel {

    private MutableLiveData<GankContentBean> liveData = new MutableLiveData<>();


    public void setValue(GankContentBean item) {
        //在UI线程中调用
//        liveData.setValue(item);
        //在子线程中调用
        liveData.postValue(item);
    }

    public LiveData<GankContentBean> getValue() {
        return liveData;
    }


    public void loadData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final GankContentBean bean = GankContentMgmt.getMovieContents();
                if (bean != null) {
                    setValue(bean);
                }
            }
        };
        ThreadMgrUtils.executeNetworkTask(runnable);
    }
}
