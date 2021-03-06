package cn.jkdev.taobaounion.presenter;

import cn.jkdev.taobaounion.base.IBasePresenter;
import cn.jkdev.taobaounion.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {

    /**
     * 根据分类ID去获取内容
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);//发生时产生的结果单独callback -->pagercallback


    void loadMore(int categoryId);

    void reload(int categoryId);

//    void registerViewCallback(ICategoryPagerCallback callback);
//
//    void unregisterViewCalback(ICategoryPagerCallback callback);

}
