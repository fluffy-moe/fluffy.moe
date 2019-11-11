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
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.StringRes;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Utils {
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
