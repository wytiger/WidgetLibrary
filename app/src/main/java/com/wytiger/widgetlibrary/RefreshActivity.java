package com.wytiger.widgetlibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.wytiger.pulltorefresh.PtrClassicFrameLayout;
import com.wytiger.pulltorefresh.PtrDefaultHandler;
import com.wytiger.pulltorefresh.PtrFrameLayout;

public class RefreshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

        PtrClassicFrameLayout ptr_home = (PtrClassicFrameLayout)findViewById(R.id.ptr_home);
        ptr_home.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Log.i("wy","onRefreshBegin");
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                return true;
            }
        });

    }
}
