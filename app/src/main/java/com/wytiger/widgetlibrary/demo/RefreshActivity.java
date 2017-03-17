package com.wytiger.widgetlibrary.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.wytiger.adapter.abslistview.CommonAdapter;
import com.wytiger.adapter.abslistview.ViewHolder;
import com.wytiger.loadmore.LoadMoreContainer;
import com.wytiger.loadmore.LoadMoreHandler;
import com.wytiger.loadmore.LoadMoreListViewContainer;
import com.wytiger.pulltorefresh.PtrClassicFrameLayout;
import com.wytiger.pulltorefresh.PtrDefaultHandler;
import com.wytiger.pulltorefresh.PtrFrameLayout;
import com.wytiger.widgetlibrary.R;

import java.util.ArrayList;
import java.util.List;

public class RefreshActivity extends AppCompatActivity {
    Handler handler = new Handler();
    List<String> datas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

        final PtrClassicFrameLayout ptr_home = (PtrClassicFrameLayout) findViewById(R.id.ptr_home);
        final LoadMoreListViewContainer loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);
        final ListView lv = (ListView) findViewById(R.id.load_more_small_image_list_view);

        initData(loadMoreListViewContainer);

        final   CommonAdapter commonAdapter = new CommonAdapter<String>(RefreshActivity.this, android.R.layout.simple_list_item_1, datas) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(android.R.id.text1, item);
            }
        };

        lv.setAdapter(commonAdapter);

        //下拉刷新
        ptr_home.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                Log.d("wy", "checkCanDoRefresh");
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, lv, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                Log.i("wy", "onRefreshBegin");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptr_home.refreshComplete();
                    }
                }, 100);
            }
        });


        //上啦加载更多
        loadMoreListViewContainer.setAutoLoadMore(true);
        loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                Log.i("wy", "onLoadMore");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        datas.add(new String("  ListView item1"));
                        datas.add(new String("  ListView item2"));
                        datas.add(new String("  ListView item3"));

                        commonAdapter.notifyDataSetChanged();
                        loadMoreListViewContainer.loadMoreFinish(false, true);
                    }
                },3000);
            }
        });

    }

    private void initData(LoadMoreListViewContainer loadMoreListViewContainer) {
        for (int i = 0; i < 30; i++) {
            datas.add(i, "Test" + i);
        }
        //TODO: 这里需要标志emptyResult为false才能加载更多
        loadMoreListViewContainer.loadMoreFinish(false, true);
    }
}
