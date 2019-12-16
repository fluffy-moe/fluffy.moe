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

import android.content.Context;

import moe.fluffy.app.assistant.BloodTestDrawable;

public class BloodTestSubType {
	private String examineItem, itemUnit;
	private double result;
	private BloodTestDrawable.ScaleRect bloodTestDrawable;
	BloodTestSubType(String a, double b, double referenceDown, double referenceUp, String _itemUnit) {
		examineItem = a;
		result = b;
		bloodTestDrawable = BloodTestDrawable.ScaleRect.build(b, referenceDown, referenceUp);
		itemUnit = _itemUnit;
	}

	public String getExamineItem() {
		return examineItem;
	}

	public double getResult() {
		return result;
	}

	public BloodTestDrawable getGraph(Context context) {
		return new BloodTestDrawable(context, bloodTestDrawable);
	}

	public String getUnit() {
		return itemUnit;
	}
}
