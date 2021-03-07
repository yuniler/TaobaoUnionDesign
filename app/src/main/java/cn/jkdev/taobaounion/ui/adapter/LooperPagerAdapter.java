package cn.jkdev.taobaounion.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.jkdev.taobaounion.model.domain.HomePagerContent;
import cn.jkdev.taobaounion.utils.UrlUtils;

public class LooperPagerAdapter extends PagerAdapter {
    //以集合方式保存数据
    private List<HomePagerContent.DataBean> mData = new ArrayList<>();


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public int getDataSize(){
        return mData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //对position的越界进行处理
        int realPosition = position % mData.size();//也就是进行循环--伪无限
        // size 5
        //0   -->  0
        //1   -->  1
        //2   -->  2
        //3   -->  3
        //4   -->  4
        //5   -->  0
        //6   -->  1

        HomePagerContent.DataBean dataBean = mData.get(realPosition);
        String coverUrl = UrlUtils.getCoverPath(dataBean.getPict_url());
        //布局
        ImageView iv = new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(container.getContext()).load(coverUrl).into(iv);
        container.addView(iv);//记得添加到container里面
        return iv;
    }

    @Override
    public int getCount() {//可滑动多少下 size控制
        return Integer.MAX_VALUE;//填满
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<HomePagerContent.DataBean> contants) {
        mData.clear();
        mData.addAll(contants);
        notifyDataSetChanged();//更新
    }
}
