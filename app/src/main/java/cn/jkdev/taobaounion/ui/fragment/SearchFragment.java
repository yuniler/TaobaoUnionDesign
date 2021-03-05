package cn.jkdev.taobaounion.ui.fragment;

import android.view.View;

import cn.jkdev.taobaounion.R;
import cn.jkdev.taobaounion.base.BaseFragment;

/**
 * 有四个fragent一样，抽取基类,
 */
public class SearchFragment extends BaseFragment {
    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_research;
    }
    @Override
    protected void initView(View rootView) {

        setUpStatus(State.SUCCESS);
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_home,container,false);
//    }
}
