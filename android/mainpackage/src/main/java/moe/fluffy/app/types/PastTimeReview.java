/*
 ** Copyright (C) 2019-2020 KunoiSayami
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

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import moe.fluffy.app.R;

public class PastTimeReview {
	private final String type;
	private final String text;


	public PastTimeReview(String type, String text) {
		this.type = type;
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public String getText() {
		return text;
	}

	@DrawableRes
	public int  getDrawableId() {
		switch (type) {
			case "Event":
				return R.drawable.event;
			case "Symptom":
				return R.drawable.symptom;
			case "Note":
				return R.drawable.note;
			case "Water":
				return R.drawable.water;
			default:
				throw new IllegalStateException("Unexpected value");
		}
	}
}


class PastTimeReviewEx {
	private final int type;
	private final String text;

	public final static int EVENT = 1;
	public final static int SYMPTOM = 2;
	public final static int NOTE = 3;
	public final static int WATER = 4;

	public PastTimeReviewEx(int _type, String _text) {
		type = _type;
		text = _text;
	}

	@DrawableRes
	public int getDrawableId() {
		switch (type) {
			case EVENT:
				return R.drawable.event;
			case SYMPTOM:
				return R.drawable.symptom;
			case NOTE:
				return R.drawable.note;
			case WATER:
				return R.drawable.water;
			default:
				throw new IllegalStateException("Unexpected value");
		}
	}

	@StringRes
	public int getTitleId() {
		switch (type) {
			case EVENT:
				return R.string.categoryEvent;
			case SYMPTOM:
				return R.string.categorySymptom;
			case NOTE:
				return R.string.categoryNote;
			case WATER:
				return R.string.categoryWater;
			default:
				throw new IllegalStateException("Unexpected value");
		}
	}

	public int getType() {
		return type;
	}

	public String getText() {
		return text;
	}
}
