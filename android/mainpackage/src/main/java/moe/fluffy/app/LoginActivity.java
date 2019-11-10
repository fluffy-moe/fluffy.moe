package moe.fluffy.app;

import android.os.Bundle;
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


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_login);
		init();
		Connect.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36");
	}

	void init() {
		etUser = findViewById(R.id.etLoginEmail);
		etPassword = findViewById(R.id.etLoginPassword);
		imgbtnLogin = findViewById(R.id.imgbtnLogin);

		imgbtnLogin.setOnClickListener( v -> {
			try {
				new Connect(NetworkRequestType.generateLoginParams(etUser.getText().toString(), etPassword.getText().toString()), "login", new Callback() {
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
