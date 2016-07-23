package ph.codebuddy.weeha.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import ph.codebuddy.weeha.request.VerifyCode;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class VerifyActivity extends BaseLoginActivity {

    AppCompatEditText etConfirmationCode;
    AppCompatTextView tvConfirm, tvResendCode;
    String sConfirmationCode, sUsername;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        initDisplay();
        initEvents();
    }

    public void initDisplay(){
        etConfirmationCode = (AppCompatEditText) findViewById(R.id.etConfirmationCode);
        tvConfirm = (AppCompatTextView) findViewById(R.id.tvConfirm);
        tvResendCode = (AppCompatTextView) findViewById(R.id.tvResendCode);
    }

    public void initEvents(){
        extras = getIntent().getExtras();
        sUsername = extras.getString("username");
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextStringsAndConfirm();
            }
        });

        tvResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void getEditTextStringsAndConfirm(){
        sConfirmationCode = etConfirmationCode.getText().toString();

        VerifyCode verifyCode = new VerifyCode(VerifyActivity.this, sConfirmationCode, sUsername, preferences, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Boolean bool, String response) {
                if (bool) {
                    try {
                        Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
                        JSONObject data = new JSONObject(response);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("current_id", data.getString("id"));
                        editor.putString("mobile_number", data.getString("mobile_number"));
                        editor.putString("access_token", data.getString("access_token"));
                        editor.putBoolean("verified", data.getBoolean("verified"));
                        editor.putString("first_name", data.getString("first_name"));
                        editor.putString("last_name", data.getString("last_name"));
                        editor.putString("username", data.getString("username"));
                        editor.putString("address", data.getString("address"));
                        editor.putString("gender", data.getString("gender"));
                        editor.putString("birth_date", data.getString("birth_date"));
                        editor.apply();
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(VerifyActivity.this, "Verify failed", Toast.LENGTH_LONG).show();
                }

            }
        });

        verifyCode.executeVerifyCode();
    }
}
