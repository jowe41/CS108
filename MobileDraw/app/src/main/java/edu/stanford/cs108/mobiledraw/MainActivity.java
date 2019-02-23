package edu.stanford.cs108.mobiledraw;

import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static edu.stanford.cs108.mobiledraw.CustomView.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void Update(View view) {
        if (selectID >= 0){
            RectF object = drawings.get(selectID);

            EditText newX = findViewById(R.id.x);
            EditText newY = findViewById(R.id.y);
            EditText newH = findViewById(R.id.height);
            EditText newW = findViewById(R.id.width);

            String x = newX.getText().toString();
            String y = newY.getText().toString();
            String h = newH.getText().toString();
            String w = newW.getText().toString();

            if(!x.equals("") && !y.equals("") && !h.equals("") && !w.equals("")) {
                float x1 = Float.parseFloat(x);
                float y1 = Float.parseFloat(y);
                float h1 = Float.parseFloat(h);
                float w1 = Float.parseFloat(w);

                if (mode.equals("select")) {
                    object.left = x1;
                    object.right = x1 + w1;
                    object.top = y1;
                    object.bottom = y1 + h1;
                    selectID = drawings.size()-1;
                }
                if (mode.equals("oval") || mode.equals("rect")){
                    left = x1;
                    right = x1 + w1;
                    top = y1;
                    bottom = y1 + h1;
                    drawings.remove(object);
                    drawingType.remove(mode);
                }
                CustomView customV = (CustomView) findViewById(R.id.customView);
                customV.invalidate();
            }
        }
    }
}
