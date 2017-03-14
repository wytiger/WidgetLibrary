package com.wytiger.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wytiger.utils.ScreenUtil;
import com.wytiger.dialog.listener.Listener;
import com.wytiger.widget.R;


/**
 * Author: wytiger
 * Time: 2017/02/10
 * Desc:
 */
public class AppDialog extends Dialog {
    private Context context;

    View dialogLayout;
    TextView titleView;
    TextView messageView;
    Button confirmButton;
    Button cancelButton;

    View viewSeparatorBlock;
    View viewSeparatorLine;

    private String title;
    private String message;
    private String confirmText;
    private String cancelText;

    private OnCancelButtonClickListener cancelListener;
    private OnConfirmButtonClickListener confirmListener;

    public AppDialog(Context context) {
        super(context, R.style.AppDialogTheme);
        this.context = context;
    }

    public AppDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public AppDialog setMessage(String message) {
        this.message = message;

        return this;
    }

    public AppDialog setConfirmButton(String confirmText, OnConfirmButtonClickListener confirmListener) {
        this.confirmText = confirmText;
        this.confirmListener = confirmListener;

        return this;
    }

    public AppDialog setCancelButton(String cancelText, OnCancelButtonClickListener cancelListener) {
        this.cancelText = cancelText;
        this.cancelListener = cancelListener;

        return this;
    }

    public AppDialog setCancelableFlag(boolean flag) {
        super.setCancelable(flag);

        return this;
    }

    public AppDialog setCanceledOnTouchOutsideFlag(boolean flag) {
        super.setCanceledOnTouchOutside(flag);

        return this;
    }



    public AppDialog setOnDismissListener(final Listener listener) {
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                listener.call();
            }
        });
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = LayoutInflater.from(context).inflate(R.layout.app_dialog, null);
        dialogLayout = rootView.findViewById(R.id.ll_dialog);
        titleView = (TextView) rootView.findViewById(R.id.tv_title);
        messageView = (TextView) rootView.findViewById(R.id.tv_message);
        confirmButton = (Button) rootView.findViewById(R.id.btn_confirm);
        cancelButton = (Button) rootView.findViewById(R.id.btn_cancel);
        viewSeparatorLine = rootView.findViewById(R.id.view_separator_line);
        viewSeparatorBlock = rootView.findViewById(R.id.view_separator_block);
        setContentView(rootView);

        dialogLayout.setMinimumWidth((int) (ScreenUtil.getScreenWidth(context) * 0.8));

        if (!TextUtils.isEmpty(title)) {
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(title);
            //有标题的时候不需要额外间距,同时需要显示分割线
            viewSeparatorBlock.setVisibility(View.GONE);
//            viewSeparatorLine.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(message)) {
            messageView.setVisibility(View.VISIBLE);
            messageView.setText(message);
        }


        if (!TextUtils.isEmpty(cancelText)) {
            cancelButton.setVisibility(View.VISIBLE);
            cancelButton.setText(cancelText);
            if (null != cancelListener) {
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelListener.onCancelButtonClick(AppDialog.this);
                    }
                });
            } else {
                cancelButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dismiss();
                            }
                        }
                );
            }
        }
        if (!TextUtils.isEmpty(confirmText)) {
            confirmButton.setVisibility(View.VISIBLE);
            confirmButton.setText(confirmText);
        }
        if (null != confirmListener) {
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmListener.onConfirmButtonClick(AppDialog.this);
                }
            });
        }
    }


    public static interface OnConfirmButtonClickListener {
        void onConfirmButtonClick(Dialog dialog);
    }

    public void setOnConfirmButtonClickListener(OnConfirmButtonClickListener confirmListener) {
        this.confirmListener = confirmListener;
    }

    public static interface OnCancelButtonClickListener {
        void onCancelButtonClick(Dialog dialog);
    }

    public void setOnCancelButtonClickListener(OnCancelButtonClickListener cancelListener) {
        this.cancelListener = cancelListener;
    }
}
