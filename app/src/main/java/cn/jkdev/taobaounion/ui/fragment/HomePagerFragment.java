package cn.jkdev.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import butterknife.BindView;
import cn.jkdev.taobaounion.R;
import cn.jkdev.taobaounion.base.BaseFragment;
import cn.jkdev.taobaounion.model.domain.Categories;
import cn.jkdev.taobaounion.model.domain.HomePagerContent;
import cn.jkdev.taobaounion.presenter.ICategoryPagerPresenter;
import cn.jkdev.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import cn.jkdev.taobaounion.ui.adapter.HomePagerContentAdapter;
import cn.jkdev.taobaounion.ui.adapter.LooperPagerAdapter;
import cn.jkdev.taobaounion.utils.Constants;
import cn.jkdev.taobaounion.utils.LogUtils;
import cn.jkdev.taobaounion.utils.SizeUtils;
import cn.jkdev.taobaounion.view.ICategoryPagerCallback;

/**UI层
 * 首页的pager的view 滑动显示， 每个页面的fragment的id和title
 * 将每个页的数据和每个页的title绑定
 */
public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {


    private ICategoryPagerPresenter mPagerPresenter;
    private int mMaterialId;
    private HomePagerContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

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

    @BindView(R.id.looper_pager)
    public ViewPager looperPager;

    @BindView(R.id.home_paegr_title)
    public TextView currentCategoriesTitleTv;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_refresh)
     public TwinklingRefreshLayout twinklingRefreshLayout;



    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initListener() {
        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mLooperPagerAdapter.getDataSize() == 0) {
                    return;
                }
                //对position进行处理,放在数据越界
                int targetPosition = position % mLooperPagerAdapter.getDataSize();
                // 切换指示器
                updataLooperIndicator(targetPosition);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d(HomePagerFragment.this,"触发了加载更多 ...");
                //真正加载更多数据回来
                if (mPagerPresenter != null) {
                    mPagerPresenter.loadMore(mMaterialId);
                }
            }
        });
    }

    /**
     *切换指示器
     * @param targetPosition
     */
    private void updataLooperIndicator(int targetPosition) {
        for(int i  = 0; i < looperPointContainer.getChildCount(); i++){
            View point = looperPointContainer.getChildAt(i);

            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_unselect_point);
            }
        }

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

        //创建looper适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        //设置looper适配器
        looperPager.setAdapter(mLooperPagerAdapter);

        //设置refresh相关属性
        twinklingRefreshLayout.setEnableRefresh(false);//上拉
        twinklingRefreshLayout.setEnableLoadmore(true);//下拉
//        twinklingRefreshLayout.setBottomView();

    }

    @Override
    protected void initPresenter() {
        //当前类作为回调
        mPagerPresenter = CategoryPagerPresenterImpl.getInstance();
        mPagerPresenter.registerViewCallback(this);//立马注册



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
        if (mPagerPresenter != null) {
            mPagerPresenter.getContentByCategoryId(mMaterialId);
        }
        if (currentCategoriesTitleTv != null) {
            currentCategoriesTitleTv.setText(title);
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
    public int getCategoryId() {
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
        mLooperPagerAdapter.setData(contants);
        LogUtils.d(this,"looper size --> " + contants.size());
        //中间点 % 2的size 不一定为 0 ，所以可能不是第一个
        int dx = (Integer.MAX_VALUE / 2) - contants.size();
        int targetCenterPosition = (Integer.MAX_VALUE / 2) - dx;
        //将looper设置到中间点
        looperPager.setCurrentItem(targetCenterPosition);
        LogUtils.d(this,"   url -- > " + contants.get(0).getPict_url());//拿到图片第一个

        looperPointContainer.removeAllViews();
//        GradientDrawable selectedDrawable = (GradientDrawable) getContext().getDrawable(R.drawable.shape_indicator_point);
//        GradientDrawable normalDrawable = (GradientDrawable) getContext().getDrawable(R.drawable.shape_indicator_unselect_point);
//        normalDrawable.setColor(getContext().getColor(R.color.white)); -- 为什么显示不出来

        //添加点
        for (int i = 0; i < contants.size();i++){
            View point = new View(getContext());
            //设置view形式
            int size = SizeUtils.dip2px(getContext(), 8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size,size);
            layoutParams.leftMargin = SizeUtils.dip2px(getContext(),5);
            layoutParams.rightMargin = SizeUtils.dip2px(getContext(),5);
            //添加view，及设置颜色
            point.setLayoutParams(layoutParams);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_indicator_point);
            }else {
                point.setBackgroundResource(R.drawable.shape_indicator_unselect_point);
            }

            looperPointContainer.addView(point);
        }
    }

    /**
     * 防止数据一致叠加
     */
    @Override
    protected void release() {

        if (mPagerPresenter != null) {
            mPagerPresenter.unregisterViewCallback(this);
        }
    }
}
