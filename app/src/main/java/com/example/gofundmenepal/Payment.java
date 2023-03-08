package com.example.gofundmenepal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import khalti.checkOut.api.Config;
import khalti.checkOut.api.OnCheckOutListener;
import khalti.checkOut.helper.KhaltiCheckOut;
import khalti.widget.KhaltiButton;

public class Payment extends AppCompatActivity implements View.OnClickListener {

    Config config;
    TextView payamt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        payamt = (TextView) findViewById(R.id.paymentAmt);
        KhaltiButton khaltiButton = (KhaltiButton) findViewById(R.id.khalti_button);
        khaltiButton.setOnClickListener(Payment.this);
    }

    @Override
    public void onClick(View v) {
        long amt;
        Log.d("---------","----------------------------------");
        Log.d("Amount",payamt.getText().toString());
        amt = Long.parseLong(payamt.getText().toString()) * 100;
        config = new Config("live_public_key_0a8da1a2174a4ceaa1e2722cdf18373e", "1", "Fundnepal", "fundnepal.com", amt, new OnCheckOutListener() {

            @Override
            public void onSuccess(HashMap<String, Object> data) {
                Log.i("Payment confirmed", data+"");
                try{
                    postRequest(data);
                }
                catch (Exception e){
                    Log.d("Error",e.toString());
                }
            }

            @Override
            public void onError(String action, String message) {
                Log.i(action, message);
            }

        });
        KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(this, config);
        khaltiCheckOut.show();
    }

    public void postRequest(HashMap<String, Object> data) throws IOException {

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = "https://khalti.com/api/v2/payment/verify/";

        OkHttpClient client = new OkHttpClient();

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("token", data.get("token"));
            postdata.put("amount", data.get("amount"));
        } catch(JSONException e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", "Key live_secret_key_a019b3e397af4830bb145be51b39f5c3")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String mMessage = response.body().string();
            }
        });
    }
}
