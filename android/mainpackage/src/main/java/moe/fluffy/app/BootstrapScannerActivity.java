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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

import moe.fluffy.app.assistant.Callback;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.assistant.Utils;
import moe.fluffy.app.assistant.firebase.FirebaseOCR;

public class BootstrapScannerActivity extends AppCompatActivity {

	private static final String TAG = "log_BootstrapScannerActivity";
	public static final String REQUEST_OCR_ACTIVITY = "sHZEvhS0epBdx";
	public static final String IMAGE_FILE_LOCATE = "N10AOMUSrdv";

	private static final int SELECT_FROM_GALLERY = 0x0002;
	private static final int GO_OCR_DETECT = 0x0003;
	public static final int OCR_ACTIVITY = 21;
	private static final int REQUEST_CROP_IMAGE = 1006;
	BroadcastReceiver galleryRequestReceiver, historyRequestReceiver,
		ocrRequestReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);
		init();
		callScannerActivity();
	}

	private void callScannerActivity() {
		new IntentIntegrator(this).setOrientationLocked(false).setCaptureActivity(ScanActivity.class).initiateScan();
	}

	private void init() {
		galleryRequestReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent i) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FROM_GALLERY);
			}
		};
		historyRequestReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				startActivity(new Intent(BootstrapScannerActivity.this, FoodHistoryActivity.class));
			}
		};

		ocrRequestReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				startActivityForResult(new Intent(BootstrapScannerActivity.this, CameraActivity.class), OCR_ACTIVITY);
			}
		};

		LocalBroadcastManager.getInstance(this).registerReceiver(galleryRequestReceiver,
				new IntentFilter(getString(R.string.IntentFilter_request_choose_from_gallery)));

		LocalBroadcastManager.getInstance(this).registerReceiver(historyRequestReceiver,
				new IntentFilter(getString(R.string.IntentFilter_open_food_history)));

		LocalBroadcastManager.getInstance(this).registerReceiver(ocrRequestReceiver,
				new IntentFilter(REQUEST_OCR_ACTIVITY));

	}

	private void callCropPhoto(File _file) {
		Log.d(TAG, "callCropPhoto: called");
		Uri uri = Uri.fromFile(_file);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 117);
		intent.putExtra("outputY", 287);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		intent = Intent.createChooser(intent, "Crop photo");
		startActivityForResult(intent, REQUEST_CROP_IMAGE);
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume: Resuming");
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult: request_code =>" + requestCode);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case SELECT_FROM_GALLERY:
					try {
						String result;
						Bitmap bmp = Utils.getBitmap(this, data);
						if (bmp != null) {
							result = Utils.realDecode(bmp);
							Toast.makeText(this, "Result: " + result, Toast.LENGTH_LONG).show();
							Log.v(TAG, "Scan result is => " + result);
							checkBarcodeInDatabase(result);
						}
					} catch (IOException e) {
						Log.e(TAG, "onActivityResult: Error while read image", e);
						PopupDialog.build(this, e);
					}
					break;
				case GO_OCR_DETECT:
					Log.d(TAG, "onActivityResult: start history activity");
					Intent foodViewIntent = new Intent(this, FoodHistoryActivity.class);
					if (data != null) {
						foodViewIntent.putExtra(getString(R.string.extraBarcode), data.getStringExtra(getString(R.string.extraBarcode)));
						foodViewIntent.putExtra(getString(R.string.extraOcrResult), data.getStringExtra(getString(R.string.extraOcrResult)));
					}
					startActivity(foodViewIntent);
					break;
				case OCR_ACTIVITY:
					Log.v(TAG, "onActivityResult: got result");
					if (data != null) {
						String path = data.getStringExtra(IMAGE_FILE_LOCATE);
						if (path != null) {
							callCropPhoto(new File(path));
						}
					}
					break;
				case REQUEST_CROP_IMAGE:
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bmp = extras.getParcelable("data");
						new FirebaseOCR(bmp).setCallBack(new Callback() {
							@Override
							public void onSuccess(Object o) {
								FirebaseOCR f = (FirebaseOCR)o;
								String s = f.getLastResult();
								Toast.makeText(BootstrapScannerActivity.this, s, Toast.LENGTH_LONG).show();
							}

							@Override
							public void onFailure(Object o, Throwable e) {

							}

							@Override
							public void onFinish(Object o, @Nullable Throwable e) {

							}
						}).run();
					}
					break;
				default:
					super.onActivityResult(requestCode, resultCode, data);
			}
			return ;
		} else {
			if (requestCode == SELECT_FROM_GALLERY) {
				callScannerActivity();
			}
			super.onActivityResult(requestCode, resultCode, data);
		}

		if (requestCode == IntentIntegrator.REQUEST_CODE) {
			IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
			if (result.getContents() != null) {
				Log.d(TAG, "Scanned");
				//Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
				checkBarcodeInDatabase(result.getContents());
			}
			finish();
			return;
		}
		//finish();
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}

	private void checkBarcodeInDatabase(String barcode) {
		// TODO: check database here (Cloud database)
		Toast.makeText(this, "Barcode not in database, please scan the food name manually", Toast.LENGTH_SHORT).show();
		Intent historyIntent = new Intent(this, OCRActivity.class);
		historyIntent.putExtra("barcode", barcode);
		startActivityForResult(historyIntent, GO_OCR_DETECT);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(galleryRequestReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(historyRequestReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(ocrRequestReceiver);
	}
}
