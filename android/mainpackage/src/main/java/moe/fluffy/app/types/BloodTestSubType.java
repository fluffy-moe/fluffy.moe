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

public class BloodTestSubType {
	private String examineItem;
	private double result, reference;
	private Object objReversed;
	BloodTestSubType(String a, double b, double _reference, Object _objReversed) {
		examineItem = a;
		result = b;
		reference = _reference;
		objReversed = _objReversed;
	}

	public String getExamineItem() {
		return examineItem;
	}

	public double getResult() {
		return result;
	}

	public Object getGraph() {
		return objReversed;
	}

	public double getReference() {
		return reference;
	}
}
