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

import java.util.ArrayList;

public class EventDashboardItem {

	private static String TAG = "log_EventDashboard";

	String title;
	ArrayList<EventsItem> events;

	public EventDashboardItem(boolean isToday, ArrayList<EventsItem> _events) {
		title = isToday? "Today": "Coming soon";
		events = _events;
	}

	public String getTitle() {
		return title;
	}

	public ArrayList<EventsItem> getEvents() {
		//Log.d(TAG, "getEvents: size => " + events.size());
		return events;
	}
}
