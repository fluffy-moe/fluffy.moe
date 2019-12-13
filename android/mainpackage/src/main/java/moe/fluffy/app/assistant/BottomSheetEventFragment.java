package moe.fluffy.app.assistant;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import moe.fluffy.app.activities.HomeActivity;
import moe.fluffy.app.R;
import moe.fluffy.app.types.Date;
import moe.fluffy.app.types.Datetime;
import moe.fluffy.app.types.EventsType;
import moe.fluffy.app.types.SerializableBundle;

public class BottomSheetEventFragment extends BottomSheetDialogFragment {
	private final String TAG = "log_BottomSheetEventFragment";

	private View viewAddEventPopup;
	private DateTimeWheelView dateTimeWheelView;
	private SimpleCallback listener;

	public final static String keyName = "0";

	private ImageButton btnColorSelected;
	private Button categorySelected;
	private String categorySelectedText;
	@ColorRes private int colorSelected;

	private Datetime preDaytime;

	public BottomSheetEventFragment() {
		super();
	}

	public BottomSheetEventFragment(Datetime d) {
		super();
		preDaytime = d;
	}


	@Nullable
	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView: test");
		viewAddEventPopup = inflater.inflate(R.layout.calendar_add_event_bottom, container, false);
		ImageButton btnConfirm = viewAddEventPopup.findViewById(R.id.imgbtnCalendarSave);
		EditText etBody = viewAddEventPopup.findViewById(R.id.etCalendarBody);
		Switch swAlarm = viewAddEventPopup.findViewById(R.id.swCalendarAlarm);
		dateTimeWheelView = new DateTimeWheelView(viewAddEventPopup, 54);
		/*Window w = getWindow();
		if (w != null) {
			w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				/*w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				w.requestFeature(Window.FEATURE_NO_TITLE);
		}

		dialog.setContentView(viewAddEventPopup);*/
		//timePicker.setIs24HourView(true);
		Bundle bundle = getArguments();
		if (bundle != null) {
			SerializableBundle serializableBundle = (SerializableBundle) bundle.getSerializable(keyName);
			if (serializableBundle != null){
				listener = serializableBundle.getListener();
			}
			bundle.clear();
		}
		if (preDaytime == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(calendar.getTime());
			dateTimeWheelView.setPicker(calendar);
		}
		else {
			dateTimeWheelView.setPicker(preDaytime);
		}
		dateTimeWheelView.setItemsVisible(3);
		/*FragmentManager childManager = getChildFragmentManager();
		FragmentTransaction childTransaction = childManager.beginTransaction();
		pvTime = new TimePickerFragment();*/
		/*bundle.putSerializable("1", new SerializableBundle(mFrameLayout));
		pvTime.setArguments(bundle);*/
		initPopupColorPick(viewAddEventPopup);
		initCategoryButton(viewAddEventPopup);
		etBody.setOnFocusChangeListener((view, hasFocus) ->
				Utils.onFocusChange(hasFocus, safeGetContext(), etBody, R.string.etCalendarAddEventHint, false));
		btnConfirm.setOnClickListener( l -> {
			EventsType et = new EventsType(dateTimeWheelView, colorSelected, categorySelectedText,
					etBody.getText().toString(), swAlarm.isChecked());
			HomeActivity.dbHelper.insertEvent(et);
			//planedEvents.add(et);
			if (et.getDayBody().moreThanOrEqual(Date.getToday())) {
				if (listener != null)
					listener.OnFinished(et);
			}
			//Log.d(TAG, "init: Insert successful");
			btnColorSelected = null;
			colorSelected = 0;
			this.dismiss();
		});

		/*childTransaction.replace(R.id.viewCalendarPick, pvTime);
		childTransaction.commitAllowingStateLoss();*/
		return viewAddEventPopup;
	}

	/**
	 * Specially thanks https://stackoverflow.com/a/40628141
	 * This function spend me entire night to solve it.
	 */
	@NotNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// https://stackoverflow.com/a/46469709
		setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
		Dialog d = super.onCreateDialog(savedInstanceState);
		Window w = d.getWindow();
		if (w != null) {
			w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			w.requestFeature(Window.FEATURE_NO_TITLE);
			//w.setBackgroundDrawableResource(R.drawable.round_background);
		}
		d.setOnShowListener(dialog -> {
			BottomSheetDialog d1 = (BottomSheetDialog) dialog;
			FrameLayout bottomSheet = d1.findViewById(com.google.android.material.R.id.design_bottom_sheet);
			if (bottomSheet == null)
				throw new RuntimeException("Bottom sheet dialog is null");
			BottomSheetBehavior behaviour = BottomSheetBehavior.from(bottomSheet);
			behaviour.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
				@Override
				public void onStateChanged(@NonNull View bottomSheet, int newState) {
					if (newState == BottomSheetBehavior.STATE_DRAGGING) {
						behaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
					}
				}

				@Override
				public void onSlide(@NonNull View bottomSheet, float slideOffset) {

				}
			});
		});
		return d;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/*@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mFrameLayout = mView.findViewById(R.id.fragment_date_picker);
		initTimePicker();
	}*/

	private void initCategoryButton(View viewAddEventPopup) {
		Button btnEvent, btnNote, btnSymptom, btnWater;
		btnEvent = viewAddEventPopup.findViewById(R.id.btnCalendarEvent);
		btnNote = viewAddEventPopup.findViewById(R.id.btnCalendarNote);
		btnSymptom = viewAddEventPopup.findViewById(R.id.btnCalendarSymptom);
		btnWater = viewAddEventPopup.findViewById(R.id.btnCalendarWater);
		categorySelectedText = getString(R.string.categoryEvent);
		categorySelected = btnEvent;
		btnEvent.setOnClickListener(v ->
				categoryOnClick(btnEvent, getString(R.string.categoryEvent), viewAddEventPopup));
		btnNote.setOnClickListener(v ->
				categoryOnClick(btnNote, getString(R.string.categoryNote), viewAddEventPopup));
		btnSymptom.setOnClickListener(v ->
				categoryOnClick(btnSymptom, getString(R.string.categorySymptom), viewAddEventPopup));
		btnWater.setOnClickListener(v ->
				categoryOnClick(btnWater, getString(R.string.categoryWater), viewAddEventPopup));
	}

	private void categoryOnClick(Button btn, String category, View viewAddEventPopup) {
		//Log.v(TAG, "categoryOnClick => " + category);
		EditText etBody = viewAddEventPopup.findViewById(R.id.etCalendarBody);
		TextView txtTitle = viewAddEventPopup.findViewById(R.id.txtCalendarTitle);
		if (categorySelected != null) {
			categorySelected.setBackgroundResource(R.drawable.ic_rectangle_225);
			categorySelected.setTextColor(getColor(R.color.colorNotSelect));
		}
		btn.setBackgroundResource(R.drawable.ic_rectangle_227);
		btn.setTextColor(getColor(R.color.colorSelect));
		categorySelectedText = category;
		categorySelected = btn;
		if (category.equals(getString(R.string.categoryWater))) {
			txtTitle.setText(R.string.title_for_water);
			if (!etBody.isFocused())
				etBody.setText(R.string.etCalendarAddWaterHint);
			etBody.setOnFocusChangeListener((view, hasFocus) ->
					Utils.onFocusChange(hasFocus, safeGetContext(), etBody, R.string.etCalendarAddWaterHint, false));

		} else {
			txtTitle.setText(R.string.text_title_for_calendar);
			if (!etBody.isFocused())
				etBody.setText(R.string.etCalendarAddEventHint);
			etBody.setOnFocusChangeListener((view, hasFocus) ->
					Utils.onFocusChange(hasFocus, safeGetContext(), etBody, R.string.etCalendarAddEventHint, false));
		}
	}

	private void initPopupColorPick(View viewAddEventPopup) {
		ImageButton imgbtnColor1, imgbtnColor2, imgbtnColor3, imgbtnColor4, imgbtnColor5;
		imgbtnColor1 = viewAddEventPopup.findViewById(R.id.rdbtnCalendarOrange);
		imgbtnColor2 = viewAddEventPopup.findViewById(R.id.rdbtnCalendarRed);
		imgbtnColor3 = viewAddEventPopup.findViewById(R.id.rdbtnCalendarBlue);
		imgbtnColor4 = viewAddEventPopup.findViewById(R.id.rdbtnCalendarGreen);
		imgbtnColor5 = viewAddEventPopup.findViewById(R.id.rdbtnCalendarPink);
		initColorPick(imgbtnColor1, 1);
		initColorPick(imgbtnColor2, 2);
		initColorPick(imgbtnColor3, 3);
		initColorPick(imgbtnColor4, 4);
		initColorPick(imgbtnColor5, 5);

		// let button 1 to color default
		changeClickColor(imgbtnColor1, R.color.event_c1);
		btnColorSelected = imgbtnColor1;
		colorSelected = 1;
	}

	/**
	 * Reference: https://stackoverflow.com/a/29204632
	 *
	 * @param v image button
	 * @param color color from color resource file id
	 */
	private void changeStrokeColor(View v, @ColorRes int color) {
		((GradientDrawable) v.getBackground()).setStroke(10, getColor(color));
	}

	/**
	 * Reference https://stackoverflow.com/a/17825210
	 *
	 * @see #changeStrokeColor(View, int)
	 */
	private void changeClickColor(View v, @ColorRes int color) {
		((GradientDrawable)v.getBackground()).setColor(getColor(color));
	}

	private void colorOnClickListener(ImageButton imgbtn, int color) {
		if (btnColorSelected != null && btnColorSelected != imgbtn) {
			changeClickColor(btnColorSelected, android.R.color.transparent);
		}
		changeClickColor(imgbtn, getColorRes(color));
		btnColorSelected = imgbtn;
		colorSelected = color;
	}

	@ColorInt
	private int getColor(int color){
		return safeGetContext().getColor(color);
	}

	@SuppressLint("DefaultLocale")
	@ColorRes
	private int getColorRes(int id) {
		return getResources().getIdentifier(getString(R.string.fmt_event_color, id), "color", safeGetContext().getPackageName());
	}

	private void initColorPick(ImageButton imgbtn, int color) {
		changeStrokeColor(imgbtn, getColorRes(color));
		// Make sure circle is hollow
		changeClickColor(imgbtn, android.R.color.transparent);
		imgbtn.setOnClickListener(v ->
				colorOnClickListener(imgbtn, color));
	}


	private Context safeGetContext() {
		Context context = getContext();
		if (context == null)
			throw new RuntimeException("null context");
		return context;
	}
}