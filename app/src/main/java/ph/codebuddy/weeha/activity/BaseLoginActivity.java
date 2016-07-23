package ph.codebuddy.weeha.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ph.codebuddy.weeha.R;

/**
 * Created by rommeldavid on 23/07/16.
 */
public class BaseLoginActivity extends AppCompatActivity {
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("WEEHA_PREFS", Context.MODE_PRIVATE);



    }
}
