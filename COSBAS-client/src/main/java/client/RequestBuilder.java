package client;

/**
 * Created by POLSKA on 24/06/2015.
 */

import java.io.File;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

public class RequestBuilder {
    public RequestBuilder()
    {

    }

    public HttpPost buildPostRequest(String url)
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("User-Agent", "piClient");
        httpPost.setEntity(buildHttpEntity());

        return httpPost;
    }

    public HttpEntity buildHttpEntity()
    {
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        //config files?? we need to know from where they coming in or leaving. reporting!
        StringBody action = new StringBody("enter", ContentType.MULTIPART_FORM_DATA);
        StringBody entExtId = new StringBody("1", ContentType.MULTIPART_FORM_DATA);
        entityBuilder.addPart("ID", entExtId);
        entityBuilder.addPart("Action", action);

        File[] files = getFaceFiles();
        if(files != null)
        {
            int count = 1;
            for (File face : files)
            {
                FileBody fileBody = new FileBody(face);
                entityBuilder.addPart("face"+count, fileBody);
                count++;
            }
        }

        File finger = getFingerFile();
        if(finger != null)
        {
            FileBody fileBody = new FileBody(finger);
            entityBuilder.addPart("finger",fileBody);
        }

        HttpEntity entity = entityBuilder.build();
        return entity;
    }

    public File[] getFaceFiles()
    {
        File directory = new File("images/faces");
        if(directory.exists())
        {
            File[] faces = directory.listFiles();

            return faces;
        }
        return null;
    }

    public File getFingerFile()
    {
        File directory = new File("images/finger");
        if(directory.exists())
        {
            File finger = directory.listFiles()[0];
            return finger;
        }
        return null;
    }

}
