package com.androidchatapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import java.util.Random;

public class AdminPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


      //  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
          //  @Override
          //  public void onClick(View view) {
          //      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //            .setAction("Action", null).show();
        //    }
        //});
        Firebase.setAndroidContext(this);
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView user = (TextView) findViewById(R.id.textView2);
                user.setText(genCreds());
                TextView pass = (TextView) findViewById(R.id.textView5);
                pass.setText(genCreds());
                userReg(user, pass);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public String genCreds() {
        // Do something in response to button
        //Intent intent = new Intent(this, GenerateUser.class);
        //startActivity(intent);
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 15) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public void userReg(TextView name, TextView pass){
        //int type = 0;
        final String userName = name.getText().toString();
        final String userPass = pass.getText().toString();
        //String purpose = "register";
       // BackEndDBTasks backEndDBTasks = new BackEndDBTasks(this);
        //backEndDBTasks.execute(purpose,userName,userPass);
        //finish();
       final ProgressDialog pd = new ProgressDialog(AdminPage.this);
       pd.setMessage("Loading...");
       pd.show();

        //String url = "https://androidchatapp-76776.firebaseio.com/users.json";
        String url="https://androidchatapp-f936f.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                //Firebase reference = new Firebase("https://androidchatapp-76776.firebaseio.com/users");
                Firebase reference = new Firebase("https://androidchatapp-f936f.firebaseio.com/users");

                if(s.equals("null")) {
                    reference.child(userName).child("password").setValue(userPass);

                    Toast.makeText(AdminPage.this, "registration successful", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(userName)) {
                            reference.child(userName).child("password").setValue(userPass);
                            reference.child(userName).child("accounttype").setValue(0); //all non-admins are type 0
                            reference.child(userName).child("messages").setValue(true);
                            Toast.makeText(AdminPage.this, "registration successful", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AdminPage.this, "username already exists", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                pd.dismiss();
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError );
                pd.dismiss();
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(AdminPage.this);
        rQueue.add(request);
    }
    }

