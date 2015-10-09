package modules;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

/**
 * {@author Szymon}
 */
public class HttpPostRequestBuilder {
    HttpPost httpPost;
    MultipartEntityBuilder entityBuilder;
    HttpEntity entity;

    public HttpPostRequestBuilder(String url, String map)
    {
        httpPost = new HttpPost(url + map);
        entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    }

    public void addUserAgentHeader(String uAgent)
    {
        httpPost.addHeader("User-Agent", uAgent);
    }

    public void addStringEntities(String name, String value)
    {
        StringBody stringEntity = new StringBody(value, ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addPart(name, stringEntity);
    }

    public void addOtherEntities(String name, byte[] value)
    {
        entityBuilder.addBinaryBody(name, value);
    }

    public void setEntity()
    {
        entity = entityBuilder.build();
        httpPost.setEntity(entity);
    }

    public HttpPost getHttpPost()
    {
        return httpPost;
    }
}
