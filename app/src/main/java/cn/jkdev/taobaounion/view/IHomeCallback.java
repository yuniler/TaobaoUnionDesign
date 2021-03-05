package cn.jkdev.taobaounion.view;

import cn.jkdev.taobaounion.base.IBaseCallback;
import cn.jkdev.taobaounion.model.domain.Categories;

public interface IHomeCallback extends IBaseCallback {

    void onCategoriesLoaded(Categories categories);


}
