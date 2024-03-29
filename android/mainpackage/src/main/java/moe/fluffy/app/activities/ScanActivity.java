package moe.fluffy.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.ViewfinderView;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import moe.fluffy.app.R;

/**
 * Custom Scanner Activity extending from Activity to display a custom layout form scanner view.
 */
public class ScanActivity extends Activity{

	private CaptureManager capture;
	private DecoratedBarcodeView barcodeScannerView;
	//private Button switchFlashlightButton;
	private ViewfinderView viewfinderView;


	TextView txtTitle, txtHint;

	ImageButton imgbtnBack, imgbtnChooseFromGallery, imgbtnShowRecord;
	private ImageButton imgbtnOCR;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_custom_scanner);

		barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);

		viewfinderView = findViewById(R.id.zxing_viewfinder_view);

		init();
		capture = new CaptureManager(this, barcodeScannerView);
		capture.initializeFromIntent(getIntent(), savedInstanceState);
		capture.decode();

		changeMaskColor(null);
		changeLaserVisibility(true);
	}

	void init() {
		txtTitle = findViewById(R.id.txtScannerTitle);
		txtHint = findViewById(R.id.txtScannerContent);
		imgbtnBack = findViewById(R.id.imgbtnScannerBack);
		imgbtnChooseFromGallery = findViewById(R.id.imgbtnScannerDevice);
		imgbtnShowRecord = findViewById(R.id.imgbtnScannerRecord);
		imgbtnOCR = findViewById(R.id.imgbtnScannerTranslate);

		// https://stackoverflow.com/a/55938561
		txtTitle.setTypeface(ResourcesCompat.getFont(this, R.font.segoe_ui_bold));
		txtHint.setTypeface(ResourcesCompat.getFont(this, R.font.segoe_ui_bold));
		imgbtnOCR.setOnClickListener(v -> {
			LocalBroadcastManager.getInstance(this).sendBroadcast(
				new Intent(BootstrapScannerActivity.BROADCAST_REQUEST_OCR_ACTIVITY));
			finish();
		});
		imgbtnBack.setOnClickListener(v -> finish());
		imgbtnChooseFromGallery.setOnClickListener(v -> {
			LocalBroadcastManager.getInstance(this).sendBroadcast(
					new Intent(BootstrapScannerActivity.BROADCAST_REQUEST_GALLERY));
			finish();
		});
		imgbtnShowRecord.setOnClickListener(v -> {
			//getIntent().putExtra(getString(R.string.extraAction), "record");
			LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BootstrapScannerActivity.BROADCAST_FOOD_HISTORY));
			finish();
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		capture.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		capture.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		capture.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(@NotNull Bundle outState) {
		super.onSaveInstanceState(outState);
		capture.onSaveInstanceState(outState);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
	}


	public void changeMaskColor(View view) {
		Random rnd = new Random();
		int color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
		viewfinderView.setMaskColor(color);
	}

	public void changeLaserVisibility(boolean visible) {
		viewfinderView.setLaserVisibility(visible);
	}

}
