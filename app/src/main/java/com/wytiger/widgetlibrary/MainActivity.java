package com.wytiger.widgetlibrary;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wytiger.dialog.AppDialog;
import com.wytiger.loadingdialog.AppLoadingDialog;
import com.wytiger.widgetlibrary.demo.AdapterActivity;
import com.wytiger.widgetlibrary.demo.BannerActivity;
import com.wytiger.widgetlibrary.demo.RefreshActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tvDialog).setOnClickListener(this);
        findViewById(R.id.tvLoadingDialog).setOnClickListener(this);
        findViewById(R.id.tvRefresh).setOnClickListener(this);
        findViewById(R.id.tvBanner).setOnClickListener(this);
        findViewById(R.id.tvAdapter).setOnClickListener(this);
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
            case R.id.tvLoadingDialog:
                AppLoadingDialog appLoadingDialog = new AppLoadingDialog(this);
                appLoadingDialog.show();
                break;
            case R.id.tvRefresh:
                startActivity(new Intent(this, RefreshActivity.class));
                break;
            case R.id.tvBanner:
                startActivity(new Intent(this, BannerActivity.class));
                break;
            case R.id.tvAdapter:
                startActivity(new Intent(this, AdapterActivity.class));
                break;
        }
    }
}
