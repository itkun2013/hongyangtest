package com.ksdc.slidrecycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //recylerview控件
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        init();
    }

    private void init() {
        final List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("删除");
        }
        final MyAdapter myAdapter = new MyAdapter(data, this);
        myAdapter.setOnClickListener(new MyAdapter.OnClickListener() {
//            @Override
//            public void onMenuClick(int position) {
//                data.remove(data.get(position));
//                myAdapter.notifyDataSetChanged();
////                data.set(position, top ? "已删除" : "删除");
////                data.remove(position);
////                myAdapter.notifyDataSetChanged();
//            }

            @Override
            public void onContentClick(int position) {
                Toast.makeText(MainActivity.this, "click pos = " + position, Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                myAdapter.setScrollingMenu(null);
            }
        });
    }

}
