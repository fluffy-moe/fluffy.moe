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
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.assistant.Utils;

public class BootstrapScannerActivity extends AppCompatActivity {

	private static final String TAG = "log_BootstrapScannerActivity";

	private static final int SELECT_FROM_GALLERY = 0x0002;
	private static final int GO_OCR_DETECT = 0x0003;
	BroadcastReceiver galleryRequestReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);
		new IntentIntegrator(this).setOrientationLocked(false).setCaptureActivity(ScanActivity.class).initiateScan();
		galleryRequestReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent i) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FROM_GALLERY);
			}
		};
		LocalBroadcastManager.getInstance(this).registerReceiver(galleryRequestReceiver,
				new IntentFilter(getString(R.string.IntentFilter_request_choose_from_gallery)));
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume: Resuming");
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != IntentIntegrator.REQUEST_CODE && requestCode != SELECT_FROM_GALLERY && requestCode != GO_OCR_DETECT) {
			// This is important, otherwise the result will not be passed to the fragment
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}

		if (requestCode == SELECT_FROM_GALLERY && resultCode == RESULT_OK) {
			try {
				String result;
				Bitmap bmp = Utils.getBitmap(this, data);
				if (bmp != null) {
					result = Utils.realDecode(bmp);
					Toast.makeText(this, "Result: " + result, Toast.LENGTH_LONG).show();
					Log.v(TAG, "Scan result is => " + result);
					checkBarcodeInDatabase(result);
				}
				return ;
			} catch (IOException e) {
				Log.e(TAG, "onActivityResult: Error while read image", e);
				PopupDialog.build(this, e);
			}
		}

		if (requestCode == GO_OCR_DETECT && resultCode == RESULT_OK) {
			Log.d(TAG, "onActivityResult: start history activity");
			Intent foodViewIntent = new Intent(this, FoodHistoryActivity.class);
			foodViewIntent.putExtra(getString(R.string.extraBarcode), data.getStringExtra(getString(R.string.extraBarcode)));
			foodViewIntent.putExtra(getString(R.string.extraOcrResult), data.getStringExtra(getString(R.string.extraOcrResult)));
			startActivity(foodViewIntent);
			return;
		}

		IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

		if (data != null) {
			Log.d(TAG, "onActivityResult: Record");
			String action = data.getStringExtra(getString(R.string.extraAction));
			if (action != null && action.equals("record")) {
				startActivity(new Intent(this, FoodHistoryActivity.class));
				finish();
				return;
			}
		}
		if(result.getContents() != null) {
			Log.d("MainActivity", "Scanned");
			//Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
			checkBarcodeInDatabase(result.getContents());
			return;
		}
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
	}
}
