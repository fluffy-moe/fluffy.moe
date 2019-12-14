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

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import moe.fluffy.app.activities.FoodHistoryActivity;
import moe.fluffy.app.activities.HomeActivity;
import moe.fluffy.app.R;
import moe.fluffy.app.assistant.SimpleCallback;
import moe.fluffy.app.assistant.Utils;
import moe.fluffy.app.types.FoodViewType;

public class FoodAdapter extends ArrayAdapter<FoodViewType> {
	public FoodAdapter(Context context, @NotNull ArrayList<FoodViewType> foodList) {
		super(context, android.R.layout.simple_list_item_1, foodList);
	}


	public static AlertDialog generateDialog(Context context, @NotNull FoodViewType it, @NotNull FoodAdapter adapter,
											 @Nullable SimpleCallback listener, @Nullable Bitmap bmp) {
		View viewEditFood = LayoutInflater.from(context).inflate(R.layout.edit_food_item, null);
		AlertDialog.Builder editFoodPopup = new AlertDialog.Builder(context);
		editFoodPopup.setView(viewEditFood);
		EditText etTitle, etNote, etTime;
		ImageButton imgbtnChangeImage, imgbtnConfirmEdit, imageButtonClose;
		etTitle = viewEditFood.findViewById(R.id.etIdentificationTitle);
		etTime = viewEditFood.findViewById(R.id.etIdentificationDate);
		etNote = viewEditFood.findViewById(R.id.etIdentificationNote);
		imgbtnChangeImage = viewEditFood.findViewById(R.id.imgbtnIdentificatinPic);
		imgbtnConfirmEdit = viewEditFood.findViewById(R.id.imgbtnIdentificationConfirm);
		imageButtonClose = viewEditFood.findViewById(R.id.imgbtnIdentificationOut);

		AlertDialog realEditPopup = editFoodPopup.create();

		etTitle.setText(it.getFoodName());
		etNote.setText(it.getFoodNote());
		etTime.setText(it.getDate());
		imgbtnChangeImage.setImageBitmap(bmp);

		// TODO: change photo

		imgbtnConfirmEdit.setOnClickListener(l -> {
			it.setFoodName(etTitle.getText().toString());
			it.setFoodNote(etNote.getText().toString());
			it.setDate(etTime.getText().toString().split("/"));

			if (it.getId() == null) {
				String str = saveBitmap(bmp, null);
				it.setImageSource(str);
			} else {
				saveBitmap(bmp, it.getImageSource());
			}
			HomeActivity.dbHelper.writeFoodHistory(it);
			if (listener != null) {
				listener.OnFinished(it);
			}
			adapter.notifyDataSetChanged();
			realEditPopup.dismiss();
		});

		etTime.setFocusable(false);
		etTime.setOnClickListener(v -> {

		});
		imageButtonClose.setOnClickListener(l -> realEditPopup.dismiss());
		realEditPopup.show();
		//realEditPopup.getWindow().setLayout(1200, 1224);
		return realEditPopup;
	}

	private static String saveBitmap(Bitmap bmp, @Nullable String filePath) {
		if (filePath == null)
			filePath = FoodHistoryActivity.getFileStorage() + Utils.generateRandomString();
		try (FileOutputStream out = new FileOutputStream(filePath)) {
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

		FoodViewType it = getItem(position);

		TextView txtTitle, txtNote, txtDate;
		ImageButton imgbtnLike, imgbtnEdit;
		ImageView imgItem;

		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.food_items, parent, false);
		}

		txtTitle = convertView.findViewById(R.id.txtFoodBrand);
		txtNote = convertView.findViewById(R.id.txtFoodName);
		txtDate = convertView.findViewById(R.id.txtFoodFlavor);
		imgbtnLike = convertView.findViewById(R.id.btnFoodLike);
		imgItem = convertView.findViewById(R.id.imgItem);
		imgbtnEdit = convertView.findViewById(R.id.btnFoodEdit);

		// TODO: show photo

		if (it != null) {
			txtTitle.setText(it.getFoodName());
			txtNote.setText(it.getFoodNote());
			txtDate.setText(it.getDate());

			try {
				imgItem.setImageBitmap(Utils.getBitmap(getContext(), Uri.fromFile(new File(it.getImageSource()))));
			} catch (IOException e) {
				e.printStackTrace();
			}
			imgbtnEdit.setOnClickListener(v ->
					generateDialog(getContext(), it, this, null, null));
		}
		return convertView;
	}
}
