package com.practice.android.moments.Helper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


public class ServiceHandler {
    public final static int GET = 1;
    public final static int POST = 2;
    public static List<String> cookieList;
    public static List<String> cookieListloginsaved;
    static String response = null;
    String cookie;
    CookieStore cookieStore;
    String subString;
    Context con;
    HttpResponse resp1;

    public ServiceHandler(Context context) {
        con = context;
    }

    /*
     * Making service call
     *
     * @url - url to make request
     *
     * @method - http request method
     */
    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

	/*
     * Making service call
	 *
	 * @url - url to make request
	 *
	 * @method - http request method
	 *
	 * @params - http request params
	 */

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public String makeServiceCall(String url1, int method, ArrayList<BasicNameValuePair> params) {
        try {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(url1);
                urlConnection = (HttpURLConnection) url.openConnection();
                //CookieSyncManager.createInstance(con);
                // Set cookies in requests
                //CookieManager cookieManager = CookieManager.getInstance();
                ///cookie = cookieManager.getCookie(urlConnection.getURL().toString());

                //cookieStore = new BasicCookieStore();

                //	if (cookie != null) {
                //	urlConnection.setRequestProperty("Cookie", cookie);
                //	}

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            HttpParams params1 = new BasicHttpParams();
            HttpProtocolParams.setVersion(params1, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params1, "UTF-8");
            params1.setBooleanParameter("http.protocol.expect-continue", false);

            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient(params1);
            HttpContext localContext = new BasicHttpContext();
            //localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            if (method == POST) {
                HttpPost httpPost = new HttpPost(url1);
                //httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
                httpResponse = httpClient.execute(httpPost, localContext);
            } else if (method == GET) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url1 += "?" + paramString;
                }

                HttpGet httpGet = new HttpGet(url1);
                httpGet.addHeader("Cookie", cookie);
                httpResponse = httpClient.execute(httpGet, localContext);
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity, HTTP.UTF_8);

            subString = Normalizer.normalize(response, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response;
    }
}
