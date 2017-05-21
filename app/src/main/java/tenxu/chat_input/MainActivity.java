package tenxu.chat_input;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tenxu.chat_input.interfaces.FuncShowInterface;
import tenxu.chat_input.listeners.FuncShowListener;
import tenxu.chat_input.ui.fragments.Func1Fragment;
import tenxu.chat_input.view.AudioRecordButton;
import tenxu.chat_input.view.ChatInput;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.id_content_lv)
    ListView mIdContentLv;
    @InjectView(R.id.input_panel)
    ChatInput mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mInput.initEmotionKeyboard(this, mIdContentLv, new ChatInput.ScrollBottomListener() {
            @Override
            public void scrollBottom() {
            }
        });

        Func1Fragment fragment = mInput.initBottomFunc(getSupportFragmentManager(), new Func1Fragment.PrepareFunc() {
            @Override
            public void prepareFunc1(ChatInput chatInput) {
//                if (chatInput.requestCamera(MainActivity.this) && chatInput.requestStorage(MainActivity.this)) {
//                    Intent intent = new Intent(MainActivity.this, ImageGridActivity.class);
//                    startActivityForResult(intent, IMAGE_PICKER);
//                }
            }

            @Override
            public void prepareFunc2(ChatInput chatInput) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("*/*");
//                startActivityForResult(intent, FILE_CODE);
            }

            @Override
            public void prepareFunc3(ChatInput chatInput) {

            }

            @Override
            public void prepareFunc4(ChatInput chatInput) {

            }
        });
        fragment.initFuncListener();

        mInput.initAudioFinishRecorder(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
//                uploadFileToServer(seconds, filePath, Constants.VOICE_TYPE, null);
            }
        });
        mInput.setmActivity(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            for (FuncShowInterface funcShowInterface : FuncShowListener.getsFuncShowInterfaces()) {
                funcShowInterface.funcShow(MainActivity.this);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
