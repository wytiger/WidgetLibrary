package com.wytiger.widgetlibrary;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wytiger.dialog.AppDialog;
import com.wytiger.progressdialog.AppProgressDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tvDialog).setOnClickListener(this);
        findViewById(R.id.tvProgressDialog).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDialog:
                AppDialog appDialog = new AppDialog(this);
                appDialog.setMessage("Dialog测试")
                        .setConfirmButton("确定", new AppDialog.OnConfirmButtonClickListener() {
                            @Override
                            public void onConfirmButtonClick(Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .setCancelButton("取消", new AppDialog.OnCancelButtonClickListener() {
                            @Override
                            public void onCancelButtonClick(Dialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;
            case R.id.tvProgressDialog:
                AppProgressDialog appProgressDialog = new AppProgressDialog(this);
                appProgressDialog.show();
                break;
        }
    }
}
