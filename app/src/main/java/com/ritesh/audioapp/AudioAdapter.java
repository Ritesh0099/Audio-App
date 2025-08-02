package com.ritesh.audioapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.AudioViewHolder> {
    private Context context;
    private File[] audioFiles;

    public AudioAdapter(Context context, File[] audioFiles) {
        this.context = context;
        this.audioFiles = audioFiles;
    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.audio_item, parent, false);
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        File file = audioFiles[position];
        holder.audioName.setText(file.getName());

        holder.playBtn.setOnClickListener(v -> {
            MediaPlayer player = new MediaPlayer();
            try {
                player.setDataSource(file.getAbsolutePath());
                player.prepare();
                player.start();
                Toast.makeText(context, "Playing: " + file.getName(), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "Error playing file", Toast.LENGTH_SHORT).show();
            }
        });

        holder.shareBtn.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("audio/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, androidx.core.content.FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider",
                    file
            ));
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(shareIntent, "Share audio"));
        });

        holder.deleteBtn.setOnClickListener(v -> {
            if (file.delete()) {
                Toast.makeText(context, "Deleted: " + file.getName(), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return audioFiles.length;
    }

    static class AudioViewHolder extends RecyclerView.ViewHolder {
        TextView audioName;
        ImageButton playBtn, shareBtn, deleteBtn;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
            audioName = itemView.findViewById(R.id.audioName);
            playBtn = itemView.findViewById(R.id.playBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
