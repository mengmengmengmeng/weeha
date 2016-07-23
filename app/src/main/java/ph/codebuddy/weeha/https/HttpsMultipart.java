package ph.codebuddy.weeha.https;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * Created by rommeldavid on 23/07/16.
 */
public class HttpsMultipart {
    private String boundary;

    private static final String LINE_FEED = "\r\n";

    private HttpsURLConnection httpsConn;

    private String charset = "UTF-8";

    private OutputStream outputStream;

    private PrintWriter writer;

    private int method; //Request method if POST, GET, DELETE, or PUT.

    private String requestURL;

    //private String token; //Auth token to be passed in header.

    public final static int GET = 1;

    public final static int POST = 2;

    public final static int DELETE = 3;

    public final static int PUT = 4;

    private HttpsMultipartResponse onHttpsResponse;

    public HttpsMultipart(){

    }

    public void initialize(String SrequestURL, String token, int Imethod)
            throws IOException, NoSuchAlgorithmException, KeyManagementException {
        this.requestURL = SrequestURL;
        this.method = Imethod;

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";

        URL url = new URL(requestURL);
        httpsConn = (HttpsURLConnection) url.openConnection();

        SSLContext sc;
        sc = SSLContext.getInstance("TLS");
        sc.init(null, null, new java.security.SecureRandom());

        if(method == POST || method == PUT){
            httpsConn.setUseCaches(false);
            httpsConn.setDoOutput(true); // indicates POST method
            httpsConn.setDoInput(true);
            if(method == POST){
                httpsConn.setRequestMethod("POST");
            }else{
                httpsConn.setRequestMethod("PUT");
            }
            httpsConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);

            httpsConn.setSSLSocketFactory(sc.getSocketFactory());
            if(token.length() != 0){
                httpsConn.setRequestProperty("Authorization", token);
            }

            outputStream = httpsConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                    true);
        }else if(method == GET){
            httpsConn.setDoInput(true);
            httpsConn.setRequestMethod("GET");
            httpsConn.setReadTimeout(10000);
            httpsConn.setConnectTimeout(15000);
            httpsConn.setSSLSocketFactory(sc.getSocketFactory());
            httpsConn.setRequestProperty("Authorization", token);
            httpsConn.connect();
        }else if(method == DELETE){
            httpsConn.setUseCaches(false);
            httpsConn.setDoOutput(true); // indicates POST method
            httpsConn.setDoInput(true);
            httpsConn.setRequestMethod("DELETE");
            httpsConn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);
            httpsConn.setSSLSocketFactory(sc.getSocketFactory());
            httpsConn.setRequestProperty("Authorization", token);
            outputStream = httpsConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                    true);
        }

    }

    /**
     * Adds a form field to the request
     * @param name field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=").append(charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    public void addUriBuilder(Uri.Builder uri){
        String query = uri.build().getEncodedQuery();

        try {
            OutputStream os = httpsConn.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"").append(fieldName).append("\"; filename=\"").append(fileName).append("\"")
                .append(LINE_FEED);
        writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     * @param name - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name).append(": ").append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public void finish(HttpsMultipartResponse LonHttpsMultipartResponse) throws IOException {
        String response;
        boolean result;
        this.onHttpsResponse = LonHttpsMultipartResponse;

        if(method != GET){
            writer.append(LINE_FEED).flush();
            writer.append("--").append(boundary).append("--").append(LINE_FEED);
            writer.close();
        }

        // checks server's status code first
        int status = httpsConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK || status == HttpURLConnection.HTTP_CREATED) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpsConn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            response = String.valueOf(sb);
            result = true;
            httpsConn.disconnect();
            reader.close();

        } else {

            InputStream in = httpsConn.getErrorStream();
            StringBuffer sb = new StringBuffer();
            try {
                int chr;
                while ((chr = in.read()) != -1) {
                    sb.append((char) chr);
                }
                response = sb.toString();
                result = false;
            } finally {
                in.close();
            }

        }

        onHttpsResponse.onHttpsMultipartResponse(result, response);

    }
}
