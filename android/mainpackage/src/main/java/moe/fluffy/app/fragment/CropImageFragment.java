package moe.fluffy.app.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.theartofdev.edmodo.cropper.CropImageView;

public class CropImageFragment extends Fragment
	implements CropImageView.OnSetImageUriCompleteListener,
		CropImageView.OnCropImageCompleteListener {
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//View rootView = LayoutInflater.
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {

	}

	@Override
	public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {

	}
}
