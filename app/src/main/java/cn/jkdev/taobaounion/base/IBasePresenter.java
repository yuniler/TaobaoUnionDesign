package cn.jkdev.taobaounion.base;

import cn.jkdev.taobaounion.view.IHomeCallback;

/**
 * 抽取基类presenter
 * 使用泛型归并不同接口
 * @param <T>
 */
public interface IBasePresenter<T> {

    /**
     * 注册UI通知接口
     * @param callback
     */
    void registerViewCallback(T callback);
    /**
     * 取消注册UI通知接口
     * @param callback
     */
    void unregisterViewCallback(T callback);
}
