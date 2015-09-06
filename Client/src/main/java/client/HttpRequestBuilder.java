package client;

/**
 * Created by POLSKA on 24/06/2015.
 */

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.omg.CORBA.NameValuePair;
import sun.net.www.http.HttpClient;

public class HttpRequestBuilder
{

    public HttpRequestBuilder()
    {
    }



    /**
     *
     * @param _url the url of the server
     * @param _id the doors id, helps for logging
     * @param _action to determine whether the client is an exit or entrance
     * @param _biometricData contains a list of the biometric data(facial, fingerprint, keycode)
     */
    public HttpPost buildRequest(String _url, String _map, String _id, String _action, ArrayList<BiometricData> _biometricData)
    {
        HttpPost httpPost = new HttpPost(_url+_map);
        httpPost.addHeader("User-Agent", "piClient");
        httpPost.setEntity(buildHttpEntity(_id, _action, _biometricData));
        return httpPost;
    }


    public HttpEntity buildHttpEntity(String _id, String _action, ArrayList<BiometricData> _biometricData)
    {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        StringBody action = new StringBody(_action, ContentType.MULTIPART_FORM_DATA);
        StringBody entExtId = new StringBody(_id, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addPart("ID", entExtId);
        entityBuilder.addPart("Action", action);

        if(_biometricData != null)
        {
            for (BiometricData data : _biometricData)
            {
                entityBuilder.addBinaryBody(data.getType(), data.getData());
            }
        }

        HttpEntity entity = entityBuilder.build();
        return entity;
    }

}