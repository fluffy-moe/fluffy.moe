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
package moe.fluffy.app.assistant;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.MultiFormatUPCEANReader;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Calendar;
import java.util.Random;

public class Utils {

	private static final String TAG = "log_Utils";
	private static Random random;

	/* copied from com.codbking.calendar.example */
	public static  int getColor(Context context,int res){
		Resources r = context.getResources();
		return r.getColor(res);
	}

	public static float getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	}


	public static int px(float dipValue) {
		Resources r = Resources.getSystem();
		final float scale = r.getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}


	//获取显示版本
	public static String getVersionName(Context context) {
		try {
			PackageManager manager =context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	//获取版本信息
	public static int getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			int version = info.versionCode;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static StateListDrawable getRoundSelectorDrawable(int alpha, int color, int radir) {
		Drawable pressDrawable = getRoundDrawable(alpha, color, radir);
		Drawable normalDrawable = getRoundDrawable(color, radir);
		return getStateListDrawable(pressDrawable, normalDrawable);
	}

	//获取带透明度的圆角矩形
	public static Drawable getRoundDrawable(int alpha, int color, int radius) {
		int normalColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
		//Drawable normalDrawable = getRoundDrawable(normalColor, radius);
		return getRoundDrawable(normalColor, radius);
	}


	//根据颜色获取圆角矩形
	public static Drawable getRoundDrawable(int color, int radius) {
		radius = px(radius);
		GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{color, color});
		drawable.setCornerRadius(radius);
		return drawable;
	}

	public static StateListDrawable getStateListDrawable(Drawable pressDrawable, Drawable normalDrawable) {
		int pressed = android.R.attr.state_pressed;
		int select = android.R.attr.state_selected;
		StateListDrawable drawable = new StateListDrawable();
		drawable.addState(new int[]{pressed}, pressDrawable);
		drawable.addState(new int[]{select}, pressDrawable);
		drawable.addState(new int[]{}, normalDrawable);
		return drawable;
	}

	public static StateListDrawable getSelectDrawable(int color) {
		int select = android.R.attr.state_selected;
		StateListDrawable drawable = new StateListDrawable();

		GradientDrawable drawable2=new GradientDrawable();
		drawable2.setShape(GradientDrawable.OVAL);
		drawable2.setColor(color);

		drawable.addState(new int[]{select}, drawable2);
		drawable.addState(new int[]{},null);

		return drawable;
	}
	/* copied from com.codbking.calendar.example end */

	public static String exceptionToString(Throwable e) {
		return exceptionToString((Exception)e);
	}

	public static String exceptionToString(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	public static void onFocusChange(boolean hasFocus,
									 @NotNull Context context,
									 @NotNull EditText et,
									 @StringRes int original_string_id,
									 boolean isPasswordField) {
		if (hasFocus) {
			if (et.getText().toString().equals(context.getString(original_string_id))) {
				et.setText("");
				// https://stackoverflow.com/a/9893496
				if (isPasswordField)
					et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
		} else {
			if (et.getText().length() == 0) {
				et.setText(original_string_id);
				if (isPasswordField)
					et.setInputType(InputType.TYPE_CLASS_TEXT);

			}
		}
	}

	public static Bitmap getBitmap(@NonNull Context context, @NonNull Intent intent) throws IOException {
		Uri u = intent.getData();
		InputStream is;
		Bitmap bmp = null;
		if (u != null) {
			is = context.getContentResolver().openInputStream(u);
			bmp = BitmapFactory.decodeStream(is);
		}
		return bmp;
	}

	public static Bitmap getBitmap(@NonNull Context context, @NonNull Uri uri) throws IOException {
		Bitmap bmp;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			ImageDecoder.Source source = ImageDecoder.createSource(context.getContentResolver(), uri);
			bmp = ImageDecoder.decodeBitmap(source);
		} else {
			bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
		}
		return bmp;
	}

	// https://stackoverflow.com/a/13133974
	public static void saveFile(@NonNull URI sourceUri, @NonNull String destinationFilename) {
		String sourceFilename= sourceUri.getPath();
		//String destinationFilename = android.os.Environment.getExternalStorageDirectory().getPath()+File.separatorChar+"abc.mp3";

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(sourceFilename));
			bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
			byte[] buf = new byte[1024];
			bis.read(buf);
			do {
				bos.write(buf);
			} while(bis.read(buf) != -1);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) bis.close();
				if (bos != null) bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public static String generateRandomString(@Nullable Integer max_length) {
		if (random == null) {
			random = new Random();
		}
		if (max_length == null) {
			max_length = 16;
		}
		StringBuilder stringBuilder = new StringBuilder();
		for (int i=0; i< max_length ; i++)
			stringBuilder.append((char)(65 + random.nextInt(26)));
		return stringBuilder.toString();
	}

	public static String realDecode(@NonNull Bitmap bitmap) {
		int width = bitmap.getWidth(), height = bitmap.getHeight();
		int[] pixels = new int[width*height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
		Reader reader = new MultiFormatUPCEANReader(null);
		Result result = null;
		try {
			result = reader.decode(binaryBitmap);
		} catch (NotFoundException | ChecksumException | FormatException e) {
			e.printStackTrace();
		}
		String text;
		if (result != null) {
			text = result.getText();
			//Log.d(TAG, "realDecode: text => " + text);
		} else {
			throw new NullPointerException();
		}
		return text;
	}

	public static String saveBitmap(@NonNull Bitmap bmp, @NonNull String filePath) {
		try (FileOutputStream out = new FileOutputStream(filePath)) {
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}

	public static Calendar getTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(calendar.getTime());
		return calendar;
	}
}
