package com.example.gravaraudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.Manifest;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Button btn_gravar, btn_parar,btn_ouvir;

    static int microPermissionCode = 200;

    MediaRecorder mediaRecorder;

    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComponentes();

        if(isMicroPresent()){
            getMicropermi();
        }
        btn_gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaRecorder = new MediaRecorder();

                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // Configura a fonte de áudio como o microfone do dispositivo
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP); // Configura o formato de saída do arquivo de áudio
                    mediaRecorder.setOutputFile(getRecordFilePath());
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB); // Configura o codificador de áudio para AMR-NB

                    mediaRecorder.prepare();
                    mediaRecorder.start();

                    Toast.makeText(MainActivity.this, "Gravando...", Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btn_parar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;

                Toast.makeText(MainActivity.this, "Gravação finalizada", Toast.LENGTH_LONG).show();
            }
        });

        btn_ouvir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(getRecordFilePath());
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    Toast.makeText(MainActivity.this, "Reproduzindo", Toast.LENGTH_LONG).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }




    private void iniciarComponentes() {
        btn_gravar = findViewById(R.id.btn_gravar);
        btn_ouvir = findViewById(R.id.btn_ouvir);
        btn_parar = findViewById(R.id.btn_parar);
    }
    boolean isMicroPresent() {
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        else{
            return false;
        }
    }
    void getMicropermi() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, microPermissionCode);
        }
    }

    String getRecordFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDiretory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDiretory, "arquivomUsica" + ".mp3");

        return file.getPath();
    }


}