package com.androidchatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

public class AboutDetails extends AppCompatActivity {
    EditText displayname,msgduration, chatpass;
    Button saveChangesButton;
    String name, chatpw;
    int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_details);
        /*displayname=(EditText)findViewById(R.id.preferredDisplayName);
        msgduration=(EditText)findViewById(R.id.messageLifeLength);
        saveChangesButton=(Button)findViewById(R.id.saveSettingsButton);
        chatpass=(EditText)findViewById(R.id.chatpw);*/
        Firebase.setAndroidContext(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        if (id == R.id.action_about) {
            about();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Intent startMain = new Intent(AboutDetails.this, Login.class);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    private void about() {
        Intent startAbout = new Intent(AboutDetails.this, AboutDetails.class);
        startActivity(startAbout);
    }
}
