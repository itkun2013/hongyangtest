package com.ksdc.slidrecycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * created by yhao on 2017/8/18.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<String> mData;
    private Context mContext;

    private SlidingMenu mOpenMenu;
    private SlidingMenu mScrollingMenu;

    SlidingMenu getScrollingMenu() {
        return mScrollingMenu;
    }

    void setScrollingMenu(SlidingMenu scrollingMenu) {
        mScrollingMenu = scrollingMenu;
    }

    void holdOpenMenu(SlidingMenu slidingMenu) {
        mOpenMenu = slidingMenu;
    }

    void closeOpenMenu() {
        if (mOpenMenu != null && mOpenMenu.isOpen()) {
            mOpenMenu.closeMenu();
            mOpenMenu = null;
        }
    }

    MyAdapter(List<String> data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent,
                false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final
    int position) {
        holder.tvPoint.setText(position + "");
//        if (holder.slidingMenu.isOpen) {
//            //如果是打开的，显示删除view
//            holder.menuText.setVisibility(View.VISIBLE);
            holder.menuText.setText(mData.get(position));
//        }else{
//            holder.menuText.setVisibility(View.INVISIBLE);
//        }
        holder.menuText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeOpenMenu();
                mData.remove(position);
                notifyDataSetChanged();
//                if (mOnClickListener != null) {
//                    mOnClickListener.onMenuClick(position);
//                }
                Toast.makeText(mContext, "删除" + position, Toast.LENGTH_SHORT).show();
            }
        });
        holder.slidingMenu.setCustomOnClickListener(new SlidingMenu.CustomOnClickListener() {
            @Override
            public void onClick() {
                if (mOnClickListener != null) {
                    mOnClickListener.onContentClick(position);
                }
            }
        });
    }

    interface OnClickListener {

        void onContentClick(int position);
    }

    private OnClickListener mOnClickListener;

    void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView menuText;
        TextView tvPoint;
        LinearLayout content;
        SlidingMenu slidingMenu;

        MyViewHolder(View itemView) {
            super(itemView);
            menuText = (TextView) itemView.findViewById(R.id.menuText);
            tvPoint = (TextView) itemView.findViewById(R.id.tv_point);
            content = (LinearLayout) itemView.findViewById(R.id.content);
            slidingMenu = (SlidingMenu) itemView.findViewById(R.id.slidingMenu);
        }
    }
}
