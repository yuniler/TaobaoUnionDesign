package cn.jkdev.taobaounion.model;

import cn.jkdev.taobaounion.model.domain.Categories;
import cn.jkdev.taobaounion.model.domain.HomePagerContent;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api {
    /**
     * 分类标签数据
     * @return
     */
    @GET("discovery/categories")
    Call<Categories> getCategories();

    /**
     * 分类具体数据
     * @param url
     * @return
     */
    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);
}
