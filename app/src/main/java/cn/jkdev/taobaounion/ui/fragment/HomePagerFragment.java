package cn.jkdev.taobaounion.ui.fragment;

import android.os.Bundle;
import android.view.View;

import cn.jkdev.taobaounion.R;
import cn.jkdev.taobaounion.base.BaseFragment;
import cn.jkdev.taobaounion.model.domain.Categories;
import cn.jkdev.taobaounion.utils.Constants;
import cn.jkdev.taobaounion.utils.LogUtils;

/**
 * 首页的pager的view 滑动显示， 每个页面的fragment的id和title
 * 将每个页的数据和每个页的title绑定
 */
public class HomePagerFragment extends BaseFragment {

    public static HomePagerFragment newInstance(Categories.DataBean category){
        //数据拿过来Categories.DataBean category
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();//传递数据-->首页的单个fragment数据
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE,category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID,category.getId());
        homePagerFragment.setArguments(bundle);

        return homePagerFragment;

    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {

        setUpStatus(State.SUCCESS);
    }

    @Override
    protected void loadData() {

        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        int materialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID);
        //加载数据

        LogUtils.d(this,"title is --> " + title);
        LogUtils.d(this,"materialId is --> " + materialId);

    }
}
