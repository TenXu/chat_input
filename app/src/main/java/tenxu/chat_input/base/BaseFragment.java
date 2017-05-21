package tenxu.chat_input.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

/**
 * 基础Fragment
 * Created by Administrator on 2017/2/24.
 */

public abstract class BaseFragment extends Fragment implements Serializable {

    public boolean mIsUserVisible = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        init();
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        initData();
        initData(savedInstanceState);
        initListener();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 初始化fview，而且是必须实现，但是不知道具体实现，定义成抽象方法
     *
     * @return view
     */
    public abstract View initView();

    public void init() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        mIsUserVisible = isVisibleToUser;
    }

    public void initData() {
    }

    public void initData(Bundle savedInstanceState) {

    }

    public void initListener() {

    }
}
