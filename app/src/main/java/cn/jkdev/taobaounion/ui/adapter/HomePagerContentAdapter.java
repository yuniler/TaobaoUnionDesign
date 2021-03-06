package cn.jkdev.taobaounion.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jkdev.taobaounion.R;
import cn.jkdev.taobaounion.model.domain.HomePagerContent;
import cn.jkdev.taobaounion.utils.LogUtils;
import cn.jkdev.taobaounion.utils.UrlUtils;

public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.InnerHolder> {
    List<HomePagerContent.DataBean> data = new ArrayList<>();
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //将基本布局进行绑定，显示
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);

        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        HomePagerContent.DataBean dataBean = data.get(position);
        //设置数据
        holder.setData(dataBean);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //创建集合保存数据
    public void setData(List<HomePagerContent.DataBean> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();//改变
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        //将图片找到--绑定数据
        @BindView(R.id.goods_cover)
        public ImageView cover;

        @BindView(R.id.goods_goods_title)
        public TextView title;

        @BindView(R.id.goods_off_price)
        public TextView offPriceTv;

        @BindView(R.id.goods_after_off_price)
        public TextView finalPriceTv;

        @BindView(R.id.originalPrice)
        public TextView originalPriceTv;

        @BindView(R.id.goods_sell_count)
        public TextView goods_sell_count;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

//        dataBean.getCoupon_amount()---数据
        public void setData(HomePagerContent.DataBean dataBean) {
            Context context = itemView.getContext();
            title.setText(dataBean.getTitle());
            LogUtils.d(this,"url ---> " + dataBean.getPict_url());
            Glide.with(context).load(UrlUtils.getCoverPath(dataBean.getPict_url())).into(cover);//图片数据绑上
            String finalPrice = dataBean.getZk_final_price();
            long coupon_amount = dataBean.getCoupon_amount();
            LogUtils.d(this,"final price --- > " + finalPrice);
            float resultPrice = Float.parseFloat(finalPrice) - coupon_amount;
            LogUtils.d(this,"resultPrice -- > " + resultPrice);
            finalPriceTv.setText(String.format("%.2f ",resultPrice  ));
            offPriceTv.setText(String.format(context.getString(R.string.text_goods_off_price),coupon_amount));
            originalPriceTv.setText(String.format(context.getString(R.string.text_goods_original_price),finalPrice));
            originalPriceTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            goods_sell_count.setText(String.format(context.getString(R.string.text_goods_sells_count),dataBean.getVolume()));
        }
    }
}
