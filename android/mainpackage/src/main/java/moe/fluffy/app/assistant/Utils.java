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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.InputType;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.StringRes;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils {
	/* copied from com.codbking.calendar.example */
	public static  int getColor(Context context,int res){
		Resources r=context.getResources();
		return r.getColor(res);
	}

	public static float getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	}


	public static int px(float dipValue) {
		Resources r=Resources.getSystem();
		final float scale =r.getDisplayMetrics().density;
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
		Drawable pressDrawable = getRoundDrawalbe(alpha, color, radir);
		Drawable normalDrawable = getRoundDrawalbe(color, radir);
		return getStateListDrawable(pressDrawable, normalDrawable);
	}

	//获取带透明度的圆角矩形
	public static Drawable getRoundDrawalbe(int alpha, int color, int radir) {
		int normalColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
		Drawable normalDrawable = getRoundDrawalbe(normalColor, radir);
		return normalDrawable;
	}


	//根据颜色获取圆角矩形
	public static Drawable getRoundDrawalbe(int color, int radir) {
		radir = px(radir);
		GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{color, color});
		drawable.setCornerRadius(radir);
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
	/* end */

	public static String exceptionToString(Throwable e) {
		return exceptionToString((Exception)e);
	}

	public static String exceptionToString(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	public static void onFocusChange(boolean hasFocus, Context context, @NotNull EditText et, @StringRes int original_string_id, boolean isPasswordField) {
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
}
