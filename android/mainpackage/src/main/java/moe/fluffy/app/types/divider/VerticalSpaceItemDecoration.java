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
package moe.fluffy.app.types.divider;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

// https://stackoverflow.com/a/27037230
public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {
	private final int verticalSpaceHeight;
	private static final String TAG = "log_VerticalSpaceItem";

	public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
		this.verticalSpaceHeight = verticalSpaceHeight;
	}

	@Override
	public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent,
							   @NotNull RecyclerView.State state) {
		if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
			outRect.bottom = verticalSpaceHeight;
		}
	}
}