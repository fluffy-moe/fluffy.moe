package moe.fluffy.app.types.divider;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

// https://stackoverflow.com/a/27037230
public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

	private final int verticalSpaceHeight;

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