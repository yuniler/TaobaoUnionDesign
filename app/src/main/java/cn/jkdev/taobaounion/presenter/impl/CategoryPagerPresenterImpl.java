package cn.jkdev.taobaounion.presenter.impl;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jkdev.taobaounion.model.Api;
import cn.jkdev.taobaounion.model.domain.HomePagerContent;
import cn.jkdev.taobaounion.presenter.ICategoryPagerPresenter;
import cn.jkdev.taobaounion.utils.LogUtils;
import cn.jkdev.taobaounion.utils.UrlUtils;
import cn.jkdev.taobaounion.view.ICategoryPagerCallback;
import cn.jkdev.taobaounion.view.RetrofitManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {
    private Map<Integer, Integer> pageInfo = new HashMap<>();
    public static final int DEFAULT_PAGE = 1;
    private Integer mCurrentPage;

    //通过单例来节省资源
    public CategoryPagerPresenterImpl() {

    }

    //所有请求数据由此处理--单例
    private static ICategoryPagerPresenter sInstance = null;

    public static ICategoryPagerPresenter getInstance() {
        if (sInstance == null) {
            sInstance = new CategoryPagerPresenterImpl();
        }
        return sInstance;
    }


    //设计接口通过id来分辨每个
    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {//加载中
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }


        Integer targetPage = pageInfo.get(categoryId);//先拿再设置
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pageInfo.put(categoryId, targetPage);
        }
        Call<HomePagerContent> task = createTask(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();

                LogUtils.d(CategoryPagerPresenterImpl.this, "code -- > " + code);//只是数据code
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pagerContent = response.body();//给出回应
                    LogUtils.d(CategoryPagerPresenterImpl.this, "pageContent --> " + pagerContent);//打出对于content
                    //把数据给到UI
                    handleHomPagerContentResult(pagerContent, categoryId);//数据绑定到结果里
                } else {
                    //网络错误处理
                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(CategoryPagerPresenterImpl.this, "'failure code is --> " + t.toString());
                handleNetworkError(categoryId);
            }
        });


    }

    private Call<HomePagerContent> createTask(int categoryId, Integer targetPage) {
        String homePagerUrl = UrlUtils.createHoemPagerUrl(categoryId, targetPage);
        LogUtils.d(this, "home pager url is " + homePagerUrl);
        //根据分类ID去加载内容--》拿网络数据  发起网络请求
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        //网络请求回来--》数据回来
        return api.getHomePagerContent(homePagerUrl);
    }

    private void handleNetworkError(int categoryId) {

        for (ICategoryPagerCallback callback : callbacks) {//加载错误
            if (callback.getCategoryId() == categoryId) {
                callback.onError();
            }

        }

    }

    //回调拿到后，通知
    private void handleHomPagerContentResult(HomePagerContent pagerContent, int categoryId) {
        //通过接口告诉外面
        //通知UI更新数据 -- 》下拉的数据
        List<HomePagerContent.DataBean> data = pagerContent.getData();//拿到数据
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (pagerContent == null || pagerContent.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    List<HomePagerContent.DataBean> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);//轮播图
                    callback.onContentLoad(data);//下拉数据
                }
            }


        }
    }

    @Override
    public void loadMore(int categoryId) {
        //加载更多数据
        //1.拿到当前页面
        mCurrentPage = pageInfo.get(categoryId);
        if (mCurrentPage == null) {
            mCurrentPage = 1;
        }

        //2.页面++
        mCurrentPage++;
        //3.加载数据
        Call<HomePagerContent> task = createTask(categoryId, mCurrentPage);
        //4.处理数据结果
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                //结果
                int code = response.code();
                LogUtils.d(CategoryPagerPresenterImpl.this,"result code is --- >" + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent result = response.body();
                    handleLoaderResult(result,categoryId);
                }else {
                    handleLoaderMoreError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                //请求失败
                LogUtils.d(CategoryPagerPresenterImpl.this,t.toString());
                handleLoaderMoreError(categoryId);
            }
        });

    }

    private void handleLoaderResult(HomePagerContent result, int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (result == null || result.getData().size()==0) {
                    callback.onLoaderMoreEmpty();
                }else {
                    callback.onLoaderMoreLoaded(result.getData());
                }
            }
        }
    }

    /**
     * 失败后
     * @param categoryId
     */
    private void handleLoaderMoreError(int categoryId) {
        mCurrentPage--;
        pageInfo.put(categoryId,mCurrentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId()==categoryId) {
                callback.onLoaderMoreError();
            }
        }


    }

    @Override
    public void reload(int categoryId) {

    }

    //多个页面使用callback -- 用集合保存
    private List<ICategoryPagerCallback> callbacks = new ArrayList<>();

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {

        //不存在数据，就添加
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {

    }


}
