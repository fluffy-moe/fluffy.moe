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

import org.mazhuang.wrapcontentlistview.WrapContentListView;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.types.BloodTestDashboardType;

public class BloodTestDashboardAdapter extends ArrayAdapter<BloodTestDashboardType> {
	BloodTestDashboardAdapter(Context context, ArrayList<BloodTestDashboardType> items) {
		super(context, android.R.layout.simple_list_item_1, items);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		BloodTestDashboardType it = getItem(position);

		TextView txtDate, txtBloodTestName;
		WrapContentListView lvTestItems;

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.bloodtest_layout, parent, false);
		}

		txtDate = convertView.findViewById(R.id.txtBloodTestDate);
		txtBloodTestName = convertView.findViewById(R.id.txtBloodTestName);
		lvTestItems = convertView.findViewById(R.id.lvTestItems);

		if (it != null) {
			txtDate.setText(getContext().getString(R.string.fmt_date, it.getYear(), it.getMonth(), it.getDay()));
			txtBloodTestName.setText(it.getBloodTestName());
			lvTestItems.setAdapter(new BloodTestSubAdapter(getContext(), it.getTestItems()));
		} else {
			NullPointerException e = new NullPointerException("BloodTestDashboard getItem() return null");
			PopupDialog.build(getContext(), e);
			throw e;
		}

		return convertView;
	}
}
