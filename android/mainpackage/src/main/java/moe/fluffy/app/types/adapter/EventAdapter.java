package moe.fluffy.app.types.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.types.EventsType;

public class EventAdapter extends ArrayAdapter<EventsType> {

	public EventAdapter(Context context, ArrayList<EventsType> adapters) {
		super(context, android.R.layout.simple_list_item_1, adapters);
	}

	@Override
	public View getView(int position, View covertView, ViewGroup parent) {
		EventsType it = getItem(position);

		if (covertView == null) {
			covertView = LayoutInflater.from(getContext()).inflate(R.layout., parent, false);
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
