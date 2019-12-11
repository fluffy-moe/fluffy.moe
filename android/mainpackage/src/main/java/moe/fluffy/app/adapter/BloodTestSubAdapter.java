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
package moe.fluffy.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.types.BloodTestSubType;

public class BloodTestSubAdapter extends ArrayAdapter<BloodTestSubType> {
	BloodTestSubAdapter(Context context, ArrayList<BloodTestSubType> l) {
		super(context, android.R.layout.simple_list_item_1, l);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		BloodTestSubType it = getItem(position);

		TextView txtItemName, txtItemValue;
		View vReferenceView;


		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_today, parent, false);
		}

		txtItemName = convertView.findViewById(R.id.txtBloodItemName);
		txtItemValue = convertView.findViewById(R.id.txtBloodItemValue);
		vReferenceView = convertView.findViewById(R.id.viewReference);

		if (it != null) {
			txtItemName.setText(it.getExamineItem());
			txtItemValue.setText(String.valueOf(it.getResult()));
		} else {
			NullPointerException e = new NullPointerException("Vaccination type return null");
			PopupDialog.build(getContext(), e);
			throw e;
		}

		return convertView;
	}
}
