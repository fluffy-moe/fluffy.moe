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
package moe.fluffy.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import moe.fluffy.app.BuildConfig;
import moe.fluffy.app.R;
import moe.fluffy.app.assistant.JSONParser;
import moe.fluffy.app.types.AddressInfoItem;
import moe.fluffy.app.adapter.AddressAdapter;

public class SearchActivity extends AppCompatActivity {

	Spinner spinnerCity, spinnerDistrict;
	ImageButton btnSearch;
	ListView lvSearchResult;

	private static boolean is_initialized_cities = false;
	private static ArrayList<String> cities;
	private static HashMap<String, ArrayList<String>> districts_data;
	private static JSONObject map_detail;
	private static String TAG = "log_search";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initData();
		setContentView(R.layout.activity_search);
		init();
	}

	void initData() {
		if (!is_initialized_cities) {
			boolean has_error = false;
			JSONObject o = JSONParser.loadJSONFromAsset(getResources().openRawResource(R.raw.cities_data));
			cities = new ArrayList<>();
			districts_data = new HashMap<>();
			if (o != null) {
				try {
					JSONArray j = o.getJSONArray("cities"), m;
					for (int i = 0; i < j.length(); i++) {
						ArrayList<String> districts = new ArrayList<>();
						// Get city name
						cities.add(j.getString(i));
						// Get district list
						try {
							m = o.getJSONObject("districts").getJSONArray(j.getString(i));
						} catch (JSONException e) {
							// pervert read fail
							has_error = true;
							e.printStackTrace();
							continue;
						}
						// Append district to array
						for (int x = 0; x < m.length(); x++)
							districts.add(m.getString(x));
						Log.v(TAG, "districts => " + districts);
						// Put "city" : [districts] to data;
						districts_data.put(j.getString(i), districts);
						// Finally clear temp stories
					}
					map_detail = JSONParser.loadJSONFromAsset(getResources().openRawResource(R.raw.location));
					Log.v(TAG, "init successful => " + cities.toString() + "\n" + districts_data.toString());
					is_initialized_cities = true;
					if (BuildConfig.DEBUG && has_error) {
						Toast.makeText(this, "Error while processing json.", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	void init_spinner_district(int pos) {
		Log.v(TAG, "init_spinner_district: called => " + cities.get(pos));

		ArrayList<String> s = districts_data.get(cities.get(pos));
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
				this, android.R.layout.simple_spinner_dropdown_item, s
		);
		spinnerDistrict.setAdapter(spinnerArrayAdapter);
	}

	void init() {
		spinnerCity = findViewById(R.id.spinnerCity);
		spinnerDistrict = findViewById(R.id.spinnerDistrict);
		btnSearch = findViewById(R.id.btnSearch);
		lvSearchResult = findViewById(R.id.lvSearchResult);

		// https://stackoverflow.com/a/41810033
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
				this, android.R.layout.simple_list_item_1, cities);
		spinnerCity.setAdapter(spinnerArrayAdapter);

		spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				init_spinner_district(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) { }
		});

		init_spinner_district(0);

		btnSearch.setOnClickListener(v -> {
			String select_city = spinnerCity.getSelectedItem().toString();
			String select_district = spinnerDistrict.getSelectedItem().toString();
			try {
				JSONArray a = map_detail.getJSONObject("data")
						.getJSONObject(select_city)
						.getJSONArray(select_district);
				ArrayList<AddressInfoItem> addressInfoList = new ArrayList<>();
				for (int i=0; i< a.length();i++) {
					addressInfoList.add(new AddressInfoItem(a.getJSONObject(i)));
				}
				init_listView(addressInfoList);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		});
		lvSearchResult.setOnItemClickListener((parent, view, position, id) ->{
			AddressInfoItem adp = (AddressInfoItem) parent.getItemAtPosition(position);
			Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + adp.getAddress());
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
			mapIntent.setPackage("com.google.android.apps.maps");
			startActivity(mapIntent);
		});
	}

	void init_listView(ArrayList<AddressInfoItem> addressInfoArray) {
		final AddressAdapter addressAdapter = new AddressAdapter(this, new ArrayList<>());
		this.lvSearchResult.setAdapter(addressAdapter);
		for (AddressInfoItem a: addressInfoArray) {
			addressAdapter.add(a);
		}
	}
}
