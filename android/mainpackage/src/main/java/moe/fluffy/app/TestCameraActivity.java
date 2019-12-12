package moe.fluffy.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import moe.fluffy.app.assistant.Callback;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.assistant.firebase.FirebaseOCR;

import static moe.fluffy.app.BootstrapScannerActivity.IMAGE_FILE_LOCATE;

public class TestCameraActivity extends AppCompatActivity {

	private static final String TAG = "log_TestCameraActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.test_camera_activity);
		startActivityForResult(new Intent(this, CameraActivity.class), 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		Log.v(TAG, "onActivityResult: requestCode =>" + requestCode + " Result code => " + resultCode);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode){
				case 1:
					CropImage.activity(Uri.fromFile(new File(CameraActivity.getSaveLocation())))
							.setCropMenuCropButtonTitle("done")
							.start(this);
					Log.v(TAG, "onActivityResult: start activity");
					break;
				case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
					Bitmap bmp;
					CropImage.ActivityResult cropResult = CropImage.getActivityResult(data);
					Uri resultUri = cropResult.getUri();
					try {
						if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
							ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), resultUri);
							bmp = ImageDecoder.decodeBitmap(source);
						} else {
							bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
						}
						ImageView img = findViewById(R.id.imgTest);
						img.setImageBitmap(bmp);
					} catch (FileNotFoundException ignore) {

					} catch (IOException e) {
						e.printStackTrace();
						PopupDialog.build(this, e);
					}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
