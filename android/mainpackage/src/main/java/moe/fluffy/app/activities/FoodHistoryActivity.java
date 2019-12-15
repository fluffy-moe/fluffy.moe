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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.Util;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import moe.fluffy.app.BuildConfig;
import moe.fluffy.app.R;
import moe.fluffy.app.adapter.FoodAdapter;
import moe.fluffy.app.assistant.Callback;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.assistant.Utils;
import moe.fluffy.app.assistant.firebase.FirebaseOCR;
import moe.fluffy.app.types.FoodViewType;
import moe.fluffy.app.types.SerializableBundle;

public class FoodHistoryActivity extends AppCompatActivity {

	ListView lvFoodHistory;

	ArrayList<FoodViewType> foodList;

	FoodAdapter foodAdapter;

	ImageButton imgbtnNavBarCamera, imgbtnNavBarMedical, imgbtnNavBarCalendar,
			imgbtnNavBarArticle, imgbtnNavBarUser;

	public static String getFileStorage() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/fluffy/foodHistory";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_food_history);
		init();
	}
	private void init() {
		lvFoodHistory = findViewById(R.id.lvFoods);
		initNavigationBar();
		foodList = HomeActivity.dbHelper.getFoodHistory();
		foodAdapter = new FoodAdapter(this, foodList);
		lvFoodHistory.setAdapter(foodAdapter);
		/* // test code
		findViewById(R.id.btnAddHistory).setOnClickListener(
				v -> {
					getIntent().putExtra(getString(R.string.extraBarcode), "12341325123");
					getIntent().putExtra(getString(R.string.extraOcrResult), "Random text");
					resumeEdit();
				}
		);*/
		resumeEdit();
	}

	void resumeEdit() {
		String barcode;
		barcode = getIntent().getStringExtra(BootstrapScannerActivity.BARCODE_FIELD);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			/* something process bar here */
			Uri bmpUri = ((SerializableBundle)bundle.getSerializable(BootstrapScannerActivity.PRODUCT_BITMAP)).getBmpUri();
			try {
				Bitmap bmp = Utils.getBitmap(this, bmpUri),
						realImage = Utils.getBitmap(this, Uri.fromFile(new File(CameraActivity.getSaveLocation())));
				FirebaseOCR fb = new FirebaseOCR(bmp);
				fb.setCallBack(new Callback() {
					@Override
					public void onSuccess(Object o) {
						FirebaseOCR f = (FirebaseOCR) o;
						FoodViewType it = new FoodViewType(barcode, f.getLastResult());
						FoodAdapter.generateDialog(FoodHistoryActivity.this, it, foodAdapter, o1 -> foodList.add((FoodViewType) o1), realImage);
					}

					@Override
					public void onFailure(Object o, Throwable e) {

					}

					@Override
					public void onFinish(Object o, @Nullable Throwable e) {

					}
				}).run();
			} catch (FileNotFoundException ignore) {

			} catch (IOException e) {
				e.printStackTrace();
				PopupDialog.build(this, e);
			}
			getIntent().removeExtra(BootstrapScannerActivity.BARCODE_FIELD);
			//getIntent().removeExtra(getString(R.string.extraOcrResult));
		}
	}

	void initNavigationBar() {
		imgbtnNavBarCamera = findViewById(R.id.imgbtnCameraPage);
		imgbtnNavBarMedical = findViewById(R.id.imgbtnMedicalPage);
		imgbtnNavBarCalendar = findViewById(R.id.imgbtnCalendarPage);
		imgbtnNavBarArticle = findViewById(R.id.imgbtnArticlePage);
		imgbtnNavBarUser = findViewById(R.id.imgbtnUserPage);

		imgbtnNavBarCamera.setImageResource(R.drawable.camera_orange);

		imgbtnNavBarCamera.setOnClickListener(v ->
				startActivity(new Intent(FoodHistoryActivity.this, BootstrapScannerActivity.class)));

		imgbtnNavBarMedical.setOnClickListener(v ->
				startActivity(new Intent(FoodHistoryActivity.this, MedicalActivity.class)));

		imgbtnNavBarArticle.setOnClickListener(v ->
				startActivity(new Intent(FoodHistoryActivity.this, ArticleActivity.class)));

		imgbtnNavBarCalendar.setOnClickListener(v ->
				startActivity(new Intent(FoodHistoryActivity.this, CalendarActivity.class)));

	}
}
