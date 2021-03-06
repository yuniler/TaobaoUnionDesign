package cn.jkdev.taobaounion.base;

public interface IBaseCallback {
    //几种状态
    void onError();

    void onLoading();

    void onEmpty();
}
