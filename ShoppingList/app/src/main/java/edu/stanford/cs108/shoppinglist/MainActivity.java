package edu.stanford.cs108.shoppinglist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clearList(View view) {
        TextView textView = findViewById(R.id.shoppingList);
        textView.setText("");
        result = "";
    }

    public void addItem(View view) {
        EditText text = findViewById(R.id.input);
        String string;
        string = text.getText().toString();
        text.setText("");
        result = result.concat(string);
        result = result.concat("\n");
        TextView textView = findViewById(R.id.shoppingList);
        textView.setText(result);
    }
}
