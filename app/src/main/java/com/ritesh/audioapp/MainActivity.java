package com.ritesh.audioapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText fileNameEditText;
    Button recordButton, showListButton;
    MediaRecorder mediaRecorder;
    boolean isRecording = false;
    String fileName, filePath;
    public static final int REQUEST_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileNameEditText = findViewById(R.id.fileNameEditText);
        recordButton = findViewById(R.id.recordButton);
        showListButton = findViewById(R.id.showListButton);

        if (!checkPermissions()) {
            requestPermissions();
        }

        recordButton.setOnClickListener(v -> {
            if (isRecording) {
                stopRecording();
                recordButton.setText("Start Recording");
                isRecording = false;
            } else {
                if (fileNameEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, "Please enter a file name", Toast.LENGTH_SHORT).show();
                } else {
                    startRecording();
                    recordButton.setText("Stop Recording");
                    isRecording = true;
                }
            }
        });

        showListButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AudioListActivity.class);
            startActivity(intent);
        });
    }

    private void startRecording() {
        fileName = fileNameEditText.getText().toString().trim() + ".3gp";
        File folder = new File(getExternalFilesDir(null), "AudioRecords");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        filePath = folder.getAbsolutePath() + "/" + fileName;

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Recording failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            Toast.makeText(this, "Recording saved: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to stop recording", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermissions() {
        int record = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return record == PackageManager.PERMISSION_GRANTED && storage == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
