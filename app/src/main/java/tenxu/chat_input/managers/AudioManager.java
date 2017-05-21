package tenxu.chat_input.managers;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Audio管理类
 * Created by Administrator on 2016/8/21.
 */
public class AudioManager {

    private boolean isPrepared;

    private MediaRecorder mMediaRecorder;
    private String mDir;
    private String mCurrentFilePath;

    private static AudioManager mInstance;

    private AudioManager(String dir){
        mDir=dir;
    }

    public String getCurrentFilePath() {
        return mCurrentFilePath;
    }

    /**
     * 鬼掉准备完毕
     */
    public interface AudioStateListener{
        void wellPrepared();
    }

    public AudioStateListener mListener;

    public void setOnAudioStateListener(AudioStateListener listener){
        mListener=listener;
    }

    public static AudioManager getInstance(String dir){
        if(mInstance==null){
            synchronized (AudioManager.class){
                if(mInstance==null){
                    mInstance=new AudioManager(dir);
                }
            }
        }
        return mInstance;
    }

    public void prepareAudio(){
        try{
            isPrepared=false;
            File dir=new File(mDir);
            if(!dir.exists()){
                dir.mkdirs();
            }

            String fileName=generateFileName();
            File file=new File(dir,fileName);

            mCurrentFilePath=file.getAbsolutePath();

            mMediaRecorder=new MediaRecorder();

            //设置输出文件
            mMediaRecorder.setOutputFile(file.getAbsolutePath());

            //设置MediaRecorder的音频源为麦克风
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置音频格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
//        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB;
            //设置音频编码
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mMediaRecorder.prepare();

            mMediaRecorder.start();
            //准备结束
            isPrepared=true;
            if(mListener!=null){
                mListener.wellPrepared();
            }


        }catch (IllegalStateException |IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 随机生成文件的名称
     * @return
     */
    private String generateFileName() {
        return UUID.randomUUID().toString()+".mp3";
    }

    public int getVoiceLevel(int maxLevel){

        if(isPrepared){
            try{
                //mMediaRecorder.getMaxAmplitude()该值范围是1-32767
                return maxLevel*mMediaRecorder.getMaxAmplitude()/32768+1;
            }catch (Exception e){
            }
        }

        return 1;
    }

    public void release(){
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder=null;
    }

    public void cancel(){
        //删除文件
        release();
        if(mCurrentFilePath!=null){
            File file=new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath=null;
        }
    }
}
