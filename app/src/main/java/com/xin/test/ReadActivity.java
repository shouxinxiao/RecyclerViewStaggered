package com.xin.test;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Administrator on 2016/12/27.
 */
public class ReadActivity extends SingleFragmentActivity {

    public static final String EXTAR_CRIME_ID = "extar_crime_id";

    public static Intent newIntent(Context content, UUID id) {
        Intent intent = new Intent(content, ReadActivity.class);
        intent.putExtra(EXTAR_CRIME_ID, id);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        UUID id = (UUID) getIntent().getSerializableExtra(EXTAR_CRIME_ID);
        return ReadFragment.newInstance(id);
    }
}
