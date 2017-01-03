package com.xin.test;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/12/27.
 */
public class TestBeanLab {
    private static TestBeanLab sTestBeanLab;

    private List<TestBean> mList;

    private TestBeanLab(Context context) {
        mList = new ArrayList<>();
        TestBean test;
        for (int i = 0; i < 20; i++) {
            test = new TestBean();
            test.setDate("2016-12-" + i);
            test.setSex(i % 2 == 1);
            if (i % 2 == 1) {
                test.setTitle("Title :初始化的数据" + i);
                test.setIcon(R.drawable.casc);
            } else {
                test.setTitle("Title :加载更多 " + i);
                test.setIcon(R.drawable.casc_1);
            }
            mList.add(test);
        }
    }

    public static TestBeanLab get(Context context) {
        if (sTestBeanLab == null) {
            sTestBeanLab = new TestBeanLab(context);
        }
        return sTestBeanLab;
    }

    public List<TestBean> getmList() {
        return mList;
    }

    public TestBean getTestBean(UUID id) {
        for (TestBean t : mList) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    public void addTestBean(TestBean t) {
        mList.add(0, t);
    }

    public void addAllTestBean(List<TestBean> list) {
        mList.addAll(list);
    }
}
