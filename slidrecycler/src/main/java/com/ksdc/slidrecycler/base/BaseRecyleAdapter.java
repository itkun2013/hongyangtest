package com.ksdc.slidrecycler.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by liangkun on 2017/9/26 0026.
 */

public abstract class BaseRecyleAdapter<T> extends RecyclerView.Adapter<BaseRecyleViewHolder> {
    @Override
    public BaseRecyleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseRecyleViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
