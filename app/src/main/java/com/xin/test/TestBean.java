package com.xin.test;

import java.util.UUID;

/**
 * Created by Administrator on 2016/12/26.
 */
public class TestBean {
    private UUID id;
    private String Title;
    private String Date;
    private boolean sex;
    private int icon;

    public TestBean() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
