package tenxu.chat_input.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lqr.emoji.EmotionKeyboard;
import com.lqr.emoji.EmotionLayout;
import com.lqr.emoji.IEmotionSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnTouch;
import tenxu.chat_input.R;
import tenxu.chat_input.adapters.BottomFuncAdapter;
import tenxu.chat_input.base.BaseFragment;
import tenxu.chat_input.ui.fragments.Func1Fragment;
import tenxu.chat_input.utils.KeyBoardUtils;

import static tenxu.chat_input.contants.Contants.MY_PERMISSIONS_REQUEST_READ_CONTACTS;

/**
 * 聊天界面输入控件
 * Created by Administrator on 2017/4/22.
 */

public class ChatInput extends RelativeLayout implements TextWatcher, IEmotionSelectedListener {

    @InjectView(R.id.id_voice)
    ImageView mIdVoice;     //喇叭按钮图片
    @InjectView(R.id.id_input_content)
    EditText mIdInputContent;   //输入文字区
    @InjectView(R.id.id_to_speak)
    AudioRecordButton mIdToSpeak;      //输入语音区
    @InjectView(R.id.id_emo)
    ImageView mIdEmo;       //打开表情按钮图片
    @InjectView(R.id.id_add)
    ImageView mIdAdd;       //打开图片或者小视频功能按钮图片
    @InjectView(R.id.id_send)
    Button mIdSend;     //发送按钮

    //底部输入区域表情贴图相关控件
    @InjectView(R.id.id_bottom_fl)
    FrameLayout mIdBottomFl;
    @InjectView(R.id.id_epv)
    EmotionLayout mIdEpv;      //表情控件

    @InjectView(R.id.id_ll_buttom_func)
    LinearLayout mIdLlButtomFunc;
    @InjectView(R.id.id_vp_func)
    ViewPager mIdVpFunc;

    private Activity mActivity;

    @OnClick({R.id.id_voice, R.id.id_send})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.id_voice:
                if (requestAudio((Activity) getContext())) {
//                    因为发送语音出去，微信客户端接受到的是文件，而不是语音，所以选择屏蔽该功能
                    int needReq1 = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO);
                    int needReq2 = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    int needReq3 = ContextCompat.checkSelfPermission(mActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (needReq1 != PackageManager.PERMISSION_GRANTED
                            || needReq2 != PackageManager.PERMISSION_GRANTED || needReq3 != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(mActivity, "您已拒绝了该程序的录音权限", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.RECORD_AUDIO,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    } else {
                        mIdBottomFl.setVisibility(View.GONE);
                        switchVoiceBg();
                    }
                }
                break;
            case R.id.id_send:
//                mChatView.sendText();
                Toast.makeText(getContext(), "send success", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void setmActivity(Activity activity) {
        mActivity = activity;
    }

    public ChatInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.chat_input, this);
        ButterKnife.inject(this);

        mIdInputContent.addTextChangedListener(this);

        initEmotionPickerView();

        closeKeyBoardAndLoseFocus();

        mIdBottomFl.setVisibility(View.GONE);
        mIdEpv.setVisibility(View.GONE);
        mIdLlButtomFunc.setVisibility(View.GONE);

    }

    public EditText getInputContent() {
        return mIdInputContent;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (TextUtils.isEmpty(mIdInputContent.getText().toString())) {
            mIdAdd.setVisibility(View.VISIBLE);
            mIdSend.setVisibility(View.GONE);
        } else {
            mIdAdd.setVisibility(View.GONE);
            mIdSend.setVisibility(View.VISIBLE);
        }
    }

    @OnTouch(R.id.id_input_content)
    public boolean contentTouch() {
        if (mIdInputContent.hasFocus()) {
            closeKeyBoardAndLoseFocus();
            return true;
        } else if (mIdBottomFl.getVisibility() == View.VISIBLE) {
            closeKeyBoardAndLoseFocus();
            return true;
        }
        return false;
    }

    /**
     * 失去焦点，并关闭键盘
     */
    public void closeKeyBoardAndLoseFocus() {
        mIdInputContent.clearFocus();
        mIdBottomFl.setVisibility(View.GONE);
        KeyBoardUtils.closeKeybord(mIdInputContent, getContext());
    }

    /**
     * 输入框聚焦监听
     *
     * @param listener 监听器
     */
    public void focusListner(OnFocusChangeListener listener) {
//        if (hasFocus) {
//            messageListScrollToBottom();
//        }
        mIdInputContent.setOnFocusChangeListener(listener);
    }

    public void setText(CharSequence text) {
        mIdInputContent.setText(text);
    }

    /**
     * 设置输入模式
     */
    public void setInputMode() {
    }

    /**
     * 获取输入框文字
     */
    public Editable getText() {
        return mIdInputContent.getText();
    }

    /**
     * 点击小喇叭切换图片的方法
     */
    private void switchVoiceBg() {
        if (mIdToSpeak.getVisibility() == View.VISIBLE) {
            hideToSpeak();
        } else {
            showToSpeak();
        }
        //修改图标
        mIdVoice.setImageResource(mIdToSpeak.getVisibility() == View.VISIBLE ? R.drawable.ic_keyboard_input : R.drawable.ic_voice_input);
    }

    /**
     * 设置表情、贴图控件
     */
    private void initEmotionPickerView() {
//        mIdEpv.setWithSticker(true);
//        mIdEpv.show(this);
        mIdEpv.setEmotionSelectedListener(this);
        mIdEpv.setEmotionAddVisiable(true);
        mIdEpv.setEmotionSettingVisiable(true);
        mIdEpv.attachEditText(mIdInputContent);
    }

    /**
     * 需要在activity方法的initView中调用
     * 初始化表情内容
     */
    public void initEmotionKeyboard(Activity activity, ListView messageList, final ScrollBottomListener listener) {
        //1、创建EmotionKeyboard对象
        EmotionKeyboard mEmotionKeyboard = EmotionKeyboard.with(activity);
        //2、绑定输入框控件
        mEmotionKeyboard.bindToEditText(mIdInputContent);
        //3、绑定输入框上面的消息列表控件
        mEmotionKeyboard.bindToContent(messageList);
        //4、绑定输入框下面的底部区域（这里是把表情区和功能区共放在FrameLayout下，所以绑定的控件是FrameLayout）
        mEmotionKeyboard.setEmotionLayout(mIdBottomFl);
        //5、绑定表情按钮（可以绑定多个，如微信就有2个，一个是表情按钮，一个是功能按钮）
        mEmotionKeyboard.bindToEmotionButton(mIdEmo, mIdAdd);
        //6、当在第5步中绑定了多个EmotionButton时，这里的回调监听的view就有用了，注意是为了判断是否要自己来控制底部的显隐，还是交给EmotionKeyboard控制
        mEmotionKeyboard.setOnEmotionButtonOnClickListener(new EmotionKeyboard.OnEmotionButtonOnClickListener() {
            @Override
            public boolean onEmotionButtonOnClickListener(View view) {
                if (mIdToSpeak.getVisibility() == View.VISIBLE) {
                    hideSpeak();
                    mIdVoice.setImageResource(R.drawable.ic_voice_input);
                }
                //输入框底部显示时
                if (mIdBottomFl.getVisibility() == View.VISIBLE) {
                    //表情控件显示而点击的按钮是ivAdd时，拦截事件，隐藏表情控件，显示功能区
                    if (mIdEpv.getVisibility() == View.VISIBLE && view.getId() == R.id.id_add) {
                        mIdEpv.setVisibility(View.GONE);
                        mIdLlButtomFunc.setVisibility(View.VISIBLE);
                        return true;
                        //功能区显示而点击的按钮是ivEmo时，拦截事件，隐藏功能区，显示表情控件
                    } else if (mIdLlButtomFunc.getVisibility() == View.VISIBLE && view.getId() == R.id.id_emo) {
                        mIdEpv.setVisibility(View.VISIBLE);
                        mIdLlButtomFunc.setVisibility(View.GONE);
                        return true;
                    } else {
                        openKeyBoardAndGetFocus();
                        mIdBottomFl.setVisibility(View.GONE);
                        //滚动到最后
                        listener.scrollBottom();
                        return true;
                    }
                } else {
                    if (view.getId() == R.id.id_emo) {
                        mIdEpv.setVisibility(View.VISIBLE);
                        mIdLlButtomFunc.setVisibility(View.GONE);
                        //点击id_add，显示功能区
                    } else {
                        mIdEpv.setVisibility(View.GONE);
                        mIdLlButtomFunc.setVisibility(View.VISIBLE);
                    }
                }
                //滚动到最后
                listener.scrollBottom();
                return false;
            }
        });
    }

    /**
     * 需要在activity方法的initView中调用
     * 初始化底部功能区
     */
    public Func1Fragment initBottomFunc(FragmentManager manager, Func1Fragment.PrepareFunc prepareFunc) {
        //底部功能区
        List<BaseFragment> fragments = new ArrayList<>();
        Func1Fragment func1Fragment1 = Func1Fragment.getInstance(this,prepareFunc,mIdBottomFl,mIdLlButtomFunc,mIdEpv);
        fragments.add(func1Fragment1);
        BottomFuncAdapter bottomFucAdapter = new BottomFuncAdapter(manager, fragments);
        mIdVpFunc.setAdapter(bottomFucAdapter);
        return func1Fragment1;
    }

    /**
     * 需要在activity方法的initView中调用
     * 初始化语音录制返回监听
     */
    public void initAudioFinishRecorder(AudioRecordButton.AudioFinishRecorderListener listener) {
        mIdToSpeak.setAudioFinishRecorderListener(listener);
//        uploadFileToServer(seconds, filePath, 1, null);
    }

    @Override
    public void onEmojiSelected(String key) {
    }

    @Override
    public void onStickerSelected(String categoryName, String stickerName, String stickerBitmapPath) {
    }

    public interface ScrollBottomListener {
        void scrollBottom();
    }

    private void showToSpeak() {
        mIdToSpeak.setVisibility(View.VISIBLE);
        mIdInputContent.setVisibility(View.GONE);
        //关闭键盘
        closeKeyBoardAndLoseFocus();
    }

    private void hideToSpeak() {
        hideSpeak();
        //打开键盘
        openKeyBoardAndGetFocus();
    }

    private void hideSpeak(){
        mIdToSpeak.setVisibility(View.GONE);
        mIdInputContent.setVisibility(View.VISIBLE);
    }

    /**
     * 获取焦点，并打开键盘
     */
    private void openKeyBoardAndGetFocus() {
        mIdInputContent.requestFocus();
        KeyBoardUtils.openKeybord(mIdInputContent, getContext());
    }

    /**
     * 申请录制小视频
     */
    public boolean requestVideo(Activity activity) {
        if (afterM()) {
            final List<String> permissionsList = new ArrayList<>();
            if ((activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if (permissionsList.size() != 0) {
                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
            int hasPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    /**
     * 申请相册
     */
    public boolean requestCamera(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.CAMERA);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private final int REQUEST_CODE_ASK_PERMISSIONS = 100;

    /**
     * 申请录音
     */
    public boolean requestAudio(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    /**
     * 申请访问文件
     */
    public boolean requestStorage(Activity activity) {
        if (afterM()) {
            int hasPermission = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return false;
            }
        }
        return true;
    }

    private boolean afterM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
