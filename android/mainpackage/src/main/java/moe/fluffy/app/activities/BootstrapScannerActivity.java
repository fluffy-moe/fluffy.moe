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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import moe.fluffy.app.R;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.assistant.Utils;
import moe.fluffy.app.types.SerializableBundle;

public class BootstrapScannerActivity extends AppCompatActivity {

	private static final String TAG = "log_BootstrapScannerActivity";

	public static final String IMAGE_FILE_LOCATE = "N10AOMUSrdv";

	public static final String BARCODE_FIELD = "barcode";
	public static final String PRODUCT_BITMAP = "bitmap";

	private String pending_send_barcode = "";

	public static final String BROADCAST_FOOD_HISTORY = "foodHistory";
	public static final String BROADCAST_REQUEST_GALLERY = "requestGallery";
	public static final String BROADCAST_REQUEST_OCR_ACTIVITY = "sHZEvhS0epBdx";

	private static final int SELECT_FROM_GALLERY = 0x0002; // SELECT GALLERY
	public static final int OCR_ACTIVITY = 21;
	BroadcastReceiver galleryRequestReceiver, historyRequestReceiver,
		ocrRequestReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);
		init();
		createMediaStore();
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
				new IntentFilter(BROADCAST_REQUEST_GALLERY));

		LocalBroadcastManager.getInstance(this).registerReceiver(historyRequestReceiver,
				new IntentFilter(BROADCAST_FOOD_HISTORY));

		LocalBroadcastManager.getInstance(this).registerReceiver(ocrRequestReceiver,
				new IntentFilter(BROADCAST_REQUEST_OCR_ACTIVITY));

	}

	private void createMediaStore() {
		File tempDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/fluffy/");
		if(!tempDir.exists()) {
			if (!tempDir.mkdirs()) {
				Toast.makeText(this, "mkdir fail, please check privileges", Toast.LENGTH_LONG).show();
			}
		}
	}

	private void callCropPhoto(File _file) {
		Log.d(TAG, "callCropPhoto: called");
		CropImage.activity(Uri.fromFile(_file))
				.setCropMenuCropButtonTitle("done")
				.start(this);
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume: Resuming");
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult: request_code =>" + requestCode + " resultCode => " + resultCode);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case SELECT_FROM_GALLERY: // AFTER SELECT IMAGE from gallery
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
					return;
				case OCR_ACTIVITY: // AFTER CAPTURE IMAGE
					Log.v(TAG, "onActivityResult: got result");
					pending_send_barcode = data.getStringExtra(BARCODE_FIELD);
					callCropPhoto(new File(CameraActivity.getSaveLocation()));
					return;
				case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE: // AFTER CROP IMAGE
					Log.v(TAG, "onActivityResult: Called crop image");
					Bitmap bmp;
					CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);
					Uri resultUri = cropResult.getUri();
					try {
						bmp = Utils.getBitmap(this, resultUri);
						Bundle bundle = new Bundle();
						bundle.putSerializable(PRODUCT_BITMAP, new SerializableBundle(bmp));
						Intent foodIntent = new Intent(this, FoodHistoryActivity.class);
						foodIntent.putExtra(BARCODE_FIELD, pending_send_barcode);
						foodIntent.putExtras(bundle);
						startActivity(foodIntent);
					} catch (FileNotFoundException ignore) {

					} catch (IOException e) {
						e.printStackTrace();
						PopupDialog.build(this, e);
					} finally {
						finish();
					}

					return;
				case IntentIntegrator.REQUEST_CODE:
					break;
				default:
					super.onActivityResult(requestCode, resultCode, data);
			}
		} else {
			if (requestCode == SELECT_FROM_GALLERY) {
				if (data.getStringExtra(BARCODE_FIELD) == null) {
					callScannerActivity();
				} else {
					Intent intent = new Intent(this, CameraActivity.class);
					intent.putExtra(BARCODE_FIELD, data.getStringExtra(BARCODE_FIELD));
					startActivityForResult(intent, OCR_ACTIVITY);
				}
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
			//finish();
			return;
		}
		//finish();
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}

	private void checkBarcodeInDatabase(String barcode) {
		// TODO: check database here (Cloud database)
		Toast.makeText(this, "Barcode not in database, please scan the food name manually", Toast.LENGTH_SHORT).show();
		Intent captureActivity = new Intent(this, CameraActivity.class);
		captureActivity.putExtra(BARCODE_FIELD, barcode);
		startActivityForResult(captureActivity, OCR_ACTIVITY);
		//Log.v(TAG, "checkBarcodeInDatabase: Started ocr activity");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(galleryRequestReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(historyRequestReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(ocrRequestReceiver);
	}
}
