/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yatoooon.demo.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yatoooon.baselibrary.http.imageloader.glide.ImageConfigImpl;
import com.yatoooon.baselibrary.utils.ArmsUtils;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.common.MyAdapter;
import com.yatoooon.demo.mvp.model.entity.User;

import java.util.List;


public class UserAdapter extends MyAdapter<User> {
    public UserAdapter(@NonNull Context context, List<User> list) {
        super(context);
        setData(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserItemHolder();
    }

    class UserItemHolder extends MyAdapter.ViewHolder {

        private final ImageView ivAvatar;
        private final TextView tvName;

        public UserItemHolder() {
            super(R.layout.list_recycle);
            ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
            tvName = (TextView) findViewById(R.id.tv_name);
        }

        @Override
        public void onBindView(int position) {
            tvName.setText(getItem(position).getLogin());
            ArmsUtils.obtainAppComponentFromContext(itemView.getContext()).imageLoader()
                    .loadImage(itemView.getContext(),
                            ImageConfigImpl.builder().res(getItem(position).getAvatarUrl()).imageView(ivAvatar).build());


        }
    }
}
