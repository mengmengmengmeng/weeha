package ph.codebuddy.weeha.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ph.codebuddy.weeha.R;
import ph.codebuddy.weeha.https.HttpRequest;
import ph.codebuddy.weeha.model.TrackRequest;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class GetTrackRequest {
    Context context;
    SharedPreferences spUtils;
    private OnTaskCompleted listener;
    String addUrl;
    public static String TAG = "[Request code..]";

    public GetTrackRequest(Context context, String url,SharedPreferences spUtils, OnTaskCompleted listener) {
        this.context = context;
        this.spUtils = spUtils;
        this.listener = listener;
        this.addUrl = url;
    }

    public void executeGetTrackRequest() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new getTrackRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new getTrackRequest().execute();
        }
    }

    public class getTrackRequest extends AsyncTask<String, Integer, Boolean> {
        Boolean asyncResult;
        String stringResult;

        @Override
        protected Boolean doInBackground(String... params) {
            HttpRequest httpRequest = new HttpRequest();
            String url = context.getString(R.string.weeha_url) + addUrl;

            stringResult = httpRequest.makeServiceCall(url, HttpRequest.GET, spUtils.getString("access_token", ""));

            Log.v("REGSSS", stringResult);

            return true;
        }

        protected void onPostExecute(Boolean result) {
            listener.onTaskCompleted(result, stringResult);
        }
    }

}
