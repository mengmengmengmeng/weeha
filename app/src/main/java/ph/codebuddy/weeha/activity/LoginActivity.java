package ph.codebuddy.weeha.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import ph.codebuddy.weeha.R;

public class LoginActivity extends FragmentActivity {

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = getSharedPreferences("WEEHA_PREFS", Context.MODE_PRIVATE);



    }
}
