package ph.codebuddy.weeha.activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import ph.codebuddy.weeha.R;

/**
 * Created by rommeldavid on 24/07/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(BaseActivity.this, "Please enable your GPS Service", Toast.LENGTH_LONG).show();
        }

        setUpToolbar();
    }

    protected abstract int getLayoutResource();

    public void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = (TextView) (toolbar != null ? toolbar.findViewById(R.id.toolbar_title) : null);
        setSupportActionBar(toolbar);

        final Drawable upArrow = ContextCompat.getDrawable(BaseActivity.this, R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(ContextCompat.getColor(BaseActivity.this, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);


        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
            toolbar.setLogo(R.mipmap.ic_launchers);
        }
    }

    public void setToolbarName(String name) {
        title.setText(name);
    }

}

