/*
 ** Copyright (C) 2019-2020 KunoiSayami
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import moe.fluffy.app.R;
import moe.fluffy.app.adapter.ReviewAdapter;
import moe.fluffy.app.assistant.DatabaseHelper;
import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.EventsItem;
import moe.fluffy.app.types.PastTimeReview;
import moe.fluffy.app.types.SerializableBundle;
import moe.fluffy.app.types.divider.HorizontalPaddingItemDecoration;

public class DayActivity extends AppCompatActivity {

	private static final String TAG = "log_DayActivity";

	public final static String keyName = "requestDay";

	ImageButton imgbtnNavBarCamera, imgbtnNavBarMedical, imgbtnNavBarCalendar,
			imgbtnNavBarArticle, imgbtnNavBarUser;

	LineChart lineChart;

	RecyclerView eventsView;

	ArrayList<PastTimeReview> pastTimeReviews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_water);
		initView();
	}


	private List<Entry> getRandomList() {
		Random r = new Random();
		List<Entry> entries = new ArrayList<>();
		for (int i = 0; i < 7 ; i ++) {
			entries.add(new Entry(i, r.nextInt(20) +  50));
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

		Date date = sb.getDate();
		TextView txtTitle = findViewById(R.id.txtWaterTitle),
				txtYear = findViewById(R.id.txtWaterSmallTitle);
		txtTitle.setText(String.format("%s %s", CalendarActivity.getMonthString(this, date.getMonth()), date.getDay()));
		txtYear.setText(String.valueOf(date.getYear()));

		pastTimeReviews = new ArrayList<>();
		/*for (int i=0; i<4; i++) {
			pastTimeReviews.add(new PastTimeReview(new Random().nextInt(4) + 1, "test"));
		}*/

		ArrayList<EventsItem> eventsItems = DatabaseHelper.getInstance().getEventByDay(date);
		if (eventsItems != null) {
			eventsItems.forEach(eventsItem ->
				pastTimeReviews.add(new PastTimeReview(eventsItem.getCategory(), eventsItem.getBody()))
			);
		}

		findViewById(R.id.imgbtnBack).setOnClickListener(v -> finish());

		RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
		eventsView = findViewById(R.id.rvPastEvents);
		//eventsView.setHasFixedSize(true);
		eventsView.setLayoutManager(layoutManager);
		eventsView.addItemDecoration(new HorizontalPaddingItemDecoration(110));
		eventsView.setAdapter(new ReviewAdapter(pastTimeReviews));

		// init chart view
		lineChart = findViewById(R.id.viewWaterAnalysis);
		lineChart.getDescription().setEnabled(false);
		lineChart.getXAxis().setEnabled(false);
		lineChart.getAxisRight().setEnabled(false);
		lineChart.setDrawBorders(false);
		lineChart.setTouchEnabled(false);
		LineDataSet ls = new LineDataSet(getRandomList(), "Water");
		ls.setDrawCircles(false);
		ls.setLineWidth(2f);
		lineChart.setDrawGridBackground(false);
		ls.setColor(getColor(R.color.colorChart));
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
				startActivity(new Intent(this, BootstrapScannerActivity.class)));

		imgbtnNavBarArticle.setOnClickListener(v ->
				startActivity(new Intent(this, ArticleActivity.class)));

		imgbtnNavBarMedical.setOnClickListener(v ->
				startActivity(new Intent(this, MedicalActivity.class)));

		imgbtnNavBarUser.setOnClickListener(v ->
				startActivity(new Intent(this, ProfileActivity.class)));

	}
}
