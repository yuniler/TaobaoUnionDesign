package cn.jkdev.taobaounion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jkdev.taobaounion.R;
import cn.jkdev.taobaounion.utils.LogUtils;

/**
 * 抽取基类。相同的fragment
 * 只是不同的view，进行abstract子类传递就可以
 */
public abstract class BaseFragment extends Fragment {

    private State currentState = State.NONE;
    private View mSuccessView;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;

    //枚举状态
    public enum State {
        NONE,LOADING,SUCCESS,ERROR,EMPTY
    }

    private Unbinder mBind;
    private FrameLayout mBaseContainer;//将几个变化页面加载到这个layout中

    @OnClick(R.id.network_error_tips)
    public void retry(){
        //点击了重新加载内容
        LogUtils.d(this,"on retry ...");
        onRetryClick();
    }

    /**
     * 如果子fragment知道网络错误后的点击，哪覆盖方法即可
     */
    protected void onRetryClick() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {//创建view

        //载view
        View rootView = loadRootView(inflater,container);
        mBaseContainer = rootView.findViewById(R.id.base_container);
        loadStatusView(inflater, container);


        mBind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initListener();
        initPresenter();
        loadData();
        return rootView;
    }


    protected void initListener() {

    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    /**
     * 加载各种状态的view
     *
     * @param inflater
     * @param container
     */
    private void loadStatusView(LayoutInflater inflater, ViewGroup container) {


        //加载成功的view
        //view直接为success
        mSuccessView = loadSuccessView(inflater, container);
        mBaseContainer.addView(mSuccessView);//将每种状态添加进去

        //加载loading页面
        mLoadingView = loadLoadingView(inflater, container);
        mBaseContainer.addView(mLoadingView);

        //加载错误页面
        mErrorView = loadErrorView(inflater, container);
        mBaseContainer.addView(mErrorView);

        //内容为空的页面
        mEmptyView = loadEmptyView(inflater, container);
        mBaseContainer.addView(mEmptyView);

        setUpStatus(State.NONE);

    }

    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error,container,false);
    }

    protected View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty,container,false);
    }

    /**
     * 子类通过改方法切换页面
     * 暴露方法去切换状态
     */
    public void setUpStatus(State status){
        this.currentState = status;
        //控制显示隐藏
        mSuccessView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);

        mLoadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
//        if (currentState == State.LOADING) {
//            mLoadingView.setVisibility(View.VISIBLE);//显示
//        } else {
//            mLoadingView.setVisibility(View.GONE);//隐藏
//        }
        mErrorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);

        mEmptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);
    }

    /**
     * 加载loading 功能
     * @param inflater
     * @param container
     */
    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    protected void initView(View rootView) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();//结束时关闭-解绑
        }
        release();
    }

    protected void release() {
        //释放资源
    }

    protected void initPresenter() {
        //创建presenter

    }

    protected void loadData() {
        //加载数据
    }

    //view的写法就是这样
    protected View loadSuccessView(LayoutInflater inflater, ViewGroup container) {//加载view.  id,container,false
        int resId = getRootViewResId();
        return inflater.inflate(resId, container, false);
    }//返回view

    protected abstract int getRootViewResId();//子类实现返回id


}
