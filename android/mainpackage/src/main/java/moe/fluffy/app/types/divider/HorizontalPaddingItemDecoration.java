package moe.fluffy.app.types.divider;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

// https://stackoverflow.com/a/34297104
public class HorizontalPaddingItemDecoration extends RecyclerView.ItemDecoration {
	private final int size;

	public HorizontalPaddingItemDecoration(int size) {
		this.size = size;
	}

	@Override
	public void getItemOffsets(@NotNull Rect outRect, @NotNull View view,
							   @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
		super.getItemOffsets(outRect, view, parent, state);

		// Apply offset only to first item
		if (parent.getChildAdapterPosition(view) == 0) {
			outRect.left += size;
		}
	}
}