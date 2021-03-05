package cn.jkdev.taobaounion.presenter.impl;

import android.util.Log;

import java.net.HttpURLConnection;

import cn.jkdev.taobaounion.model.Api;
import cn.jkdev.taobaounion.model.domain.Categories;
import cn.jkdev.taobaounion.presenter.IHomePresenter;
import cn.jkdev.taobaounion.utils.LogUtils;
import cn.jkdev.taobaounion.view.IHomeCallback;
import cn.jkdev.taobaounion.view.RetrofitManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {

    private IHomeCallback mCallback = null;

    @Override
    public void getCategories() {
        if (mCallback != null) {
            mCallback.onLoading();
        }

        //加载分类数据
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                //结果
                int code = response.code();
                LogUtils.d(HomePresenterImpl.this, "result code -->  " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    //请求成功
                    Categories categories = response.body();

                    if (mCallback != null) {
                        if (categories == null || categories.getData().size() == 0) {
                            mCallback.onEmpty();
                        } else {
                            //LogUtils.d(HomePresenterImpl.this,categories.toString() + "");
                            mCallback.onCategoriesLoaded(categories);//--》homeFrag就会有数据
                        }

                    }
                } else {
                    //请求失败
                    LogUtils.i(HomePresenterImpl.this, "请求失败");

                    if (mCallback != null) {
                        mCallback.onNetworkError();
                    }
                }

            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                //加载失败结果
                LogUtils.e(HomePresenterImpl.this, "请求失败" + t);
                if (mCallback != null) {
                    mCallback.onNetworkError();
                }
            }
        });

    }

    @Override
    public void registerViewCallback(IHomeCallback callback) {
        //数据回调--实时的数据--只有一个页面使用到分类--喜马拉雅俩个播放状态-需俩个callback
        this.mCallback = callback;

    }

    @Override
    public void unregisterViewCallback(IHomeCallback callback) {
        mCallback = null;
    }
}
