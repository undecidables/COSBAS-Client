package modules;

import com.google.gson.Gson;

/**
 * Created by simon on 2015-10-08.
 */
public class BuildObjectFromJSONResponse {

    public static Object buildOject(String response, Class _class)
    {
        return new Gson().fromJson(response, _class);
    }
}

