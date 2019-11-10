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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.gigamole.infinitecycleviewpager.OnInfiniteCyclePageTransformListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moe.fluffy.app.assistant.JSONParser;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.types.ArticleType;
import moe.fluffy.app.types.ArticlesMap;
import moe.fluffy.app.types.adapter.ImageAdapter;

public class ArticleActivity extends AppCompatActivity {


	static private String TAG  = "log_ArticleActivity";
	List<Integer> lst = new ArrayList<>();

	HashMap<Integer, View.OnClickListener> mapResouce = new HashMap<>();

	private static boolean is_articles_initialized = false;
	private static ArticlesMap articles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_article);
		initArticles();
		initView();
	}


	void initView() {
		HorizontalInfiniteCycleViewPager h = findViewById(R.id.horizontalInfiniteCycleViewPager);
		ImageAdapter im = new ImageAdapter(lst, mapResouce, getBaseContext());
		for (int i = 0;i < articles.getList().size(); i++) {
			ArticleType a = articles.getList().get(i);
			lst.add(a.getCoverId());
			final int _i = i;
			mapResouce.put(a.getCoverId(), v -> {
				Intent intent = new Intent(ArticleActivity.this, ShowArticleActivity.class);
				intent.putExtra("articleIndex", _i);
				startActivity(intent);
			});
		}
		h.setAdapter(im);
	}

	void initArticles() {
		if (!is_articles_initialized) {
			articles = ArticlesMap.getInstance();
			JSONObject _articles_file = JSONParser.loadJSONFromAsset(getResources().openRawResource(R.raw.articles));
			if (_articles_file != null) {
				// TODO: remove hardcode encoding
				try {
					JSONArray _articles = _articles_file.getJSONArray("articles");
					articles.push(new ArticleType(_articles.getJSONObject(0), R.drawable.dog1, R.drawable.dog12));
					articles.push(new ArticleType(_articles.getJSONObject(1), R.drawable.dog2, R.drawable.dog22));
					articles.push(new ArticleType(_articles.getJSONObject(2), R.drawable.cat1, R.drawable.cat12));
					articles.push(new ArticleType(_articles.getJSONObject(3), R.drawable.cat2, R.drawable.cat22));
					articles.push(new ArticleType(_articles.getJSONObject(4), R.drawable.bird1, R.drawable.bird12));
					articles.push(new ArticleType(_articles.getJSONObject(5), R.drawable.bird2, R.drawable.bird22));
					articles.push(new ArticleType(_articles.getJSONObject(6), R.drawable.other1, R.drawable.other12));
					articles.push(new ArticleType(_articles.getJSONObject(7), R.drawable.other2, R.drawable.other22));
					articles.push(new ArticleType(_articles.getJSONObject(8), R.drawable.dog3, R.drawable.dog32));
					articles.push(new ArticleType(_articles.getJSONObject(9), R.drawable.dog4, R.drawable.dog42));
					articles.push(new ArticleType(_articles.getJSONObject(10), R.drawable.cat3, R.drawable.cat32));
					articles.push(new ArticleType(_articles.getJSONObject(11), R.drawable.cat4, R.drawable.cat42));
					articles.push(new ArticleType(_articles.getJSONObject(12), R.drawable.bird4, R.drawable.bird42));
					articles.push(new ArticleType(_articles.getJSONObject(13), R.drawable.bird3, R.drawable.bird32));
					articles.push(new ArticleType(_articles.getJSONObject(14), R.drawable.other3, R.drawable.other32));
					articles.push(new ArticleType(_articles.getJSONObject(15), R.drawable.other4, R.drawable.other42));
				} catch (JSONException e) {
					PopupDialog.build(this, e);
				}
			}
			else {
				PopupDialog.build(this, new NullPointerException());
			}
			is_articles_initialized = true;
		}
	}

	static ArticleType getArticles(int index) {
		return articles.getList().get(index);
	}

}
