package ph.codebuddy.weeha.request;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import ph.codebuddy.weeha.https.HttpRequest;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class GetMyLocation {
    Context context;
    SharedPreferences spUtils;
    private OnTaskCompleted listener;
    public static String TAG = "[Request code..]";

    public GetMyLocation(Context context, SharedPreferences spUtils, OnTaskCompleted listener) {
        this.context = context;
        this.spUtils = spUtils;
        this.listener = listener;
    }

    public void executeGetLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new getLocation().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new getLocation().execute();
        }
    }

    public class getLocation extends AsyncTask<String, Integer, Boolean> {
        Boolean asyncResult;
        String stringResult;

        @Override
        protected Boolean doInBackground(String... params) {
            HttpRequest httpRequest = new HttpRequest();
            String url = "https://devapi.globelabs.com.ph/location/v1/queries/location?access_token=" +
                    spUtils.getString("access_token", "") + "&address=" + spUtils.getString("mobile_number", "") + "&requestedAccuracy=metres";

            stringResult = httpRequest.makeServiceCall(url, HttpRequest.GET, "");

            Log.v("YEAH", stringResult);

            return true;
        }

        protected void onPostExecute(Boolean result) {
            listener.onTaskCompleted(result, stringResult);
        }
    }

}