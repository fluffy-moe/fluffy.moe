package moe.fluffy.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BoostScanActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boost_scan);
		new IntentIntegrator(this).setOrientationLocked(false).setCaptureActivity(ScanActivity.class).initiateScan();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode != IntentIntegrator.REQUEST_CODE) {
			// This is important, otherwise the result will not be passed to the fragment
			super.onActivityResult(requestCode, resultCode, data);
			return;
		}

		IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);

		if(result.getContents() == null) {
			Log.d("MainActivity", "Cancelled scan");
			Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
		} else {
			Log.d("MainActivity", "Scanned");
			Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
		}
		finish();
	}
}
