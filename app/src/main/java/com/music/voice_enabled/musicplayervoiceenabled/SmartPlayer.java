package com.music.voice_enabled.musicplayervoiceenabled;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SmartPlayer extends AppCompatActivity
{
    private RelativeLayout parentRelativeLayout;
    private SpeechRecognizer speechRecognizer;
    private Intent speechRecogniserIntent;
    private String keeper = "";

    private ImageView pausePlayBtn , nextBtn , previousBtn;
    private TextView audioNameTxt;

    private ImageView imageView ;
    private RelativeLayout lowerRelativeLayout ;
    private Button voiceEnabledBtn;
    private String mode = "ON";

    private MediaPlayer mymediaPlayer;







    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_player);

        checkVoiceCommandPermission();

        pausePlayBtn = findViewById(R.id.play_pause_btn);
        nextBtn = findViewById(R.id.next_btn);
        previousBtn = findViewById(R.id.previous_btn);
        imageView = findViewById(R.id.logo);
        lowerRelativeLayout = findViewById(R.id.lower);
        voiceEnabledBtn = findViewById(R.id.VOICE_ENABLE_BUTTON);
        audioNameTxt = findViewById(R.id.audioName);





        parentRelativeLayout = findViewById(R.id.parentRelativeLayout);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(SmartPlayer.this);
        speechRecogniserIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecogniserIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecogniserIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {


                ArrayList<String>  matchesFound = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matchesFound != null)
                {
                    keeper = matchesFound.get(0);
                    Toast.makeText(SmartPlayer.this,"Result =" + keeper, Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });


        parentRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecogniserIntent);
                        keeper="";
                        break;

                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        break;
                }

                return  false ;

            }
        });


        voiceEnabledBtn.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                if (mode.equals("ON"))
                {
                    mode = "OFF";
                    voiceEnabledBtn.setText("Voice Mode - OFF");
                    lowerRelativeLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    mode = "ON";
                    voiceEnabledBtn.setText("Voice Mode - ON");
                    lowerRelativeLayout.setVisibility(View.GONE);
                }




            }
        });








    }

    private  void  checkVoiceCommandPermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(!(ContextCompat.checkSelfPermission(SmartPlayer.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED))
            {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));

                startActivity(intent);
                finish();



            }
        }
    }
}
