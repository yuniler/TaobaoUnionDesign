package cn.jkdev.taobaounion.view;

import java.util.List;

import cn.jkdev.taobaounion.model.domain.HomePagerContent;

public interface ICategoryPagerCallback {

    /**
     * 数据加载回来 --》chengg
     * @param contents
     */
    void onContentLoad(List<HomePagerContent.DataBean> contents);//拿取数据的contents

    /**
     * 数据加载中
     * @param categoryId
     */
    void onLoading(int categoryId);
    /**
     * 网络错误
     * @param categoryId
     */
    void onError(int categoryId);
    /**
     * 数据为空
     * @param categoryId
     */
    void onEmpty(int categoryId);

    /**
     * 加载更多网络错误
     * @param categoryId
     */
    void onLoaderMoreError(int categoryId);

    /**
     * 没有更多内容
     * @param categoryId
     */
    void onLoaderMoreEmpty(int categoryId);

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
