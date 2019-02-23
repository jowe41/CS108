package edu.stanford.cs108.shippingcalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    float cost = 0;
    float costPerLbs = 10;
    boolean addInsurance = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void calculateCost(View view) {
        EditText editText = findViewById(R.id.inPutValue);
        float lbs = Float.parseFloat(editText.getText().toString());
        String choice = "";
        RadioGroup group = findViewById(R.id.Choice);
        int shipMethod = group.getCheckedRadioButtonId();
        switch(shipMethod){
            case R.id.NextDay:
                choice = "nextDay";
                break;
            case R.id.SecondDay:
                choice = "secondDay";
                break;
            case R.id.Standard:
                choice = "Standard";
                break;
        }
        if (choice.equals("nextDay")){
            costPerLbs = 10;
        }
        else if(choice.equals("secondDay")){
            costPerLbs = 5;
        } else{
            costPerLbs = 3;
        }
        if (addInsurance) {
            cost = lbs * costPerLbs;
            cost *= 1.2;
        } else{
            cost = lbs * costPerLbs;
        }

        TextView textView = findViewById(R.id.cost);
        String final_cost = "Cost: $".concat(Integer.toString((int)Math.ceil(cost)));
        textView.setText(final_cost);
    }

    public void addInsurance(View view) {
        CheckBox checkBox = findViewById(R.id.isInsurance);
        if (checkBox.isChecked()){
            addInsurance = true;
        } else{
            addInsurance = false;
        }
    }
}
