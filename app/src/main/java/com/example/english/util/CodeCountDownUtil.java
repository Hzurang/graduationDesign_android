package com.example.english.util;

import android.os.CountDownTimer;

import com.tencent.mmkv.MMKV;

public class CodeCountDownUtil {
    private static final String KEY_TIME = "TotalTimeMills";
    private static MMKV mMkv = MMKV.mmkvWithID("CodeCountDownUtil");
    private static long mTotalTimeMills = 0L;
    private static OnCountDownListener mListener;

    private static CountDownTimer mCountDownTimer;

    /**
     * 开始倒计时
     * [totalTimeMills] 倒计时时间毫秒值
     */
    public static void start(long totalTimeMills, OnCountDownListener listener) {
        mListener = listener;
        mTotalTimeMills = totalTimeMills + System.currentTimeMillis();
        mMkv.encode(KEY_TIME, mTotalTimeMills);
        mCountDownTimer = new CountDownTimer(totalTimeMills, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mListener.onTick(millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                mListener.onFinish();
                reset();
            }
        };
        mCountDownTimer.start();
    }

    /**
     * 取消倒计时
     */
    public static void cancel() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mListener = null;
    }

    /**
     * 重置倒计时，将剩余时间置为0
     * 例如某些需求下，上一次获取验证码的倒计时时间未结束，此时将获取验证码页面关闭之后再次打开页面，可
     * 以直接获取验证码，此时可以在onDestroy()中调用reset()而不是cancel()。
     */
    public static void reset() {
        mMkv.encode(KEY_TIME, 0L);
        cancel();
        mTotalTimeMills = 0L;
    }

    /**
     * 获取当前的倒计时时间毫秒值
     */
    public static long getCountTimeMills() {
        mTotalTimeMills = mMkv.decodeLong(KEY_TIME);
        return mTotalTimeMills - System.currentTimeMillis();
    }

    /**
     * 当前时间是否在倒计时范围内
     * @return true: 倒计时正在进行 false:倒计时未进行
     */
    public static boolean isCounting() {
        mTotalTimeMills = mMkv.decodeLong(KEY_TIME);
        return System.currentTimeMillis() < mTotalTimeMills;
    }

    public interface OnCountDownListener {
        /**
         * 倒计时
         * [seconds] 倒计时剩余时间，秒为单位
         */
        void onTick(long seconds);

        /**
         * 倒计时结束
         */
        void onFinish();
    }
}
