package modules;

import org.apache.http.client.methods.HttpPost;

/**
 * Created by simon on 2015-10-09.
 */
public interface HttpPostBuilderInterface {

    public HttpPost buildPost(Object data);
}
