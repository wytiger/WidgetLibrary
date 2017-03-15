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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

        final PtrClassicFrameLayout ptr_home = (PtrClassicFrameLayout) findViewById(R.id.ptr_home);
        final LoadMoreListViewContainer loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);
        final ListView lv = (ListView) findViewById(R.id.load_more_small_image_list_view);

        int listSize;
        final List<String> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add(i, "Test" + i);
        }
        listSize = datas.size();

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
                Log.i("wy", "checkCanDoRefresh");
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
//        loadMoreListViewContainer.setAutoLoadMore(false);
//        loadMoreListViewContainer.useDefaultFooter();
//        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
//            @Override
//            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
//                Log.i("wy", "onLoadMore");
//                loadMoreListViewContainer.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        commonAdapter.notifyDataSetChanged();
//                        loadMoreListViewContainer.loadMoreFinish(false,true);
//                    }
//                },3000);
//            }
//        });

        //上啦加载更多
        loadMoreListViewContainer.setAutoLoadMore(true);
        loadMoreListViewContainer.useDefaultFooter();
        // binding view and data
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                Log.i("wy", "onLoadMore");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // load more
                        for (int i = 10; i < 20; i++) {
                            datas.add(i, "demo" + i);
                        }
//                        listSize = list.size();
                        commonAdapter.notifyDataSetChanged();
                        loadMoreListViewContainer.loadMoreFinish(false, true);
                    }
                },3000);
            }
        });

    }
}
