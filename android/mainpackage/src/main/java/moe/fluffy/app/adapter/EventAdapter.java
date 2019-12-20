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

import android.annotation.SuppressLint;
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
import moe.fluffy.app.types.EventsItem;

@SuppressLint("DefaultLocale")
public class EventAdapter extends ArrayAdapter<EventsItem> {

	public EventAdapter(Context context, ArrayList<EventsItem> items) {
		super(context, android.R.layout.simple_list_item_1, items);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		EventsItem it = getItem(position);

		TextView txtDay, txtWeek, txtTime, txtEvent;
		View viewSideColor;

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_items, parent, false);
		}

		txtDay = convertView.findViewById(R.id.txtDateEventItems);
		txtWeek = convertView.findViewById(R.id.txtWeekEventItems);

		txtTime = convertView.findViewById(R.id.txtTimeEventItems);
		txtEvent = convertView.findViewById(R.id.txtEventItems);

		viewSideColor = convertView.findViewById(R.id.viewTodayLineColor2);

		if (it != null) {
			viewSideColor.setBackgroundColor(getContext().getColor(
					getContext().getResources().getIdentifier(getContext().getString(R.string.fmt_event_color,
							it.getColor()), "color", getContext().getPackageName())));

			txtDay.setText(String.valueOf(it.getDay()));
			txtWeek.setText(it.getDayOfWeek());
			txtEvent.setText(it.getBody());
			txtTime.setText(String.format("%02d:%02d", it.getHour(), it.getMinute()));
		}

		return convertView;
	}
}
