/*
 ** Copyright (C) 2019 KunoiSayami
 **
 ** This file is part of Fluffy and is released under
 ** the AGPL v3 License: https://www.gnu.org/licenses/agpl-3.0.txt
 **
 ** This program is free software: you can redistribute it and/or modify
 ** it under the terms of the GNU Affero General Public License as published by
 ** the Free Software Foundation, either version 3 of the License, or
 ** any later version.
 **
 ** This program is distributed in the hope that it will be useful,
 ** but WITHOUT ANY WARRANTY; without even the implied warranty of
 ** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 ** GNU Affero General Public License for more details.
 **
 ** You should have received a copy of the GNU Affero General Public License
 ** along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package moe.fluffy.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import moe.fluffy.app.R;

public class ImageAdapter extends PagerAdapter {
	List<Integer> mList;
	Map<Integer, View.OnClickListener> mapRescoure;
	Context context;
	LayoutInflater lay;
	private static String TAG = "log_ImageAdapter";

	public ImageAdapter(List<Integer> l, Map<Integer, View.OnClickListener> _mapResource, Context c) {
		this.mList = l;
		this.context = c;
		this.mapRescoure = _mapResource;
		lay = LayoutInflater.from(c);
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view.equals(object);
	}

	@Override
	public void destroyItem(ViewGroup c, int position, @NotNull Object obj) {
		c.removeView((View)obj);
	}

	@NonNull
	@Override
	public Object instantiateItem(@NotNull ViewGroup c, int position) {
		View v = lay.inflate(R.layout.card_view, c, false);
		ImageView imgView = v.findViewById(R.id.image_card);
		imgView.setImageResource(mList.get(position));
		imgView.setOnClickListener(mapRescoure.get(mList.get(position)));
		c.addView(v);
		return v;
	}

}
