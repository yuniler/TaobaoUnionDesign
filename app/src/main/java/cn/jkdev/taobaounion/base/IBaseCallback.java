package cn.jkdev.taobaounion.base;

public interface IBaseCallback {
    //几种状态
    void onNetworkError();

    void onLoading();

    void onEmpty();
}
