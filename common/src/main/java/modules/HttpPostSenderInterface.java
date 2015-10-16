package modules;

import org.apache.http.client.methods.HttpPost;

/**
 * Created by simon on 2015-10-09.
 */
public interface HttpPostSenderInterface {

    public Object sendPostRequest(HttpPost httpPost) throws Exception;
}
