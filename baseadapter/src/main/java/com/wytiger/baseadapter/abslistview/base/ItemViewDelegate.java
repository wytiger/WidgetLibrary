package com.wytiger.baseadapter.abslistview.base;


import com.wytiger.baseadapter.abslistview.ViewHelper;

/**
 * Created by zhy on 16/6/22.
 */
public interface ItemViewDelegate<T>
{

    public abstract int getItemViewLayoutId();

    public abstract boolean isForViewType(T item, int position);

    public abstract void convert(ViewHelper holder, T t, int position);



}
