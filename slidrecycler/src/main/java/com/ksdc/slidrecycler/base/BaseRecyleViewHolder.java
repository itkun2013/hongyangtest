package com.ksdc.slidrecycler.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liangkun on 2017/9/26 0026.
 */

public abstract class BaseRecyleViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseRecyleViewHolder(ViewGroup viewGroup, int layoutId) {
        super(View.inflate(viewGroup.getContext(), layoutId, viewGroup));
    }
}
