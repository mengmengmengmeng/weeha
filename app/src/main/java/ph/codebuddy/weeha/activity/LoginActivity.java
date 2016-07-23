package ph.codebuddy.weeha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import ph.codebuddy.weeha.R;

public class LoginActivity extends AppCompatActivity {
    AppCompatTextView tvSignUp, tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvSignUp = (AppCompatTextView) findViewById(R.id.tvSignUp);
        tvLogin = (AppCompatTextView) findViewById(R.id.tvLogin);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
}
