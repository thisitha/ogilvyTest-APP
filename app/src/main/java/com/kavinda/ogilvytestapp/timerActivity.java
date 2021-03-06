package com.kavinda.ogilvytestapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class timerActivity extends AppCompatActivity {
Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        getTimerDuration();

    }

    private void getTimerDuration() {
          RequestQueue mRequestQueue;
          StringRequest mStringRequest;
          String url = "https://ogilvytest1.000webhostapp.com/";

        mRequestQueue = Volley.newRequestQueue(this);


        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String timeDuration ="";
                TextView timerText = findViewById(R.id.timerView);
                try {
                    JSONObject reader = new JSONObject(response);
                     timeDuration =reader.getString("time");
                    //Toast.makeText(getApplicationContext(), "Response :" + timeDuration, Toast.LENGTH_LONG).show();//display the response on screen
                    new CountDownTimer(Integer.parseInt(timeDuration), 100) {

                        public void onTick(long millisUntilFinished) {
                            timerText.setText("Seconds Remaining: " + millisUntilFinished / 1000);
                        }

                        @RequiresApi(api = Build.VERSION_CODES.O)
                        public void onFinish() {

                            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                            NotificationChannel channel = new NotificationChannel("1", "TimesUp", NotificationManager.IMPORTANCE_HIGH);
                            manager.createNotificationChannel(channel);
                            Notification notification = new Notification.Builder(timerActivity.this, "1")

                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Times Upppp!!")
                                    .setContentText("Your Time is up")
                                    .build();
                            manager.notify(1, notification);
                            timerText.setText("Times Up !!");
                        }
                    }.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }

        );

        mRequestQueue.add(mStringRequest);
    }
}
