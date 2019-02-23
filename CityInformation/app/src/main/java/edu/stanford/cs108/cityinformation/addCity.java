package edu.stanford.cs108.cityinformation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class addCity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        db = openOrCreateDatabase("citiesDB",MODE_PRIVATE,null);
    }

    public void add(View view){
        EditText name = findViewById(R.id.name);
        EditText continent = findViewById(R.id.continent);
        EditText population = findViewById(R.id.population);

        String Name = name.getText().toString();
        String Continent = continent.getText().toString();
        String Population = population.getText().toString();

        if (!Name.isEmpty() && !Continent.isEmpty() && !Population.isEmpty()) {
            String insert = "INSERT INTO cities VALUES" +
                    "('" + Name + "', '" + Continent + "', '" + Population + "', " + "NULL)";
            db.execSQL(insert);

            Context context = getApplicationContext();
            Toast.makeText(context, Name + " Added !", Toast.LENGTH_SHORT).show();
        }
    }
}
