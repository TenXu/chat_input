package tenxu.chat_input.ui.fragments;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.lqr.emoji.EmotionLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import tenxu.chat_input.R;
import tenxu.chat_input.base.BaseFragment;
import tenxu.chat_input.interfaces.FuncShowInterface;
import tenxu.chat_input.listeners.FuncShowListener;
import tenxu.chat_input.view.ChatInput;

/**
 * 底部功能Fragment
 * Created by Administrator on 2017/3/2.
 */

public class Func1Fragment extends BaseFragment implements FuncShowInterface {

    @InjectView(R.id.tl_func)
    ViewGroup mLlFunc;

    @InjectView(R.id.id_ll_pic)
    LinearLayout mLlPic;
    @InjectView(R.id.id_ll_link)
    LinearLayout mLlLink;
    @InjectView(R.id.id_ll_file)
    LinearLayout mLlFile;       //文件

    public Func1Fragment() {
    }

    private static Func1Fragment func1Fragment = new Func1Fragment();

    private static ChatInput mChatInput;

    private static PrepareFunc mPrepareFunc;
    private static LinearLayout mParentButtomFunc;
    private static EmotionLayout mParentEpv;
    private static FrameLayout mParentBottomFl;

    public static Func1Fragment getInstance(ChatInput chatInput,
                                            PrepareFunc prepareFunc,FrameLayout bottomFl,
                                            LinearLayout llButtomFunc, EmotionLayout epv) {
        mChatInput = chatInput;
        mParentBottomFl=bottomFl;
        mPrepareFunc = prepareFunc;
        mParentButtomFunc=llButtomFunc;
        mParentEpv=epv;
        return func1Fragment;
    }

    @OnClick({R.id.id_ll_pic, R.id.id_ll_link,R.id.id_ll_file,R.id.id_ll_collect})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.id_ll_pic:
                mPrepareFunc.prepareFunc1(mChatInput);
                break;
            case R.id.id_ll_file:
                mPrepareFunc.prepareFunc2(mChatInput);
                break;
            case R.id.id_ll_collect:
                mPrepareFunc.prepareFunc3(mChatInput);
                break;
            case R.id.id_ll_link:
                mPrepareFunc.prepareFunc4(mChatInput);
                break;
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_func_page1, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void initListener() {

    }

    public void initFuncListener(){
        FuncShowListener.addFuncShowInterface(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onDestroyView() {
        FuncShowListener.removeFuncShowInterface(this);
        super.onDestroyView();
    }

    @Override
    public void funcShow(Activity activity) {
        if(mParentBottomFl!=null&&mParentBottomFl.getVisibility()==View.VISIBLE||
                mParentButtomFunc!=null&&mParentButtomFunc.getVisibility()==View.VISIBLE||
                mParentEpv!=null&&mParentEpv.getVisibility()==View.VISIBLE){
            mParentBottomFl.setVisibility(View.GONE);
            mParentEpv.setVisibility(View.GONE);
            mParentButtomFunc.setVisibility(View.GONE);
        }else{
            activity.finish();
        }
    }

    public interface PrepareFunc {
        void prepareFunc1(ChatInput chatInput);
        void prepareFunc2(ChatInput chatInput);
        void prepareFunc3(ChatInput chatInput);
        void prepareFunc4(ChatInput chatInput);
    }
}
