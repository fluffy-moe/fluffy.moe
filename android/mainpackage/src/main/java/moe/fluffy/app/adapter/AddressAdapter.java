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


import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.types.AddressInfoItem;

public class AddressAdapter extends ArrayAdapter<AddressInfoItem> {
	public AddressAdapter(Context context, ArrayList<AddressInfoItem> adapters) {
		super(context, android.R.layout.simple_list_item_1, adapters);
	}

	@Override
	public View getView(int position, View covertView, ViewGroup parent) {
		AddressInfoItem it = getItem(position);

		if (covertView == null) {
			covertView = LayoutInflater.from(getContext()).inflate(R.layout.search_result, parent, false);
		}

		TextView txtTitle = covertView.findViewById(R.id.txtSearchResultTitle),
				txtAddress = covertView.findViewById(R.id.txtClinicName),
				txtPhone = covertView.findViewById(R.id.txtClinicPhone);

		txtTitle.setText(it.getName());
		txtPhone.setText(it.getPhone());
		txtAddress.setText(it.getAddress());

		return covertView;
	}
}
