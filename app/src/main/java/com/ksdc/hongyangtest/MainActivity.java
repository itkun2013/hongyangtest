package com.ksdc.hongyangtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private static LinkedList<Integer> linkedList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ThreadPool.execute(convertToList);
    }

    private static void addDate() {
        String sb = "";
        for (int i = 0; i < 10000; i++) {
            sb += i+" ";
        }
    }

    private Runnable convertToList = new Runnable() {
        @Override
        public void run() {
            while (true){
             addDate();
            }
//            //这里易出现角标越界
//            linkedList.remove(0);
//            linkedList.remove(0);
        }
    };
}
