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
package moe.fluffy.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.gigamole.infinitecycleviewpager.OnInfiniteCyclePageTransformListener;

import java.util.ArrayList;
import java.util.List;

import moe.fluffy.app.types.adapter.ImageAdapter;

public class ArticleActivity extends AppCompatActivity {


	static private String TAG  = "log_ArticleActivity";
	List<Integer> lst = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_article);

		lst.add(R.drawable.c1);
		lst.add(R.drawable.c2);
		lst.add(R.drawable.b1);

		HorizontalInfiniteCycleViewPager h = findViewById(R.id.horizontalInfiniteCycleViewPager);
		ImageAdapter i = new ImageAdapter(lst, getBaseContext());
		h.setAdapter(i);
		h.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				//Log.d(TAG, "onPageScrolled: ");
			}

			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {

				// state 0 is stopped
				/*int p = h.getRealItem();
				int sz = h.getAdapter().getCount();
				View v = h.getChildAt(h.getCurrentItem());
				if (state != 0) return;
				Log.d(TAG, "onPageScrollStateChanged: p => " + p + " sz => " + h.getCurrentItem() + " state => " + state);
				for (int i = 0; i < h.getChildCount(); i++){
					View r = h.getChildAt(i);
					if (r.equals(v)) {
						r.setAlpha(1.0f);
						continue;
					}
					r.setAlpha(0.5f);

				}*/
			}
		});
	}

}
