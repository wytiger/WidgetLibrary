package com.wytiger.loadingdialog;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * description:自定义进度对话框
 * Created by wytiger on 2016-11-9.
 */

public class AppLoadingDialog extends AlertDialog {
    private Context context;
    private Animation animation;
    private View dialogView;
    private ImageView ivImage;
    private TextView tvMessage;

    private String message;

    public AppLoadingDialog(Context context) {
//        super(context);
        //使用这个主题去除边框等
        super(context, R.style.AppProgressDialogTheme);
        this.context = context;
    }

    public AppLoadingDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public AppLoadingDialog setMessage(String message) {
        this.message = message;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_progress);

        //点击imageview外侧区域，动画会消失
        setCanceledOnTouchOutside(true);

        dialogView = findViewById(R.id.ll_dialog);
        ivImage = (ImageView) dialogView.findViewById(R.id.ivImage);
        tvMessage = (TextView) dialogView.findViewById(R.id.tvMessage);
        if (!TextUtils.isEmpty(message)) {
            tvMessage.setText(message);
        }


        dialogView.setMinimumWidth((int) (ScreenUtil.getScreenWidth(context) * 0.45));
        dialogView.setMinimumHeight((int) (ScreenUtil.getScreenHeight(context) * 0.20));

        //加载动画资源
        animation = AnimationUtils.loadAnimation(context, R.anim.rotate_custom_progress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (animation != null) {
            ivImage.startAnimation(animation);
        }
    }

    @Override
    protected void onStop() {
        ivImage.clearAnimation();
        super.onStop();
    }
}
