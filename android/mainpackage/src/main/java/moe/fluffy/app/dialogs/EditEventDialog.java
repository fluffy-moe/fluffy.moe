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
package moe.fluffy.app.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import moe.fluffy.app.R;

public class EditEventDialog extends Dialog {
	public EditEventDialog(@NonNull Context context) {
		super(context, R.style.round_dialog);
	}

	public EditEventDialog initDialog(@NonNull LayoutInflater inflater) {
		View convertView = inflater.inflate(R.layout.dialog_new_event, null);
		EditText etNewEvent, etNewSymptom, etNewNote, etNewWater;
		etNewEvent = convertView.findViewById(R.id.etNewEvent);
		etNewSymptom = convertView.findViewById(R.id.etNewSymptom);
		etNewNote = convertView.findViewById(R.id.etNewNote);
		etNewWater = convertView.findViewById(R.id.etNewWater);
		return this;
	}
}
