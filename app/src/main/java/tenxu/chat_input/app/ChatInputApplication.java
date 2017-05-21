package tenxu.chat_input.app;

import android.app.Application;
import android.content.Context;

import com.lqr.emoji.LQREmotionKit;

/**
 * Created by Administrator on 2017/5/21.
 */

public class ChatInputApplication extends Application {

    @Override
    public void onCreate() {
        Context mContext = getApplicationContext();
        LQREmotionKit.init(mContext);
    }
}
