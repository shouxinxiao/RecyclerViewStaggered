package com.xin.test;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

import static android.support.v7.widget.RecyclerView.OnClickListener;
import static android.support.v7.widget.RecyclerView.OnScrollListener;
import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by Administrator on 2016/12/24.
 */
public class RecyclerViewTest extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyAdapter mAdapter;
    private int lastVisibleItem = 1;
    private int updateIndex = 0;

    public static Fragment newIntener() {
        return new RecyclerViewTest();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.red,
                R.color.green,
                R.color.blue,
                R.color.yellow);
        mRecyclerView.setHasFixedSize(true);
        final StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        updateUI();
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout.setOnRefreshListener(this);

        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);

        mRecyclerView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int[] po = mLayoutManager.findLastVisibleItemPositions(new int[mLayoutManager.getSpanCount()]);
                lastVisibleItem = getMaxPosition(po);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        TestBeanLab testBeanLab = TestBeanLab.get(getActivity());
        List<TestBean> testBeans = testBeanLab.getmList();
        if (mAdapter == null) {
            mAdapter = new MyAdapter(testBeans);
            mRecyclerView.setAdapter(mAdapter);
        } else {
//            mAdapter.notifyDataSetChanged();//
            ///局部刷新
            mAdapter.notifyItemChanged(updateIndex);
        }
    }


    /**
     * 1
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }

    private void loadData() {
        Observable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).map(new Func1<Long, Object>() {
            @Override
            public Object call(Long aLong) {
                List<TestBean> data = new ArrayList<>();
                TestBean test;
                for (int i = 10; i < 15; i++) {
                    test = new TestBean();
                    test.setDate("2016-12-" + i);
                    test.setSex(i % 2 == 1);
                    if (i % 2 == 1) {
                        test.setTitle("Title :加载更多啦啦啦啦啦啦啦啊范德萨广东省分行富国汇股份合法的和法国恢复供电恢复的地方韩国的和斯蒂芬啦啦啦啦 " + i);
                        test.setIcon(R.drawable.casc_1);
                    } else {
                        test.setTitle("Title :加载更多 " + i);
                        test.setIcon(R.drawable.casc);
                    }
                    data.add(test);

                }
                TestBeanLab.get(getActivity()).addAllTestBean(data);
                mAdapter.notifyDataSetChanged();
                return null;
            }
        }).subscribe();

    }

    @Override
    public void onRefresh() {       //下拉刷新
        Observable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).map(new Func1<Long, Object>() {
            @Override
            public Object call(Long aLong) {
                TestBean test = new TestBean();
                test.setTitle("下拉刷新出来的数据");
                test.setDate(new Date().toString());
                test.setSex(true);
                test.setIcon(R.drawable.casc);
                TestBeanLab.get(getActivity()).addTestBean(test);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);

                return null;
            }
        }).subscribe();
    }

    class MyViewHolder extends ViewHolder implements OnClickListener {

        private EditText tv_title;
        private TextView tv_date;
        private CheckBox cb_sex;
        private ImageView icon;
        private TestBean data;

        private int position;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (EditText) itemView.findViewById(R.id.tv_title);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            cb_sex = (CheckBox) itemView.findViewById(R.id.cb_sex);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            itemView.setOnClickListener(this);
            tv_title.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    data.setTitle(tv_title.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            cb_sex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    data.setSex(isChecked);
                }
            });
        }

        public void bindData(TestBean test, int position) {
            data = test;
            this.position = position;
            tv_title.setText(data.getTitle());
            tv_date.setText(data.getDate());
            cb_sex.setChecked(data.isSex());
            icon.setImageResource(data.getIcon());

        }

        @Override
        public void onClick(View v) {
            Intent intent = ReadActivity.newIntent(getActivity(), data.getId());
            updateIndex = position;
            startActivity(intent);
        }
    }

    class FooterViewHolder extends ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_FOOTER = 1;
        private static final int TYPE_ITEM = 0;
        private List<TestBean> data;

        public MyAdapter(List<TestBean> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return data.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            //最后一个item设置为footerView
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view = layoutInflater.inflate(R.layout.list_item_recycler_view, parent, false);
                return new MyViewHolder(view);
            } else if (viewType == TYPE_FOOTER) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.footer_view, parent, false);
                view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                return new FooterViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (holder instanceof MyViewHolder) {
                ((MyViewHolder) holder).bindData(data.get(position), position);
            }
        }
    }

    class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
        }
    }
}
