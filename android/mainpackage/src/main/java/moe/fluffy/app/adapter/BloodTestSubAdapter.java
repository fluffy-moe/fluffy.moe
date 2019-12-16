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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.types.BloodTestSubType;

public class BloodTestSubAdapter extends RecyclerView.Adapter<BloodTestSubAdapter.ViewType> {
	private ArrayList<BloodTestSubType> bloodTestSubTypes;
	BloodTestSubAdapter(ArrayList<BloodTestSubType> l) {
		bloodTestSubTypes = l;
	}

	public static class ViewType extends RecyclerView.ViewHolder {

		View rootView;
		public ViewType(@NonNull View itemView) {
			super(itemView);
			rootView = itemView;
		}

		void setViewProp(BloodTestSubType it) {

			TextView txtItemName, txtItemValue;
			ImageView vReferenceView;

			txtItemName = rootView.findViewById(R.id.txtBloodItemName);
			txtItemValue = rootView.findViewById(R.id.txtBloodItemValue);
			vReferenceView = rootView.findViewById(R.id.viewReference);

			vReferenceView.setImageDrawable(it.getGraph(rootView.getContext()));
			txtItemName.setText(it.getExamineItem());
			txtItemValue.setText(String.valueOf(it.getResult()));
		}
	}

	@NonNull
	@Override
	public ViewType onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bloodtest_layout, parent, false);
		return new ViewType(convertView);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewType holder, int position) {
		holder.setViewProp(bloodTestSubTypes.get(position));
	}

	@Override
	public int getItemCount() {
		return bloodTestSubTypes.size();
	}


}
