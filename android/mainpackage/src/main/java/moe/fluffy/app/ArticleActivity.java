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

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import moe.fluffy.app.assistant.JSONParser;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.types.ArticleType;
import moe.fluffy.app.types.ArticlesMap;
import moe.fluffy.app.types.adapter.ImageAdapter;

public class ArticleActivity extends AppCompatActivity {

	static private String TAG  = "log_ArticleActivity";
	List<Integer> lst = new ArrayList<>();

	HashMap<Integer, View.OnClickListener> mapResource = new HashMap<>();

	private static boolean is_articles_initialized = false;
	private static ArticlesMap articles;

	private static HashMap<String, Integer> countArticles = new HashMap<>();

	private final static String[] categories = {"Dog", "Cat", "Bird", "Others"};

	TextView txtCountDog, txtCountCat, txtCountBird, txtCountOther;
	HorizontalInfiniteCycleViewPager articlesCarousel;

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

	void initCarousel() {
		ImageAdapter im = new ImageAdapter(lst, mapResource, getBaseContext());
		for (int i = 0;i < articles.getList().size(); i++) {
			ArticleType a = articles.getList().get(i);
			lst.add(a.getCoverId());
			final int _i = i;
			mapResource.put(a.getCoverId(), v -> {
				Intent intent = new Intent(ArticleActivity.this, ShowArticleActivity.class);
				intent.putExtra("articleIndex", _i);
				startActivity(intent);
			});
		}
		articlesCarousel.setAdapter(im);
	}

	void initView() {
		txtCountDog = findViewById(R.id.txtDogNumBook);
		txtCountCat = findViewById(R.id.txtCatNumBook);
		txtCountBird = findViewById(R.id.txtBirdNumBook);
		txtCountOther = findViewById(R.id.txtOtherNumBook);
		articlesCarousel = findViewById(R.id.horizontalInfiniteCycleViewPager);
		initCarousel();
		updateCount();
	}

	void updateCount() {
		txtCountDog.setText(String.valueOf(countArticles.get(categories[0])));
		txtCountCat.setText(String.valueOf(countArticles.get(categories[1])));
		txtCountBird.setText(String.valueOf(countArticles.get(categories[2])));
		txtCountOther.setText(String.valueOf(countArticles.get(categories[3])));
	}

	void initArticles() {
		if (!is_articles_initialized) {
			articles = ArticlesMap.getInstance();
			JSONObject _articles_file = JSONParser.loadJSONFromAsset(getResources().openRawResource(R.raw.articles));
			if (_articles_file != null) {
				try {
					JSONArray _articles = _articles_file.getJSONArray("articles");
					for (int i = 0; i < _articles.length(); i++) {
						JSONObject j = _articles.getJSONObject(i);
						@DrawableRes int coverId =
								getResources().getIdentifier(j.getString("filename"), "drawable", this.getPackageName());
						@DrawableRes int headerId =
								getResources().getIdentifier(j.getString("photo_filename"), "drawable", this.getPackageName());
						articles.push(new ArticleType(j, coverId, headerId));
						String category = j.getString("category");
						Integer count_category = countArticles.get(category);
						if (count_category != null) {
							countArticles.replace(category, count_category + 1);
						}
						else {
							countArticles.put(category, 1);
						}
					}
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
