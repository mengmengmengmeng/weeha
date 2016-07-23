package ph.codebuddy.weeha.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import ph.codebuddy.weeha.R;
import ph.codebuddy.weeha.request.OnTaskCompleted;
import ph.codebuddy.weeha.request.RequestCode;
import ph.codebuddy.weeha.request.VerifyCode;

/**
 * Created by rommeldavid on 23/07/16.
 */
public class SignUpActivity extends BaseLoginActivity {

    AppCompatEditText etMobileNumber, etFirstName, etLastName, etUsername;
    AppCompatTextView tvRequestCode;
    String sMobileNumber, sFirstName, sLastname, sUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initDisplay();
        initEvents();
    }

    public void initDisplay() {
        etMobileNumber = (AppCompatEditText) findViewById(R.id.etMobileNumber);
        etFirstName = (AppCompatEditText) findViewById(R.id.etFirstName);
        etLastName = (AppCompatEditText) findViewById(R.id.etLastName);
        etUsername = (AppCompatEditText) findViewById(R.id.etUsername);
        tvRequestCode = (AppCompatTextView) findViewById(R.id.tvRequestCode);
    }

    public void initEvents() {
        tvRequestCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextStringsAndRequestCode();
            }
        });
    }

    public void getEditTextStringsAndRequestCode() {
        sMobileNumber = etMobileNumber.getText().toString();
        sFirstName = etFirstName.getText().toString();
        sLastname = etLastName.getText().toString();
        sUsername = etUsername.getText().toString();

        RequestCode requestCode = new RequestCode(SignUpActivity.this, sMobileNumber, sFirstName, sLastname, sUsername, preferences, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Boolean bool, String response) {
                if (bool) {
                    try {
                        Intent intent = new Intent(SignUpActivity.this, VerifyActivity.class);
                        JSONObject data = new JSONObject(response);
                        intent.putExtra("username", data.getString("username"));
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_LONG).show();
                }
            }
        });

        requestCode.executeRequestCode();
    }


}
