package com.ritesh.audioapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import java.io.File;

public class AudioListActivity extends AppCompatActivity {

    RecyclerView audioRecyclerView;
    File[] audioFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);
        audioRecyclerView = findViewById(R.id.audioRecyclerView);

        File directory = new File(getExternalFilesDir(null), "AudioRecords");
        if (directory.exists()) {
            audioFiles = directory.listFiles();
            audioRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            audioRecyclerView.setAdapter(new AudioAdapter(this, audioFiles));
        } else {
            Toast.makeText(this, "No recordings found", Toast.LENGTH_SHORT).show();
        }
    }
}
