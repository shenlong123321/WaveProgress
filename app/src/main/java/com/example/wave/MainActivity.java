package com.example.wave;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Wave wave;
    private float waveHeightPercent=0.5f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wave=(Wave)findViewById(R.id.wave);
        wave.startToWave();
        Timer timer=new Timer(true);
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                if(waveHeightPercent<=0){
                    waveHeightPercent=1;
                }else{
                    waveHeightPercent-=0.001;
                }
                wave.setProgress(waveHeightPercent);
            }
        };
        timer.schedule(timerTask,0,1000);
    }
}
