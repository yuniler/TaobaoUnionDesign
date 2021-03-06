package cn.jkdev.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import cn.jkdev.taobaounion.R;
import cn.jkdev.taobaounion.base.BaseFragment;
import cn.jkdev.taobaounion.model.domain.Categories;
import cn.jkdev.taobaounion.model.domain.HomePagerContent;
import cn.jkdev.taobaounion.presenter.ICategoryPagerPresenter;
import cn.jkdev.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import cn.jkdev.taobaounion.ui.adapter.HomePagerContentAdapter;
import cn.jkdev.taobaounion.utils.Constants;
import cn.jkdev.taobaounion.utils.LogUtils;
import cn.jkdev.taobaounion.view.ICategoryPagerCallback;

/**
 * 首页的pager的view 滑动显示， 每个页面的fragment的id和title
 * 将每个页的数据和每个页的title绑定
 */
public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {


    private ICategoryPagerPresenter mICategoryPagerPresenter;
    private int mMaterialId;
    private HomePagerContentAdapter mContentAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category){
        //数据拿过来Categories.DataBean category
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();//传递数据-->首页的单个fragment数据
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE,category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID,category.getId());
        homePagerFragment.setArguments(bundle);

        return homePagerFragment;
    }
    @BindView(R.id.home_pager_content_view)
    public RecyclerView mContentList;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {

        //设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {//就是adapter和view绑定--显示
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 8;//item的上下边距
                outRect.bottom = 8;
            }
        });
        //创建适配器
        mContentAdapter = new HomePagerContentAdapter();
        //设置适配器
        mContentList.setAdapter(mContentAdapter);

    }

    @Override
    protected void initPresenter() {
        //当前类作为回调
        mICategoryPagerPresenter = CategoryPagerPresenterImpl.getInstance();
        mICategoryPagerPresenter.registerViewCallback(this);//立马注册



    }

    @Override
    protected void loadData() {

        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        //加载数据

        LogUtils.d(this,"title is --> " + title);
        LogUtils.d(this,"materialId is --> " + mMaterialId);
        //加载 TODO:
        if (mICategoryPagerPresenter != null) {
            mICategoryPagerPresenter.getContentByCategoryId(mMaterialId);
        }


    }

    @Override
    public void onContentLoad(List<HomePagerContent.DataBean> contents) {

        //数据列表加载到了
        mContentAdapter.setData(contents);
        // 更新UI
        setUpStatus(State.SUCCESS);

    }

    @Override
    public int getCategory() {
        return mMaterialId;

    }

    @Override
    public void onLoading() {

        setUpStatus(State.LOADING);
    }

    @Override
    public void onError() {
//        if (mMaterialId != categoryId) {//对应id才是找到了
//            return;
//        }
        //网络错误
        setUpStatus(State.ERROR);
    }

    @Override
    public void onEmpty() {
        //定义接口，整合代码--》太多return

        setUpStatus(State.EMPTY);
    }

    @Override
    public void onLoaderMoreError() {

    }

    @Override
    public void onLoaderMoreEmpty() {

    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contants) {

    }

    /**
     * 防止数据一致叠加
     */
    @Override
    protected void release() {

        if (mICategoryPagerPresenter != null) {
            mICategoryPagerPresenter.unregisterViewCallback(this);
        }
    }
}
