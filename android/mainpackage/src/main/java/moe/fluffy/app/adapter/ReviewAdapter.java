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
import moe.fluffy.app.types.PastTimeReview;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewType> {

	private ArrayList<PastTimeReview> pastTimeReviews;
	public static class ViewType extends RecyclerView.ViewHolder {
		View rootView;
		ViewType(View v) {
			super(v);
			rootView = v;
		}

		void setText(String s) {
			TextView t = rootView.findViewById(R.id.txtEventRecord);
			t.setText(s);
		}

		void setProp(PastTimeReview pt) {
			TextView title = rootView.findViewById(R.id.txtEventEvent);
			//		body = rootView.findViewById(R.id.txtEventRecord);
			ImageView imgHeader = rootView.findViewById(R.id.imgbtnEventEvent);
			title.setText(pt.getType());
			setText(pt.getText());
			imgHeader.setImageResource(pt.getDrawableId());
		}
	}

	public ReviewAdapter(ArrayList<PastTimeReview> _pastTimeReviews) {
		pastTimeReviews = _pastTimeReviews;
	}

	@NonNull
	@Override
	public ViewType onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_past_view, parent, false);
		return new ViewType(v);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewType holder, int position) {
		 PastTimeReview pt =  pastTimeReviews.get(position);
		 holder.setProp(pt);
	}

	@Override
	public int getItemCount() {
		return pastTimeReviews.size();
	}
}
