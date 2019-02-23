package edu.stanford.cs108.cityinformation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = openOrCreateDatabase("citiesDB",MODE_PRIVATE,null);
        Cursor tablesCursor = db.rawQuery(
                "SELECT * FROM sqlite_master WHERE type='table' AND name='cities';",
                null);
        if (tablesCursor.getCount() == 0){
            createDB();
            insertData();
        }
    }

    private void createDB(){
        String clear = "DROP TABLE IF EXISTS cities";
        db.execSQL(clear);

        String string = "CREATE TABLE cities " +
                "(Name TEXT, Continent TEXT, Population INTEGER, " +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT)";
        db.execSQL(string);
    }

    private void insertData(){
        String data = "INSERT INTO cities VALUES" +
                "('Cairo','Africa',15200000, NULL)," +
                "('Lagos','Africa',21000000, NULL)," +
                "('Kyoto','Asia',1474570, NULL)," +
                "('Mumbai','Asia',20400000, NULL)," +
                "('Shanghai','Asia',24152700, NULL)," +
                "('Melbourne','Australia',3900000, NULL)," +
                "('London','Europe',8580000, NULL)," +
                "('Rome','Europe',2715000, NULL)," +
                "('Rostov-on-Don','Europe',1052000, NULL)," +
                "('San Francisco','North America',5780000, NULL)," +
                "('San Jose','North America',7354555, NULL)," +
                "('New York','North America',21295000, NULL)," +
                "('Rio de Janeiro','South America',12280702, NULL)," +
                "('Santiago','South America',5507282, NULL)";
        db.execSQL(data);
    }


    public void onSumbit(View view) {
        Intent intent = new Intent(this, lookUpCities.class);
        startActivity(intent);
    }

    public void addCity(View view) {
        Intent intent = new Intent(this, addCity.class);
        startActivity(intent);
    }

    public void reset(View view){
        String clear = "DROP TABLE IF EXISTS cities";
        db.execSQL(clear);
        createDB();
        insertData();
        Context context = getApplicationContext();
        Toast.makeText(context, "Database Reset", Toast.LENGTH_SHORT).show();
    }
}
