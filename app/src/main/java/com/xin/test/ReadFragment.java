package com.xin.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by Administrator on 2016/12/27.
 */
public class ReadFragment extends Fragment {
    private static final String ARG_ID = "id";
    private TestBean mTestBean;

    private EditText tv_title;
    private TextView tv_date;
    private CheckBox cb_sex;
    private ImageView icon;

    public static Fragment newInstance(UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_ID, id);
        ReadFragment fragment = new ReadFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID id = (UUID) getActivity().getIntent().getSerializableExtra(ReadActivity.EXTAR_CRIME_ID);
        mTestBean = TestBeanLab.get(getActivity()).getTestBean(id);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_item_recycler_view, container, false);
        tv_title = (EditText) view.findViewById(R.id.tv_title);
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        cb_sex = (CheckBox) view.findViewById(R.id.cb_sex);
        icon = (ImageView) view.findViewById(R.id.icon);

        tv_title.setText(mTestBean.getTitle());
        tv_date.setText(mTestBean.getDate());
        cb_sex.setChecked(mTestBean.isSex());
        icon.setImageResource(mTestBean.getIcon());
        tv_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTestBean.setTitle(tv_title.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        cb_sex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTestBean.setSex(isChecked);
            }
        });
        return view;
    }
}