package com.example.paintview3;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBar;
    TextView GlobalProgress;
    TextView Count;
    Integer radius;
    Button btnUndo;
    Button btnRedo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final GlobalClassTest global = (GlobalClassTest)getApplication();

        final PaintView3 pv = findViewById(R.id.pv);


        seekBar= findViewById(R.id.seekBar);
        Count = findViewById(R.id.Count);
        GlobalProgress = findViewById(R.id.gpText) ;
        radius = seekBar.getProgress();
        btnUndo = findViewById(R.id.btnUndo);
        btnRedo = findViewById(R.id.btnRedo);

        global.setRadius(radius);

        GlobalProgress.setText(Integer.toString(global.getRadius()));
        Count.setText(Integer.toString(radius));
        //Captures seek bar change and sets global variable.
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


                int radUpdate = seekBar.getProgress();
                global.setRadius(radUpdate);
                GlobalProgress.setText(Integer.toString(global.getRadius()));
                Count.setText(Integer.toString(radUpdate));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pv.Undo();
                int size = pv.balls.size();
                Count.setText(String.valueOf(size));
            }
        });
        btnRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pv.Redo();
                int size = pv.balls.size();
                Count.setText(String.valueOf(size));

            }
        });


    }
}
