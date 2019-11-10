package moe.fluffy.app;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.security.NoSuchAlgorithmException;

import moe.fluffy.app.assistant.Callback;
import moe.fluffy.app.assistant.Connect;
import moe.fluffy.app.types.HttpRawResponse;
import moe.fluffy.app.types.NetworkRequestType;


public class LoginActivity extends AppCompatActivity {

	EditText etUser, etPassword;
	ImageButton imgbtnLogin;

	private static String TAG = "log_Login";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		init();
	}

	void init() {
		etUser = findViewById(R.id.etLoginEmail);
		etPassword = findViewById(R.id.etLoginPassword);
		imgbtnLogin = findViewById(R.id.imgbtnLogin);

		etUser.setOnFocusChangeListener((v, hasFocus) -> {
			if (hasFocus) {
				if (etUser.getText().toString().equals(getString(R.string.users_email))) {
					etUser.setText("");
				}
			} else {
				if (etUser.getText().length() == 0) {
					etUser.setText(R.string.users_email);
				}
			}
		});

		etPassword.setOnFocusChangeListener((v, hasFocus) -> {
			Log.d(TAG, "init: " + etPassword.getInputType());
			if (hasFocus) {
				if (etPassword.getText().toString().equals(getString(R.string.users_password))) {
					// clear default text
					etPassword.setText("");
					// https://stackoverflow.com/a/9893496
					etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
			else {
				if (etPassword.getText().length() == 0) {
					etPassword.setText(R.string.users_password);
					etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
				}
			}
		});

		imgbtnLogin.setOnClickListener( v -> {
			try {
				new Connect(NetworkRequestType.generateLoginParams(etUser.getText(), etPassword.getText()), "login", new Callback() {
					@Override
					public void onSuccess(Object o)  {
						HttpRawResponse r = (HttpRawResponse) o;
						if (r.getStatus() == 200) {
							Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(Object o, Throwable e) {}

					@Override
					public void finish(Object o, Throwable e) {}
				}).execute();
			} catch (NoSuchAlgorithmException e){
				e.printStackTrace();
			}
		});
	}
}
