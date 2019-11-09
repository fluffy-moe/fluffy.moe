package moe.fluffy.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.ArrayList;
import java.util.List;

import moe.fluffy.app.types.adapter.ImageAdapter;

public class CarouseActivity extends AppCompatActivity {

	List<Integer> lst = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carouse);
		lst.add(R.drawable.c1);
		lst.add(R.drawable.c2);
		lst.add(R.drawable.b1);

		HorizontalInfiniteCycleViewPager h = findViewById(R.id.hz);

		ImageAdapter i = new ImageAdapter(lst, getBaseContext());
		h.setAdapter(i);
	}
}
