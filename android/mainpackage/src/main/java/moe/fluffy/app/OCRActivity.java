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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.io.File;

import moe.fluffy.app.assistant.Callback;
import moe.fluffy.app.assistant.firebase.FirebaseOCR;

public class OCRActivity extends AppCompatActivity {


	private static String TAG = "log_OCRActivity";

	private static final int REQUEST_IMAGE_CAPTURE = 1;

	private Uri mImageUri;
	private String barcode, strResult;

	View vWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_detect);

		vWindow = findViewById(R.id.imgScannerPhoto);
		/*getLayoutInflater().inflate(R.layout.wait_layout, null);

		File photo;
		try	{
			// place where to store camera taken picture
			photo = this.createTemporaryFile("picture", ".jpg");
			if (!photo.delete()){
				Log.e(TAG, "onCreate: Delete temp file failure.");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.v(TAG, "Can't create file to take picture!");
			Toast.makeText(this, "Please check SD card! Image shot is impossible!", Toast.LENGTH_LONG).show();
			return;
		}

		barcode = getIntent().getStringExtra("barcode");

		final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mImageUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".fileprovider", photo);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);*/
	}

	private File createTemporaryFile(String part, String ext) throws Exception
	{
		File tempDir = Environment.getExternalStorageDirectory();
		tempDir = new File(tempDir.getAbsolutePath()+"/.temp/");
		Log.d(TAG, "createTemporaryFile: tmp => "+ tempDir.getAbsolutePath());
		if(!tempDir.exists()) {
			tempDir.mkdirs();
		}
		return File.createTempFile(part, ext, tempDir);
	}

	private AlertDialog buildProgressBar() {
		AlertDialog.Builder a = new AlertDialog.Builder(this);
		View b = LayoutInflater.from(this).inflate(R.layout.wait_layout, null);
		a.setView(b);
		AlertDialog dialog = a.create();
		dialog.show();
		return dialog;
	}

	public void grabImage()
	{
		this.getContentResolver().notifyChange(mImageUri, null);
		ContentResolver cr = this.getContentResolver();
		Bitmap bitmap;
		try
		{
			bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
			FirebaseOCR o = new FirebaseOCR(bitmap);
			//progressDialog = buildProgressBar();
			o.run();
			strResult = o.getLastResult();
			o.setCallBack(new Callback() {
				@Override
				public void onSuccess(Object o) {
					FirebaseOCR f = (FirebaseOCR)o;
					String s = f.getLastResult();
					if (s != null) {
						Log.d(TAG, "onSuccess: result => " + s);
						if (barcode != null) {
							// TODO: show barcode scanner page or null

							Intent rData = new Intent();
							rData.putExtra(getString(R.string.extraBarcode), barcode);
							rData.putExtra(getString(R.string.extraOcrResult), s);
							setResult(RESULT_OK, rData);
						}
						else
							setResult(RESULT_CANCELED);
						finish();
					}
				}

				@Override
				public void onFailure(Object o, Throwable e) {
					Log.e(TAG, "onFailure: error => ");
					e.printStackTrace();
				}

				@Override
				public void onFinish(Object o, @Nullable Throwable e) {
				}
			}).run();
		}
		catch (Exception e)
		{
			Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Failed to load", e);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			this.grabImage();
		}
		else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

}
