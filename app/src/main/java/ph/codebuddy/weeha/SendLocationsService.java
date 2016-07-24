package ph.codebuddy.weeha;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import ph.codebuddy.weeha.request.GetMyLocation;
import ph.codebuddy.weeha.request.OnTaskCompleted;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class SendLocationsService extends Service{

    private Timer timer = new Timer();

    private static final String TAG = "SendService";
    SharedPreferences spUtils;
    String lat, lng;


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        spUtils = getSharedPreferences("WEEHA_PREFS", Context.MODE_PRIVATE);

        final GetMyLocation getMyLocation = new GetMyLocation(this, spUtils, new OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Boolean bool, String response) {
                try {
                    JSONObject data = new JSONObject(response);
                    JSONObject terminalLocationList = data.getJSONObject("terminalLocationList");
                    JSONObject terminalLocation = terminalLocationList.getJSONObject("terminalLocation");
                    JSONObject currentLocation = terminalLocation.getJSONObject("currentLocation");
                    lat = currentLocation.getString("latitude");
                    lng = currentLocation.getString("longitude");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new postRequestTrack().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        new postRequestTrack().execute();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.v("YEYEYEYE", "YEYEYE");
                getMyLocation.executeGetLocation();  //Your code here
            }
        }, 0, 5*60*1000);//5 Minutes
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public class postRequestTrack extends AsyncTask<String, Integer, Boolean> {
        Boolean asyncResult;
        String stringResult;
        ProgressDialog progDl;
        HttpResponse response;

        @Override
        protected Boolean doInBackground(String... params) {
            String url = getString(R.string.weeha_url) + "locations";
            Log.v(TAG, "URL:" + url);

            try {
                String boundary = "---------------------------This is the boundary";
                HttpClient client = new DefaultHttpClient();

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, boundary, null);

                HttpPost do_this;
                entity.addPart("locations[lat]", new StringBody(lat));
                entity.addPart("locations[lng]", new StringBody(lng));

                do_this = new HttpPost(url);

                do_this.addHeader("Content-Type", "multipart/form-data; boundary="
                        + boundary);
                do_this.addHeader(new BasicHeader("Authorization", spUtils.getString("access_token", "")));
                do_this.setEntity(entity);
                response = client.execute(do_this);

                stringResult = EntityUtils.toString(response.getEntity());

                if (response.getStatusLine().getStatusCode() == 201) {
                    asyncResult = true;
                    Log.v("Sending locations: ", stringResult);
                } else if (response.getStatusLine().getStatusCode() == 200) {
                    asyncResult = true;
                    Log.v("Sending locations : ", stringResult);
                } else {
                    stringResult = "Sending locations Failed : " + stringResult + "  " + response.getStatusLine().getStatusCode();
                    Log.v(" ", stringResult);
                    asyncResult = false;
                }

            } catch (IOException e) {
                e.printStackTrace();
                asyncResult = false;
            }

            return asyncResult;
        }

        protected void onPostExecute(Boolean result) {
            Log.v(String.valueOf(result), ": Result");
        }
    }

}

