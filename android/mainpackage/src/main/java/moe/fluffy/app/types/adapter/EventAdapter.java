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
package moe.fluffy.app.types.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.types.EventsType;

@SuppressLint("DefaultLocale")
public class EventAdapter extends ArrayAdapter<EventsType> {

	public EventAdapter(Context context, ArrayList<EventsType> adapters) {
		super(context, android.R.layout.simple_list_item_1, adapters);
	}

	@NotNull
	@Override
	public View getView(int position, View covertView, @NotNull ViewGroup parent) {
		EventsType it = getItem(position);

		TextView txtDay, txtWeek, txtTime, txtEvent;
		View viewSideColor;

		if (covertView == null) {
			covertView = LayoutInflater.from(getContext()).inflate(R.layout.event_items, parent, false);
		}

		txtDay = covertView.findViewById(R.id.txtDateEventItems);
		txtWeek = covertView.findViewById(R.id.txtWeekEventItems);

		txtTime = covertView.findViewById(R.id.txtTimeEventItems);
		txtEvent = covertView.findViewById(R.id.txtEventItems);

		viewSideColor = covertView.findViewById(R.id.viewTodayLineColor2);

		viewSideColor.setBackgroundColor(getContext().getColor(
				getContext().getResources().getIdentifier(String.format("event_c%d",
						it.getColor()), "color", getContext().getPackageName())));

		txtDay.setText(String.valueOf(it.getDay()));
		txtWeek.setText(it.getDayOfWeek());
		txtEvent.setText(it.getBody());
		txtTime.setText(String.format("%02d:%02d", it.getHour(), it.getMinute()));

		return covertView;
	}
}
