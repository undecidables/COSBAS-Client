package HTTPPostBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

public class HTTPRequestBuilder {

    public HTTPRequestBuilder() {
    }

    /**
     * @param _url           the url of the server
     * @param _id            the doors id, helps for logging
     * @param _action        to determine whether the client is an exit or entrance
     * @param _biometricData contains a list of the biometric data(facial, fingerprint, keycode)
     */
    public HttpPost buildRequest(String _url, String _map, String _id, String _action, byte[] _biometricData) {
        HttpPost httpPost = new HttpPost(_url + _map);
        httpPost.addHeader("User-Agent", "BiometricRegistration");
        httpPost.setEntity(buildHttpEntity(_id, _action, _biometricData));
        return httpPost;
    }


    public HttpEntity buildHttpEntity(String emplid, String _email, byte[] _biometricData) {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        StringBody email = new StringBody(_email, ContentType.MULTIPART_FORM_DATA);
        StringBody personID = new StringBody(emplid, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addPart("personID", personID);
        entityBuilder.addPart("email", email);

        if (_biometricData != null) {
            entityBuilder.addBinaryBody("biometric-FACE", _biometricData);
        }

        HttpEntity entity = entityBuilder.build();
        return entity;
    }

}
