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

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

public class DateWithMark extends Datetime {
	@ColorRes protected int color;
	public DateWithMark(int _year, int _month, int _day, int _hour, int _minute, int _second, int _millionSecond, int _color) {
		super(_year, _month, _day, _hour, _minute, _second, _millionSecond);
		color = _color;
	}
	public DateWithMark(String strDate, String strTime, int _color) {
		super(strDate, strTime);
		color = _color;
	}

	@ColorRes
	public int getColor() {
		return color;
	}

	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj instanceof DateWithMark) {
			DateWithMark o = (DateWithMark) obj;
			return this.year == o.year && this.month == o.month && this.day == o.day &&
					this.hour == o.hour && this.minute == o.minute && this.second == o.second;
		}
		return super.equals(obj);
	}
}
