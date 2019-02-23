package edu.stanford.cs108.colorpick;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int Red = 0;
    int Green = 0;
    int Blue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeColor(View view) {

        SeekBar barRed = findViewById(R.id.RedBar);
        Red = barRed.getProgress();
        SeekBar barGreen = findViewById(R.id.GreenBar);
        Green = barGreen.getProgress();
        SeekBar barBlue = findViewById((R.id.BlueBar));
        Blue = barBlue.getProgress();

        View background = findViewById(R.id.background);
        background.setBackgroundColor(Color.rgb(Red, Green, Blue));

        TextView textView = findViewById(R.id.colorInfo);
        String backgroundColor = "Red: " + Integer.toString(Red)
                + ", Green: " + Integer.toString(Green)
                + ", Blue: " + Integer.toString(Blue);
        textView.setText(backgroundColor);

    }
}
