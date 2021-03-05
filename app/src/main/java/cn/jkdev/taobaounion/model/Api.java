package cn.jkdev.taobaounion.model;

import cn.jkdev.taobaounion.model.domain.Categories;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    @GET("discovery/categories")
    Call<Categories> getCategories();
}
