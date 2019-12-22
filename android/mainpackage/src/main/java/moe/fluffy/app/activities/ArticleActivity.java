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
package moe.fluffy.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import moe.fluffy.app.R;
import moe.fluffy.app.adapter.ImageAdapter;
import moe.fluffy.app.assistant.JSONParser;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.assistant.Utils;
import moe.fluffy.app.types.Article;
import moe.fluffy.app.types.ArticlesMap;

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
	EditText etSearchBook;

	ImageButton imgbtnDog, imgbtnBird, imgbtnCat, imgbtnOther;
	ImageButton imgbtnSearch;

	ImageButton imgbtnNavBarCamera, imgbtnNavBarMedical, imgbtnNavBarCalendar,
			imgbtnNavBarArticle, imgbtnNavBarUser;

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

	void initCarousel(String requireCategory, String searchTitle) {
		lst.clear();
		ImageAdapter im = new ImageAdapter(lst, mapResource, getBaseContext());
		for (int i = 0; i < articles.getList().size(); i++) {
			Article a = articles.getList().get(i);
			// Check request article category type
			if (requireCategory != null) {
				if (!a.category.equals(requireCategory))
					continue;
			}
			// Check keyword in title
			if (searchTitle != null) {
				if (!a.title.toLowerCase().contains(searchTitle))
					continue;
			}
			lst.add(a.getCoverId());
			final int _i = i;
			mapResource.put(a.getCoverId(), v -> {
				Intent intent = new Intent(this, ShowArticleActivity.class);
				intent.putExtra(getString(R.string.IntentArticleIndex), _i);
				startActivity(intent);
			});
		}
		articlesCarousel.setAdapter(im);
		articlesCarousel.notifyDataSetChanged();
	}

	void initBottomView() {
		imgbtnNavBarCamera = findViewById(R.id.imgbtnCameraPage);
		imgbtnNavBarMedical = findViewById(R.id.imgbtnMedicalPage);
		imgbtnNavBarCalendar = findViewById(R.id.imgbtnCalendarPage);
		imgbtnNavBarArticle = findViewById(R.id.imgbtnArticlePage);
		imgbtnNavBarUser = findViewById(R.id.imgbtnUserPage);

		imgbtnNavBarArticle.setImageResource(R.drawable.book_orange);

		imgbtnNavBarCamera.setOnClickListener(v ->
				startActivity(new Intent(this, BootstrapScannerActivity.class)));
		imgbtnNavBarCalendar.setOnClickListener(v ->
				startActivity(new Intent(this, CalendarActivity.class)));
		imgbtnNavBarMedical.setOnClickListener(v ->
				startActivity(new Intent(this, MedicalActivity.class)));
		imgbtnNavBarUser.setOnClickListener(v ->
				startActivity(new Intent(this, ProfileActivity.class)));

	}


	void initView() {
		txtCountDog = findViewById(R.id.txtDogNumBook);
		txtCountCat = findViewById(R.id.txtCatNumBook);
		txtCountBird = findViewById(R.id.txtBirdNumBook);
		txtCountOther = findViewById(R.id.txtOtherNumBook);
		articlesCarousel = findViewById(R.id.articlesCarousel);
		imgbtnDog = findViewById(R.id.imgbtnDogBook);
		imgbtnBird = findViewById(R.id.imgbtnBirdBook);
		imgbtnCat = findViewById(R.id.imgbtnCatBook);
		imgbtnOther = findViewById(R.id.imgbtnOtherBook);
		imgbtnSearch = findViewById(R.id.imgbtnSearchBook);
		etSearchBook = findViewById(R.id.etSearchBook);

		initBottomView();

		etSearchBook.setOnFocusChangeListener((view, hasFocus) ->
				Utils.onFocusChange(hasFocus, this, etSearchBook, R.string.searchBookEditText, false));

		imgbtnSearch.setOnClickListener(v ->
				initCarousel(null, etSearchBook.getText().toString()));

		imgbtnDog.setOnClickListener(v ->
				initCarousel(categories[0], null));
		imgbtnCat.setOnClickListener(v ->
				initCarousel(categories[1], null));
		imgbtnBird.setOnClickListener(v ->
				initCarousel(categories[2], null));
		imgbtnOther.setOnClickListener(v ->
				initCarousel(categories[3], null));

		initCarousel(null, null);
		updateCount();
	}

	int getRandomCount() {
		return (int)(Math.random() * 50 + 10);
	}

	void updateCount() {
		txtCountDog.setText(String.valueOf(getRandomCount()));
		txtCountCat.setText(String.valueOf(getRandomCount()));
		txtCountBird.setText(String.valueOf(getRandomCount()));
		txtCountOther.setText(String.valueOf(getRandomCount()));
	}

	void updateCountEx() {
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
					JSONArray _articles = _articles_file.getJSONArray(getString(R.string.jsonArticle));
					for (int i = 0; i < _articles.length(); i++) {
						JSONObject j = _articles.getJSONObject(i);
						// https://stackoverflow.com/a/3476470
						@DrawableRes int coverId =
								getResources().getIdentifier(j.getString(getString(R.string.jsonCarouseResourceName)), "drawable", this.getPackageName());
						@DrawableRes int headerId =
								getResources().getIdentifier(j.getString(getString(R.string.jsonTitleImageResourceName)), "drawable", this.getPackageName());
						articles.push(new Article(j, coverId, headerId));
						String category = j.getString(getString(R.string.jsonCategory));
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

	static Article getArticles(int index) {
		return articles.getList().get(index);
	}

}
