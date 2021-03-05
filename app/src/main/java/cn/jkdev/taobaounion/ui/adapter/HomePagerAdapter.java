package cn.jkdev.taobaounion.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.jkdev.taobaounion.model.domain.Categories;
import cn.jkdev.taobaounion.ui.fragment.HomeFragment;
import cn.jkdev.taobaounion.ui.fragment.HomePagerFragment;
import cn.jkdev.taobaounion.utils.LogUtils;

//给viewPager设置adapter
public class HomePagerAdapter extends FragmentPagerAdapter {//因为使用的是fragment里面套viewPager

    //声明集合--保存列表数据的集合
    private List<Categories.DataBean> categoryList = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categoryList.get(position).getTitle();//获取title

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        LogUtils.d(this,"getItem --> " + position);
        Categories.DataBean dataBean = categoryList.get(position);
        HomePagerFragment homePagerFragment = new HomePagerFragment().newInstance(dataBean);//拿到改内容
        return homePagerFragment;//返回fragment ---填数锯，绑定--也就是每个pager的内容

    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    public void setCategories(Categories categories) {
        //跑前先清空
        categoryList.clear();
        //添加数据过程
        List<Categories.DataBean> data = categories.getData();
        this.categoryList.addAll(data);//
        LogUtils.d(this,"categorylist size --->" + this.categoryList.size());
        notifyDataSetChanged();
    }
}
