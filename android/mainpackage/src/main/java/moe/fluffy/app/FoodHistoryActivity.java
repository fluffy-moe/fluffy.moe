package moe.fluffy.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import moe.fluffy.app.assistant.Callback;
import moe.fluffy.app.assistant.firebase.FirebaseOCR;
import moe.fluffy.app.types.FoodViewType;
import moe.fluffy.app.adapter.FoodAdapter;
import moe.fluffy.app.types.SerializableBundle;

public class FoodHistoryActivity extends AppCompatActivity {

	ListView lvFoodHistory;

	ArrayList<FoodViewType> foodList;

	FoodAdapter foodAdapter;

	ImageButton imgbtnNavBarCamera, imgbtnNavBarMedical, imgbtnNavBarCalendar,
			imgbtnNavBarArticle, imgbtnNavBarUser;


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
			FirebaseOCR fb = new FirebaseOCR(((SerializableBundle)bundle.getSerializable(BootstrapScannerActivity.PRODUCT_BITMAP)).getBmp());
			fb.setCallBack(new Callback() {
				@Override
				public void onSuccess(Object o) {
					FirebaseOCR f = (FirebaseOCR)o;
					FoodViewType it = new FoodViewType(barcode, f.getLastResult());
					FoodAdapter.generateDialog(FoodHistoryActivity.this, it, foodAdapter, new Callback() {
						@Override public void onSuccess(Object o) { }

						@Override public void onFailure(Object o, Throwable e) { }

						@Override
						public void onFinish(Object o, @Nullable Throwable e) {
							foodList.add((FoodViewType)o);
						}
					});
				}

				@Override
				public void onFailure(Object o, Throwable e) {

				}

				@Override
				public void onFinish(Object o, @Nullable Throwable e) {

				}
			}).run();
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
