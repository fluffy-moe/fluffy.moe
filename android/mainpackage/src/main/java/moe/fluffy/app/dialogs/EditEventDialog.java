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
package moe.fluffy.app.dialogs;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import moe.fluffy.app.R;
import moe.fluffy.app.assistant.SimpleCallback;
import moe.fluffy.app.types.EventsItem;

public class EditEventDialog extends Dialog {
	private static final String TAG = "log_EditEventDialog";
	private ArrayList<EventsItem> eventsItems;

	private ImageButton btnDone;
	private EditText etNewEvent, etNewSymptom, etNewNote, etNewWater;

	private SimpleCallback callback;

	public EditEventDialog(@NonNull Context context, @NonNull ArrayList<EventsItem> eventsItems,
						   @NonNull SimpleCallback callback) {
		super(context, R.style.round_dialog);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_new_event, null);
		this.eventsItems = eventsItems;
		this.callback = callback;
		this.setContentView(view);
		initDialog(view);
	}


	private EditText getEditTextByEvent(EventsItem event) {
		switch (event.getCategory()) {
			case "Event":
				return etNewEvent;
			case "Symptom":
				return etNewSymptom;
			case "Note":
				return etNewNote;
			case "Water":
				return etNewWater;
		}
		throw new IllegalStateException("exception");
	}

	public EditEventDialog initDialog(View view) {
		//View convertView = view.inflate(R.layout.dialog_new_event, null);
		etNewEvent = view.findViewById(R.id.etNewEvent);
		etNewSymptom = view.findViewById(R.id.etNewSymptom);
		etNewNote = view.findViewById(R.id.etNewNote);
		etNewWater = view.findViewById(R.id.etNewWater);
		btnDone = view.findViewById(R.id.imgbtnNewUpdate);

		eventsItems.forEach((eventsItem -> {
			getEditTextByEvent(eventsItem).setText(eventsItem.getBody());
			Log.v(TAG, "initDialog: body => " + eventsItem.getBody());
		}));

		btnDone.setOnClickListener(v -> {
			//if (eventsItems != null)
			eventsItems.forEach(eventsItem -> {
				if (!eventsItem.getBody().equals(getEditTextByEvent(eventsItem).getText().toString()))
					eventsItem.edit(getEditTextByEvent(eventsItem).getText().toString());
			});
			Log.v(TAG, "initDialog: Done!");
			dismiss();
			this.callback.OnFinished(null);
		});
		return this;
	}

	// FIXME: set width dynamic
	@Override
	public void show() {
		super.show();
		Window window = this.getWindow();
		if (window != null)
			window.setLayout(292*3, 353*3);
	}
}
