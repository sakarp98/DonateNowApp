package com.example.gofundmenepal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ChatGPTGenerate extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private EditText mEditText;
    private Button mButton;
    private String apiUrl = "https://api.openai.com/v1/completions";
    private String accessToken = "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";
    private List < Message > mMessages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpt_messages);
        mRecyclerView = findViewById(R.id.recycler_view);
        mEditText = findViewById(R.id.edit_text);
        mButton = findViewById(R.id.button);
        mMessages = new ArrayList < > ();
        mAdapter = new MessageAdapter(mMessages);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAPI();
            }
        });
    }
    private void callAPI() {
        String text = mEditText.getText().toString();
        mMessages.add(new Message(text, true));
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        mEditText.getText().clear();
        JSONObject requestBody = new JSONObject();
        try {
            //requestBody.put("model", "text-davinci-003");
            //requestBody.put("prompt", text);
            //requestBody.put("max_tokens", 4076);
            //requestBody.put("temperature", 1);
            //requestBody.put("top_p", 1);
            //requestBody.put("n", 1);
            //requestBody.put("stream", false);
            //requestBody.put("logprobs", null);
            //requestBody.put("stop", ".");
            requestBody.put("model", "text-davinci-003");
            requestBody.put("prompt", text);
            requestBody.put("max_tokens", 100);
            requestBody.put("temperature", 1);
            requestBody.put("top_p", 1);
            requestBody.put("frequency_penalty", 0.0);
            requestBody.put("presence_penalty", 0.0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, apiUrl, requestBody, new Response.Listener < JSONObject > () {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray choicesArray = response.getJSONArray("choices");
                    JSONObject choiceObject = choicesArray.getJSONObject(0);
                    String text = choiceObject.getString("text");
                    Log.e("API Response", response.toString());
                    //Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();
                    mMessages.add(new Message(text.replaceFirst("\n", "").replaceFirst("\n", ""), false));
                    mAdapter.notifyItemInserted(mMessages.size() - 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", error.toString());
            }
        }) {
            @Override
            public Map < String, String > getHeaders() throws AuthFailureError {
                Map < String, String > headers = new HashMap < > ();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
            @Override
            protected Response < JSONObject > parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
        };
        int timeoutMs = 25000; // 25 seconds timeout
        RetryPolicy policy = new DefaultRetryPolicy(timeoutMs, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        // Add the request to the RequestQueue
        MySingleton.getInstance(this).addToRequestQueue(request);
    }
}
