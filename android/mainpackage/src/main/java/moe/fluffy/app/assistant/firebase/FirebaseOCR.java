/*
 ** Copyright (C) 2019 KunoiSayami
 **
 ** This file is part of 1081-OCRDemo and is released under
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
package moe.fluffy.app.assistant.firebase;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseOCR {
	String lastResult = "";
	private static String TAG = "log_FirebaseOCR";
	FirebaseVisionTextRecognizer detector;
	FirebaseVisionImage image;

	List<FirebaseVisionText.TextBlock> blocks = new ArrayList<>();

	private Task<FirebaseVisionText> firebaseVisionTextTask;

	private static FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
			.setLanguageHints(Arrays.asList("zh", "en"))
			.build();
	FirebaseOCR(Bitmap bitmap) {
		image = FirebaseVisionImage.fromBitmap(bitmap);
		detector = FirebaseVision.getInstance().getCloudTextRecognizer(options);
	}

	Task<FirebaseVisionText> run() {
		firebaseVisionTextTask = detector.processImage(image)
				.addOnSuccessListener(result -> {
					// Task completed successfully
					StringBuilder sb = new StringBuilder();
					blocks = result.getTextBlocks();
					for (FirebaseVisionText.TextBlock block : result.getTextBlocks()) {
						sb.append(block.getText());
					}
					lastResult = sb.toString();
					Log.d(TAG, "onSuccess: text => " + lastResult);
				})
				.addOnFailureListener(e -> Log.e(TAG, "onFailure: ", e));
		return firebaseVisionTextTask;
	}

	void waitTask() throws InterruptedException {
		firebaseVisionTextTask.wait();
	}
}
