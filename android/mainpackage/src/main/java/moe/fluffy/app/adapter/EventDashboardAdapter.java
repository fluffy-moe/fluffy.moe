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
import moe.fluffy.app.types.EventDashboardItem;

public class EventDashboardAdapter extends ArrayAdapter<EventDashboardItem> {
	EventAdapter ea;
	public EventDashboardAdapter(Context context, ArrayList<EventDashboardItem> adapters) {
		super(context, android.R.layout.simple_list_item_1, adapters);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		EventDashboardItem it = getItem(position);

		TextView txtView;
		WrapContentListView lvTasks;

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_today, parent, false);
		}

		txtView = convertView.findViewById(R.id.txtCalendarViewTitle);
		lvTasks = convertView.findViewById(R.id.lvEventList);

		txtView.setText(it.getTitle());
		ea = new EventAdapter(getContext(), it.getEvents());
		lvTasks.setDivider(getContext().getDrawable(R.drawable.divider_61px));
		lvTasks.setDividerHeight(61);
		lvTasks.setAdapter(ea);

		return convertView;
	}
}
