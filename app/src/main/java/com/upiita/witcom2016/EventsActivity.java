package com.upiita.witcom2016;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.upiita.witcom2016.dataBaseHelper.Controller;
import com.upiita.witcom2016.dataBaseHelper.WitcomDataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.upiita.witcom2016.WitcomLogoActivity.URL_BASE;

public class EventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        JsonArrayRequest request = new JsonArrayRequest(WitcomLogoActivity.URL_BASE + Constants.GET_EVENTS, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                SQLiteDatabase db = new WitcomDataBase(getApplicationContext()).getWritableDatabase();
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        ContentValues values = new ContentValues();

                        ((TextView)findViewById(R.id.textTest)).append(object.getString("name"));

                        ((TextView)findViewById(R.id.textTest)).append("NEW OBJECT\n");
                    }

                } catch (JSONException e) {
                    Log.d("FATALERROR1", e.toString());
                } finally {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FATALERROR", error.toString());
            }
        });

        Controller.getInstance().addToRequestQueue(request);
    }
}
