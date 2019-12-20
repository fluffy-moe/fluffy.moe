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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.types.BloodTestDashboard;
import moe.fluffy.app.types.divider.VerticalSpaceItemDecoration;

public class BloodTestDashboardAdapter extends RecyclerView.Adapter<BloodTestDashboardAdapter.ViewType> {
	private static final String TAG = "log_BloodTestDashboardAdapt";

	private ArrayList<BloodTestDashboard> bloodTestDashboards;


	public BloodTestDashboardAdapter(ArrayList<BloodTestDashboard> items) {
		bloodTestDashboards = items;
	}

	public static class ViewType extends RecyclerView.ViewHolder {

		View rootView;
		public ViewType(@NonNull View itemView) {
			super(itemView);
			rootView = itemView;
		}

		void setViewProp(BloodTestDashboard it) {

			TextView txtDate, txtBloodTestName;
			//WrapContentListView lvTestItems;
			RecyclerView lvTestItems;

			RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(), RecyclerView.VERTICAL, false);

			txtDate = rootView.findViewById(R.id.txtBloodTestDate);
			txtBloodTestName = rootView.findViewById(R.id.txtBloodTestName);
			lvTestItems = rootView.findViewById(R.id.lvTestItems);
			lvTestItems.setHasFixedSize(true);

			lvTestItems.setLayoutManager(layoutManager);

			if (lvTestItems.getItemDecorationCount() == 0) // Already add decoration item, no need to add more
				lvTestItems.addItemDecoration(new VerticalSpaceItemDecoration(15));

			txtDate.setText(rootView.getContext().getString(R.string.fmt_date, it.getYear(), it.getMonth(), it.getDay()));
			txtBloodTestName.setText(it.getBloodTestName());
			lvTestItems.setAdapter(new BloodTestItemAdapter(it.getTestItems()));
		}
	}

	@NonNull
	@Override
	public ViewType onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloodtest_layout, parent, false);
		return new ViewType(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewType holder, int position) {
		holder.setViewProp(bloodTestDashboards.get(position));
	}

	@Override
	public int getItemCount() {
		return bloodTestDashboards.size();
	}
}
