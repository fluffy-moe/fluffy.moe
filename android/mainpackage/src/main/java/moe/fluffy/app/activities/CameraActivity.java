package moe.fluffy.app.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import moe.fluffy.app.R;

public class CameraActivity extends AppCompatActivity {
	private static final String TAG = "log_AndroidCameraApi";
	private ImageButton takePictureButton;
	private TextureView textureView;
	private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

	static {
		ORIENTATIONS.append(Surface.ROTATION_0, 0);
		ORIENTATIONS.append(Surface.ROTATION_90, 90);
		ORIENTATIONS.append(Surface.ROTATION_180, 180);
		ORIENTATIONS.append(Surface.ROTATION_270, 270);
	}

	private String cameraId;
	protected CameraDevice cameraDevice;
	protected CameraCaptureSession cameraCaptureSessions;
	protected CaptureRequest captureRequest;
	protected CaptureRequest.Builder captureRequestBuilder;
	private Size imageDimension;
	private ImageReader imageReader;
	private File file;
	private static final int REQUEST_CAMERA_PERMISSION = 200;
	private Handler mBackgroundHandler;
	private HandlerThread mBackgroundThread;
	private ImageButton imgbtnChooseFromGallery, imgbtnShowRecord;

	private String barcode;

	public static String getSaveLocation() {
		return Environment.getExternalStorageDirectory().getAbsolutePath() + "/fluffy/pic.jpg";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_detect);
		textureView = findViewById(R.id.imgScannerPhoto);
		textureView.setSurfaceTextureListener(textureListener);
		takePictureButton = findViewById(R.id.imgbtnScannerTranslate);
		takePictureButton.setOnClickListener(v -> {
			Log.v(TAG, "onCreate: Click capture, Finish job");
			takePicture();
			//getIntent().putExtra(BootstrapScannerActivity.IMAGE_FILE_LOCATE, getSaveLocation());
			getIntent().putExtra(BootstrapScannerActivity.BARCODE_FIELD, barcode);
			setResult(RESULT_OK); // MUST SET OK OR WILL RETURN NOT OK
			finish();
		});

		imgbtnChooseFromGallery = findViewById(R.id.imgbtnScannerDevice);
		imgbtnShowRecord = findViewById(R.id.imgbtnScannerRecord);

		barcode = getIntent().getStringExtra(BootstrapScannerActivity.BARCODE_FIELD);

		imgbtnChooseFromGallery.setOnClickListener(v -> {
			LocalBroadcastManager.getInstance(this).sendBroadcast(
					new Intent(BootstrapScannerActivity.BROADCAST_REQUEST_GALLERY));
			getIntent().putExtra(BootstrapScannerActivity.BARCODE_FIELD, barcode);
			setResult(RESULT_OK);
			finish();
		});

		imgbtnShowRecord.setOnClickListener(v -> {
			//getIntent().putExtra(getString(R.string.extraAction), "record");
			LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(BootstrapScannerActivity.BROADCAST_FOOD_HISTORY));
			setResult(RESULT_OK);
			finish();
		});
		Log.v(TAG, "onCreate: saved location => " + getSaveLocation());
	}

	TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
		@Override
		public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
			//open your camera here
			openCamera();
		}

		@Override
		public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
			// Transform you image captured size according to the surface width and height
		}

		@Override
		public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
			return false;
		}

		@Override
		public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		}
	};

	private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
		@Override
		public void onOpened(@NotNull CameraDevice camera) {
			//This is called when the camera is open
			Log.v(TAG, "onOpened");
			cameraDevice = camera;
			createCameraPreview();
		}

		@Override
		public void onDisconnected(@NotNull CameraDevice camera) {
			cameraDevice.close();
		}

		@Override
		public void onError(@NotNull CameraDevice camera, int error) {
			if (cameraDevice != null) {
				cameraDevice.close();
				cameraDevice = null;
			}
		}
	};

	final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
		@Override
		public void onCaptureCompleted(@NotNull CameraCaptureSession session, @NotNull CaptureRequest request, @NotNull TotalCaptureResult result) {
			super.onCaptureCompleted(session, request, result);
			Toast.makeText(CameraActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
			createCameraPreview();
		}
	};

	protected void startBackgroundThread() {
		mBackgroundThread = new HandlerThread("Camera Background");
		mBackgroundThread.start();
		mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
	}

	protected void stopBackgroundThread() {
		if (mBackgroundThread == null)
			return;
		mBackgroundThread.quitSafely();
		try {
			mBackgroundThread.join();
			mBackgroundThread = null;
			mBackgroundHandler = null;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void takePicture() {
		if (null == cameraDevice) {
			Log.e(TAG, "cameraDevice is null");
			return;
		}
		CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
		try {
			assert manager != null;
			CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
			Size[] jpegSizes;
			jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
			int width = 970;
			int height = 970;
			if (jpegSizes != null && 0 < jpegSizes.length) {
				width = jpegSizes[0].getWidth();
				height = jpegSizes[0].getHeight();
			}
			ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
			List<Surface> outputSurfaces = new ArrayList<>(2);
			outputSurfaces.add(reader.getSurface());
			outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
			final CaptureRequest.Builder captureBuilder =
					cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
			captureBuilder.addTarget(reader.getSurface());
			captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
			// Orientation
			int rotation = getWindowManager().getDefaultDisplay().getRotation();
			captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
			file = new File(getSaveLocation());
			if (file.exists()) {
				// FIXME: file should override
				file.delete();
			}
			ImageReader.OnImageAvailableListener readerListener = reader1 -> {
				try (Image image = reader1.acquireLatestImage()) {
					ByteBuffer buffer = image.getPlanes()[0].getBuffer();
					byte[] bytes = new byte[buffer.capacity()];
					buffer.get(bytes);
					try (OutputStream output = new FileOutputStream(file)) {
						output.write(bytes);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			};

			reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
			final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
				@Override
				public void onCaptureCompleted(@NotNull CameraCaptureSession session, @NotNull CaptureRequest request, @NotNull TotalCaptureResult result) {
					super.onCaptureCompleted(session, request, result);
					Log.v(TAG, "saved");
					Toast.makeText(CameraActivity.this, "Saved:" + file, Toast.LENGTH_SHORT).show();
					createCameraPreview();
				}
			};
			cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
				@Override
				public void onConfigured(@NotNull CameraCaptureSession session) {
					try {
						session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
					} catch (CameraAccessException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onConfigureFailed(@NotNull CameraCaptureSession session) {
				}
			}, mBackgroundHandler);

		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	protected void createCameraPreview() {
		try {
			SurfaceTexture texture = textureView.getSurfaceTexture();
			assert texture != null;
			texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
			Surface surface = new Surface(texture);
			captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
			captureRequestBuilder.addTarget(surface);
			cameraDevice.createCaptureSession(Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
				@Override
				public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
					//The camera is already closed
					if (null == cameraDevice) {
						return;
					}
					// When the session is ready, we start displaying the preview.
					cameraCaptureSessions = cameraCaptureSession;
					updatePreview();
				}

				@Override
				public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
					Toast.makeText(CameraActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
				}
			}, null);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	private void openCamera() {
		CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
		Log.v(TAG, "is camera open");
		try {
			cameraId = manager.getCameraIdList()[0];
			CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
			StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
			assert map != null;
			imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
			// Add permission for camera and let user grant the permission
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
				return;
			}
			manager.openCamera(cameraId, stateCallback, null);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
		Log.v(TAG, "openCamera X");
	}

	protected void updatePreview() {
		if (null == cameraDevice) {
			Log.e(TAG, "updatePreview error, return");
		}
		captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
		try {
			cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
		} catch (CameraAccessException e) {
			e.printStackTrace();
		}
	}

	private void closeCamera() {
		if (null != cameraDevice) {
			cameraDevice.close();
			cameraDevice = null;
		}
		if (null != imageReader) {
			imageReader.close();
			imageReader = null;
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == REQUEST_CAMERA_PERMISSION) {
			if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
				// close the app
				Toast.makeText(CameraActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
				finish();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "onResume");
		startBackgroundThread();
		if (textureView.isAvailable()) {
			openCamera();
		} else {
			textureView.setSurfaceTextureListener(textureListener);
		}
	}

	@Override
	protected void onPause() {
		Log.v(TAG, "onPause");
		closeCamera();
		stopBackgroundThread();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		closeCamera();
		stopBackgroundThread();
		super.onDestroy();
	}

}