package tenxu.chat_input.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import tenxu.chat_input.base.BaseFragment;

/**
 * 底部功能适配器
 * Created by Administrator on 2017/3/2.
 */
public class BottomFuncAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mFragments;

    public BottomFuncAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
