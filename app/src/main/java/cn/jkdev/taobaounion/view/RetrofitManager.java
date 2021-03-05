package cn.jkdev.taobaounion.view;

import cn.jkdev.taobaounion.R;
import cn.jkdev.taobaounion.utils.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static final RetrofitManager outInstance = new RetrofitManager();
    private final Retrofit mRetrofit;


    public static RetrofitManager getInstance() {
        return outInstance;
    }
    private RetrofitManager(){
        //创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public Retrofit getRetrofit(){
        return mRetrofit;
    }

}
