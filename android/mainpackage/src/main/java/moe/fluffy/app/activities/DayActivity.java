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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import moe.fluffy.app.R;
import moe.fluffy.app.adapter.ReviewAdapter;
import moe.fluffy.app.types.PastTimeReviewType;
import moe.fluffy.app.types.SerializableBundle;

public class DayActivity extends AppCompatActivity {

	private static final String TAG = "log_DayActivity";

	public final static String keyName = "requestDay";

	ImageButton imgbtnNavBarCamera, imgbtnNavBarMedical, imgbtnNavBarCalendar,
			imgbtnNavBarArticle, imgbtnNavBarUser;

	LineChart lineChart;

	RecyclerView eventsView;

	ArrayList<PastTimeReviewType> pastTimeReviewTypes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_water);
		initView();
	}


	private List<Entry> getList() {
		Random r = new Random();
		List<Entry> entries = new ArrayList<>();
		for (int i = 0; i < 7 ; i ++) {
			entries.add(new Entry(i, r.nextFloat()));
		}
		return entries;
	}

	private void initView() {
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			throw new RuntimeException("Bundle should be set");
		}

		SerializableBundle sb = (SerializableBundle) bundle.getSerializable(keyName);
		if (sb == null) {
			throw new RuntimeException("calendar bean should be set");
		}
		// TODO: init view here
		pastTimeReviewTypes = new ArrayList<>();
		for (int i=0; i< 5; i++) {
			pastTimeReviewTypes.add(new PastTimeReviewType(new Random().nextInt(4) + 1, "test"));
		}

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
		eventsView = findViewById(R.id.rvPastEvents);
		eventsView.setHasFixedSize(true);
		eventsView.setLayoutManager(layoutManager);
		eventsView.setAdapter(new ReviewAdapter(pastTimeReviewTypes));

		// init chart view
		lineChart = findViewById(R.id.viewWaterAnalysis);
		lineChart.getDescription().setEnabled(false);
		LineDataSet ls = new LineDataSet(getList(), "test");
		ls.setDrawCircles(false);
		ls.setLineWidth(2f);
		ls.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
		ArrayList<ILineDataSet> sets = new ArrayList<>();
		sets.add(ls);
		lineChart.setData(new LineData(sets));
		findNavigationBar();
	}

	void findNavigationBar() {
		imgbtnNavBarCamera = findViewById(R.id.imgbtnCameraPage);
		imgbtnNavBarMedical = findViewById(R.id.imgbtnMedicalPage);
		imgbtnNavBarCalendar = findViewById(R.id.imgbtnCalendarPage);
		imgbtnNavBarArticle = findViewById(R.id.imgbtnArticlePage);
		imgbtnNavBarUser = findViewById(R.id.imgbtnUserPage);

		imgbtnNavBarCalendar.setImageResource(R.drawable.calendar_orange);

		imgbtnNavBarCamera.setOnClickListener(v ->
				startActivity(new Intent(DayActivity.this, BootstrapScannerActivity.class)));

		imgbtnNavBarArticle.setOnClickListener(v ->
				startActivity(new Intent(DayActivity.this, ArticleActivity.class)));

		imgbtnNavBarMedical.setOnClickListener(v ->
				startActivity(new Intent(DayActivity.this, MedicalActivity.class)));

		imgbtnNavBarUser.setOnClickListener(v ->
				startActivity(new Intent(DayActivity.this, ProfileActivity.class)));

	}
}
