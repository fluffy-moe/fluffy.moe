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

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.assistant.Utils;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BoostScanActivity extends AppCompatActivity {


	private static final int SELECT_FROM_GALLERY = 0x0002;
	BroadcastReceiver galleryRequestReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boost_scan);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != IntentIntegrator.REQUEST_CODE && requestCode != SELECT_FROM_GALLERY) {
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
				}
			} catch (IOException e) {
				Log.e(TAG, "onActivityResult: Error while read image", e);
				PopupDialog.build(this, e);
			}
			finish();
			return;
		}

		IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

		if(result.getContents() == null) {
			Log.d("MainActivity", "Cancelled scan");
			//Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
		} else {
			Log.d("MainActivity", "Scanned");
			Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
		}
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(galleryRequestReceiver);
	}
}
