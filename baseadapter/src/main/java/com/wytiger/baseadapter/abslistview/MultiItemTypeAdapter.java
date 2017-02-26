package com.wytiger.baseadapter.abslistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wytiger.baseadapter.abslistview.base.ItemViewDelegate;
import com.wytiger.baseadapter.abslistview.base.ItemViewDelegateManager;

import java.util.List;

public class MultiItemTypeAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;

    private ItemViewDelegateManager mItemViewDelegateManager;


    public MultiItemTypeAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    private boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    @Override
    public int getViewTypeCount() {
        if (useItemViewDelegateManager())
            return mItemViewDelegateManager.getItemViewDelegateCount();
        return super.getViewTypeCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (useItemViewDelegateManager()) {
            int viewType = mItemViewDelegateManager.getItemViewType(mDatas.get(position), position);
            return viewType;
        }
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewDelegate itemViewDelegate = mItemViewDelegateManager.getItemViewDelegate(mDatas.get(position), position);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHelper viewHelper = null ;
        if (convertView == null)
        {
            View itemView = LayoutInflater.from(mContext).inflate(layoutId, parent,
                    false);
            viewHelper = new ViewHelper(mContext, itemView, parent, position);
            viewHelper.mLayoutId = layoutId;
            onViewHolderCreated(viewHelper, viewHelper.getConvertView());
        } else
        {
            viewHelper = (ViewHelper) convertView.getTag();
            viewHelper.mPosition = position;
        }


        convert(viewHelper, getItem(position), position);
        return viewHelper.getConvertView();
    }

    protected void convert(ViewHelper viewHelper, T item, int position) {
        mItemViewDelegateManager.convert(viewHelper, item, position);
    }

    public void onViewHolderCreated(ViewHelper holder , View itemView )
    {}

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}
