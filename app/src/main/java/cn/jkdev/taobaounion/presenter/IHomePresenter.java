package cn.jkdev.taobaounion.presenter;

import cn.jkdev.taobaounion.base.IBaseCallback;
import cn.jkdev.taobaounion.base.IBasePresenter;
import cn.jkdev.taobaounion.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    /**
     * 获取商品分类
     *
     */
    void getCategories();


}
