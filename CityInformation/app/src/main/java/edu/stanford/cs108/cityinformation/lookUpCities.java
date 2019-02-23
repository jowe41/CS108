package edu.stanford.cs108.cityinformation;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;

public class lookUpCities extends AppCompatActivity {
    SQLiteDatabase db;
    String[] fromArray = {"Name", "Continent", "Population"};
    int[] toArray = {R.id.resultName, R.id.resultContinent, R.id.resultPopulation};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_up_cities);
        db = openOrCreateDatabase("citiesDB",MODE_PRIVATE,null);
        Cursor tablesCursor = db.rawQuery(
                "SELECT * FROM sqlite_master WHERE type='table' AND name='cities';",
                null);
    }
    public void search(View view){
        EditText name = findViewById(R.id.name);
        EditText continent = findViewById(R.id.continent);
        EditText population = findViewById(R.id.population);

        String Name = name.getText().toString();
        String Continent = continent.getText().toString();
        String Population = population.getText().toString();

        String query = "";

        if (!Name.isEmpty()){
            query += "AND Name LIKE \'%" + Name + "%\' ";
        }

        if (!Continent.isEmpty()){
            query += "AND Continent LIKE \'%" + Continent + "%\' ";
        }

        if(!Population.isEmpty()){
            RadioButton greater = findViewById(R.id.greater);
            RadioButton less = findViewById(R.id.less);
            if (greater.isChecked()){
                query += "AND Population >= " + Population;
            }else if(less.isChecked()){
                query += "AND Population < " + Population;
            }
        }
        if (!Name.isEmpty() || !Continent.isEmpty() || !Population.isEmpty()) {
            query = query.substring(4);
            query = "WHERE " + query;
        }
        query = "SELECT * FROM cities " + query + ";";
        Cursor cursor = db.rawQuery(query, null);

        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.result,cursor,
                fromArray,toArray,0);
        ListView listView = findViewById(R.id.result);
        listView.setAdapter(adapter);
    }
}
