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
package moe.fluffy.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.assistant.PopupDialog;
import moe.fluffy.app.types.VaccinationItem;

public class VaccinationAdapter extends ArrayAdapter<VaccinationItem> {
	private static final String TAG = "VaccinationAdapter";

	public VaccinationAdapter(Context context, ArrayList<VaccinationItem> arrayList) {
		super(context, android.R.layout.simple_list_item_1, arrayList);
	}

	@ColorRes
	private int getColorRes(int status) {
		switch (status) {
			case VaccinationItem.RED:
				return R.color.colorVacRed;
			case VaccinationItem.GREEN:
				return R.color.colorVacGreen;
			default:
				return R.color.colorVacUndefined;
		}
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		VaccinationItem it = getItem(position);

		TextView txtDate, txtNobivac, txtInjectSite, txtDoctor;
		ImageView imgBackground;


		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_vaccination, parent, false);
		}

		txtDate = convertView.findViewById(R.id.txtVacTime);
		txtNobivac = convertView.findViewById(R.id.txtVacProductName);
		txtInjectSite = convertView.findViewById(R.id.txtVacInjectionName);
		txtDoctor = convertView.findViewById(R.id.txtVacDoctorName);
		imgBackground = convertView.findViewById(R.id.imgVacCheckBackground);

		if (it != null) {
			txtDate.setText(getContext().getString(R.string.fmt_date, it.getYear(), it.getMonth(), it.getDay()));
			txtNobivac.setText(it.getNobivac());
			txtInjectSite.setText(it.getInjectionSite());
			txtDoctor.setText(it.getDoctor());
			((GradientDrawable) imgBackground.getBackground()).setColor(
					getContext().getColor(getColorRes(it.getStatus())));

		} else {
			NullPointerException e = new NullPointerException("Vaccination type return null");
			PopupDialog.build(getContext(), e);
			throw e;
		}

		return convertView;

	}
}
