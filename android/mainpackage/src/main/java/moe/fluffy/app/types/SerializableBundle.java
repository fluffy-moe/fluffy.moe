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
package moe.fluffy.app.types;

import android.graphics.Bitmap;

import com.codbking.calendar.CalendarBean;

import java.io.Serializable;

import moe.fluffy.app.assistant.SimpleCallback;

// https://stackoverflow.com/a/39252048
public class SerializableBundle implements Serializable {
	private SimpleCallback listener;
	private Bitmap bmp;

	private int year, month, day;

	public SerializableBundle(SimpleCallback _listener) {
		listener = _listener;
	}

	public SimpleCallback getListener() {
		return listener;
	}

	public SerializableBundle(Bitmap _bmp) {
		bmp = _bmp;
	}

	public Bitmap getBmp() {
		return bmp;
	}

	public SerializableBundle(CalendarBean _cb) {
		year = _cb.year;
		month = _cb.moth;
		day = _cb.day;
	}

	public Date getDate() {
		return new Date(year, month, day);
	}
}
