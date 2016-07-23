package ph.codebuddy.weeha.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import ph.codebuddy.weeha.R;

/**
 * Created by rommeldavid on 23/07/16.
 */

public class SplashActivity extends AppCompatActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferences = getSharedPreferences("WEEHA_PREFS", Context.MODE_PRIVATE);
        requestFineLocation(this);

    }

    private static final int ACCESS_FINE_LOCATION = 121;
    private static final int ACCESS_COARSE_LOCATION = 122;

    private void requestFineLocation(Context context) {
        String readFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                readFineLocation);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{readFineLocation},
                    ACCESS_FINE_LOCATION);

        }else{
            requestCoarseLocation(context);
        }
    }

    private void requestCoarseLocation(Context context) {
        String readCoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
        int permissionCheck = ContextCompat.checkSelfPermission(context,
                readCoarseLocation);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{readCoarseLocation},
                    ACCESS_COARSE_LOCATION);

        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToLogin();
                }
            }, 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestCoarseLocation(SplashActivity.this);
                } else {
                    finish();
                }
                return;
            }case ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToLogin();
                }
                break;
            }
        }
    }

    public void goToLogin(){
        if(preferences.getString("USER_DETAILS", "").equals("")){
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(SplashActivity.this, MapsActivity.class);
            startActivity(intent);
            finish();
        }

    }
}