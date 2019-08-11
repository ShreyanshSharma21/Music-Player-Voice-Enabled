package com.music.voice_enabled.musicplayervoiceenabled;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.music.voice_enabled.musicplayervoiceenabled.model.AudioModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    private String allItems[];
    private ListView audioList;
    private ArrayList<String> audioFilesList = new ArrayList<>();
    private ArrayList<Integer> pathList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appExternalStoragePermission();

    }

    public void appExternalStoragePermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        getAllMediaFiles();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        Toast.makeText(MainActivity.this, "Permission for storage space is required in order to display audio files", Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }


    public void getAllMediaFiles() {
        ContentResolver resolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int audioURI = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do {
                String songTitle = cursor.getString(title);
                String path = cursor.getString(audioURI);
                Integer ID = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));

                audioFilesList.add(path);
                pathList.add(ID);

            } while (cursor.moveToNext());

            cursor.close();
        }

        ArrayAdapter<String> arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, audioFilesList);
        audioList = findViewById(R.id.audioFilesList);
        audioList.setAdapter(arrayAdapter);
        audioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SmartPlayer.class);
                AudioModel model = new AudioModel(audioFilesList.get(position), pathList.get(position));
                intent.putExtra("audio_info", model);
                startActivity(intent);
            }
        });
    }
}
