package cn.jkdev.taobaounion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import cn.jkdev.taobaounion.R;
import cn.jkdev.taobaounion.base.BaseFragment;
import cn.jkdev.taobaounion.model.domain.Categories;
import cn.jkdev.taobaounion.presenter.IHomePresenter;
import cn.jkdev.taobaounion.presenter.impl.HomePresenterImpl;
import cn.jkdev.taobaounion.ui.adapter.HomePagerAdapter;
import cn.jkdev.taobaounion.utils.LogUtils;
import cn.jkdev.taobaounion.view.IHomeCallback;

/**
 * 有四个fragent一样，抽取基类,
 */
public class HomeFragment extends BaseFragment implements IHomeCallback {

    //初始化指示器
    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;

    private IHomePresenter mHomePresenter;

    @BindView(R.id.home_pager)
    public ViewPager homepager;
    private HomePagerAdapter mHomePagerAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        //初始化viewPager
        mTabLayout.setupWithViewPager(homepager);//Tab和viewPager进行绑定
        //设置适配器
        //和main里不一样了
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());//之后和加载数据绑定

        //绑定adapter
        homepager.setAdapter(mHomePagerAdapter);

    }

    @Override
    protected void initPresenter() {

        mHomePresenter = new HomePresenterImpl();
        mHomePresenter.registerViewCallback(this);
    }

    /**
     * 子类实现的一个例子，并不影响Base。。。
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {

        return inflater.inflate(R.layout.base_home_fragment_layout,container,false);
    }

    @Override
    protected void loadData() {
//        setUpStatus(State.LOADING);

        mHomePresenter.getCategories();

    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        //交给逻辑层判断 -- 》 impl
//        if (categories == null || categories.getData().size() == 0) {
//            setUpStatus(State.EMPTY);
//        } else {

//        }
        setUpStatus(State.SUCCESS);
        LogUtils.d(this,"onCategoriesLoaded ... ");
        //加载数据回来

        if (mHomePagerAdapter != null) {//将数据判空
            mHomePagerAdapter.setCategories(categories);//view传回来，更新adapter
        }
    }

    @Override
    public void onError() {
        setUpStatus(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpStatus(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpStatus(State.EMPTY);
    }

    @Override
    protected void release() {
        if (mHomePresenter != null) {
            mHomePresenter.unregisterViewCallback(this);
        }

    }

    @Override
    protected void onRetryClick() {
        //网络错误点击了重试
        //重新加载分类内容
        if (mHomePresenter != null) {
            mHomePresenter.getCategories();
        }


    }
    //    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_home,container,false);
//    }
}
