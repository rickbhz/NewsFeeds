package newsfeed.projetouna.com.newsfeed;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.toolbox.JsonObjectRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import java.util.HashMap;
import java.util.Map;

import newsfeed.projetouna.com.adapter.FeedListAdapter;
import newsfeed.projetouna.com.app.AppController;

public class FeedList extends Activity {

    Map<Integer, String> map = new HashMap<Integer, String>();
    private static final String TAG = FeedList.class.getSimpleName();
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private int id;
    public void setMap(Map<Integer, String> map) {
        this.map = map;
        map.put(7, getString(R.string.feedlist_ultimasnoticas));
        map.put(5, getString(R.string.feedlist_mundo));
        map.put(0, getString(R.string.feedlist_brasil));
        map.put(1, getString(R.string.feedlist_ciencia));
        map.put(3, getString(R.string.feedlist_entretenimento));
        map.put(4, getString(R.string.feedlist_esporte));
        map.put(6, getString(R.string.feedlist_saude));
        map.put(2, getString(R.string.feedlist_economia));
    }

    public String getMap(int key){
        String value = this.map.get(key);
        return value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Intent intent = getIntent();
        id = intent.getExtras().getInt("numberId");
        setMap(map);

        String urlFeed = "https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q=" + getMap(id) + "&ned=pt-BR_br&start=0&rsz=8";


        listView = (ListView) findViewById(R.id.list);

        feedItems = new ArrayList<FeedItem>();

        listAdapter = new FeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);

        // These two lines not needed,
        // just to get the look of facebook (changing background color & hiding the icon)
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8C1717")));
        getActionBar().setIcon(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        getActionBar().setTitle(getMap(id));

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Entry entry = cache.get(urlFeed);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                    urlFeed, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONObject("responseData").getJSONArray("results");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setTitle(feedObj.getString("titleNoFormatting"));
                item.setName(feedObj.getString("publisher"));
                String feedUrl = feedObj.isNull("unescapedUrl") ? null : feedObj.getString("unescapedUrl");
                item.setUrl(feedUrl);

                JSONObject imageObj = feedObj.getJSONObject("image");

                // Image might be null sometimes
                String image = imageObj.isNull("url") ? null : imageObj.getString("url");
                item.setImage(image);

                item.setTimeStamp(feedObj.getString("publishedDate"));

                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista, menu);
        return true;
    }

}
