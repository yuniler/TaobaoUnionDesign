package cn.jkdev.taobaounion.view;

import java.util.List;

import cn.jkdev.taobaounion.base.IBaseCallback;
import cn.jkdev.taobaounion.model.domain.HomePagerContent;

public interface ICategoryPagerCallback extends IBaseCallback {

    /**
     * 数据加载回来 --》chengg
     * @param contents
     */
    void onContentLoad(List<HomePagerContent.DataBean> contents);//拿取数据的contents

    int getCategory();//让UI层去返回



    /**
     * 加载更多网络错误

     */
    void onLoaderMoreError();

    /**
     * 没有更多内容

     */
    void onLoaderMoreEmpty();

    /**
     * 加载到了更多内容
     * @param contents
     */
    void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents);

    /**
     *
     * 轮播图内容加载到了
     * @param contants
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contants);
}
