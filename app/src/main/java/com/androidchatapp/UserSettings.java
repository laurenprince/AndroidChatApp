package com.androidchatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSettings extends AppCompatActivity {
    EditText displayname,msgduration, chatpass;
    Button saveChangesButton;
    String name, chatpw;
    int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        displayname=(EditText)findViewById(R.id.preferredDisplayName);
        msgduration=(EditText)findViewById(R.id.messageLifeLength);
        saveChangesButton=(Button)findViewById(R.id.saveSettingsButton);
        chatpass=(EditText)findViewById(R.id.chatpw);
        Firebase.setAndroidContext(this);

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = displayname.getText().toString();
                chatpw=chatpass.getText().toString();
                if(name.equals(""))
                    name=UserDetails.username;

                try{
                    duration = Integer.parseInt(msgduration.getText().toString());
                } catch (NumberFormatException e){
                    duration=5;
                }


                String url = "https://androidchatapp-f936f.firebaseio.com/users.json";

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Firebase reference = new Firebase("https://androidchatapp-f936f.firebaseio.com/users");
                        try {
                            JSONObject obj = new JSONObject(s);

                            reference.child(UserDetails.username).child("messageLifeLength").setValue(duration);
                            reference.child(UserDetails.username).child("displayName").setValue(name);
                            reference.child(UserDetails.username).child("chatpassword").setValue(chatpw);
                            Toast.makeText(UserSettings.this, "Settings saved!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(UserSettings.this,Users.class);
                            startActivity(intent);
                        } catch (JSONException e) {
                            Toast.makeText(UserSettings.this, "Error saving user settings...", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError){
                        System.out.println(""+volleyError);
                    }
                });
                RequestQueue rQueue = Volley.newRequestQueue(UserSettings.this);
                rQueue.add(request);
            }

        });

    }
}