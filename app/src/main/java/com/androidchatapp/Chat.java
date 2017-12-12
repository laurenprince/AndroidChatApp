package com.androidchatapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.view.View.TEXT_ALIGNMENT_TEXT_START;


public class Chat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2, reference3, reference4, ref1, ref2;
    private static final String TAG = "MainActivity";
    int count = 0;
    long sclock;
    long fclock;
    long tmp;

    {
        tmp = 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //final Intent starterIntent = getIntent();
        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        Firebase.setAndroidContext(this);
        //androidchatapp-76776

        reference1 = new Firebase("https://androidchatapp-f936f.firebaseio.com/threads/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://androidchatapp-f936f.firebaseio.com/threads/" + UserDetails.chatWith + "_" + UserDetails.username);
        //ref1=new Firebase("https://androidchatapp-f936f.firebaseio.com/threadpws/" + UserDetails.username + "_" + UserDetails.chatWith);
        //ref2 = new Firebase("https://androidchatapp-f936f.firebaseio.com/threadpws/" + UserDetails.chatWith + "_" + UserDetails.username);

       // scrollView.scrollTo(0,scrollView.getBottom());
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String messageText = messageArea.getText().toString();
                if(!messageText.equals("")){
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    Map<String, String> map = new HashMap<String, String>();
                    String strDate = dateFormat.format(date);

                    map.put("message", messageText);
                    map.put("user", UserDetails.username);

                    String key = genkey();

                    Map<String, String> map2 = new HashMap<String, String>();
                    Map<String, String> map3 = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    map.put("key",key);
                    map2.put("date",strDate);
                    map2.put("message", "S: "+strDate);
                    map2.put("key", key);
                    map2.put("user", UserDetails.username);

                    map3.put("date",strDate);
                    map3.put("message", "U: "+strDate);
                    map3.put("key", key);
                    map3.put("user", UserDetails.username);

                    //reference1.push().setValue(map2);
                    //reference2.push().setValue(map3);
                    reference1.child(strDate+" SENT").setValue(map2);
                    reference2.child(strDate+" RECEIVED").setValue(map3);

                    reference3 = new Firebase("https://androidchatapp-f936f.firebaseio.com/messages/" + key);
                    reference3.push().setValue(map);

                    messageArea.setText("");
                    count++;


                    //scrollView.scrollTo(0,140);
                }

                /*if(count > 3){
                    reference1.removeValue();
                    reference2.removeValue();
                    //setContentView(R.layout.activity_chat);
                   // finish();
                    //startActivity(starterIntent);
                    recreate();
                }*/

            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String key = map.get("key").toString();
                String dateN = map.get("date").toString();
                Log.d(TAG, "HEREEEEE: "+message+userName);
                if(userName.equals(UserDetails.username)){
                    addMessageBox("You:-\n" + message, 1, key,dateN);
                }
                else{
                    addMessageBox(UserDetails.chatWith + ":-\n" + message, 2, key,dateN);
                    //reference3.push();

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void addMessageBox(String message, int type, final String key, final String dateK) {
        final Button clickTextView = new Button(Chat.this);
        final String messageN = message;
        clickTextView.setText(message);
        final String keynew = key;

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.RIGHT;
            clickTextView.setBackgroundResource(R.drawable.rounded_rectangle_one);
            clickTextView.setPadding(50,50,50,50);
            clickTextView.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            clickTextView.setMaxWidth(600);



        } else {
            lp2.gravity = Gravity.LEFT;
            clickTextView.setBackgroundResource(R.drawable.rounded_rectangle_two);
            clickTextView.setPadding(50,50,50,50);
            clickTextView.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            clickTextView.setMaxWidth(600);
            String rmsg = clickTextView.getText().toString();
            Log.d(TAG,rmsg);
            if(rmsg.contains("R:")){
                clickTextView.setBackgroundResource(R.drawable.rounded_rectangle_grey);
            }
            else if(rmsg.contains("U:")){
                clickTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //clickTextView.setText("HELLO");
                        //Firebase.setAndroidContext(Chat.this);
                        reference4 = new Firebase("https://androidchatapp-f936f.firebaseio.com/messages/" + keynew);

                        reference4.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Map map = dataSnapshot.getValue(Map.class);
                                String messagek = map.get("message").toString();
                                String userName = map.get("user").toString();
                                String key = map.get("key").toString();
                                Log.d(TAG, "HEREEEEE: "+messagek+userName);
                                clickTextView.setText(messagek);

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                        new CountDownTimer(5000,1000){
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                Firebase reference5 = new Firebase("https://androidchatapp-f936f.firebaseio.com/messages/" + keynew);
                                reference5.removeValue();
                                Firebase reference6 = new Firebase("https://androidchatapp-f936f.firebaseio.com/threads/" + UserDetails.username + "_" + UserDetails.chatWith);
                                Firebase reference7 = new Firebase("https://androidchatapp-f936f.firebaseio.com/threads/" + UserDetails.chatWith + "_" + UserDetails.username);
                                String messageK = dateK;
                                reference2.child(dateK+" SENT").child("message").setValue("R: "+dateK);
                                reference1.child(dateK+" RECEIVED").child("message").setValue("R: "+dateK);



                                clickTextView.setText(UserDetails.chatWith + ":-\n" +"R: "+dateK);
                                clickTextView.setBackgroundResource(R.drawable.rounded_rectangle_grey);
                                Log.d(TAG, "CHYASS: "+messageK);
                                //reference1.childsetValue("READ");

                            }
                        }.start();
                    }
                });
            }
        }
        clickTextView.setLayoutParams(lp2);
        layout.addView(clickTextView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    public String genkey() {
        // Do something in response to button
        //Intent intent = new Intent(this, GenerateUser.class);
        //startActivity(intent);
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuv1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 15) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

}