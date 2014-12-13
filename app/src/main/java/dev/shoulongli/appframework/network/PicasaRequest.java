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
public class PicasaRequest extends JsonRequest<JSONObject> {

    /**
     * Creates a new request.
     *
     * @param method        the HTTP method to use
     * @param url           URL to fetch the JSON from
     * @param jsonRequest   A {@link JSONObject} to post with the request. Null is allowed and
     *                      indicates no parameters will be posted along with request.
     * @param listener      Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public PicasaRequest(int method, String url, JSONObject jsonRequest,
                         Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                errorListener);
    }

    /**
     * Constructor which defaults to <code>GET</code> if <code>jsonRequest</code> is
     * <code>null</code>, <code>POST</code> otherwise.
     *
     * @see #PicasaRequest(int, String, JSONObject, com.android.volley.Response.Listener, com.android.volley.Response.ErrorListener)
     */
    public PicasaRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                         Response.ErrorListener errorListener) {
        this(jsonRequest == null ? Method.GET : Method.POST, url, jsonRequest,
                listener, errorListener);
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

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}

