package ph.codebuddy.weeha.https;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by rommeldavid on 24/07/16.
 */
public class HttpRequest {

    static String response = null;
    public final static int GET = 1;

    public HttpRequest() {

    }

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public String makeServiceCall(String url, int method, String header) {
        return this.makeServiceCall(url, method, null, header);
    }

    /**z
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String makeServiceCall(String url, int method,
                                  List<NameValuePair> params, String header) {
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);

                httpGet.setHeader("Authorization", header);

                httpResponse = httpClient.execute(httpGet);

            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity, HTTP.UTF_8);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.v("WAT", e.toString());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            Log.v("WATs", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("WATss", e.toString());
        }

        return response;

    }
}