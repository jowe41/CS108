package edu.stanford.cs108.mobiledraw;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class CustomView extends View {
    protected static String mode = "rect";
    protected static Integer selectID = -1;
    protected static List<RectF> drawings = new ArrayList<RectF>();
    protected static List<String> drawingType = new ArrayList<String>();
    protected static float top, bottom, left, right;
    private float point1X, point1Y, point2X, point2Y, findPointX, findPointY;
    private Paint paintObject, paintObjectF, selectObject;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {
        paintObject = new Paint();
        paintObject.setColor(Color.RED);
        paintObject.setStyle(Paint.Style.STROKE);
        paintObject.setStrokeWidth(5.0f);

        paintObjectF = new Paint();
        paintObjectF.setColor(Color.WHITE);
        paintObjectF.setStyle(Paint.Style.FILL);

        selectObject = new Paint();
        selectObject.setColor(Color.BLUE);
        selectObject.setStyle(Paint.Style.STROKE);
        selectObject.setStrokeWidth(15.0f);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mode.equals("rect") || mode.equals("oval")){
            RectF rect = new RectF(left, top, right, bottom);
            drawings.add(rect);
            drawingType.add(mode);
            selectID = drawings.size()-1;
            drawObject(canvas, selectID);
            showInfo(selectID);
        }
        if (mode.equals("select")){
            selectID = findObject();
            showInfo(selectID);
            drawObject(canvas, selectID);
        }
        if (mode.equals("erase")){
            selectID = findObject();
            if (selectID >= 0){
                drawings.remove(drawings.get(selectID));
                drawingType.remove(drawingType.get(selectID));
            }
            selectID = -1;
            showInfo(selectID);
            drawObject(canvas, selectID);
        }
    }

    private Integer findObject() {
        int index = -1;
        for (int i = drawings.size()-1; i >=0; i--){
            RectF object = drawings.get(i);
            if (object.left <= findPointX && object.right >= findPointX && object.top <= findPointY && findPointY <= object.bottom){
                index = i;
                break;
            }
        }
        return index;
    }

    private void drawObject(Canvas canvas, int index) {
        for (int i = 0; i < drawings.size(); i++) {
            if (index == i) {
                if (drawingType.get(i).equals("rect")) {
                    canvas.drawRect(drawings.get(i), selectObject);
                    canvas.drawRect(drawings.get(i), paintObjectF);
                }
                if (drawingType.get(i).equals("oval")) {
                    canvas.drawOval(drawings.get(i), selectObject);
                    canvas.drawOval(drawings.get(i), paintObjectF);
                }
            } else {
                if (drawingType.get(i).equals("rect")) {
                    canvas.drawRect(drawings.get(i), paintObject);
                    canvas.drawRect(drawings.get(i), paintObjectF);
                }
                if (drawingType.get(i).equals("oval")) {
                    canvas.drawOval(drawings.get(i), paintObject);
                    canvas.drawOval(drawings.get(i), paintObjectF);
                }
            }
        }
    }
    private void showInfo(Integer selectID) {
        EditText infoX = (EditText) ((Activity) getContext()).findViewById(R.id.x);
        EditText infoY = (EditText) ((Activity) getContext()).findViewById(R.id.y);
        EditText infoH = (EditText) ((Activity) getContext()).findViewById(R.id.height);
        EditText infoW = (EditText) ((Activity) getContext()).findViewById(R.id.width);
        if (selectID >= 0 && mode != "erase" ){
            RectF object= drawings.get(selectID);
            infoX.setText(Float.toString(object.left));
            infoY.setText(Float.toString(object.top));
            infoH.setText(Float.toString(object.height()));
            infoW.setText(Float.toString(object.width()));
        } else{
            infoX.setText("");
            infoY.setText("");
            infoH.setText("");
            infoW.setText("");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        RadioButton select = (RadioButton)((Activity)getContext()).findViewById(R.id.select);
        RadioButton rect = (RadioButton)((Activity)getContext()).findViewById(R.id.rect);
        RadioButton oval = (RadioButton)((Activity)getContext()).findViewById(R.id.oval);
        RadioButton erase = (RadioButton)((Activity)getContext()).findViewById(R.id.erase);

        if (select.isChecked()){
            mode = "select";
        } else if(rect.isChecked()){
            mode = "rect";
        } else if (oval.isChecked()){
            mode = "oval";
        } else if (erase.isChecked()){
            mode = "erase";
        }

        if (mode.equals("select") || mode.equals("erase")){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    findPointX = event.getX();
                    findPointY = event.getY();
                    invalidate();
                    break;
            }
        } else{
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    point1X = event.getX();
                    point1Y = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    point2X = event.getX();
                    point2Y = event.getY();
                    left = point1X < point2X ? point1X : point2X;
                    right = point1X > point2X ? point1X: point2X;
                    top = point1Y < point2Y ? point1Y : point2Y;
                    bottom = point1Y > point2Y ? point1Y : point2Y;
                    invalidate();
                    break;
            }

        }
        return true;
    }
}
