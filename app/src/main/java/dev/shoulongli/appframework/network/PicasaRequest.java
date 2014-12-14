package dev.shoulongli.appframework.network;

/**
 * Created by shoulongli on 12/13/14.
 */

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.api.data.picasa.v2.PicasaWebAlbums;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import dev.shoulongli.picasaplus.picasa.util.PicasaUtil;
import dev.shoulongli.picasaplus.picasa.util.PicasaWebAlbum;


/**
 * Created by shoulongli on 11/27/14.
 */

/**
 * A request for retrieving a {@link JSONObject} response body at a given URL, allowing for an
 * optional {@link JSONObject} to be passed in as part of the request body.
 */
public class PicasaRequest<T> extends GsonRequest<T> {


    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url   URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     */
    public PicasaRequest(String url, Class<T> clazz,
                         Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(url, clazz, null, listener, errorListener);

    }

    /*
    https://developers.google.com/youtube/v3/live/authentication
    Calling the YouTube Live Streaming API
    After obtaining an access token for a user, your application can use that token to submit authorized API requests on that user's behalf. The API supports two ways to specify an access token:

    Specify the access token as the value of the Authorization: Bearer HTTP request header. This is the recommended approach.

    GET /youtube/v3/channels?part=id&mine=true HTTP/1.1
    Host: www.googleapis.com
    Authorization: Bearer ACCESS_TOKEN
    ...
    You can test this using cURL with the following command:

    curl -H "Authorization: Bearer ACCESS_TOKEN" https://www.googleapis.com/youtube/v3/channels?part=id&mine=true
    Specify the access token as the value of the access_token query parameter:

    https://www.googleapis.com/youtube/v3/channels?part=id&mine=true&access_token=ACCESS_TOKEN
    You can test this using cURL with the following command:

    curl https://www.googleapis.com/youtube/v3/channels?part=id&mine=true&access_token=ACCESS_TOKEN

     */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        String auth = "Bearer " + PicasaUtil.getToken();
        headers.put("Authorization", auth);
        headers.put("GData-Version", PicasaWebAlbums.VERSION);
        return headers;
    }

}

