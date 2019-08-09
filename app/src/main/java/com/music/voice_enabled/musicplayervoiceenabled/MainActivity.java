package com.music.voice_enabled.musicplayervoiceenabled;

import android.Manifest;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private String allItems[];
    private ListView  audioList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appExternalStoragePermission();
        audioList = findViewById(R.id.audioFilesList);
    }

    public  void  appExternalStoragePermission()
    {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response)
                    {
                        displayAudioFilesName();


                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response)
                    {

                        Toast.makeText(MainActivity.this,"Permission for storage space is required in order to display audio files",Toast.LENGTH_LONG).show();



                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token)
                    {
                        token.continuePermissionRequest();


                    }
                }).check();
    }

    public ArrayList<File> readOnlyAudioFiles(File file)
    {

        ArrayList<File> arrayList = new ArrayList<>();
        File[] allFiles = file.listFiles();

        for (File individualFile : allFiles)
        {
            if (individualFile.isDirectory() && !individualFile.isHidden())
            {
                arrayList.addAll(readOnlyAudioFiles(individualFile));
            }
            else
            {
                if (individualFile.getName().endsWith(".mp3") || individualFile.getName().endsWith(".aac") || individualFile.getName().endsWith(".wav") || individualFile.getName().endsWith("wma"))
                {
                    arrayList.add(individualFile);
                }

            }
        }

        return arrayList;

    }

    public void displayAudioFilesName()
    {
        final ArrayList<File> audioFiles = readOnlyAudioFiles(Environment.getExternalStorageDirectory());
        allItems = new String[audioFiles.size()];

        for (int audioFileCounter = 0 ; audioFileCounter < audioFiles.size() ; audioFileCounter++)
        {
            allItems[audioFileCounter] =  audioFiles.get(audioFileCounter).getName();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1, allItems);
        audioList.setAdapter(arrayAdapter);

    }
}
