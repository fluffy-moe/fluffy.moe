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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import moe.fluffy.app.types.ArticleType;


public class ShowArticleActivity extends Activity {

	ImageView imgHeader;
	TextView txtTitle, txtAuthor, txtBody, txtDay;

	ImageButton imgbtnFavorite;

	boolean bookmarked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.article_detail_page);
		initViews();
	}

	void initViews() {
		imgHeader = findViewById(R.id.img_articlepic);
		txtTitle = findViewById(R.id.txtArticleTitle);
		txtAuthor = findViewById(R.id.txtAuthor);
		txtDay = findViewById(R.id.txtPublishTime);
		txtBody = findViewById(R.id.txtArticleContent);
		int index = getIntent().getIntExtra(getString(R.string.IntentArticleIndex), -1);
		if (index != -1) {
			ArticleType a = ArticleActivity.getArticles(index);
			imgHeader.setImageResource(a.getHeaderId());
			txtTitle.setText(a.title);
			txtAuthor.setText(a.author);
			txtDay.setText(a.date);
			txtBody.setText(a.body);
		}
		imgbtnFavorite = findViewById(R.id.imgbtnFavArticle);

		imgbtnFavorite.setOnClickListener(v -> {
			if (bookmarked) {
				imgbtnFavorite.setBackground(getDrawable(R.drawable.bookmark_gray));
				bookmarked = false;
			} else {
				imgbtnFavorite.setBackground(getDrawable(R.drawable.bookmark_orange));
				bookmarked = true;
			}
		});
	}
}
