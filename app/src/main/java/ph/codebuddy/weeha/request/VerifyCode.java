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

import java.io.IOException;

import ph.codebuddy.weeha.R;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class VerifyCode {
    Context context;
    String code, username;
    SharedPreferences spUtils;
    private OnTaskCompleted listener;
    public static String TAG = "[Verify code..]";

    public VerifyCode(Context context, String code, String username, SharedPreferences spUtils, OnTaskCompleted listener) {
        this.context = context;
        this.code = code;
        this.username = username;
        this.spUtils = spUtils;
        this.listener = listener;
    }

    public void executeVerifyCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new verifyCode().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new verifyCode().execute();
        }
    }

    public class verifyCode extends AsyncTask<String, Integer, Boolean> {
        Boolean asyncResult;
        String stringResult;
        ProgressDialog progDl;
        HttpResponse response;

        @Override
        protected void onPreExecute() {
            progDl = new ProgressDialog(context);
            progDl.setTitle("Please wait...");
            progDl.setCancelable(false);
            progDl.show();

        }

        @Override
        protected Boolean doInBackground(String... params) {
            String url = context.getString(R.string.weeha_url) + "users/" + username + "/confirm";
            Log.v(TAG, "URL:" + url);
            /*try {
                HttpsMultipart httpsMultipart = new HttpsMultipart();
                String url = context.getString(R.string.weeha_url) + "users/sign_up";

                //String header = "Tripkada "+ spUtils.getTripkadaUID() + ":" + hmac;

                //Log.d(TAG, "[Header] " + header);

                httpsMultipart.initialize(url, "", HttpsMultipart.POST);

                httpsMultipart.addFormField("users[mobile_number]", mobile_number);
                httpsMultipart.addFormField("users[first_name]", first_name);
                httpsMultipart.addFormField("users[last_name]", last_name);
                httpsMultipart.addFormField("users[username]", username);


                httpsMultipart.finish(new HttpsMultipartResponse() {
                    @Override
                    public void onHttpsMultipartResponse(Boolean result, String successResponse) {
                        Log.v(TAG, successResponse);
                        stringResult = successResponse;
                        asyncResult = result;
                    }
                });

            } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
                asyncResult = false;
            }*/

            try {
                String boundary = "---------------------------This is the boundary";
                HttpClient client = new DefaultHttpClient();

                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, boundary, null);

                HttpPost do_this;
                entity.addPart("users[confirmation_code]", new StringBody(code));
                //entity.addPart("birthday", new StringBody(birthday));
                //entity.addPart("avatar", new FileBody(file, "image/jpeg"));
                do_this = new HttpPost(url);

                /*do_this.addHeader("Content-Type", "multipart/form-data; boundary="
                        + boundary);*/
                //do_this.addHeader(new BasicHeader("Authorization", preferences.getString("token", "")));
                do_this.setEntity(entity);
                response = client.execute(do_this);

                stringResult = EntityUtils.toString(response.getEntity());

                if (response.getStatusLine().getStatusCode() == 201) {
                    asyncResult = true;
                    Log.v("Verification : ", stringResult);
                } else if (response.getStatusLine().getStatusCode() == 200) {
                    asyncResult = true;
                    Log.v("Verification : ", stringResult);
                } else {
                    stringResult = "Verification Failed : " + stringResult + "  " + response.getStatusLine().getStatusCode();
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
            listener.onTaskCompleted(result, stringResult);
            progDl.dismiss();
        }
    }
}
