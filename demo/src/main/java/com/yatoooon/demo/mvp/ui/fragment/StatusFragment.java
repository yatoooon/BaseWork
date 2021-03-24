package com.yatoooon.demo.mvp.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.yatoooon.baselibrary.base.BaseAdapter;
import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.widget.layout.WrapRecyclerView;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.common.AppFragment;
import com.yatoooon.demo.di.component.DaggerStatusComponent;
import com.yatoooon.demo.mvp.contract.StatusContract;
import com.yatoooon.demo.mvp.presenter.StatusPresenter;
import com.yatoooon.demo.mvp.ui.adapter.StatusAdapter;

import javax.inject.Inject;

import butterknife.BindView;


public class StatusFragment extends AppFragment<StatusPresenter> implements StatusContract.View, OnRefreshLoadMoreListener,
        BaseAdapter.OnItemClickListener {

    @BindView(R.id.rv_status_list)
    WrapRecyclerView rvStatusList;
    @BindView(R.id.rl_status_refresh)
    SmartRefreshLayout rlStatusRefresh;

    public static StatusFragment newInstance() {
        StatusFragment fragment = new StatusFragment();
        return fragment;
    }

    @Inject
    StatusAdapter mAdapter;

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerStatusComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_status;
    }

    @Override
    public void initView() {
        mAdapter.setOnItemClickListener(this);
        rvStatusList.setAdapter(mAdapter);

        TextView headerView = rvStatusList.addHeaderView(R.layout.item_picker);
        headerView.setText("我是头部");
        headerView.setOnClickListener(v -> toast("点击了头部"));

//        TextView footerView = rvStatusList.addFooterView(R.layout.item_picker);
//        footerView.setText("我是尾部");
//        footerView.setOnClickListener(v -> toast("点击了尾部"));

        rlStatusRefresh.setOnRefreshLoadMoreListener(this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.requestData(mAdapter.getItemCount());
    }


    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        toast(mAdapter.getItem(position));
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        postDelayed(() -> {
            mAdapter.clearData();
            mPresenter.requestData(mAdapter.getItemCount());
            refreshLayout.finishRefresh();
        }, 1000);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        postDelayed(() -> {
            mPresenter.requestData(mAdapter.getItemCount());
            refreshLayout.finishLoadMore();
        }, 1000);
    }
}
