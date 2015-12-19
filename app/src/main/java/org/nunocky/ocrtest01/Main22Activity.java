package org.nunocky.ocrtest01;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.nunocky.ocrtest01.R;

public class Main22Activity extends Activity {

    private ResearchHistory researchHistory;
    private SimpleCursorAdapter myadapter;
    private ListView listview;
    private String[] from;
    private int[] to;
    private Cursor cursor;
    private boolean kind;
    private boolean type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main22);

        researchHistory=new ResearchHistory(getApplicationContext());
        cursor=researchHistory.Query("SELECT * FROM mytable");
        from = new String[]{"research", "result"};
        to = new int[]{R.id.research, R.id.result};
        while(cursor.moveToNext()) {
            Log.v("tama","id"+ cursor.getString(cursor.getColumnIndex("_id")));
            Log.v("tama","kind"+ cursor.getString(cursor.getColumnIndex("kind")));
            Log.v("tama","type"+ cursor.getString(cursor.getColumnIndex("type")));
            Log.v("tama", "research"+cursor.getString(cursor.getColumnIndex("research")));
            Log.v("tama", "result"+cursor.getString(cursor.getColumnIndex("result")));
        }
        myadapter = new SimpleCursorAdapter(this, R.layout.list, cursor, from, to,0);
        listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(myadapter);



        ArrayAdapter<CharSequence> adapter1 =
                ArrayAdapter.createFromResource(this, R.array.sample_array,
                        android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner1=(Spinner)findViewById(R.id.spinner3);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                if (spinner.getSelectedItemPosition() == 0) {
                    if (type) {
                        Cursor cursor = researchHistory.Query("SELECT * FROM mytable WHERE kind = '0' AND type = '0'");
                        myadapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list, cursor, from, to, 0);
                        listview.setAdapter(myadapter);
                    } else {
                        Cursor cursor = researchHistory.Query("SELECT * FROM mytable WHERE kind = '0' AND type = '1'");
                        myadapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list, cursor, from, to, 0);
                        listview.setAdapter(myadapter);
                    }
                    kind=true;

                } else {
                    if (type) {
                        Cursor cursor = researchHistory.Query("SELECT * FROM mytable WHERE kind = '1' AND type = '0'");
                        myadapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list, cursor, from, to, 0);
                        listview.setAdapter(myadapter);
                    } else {
                        Cursor cursor = researchHistory.Query("SELECT * FROM mytable WHERE kind = '1' AND type = '0'");
                        myadapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list, cursor, from, to, 0);
                        listview.setAdapter(myadapter);
                    }
                    kind=false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<CharSequence> adapter2 =
                ArrayAdapter.createFromResource(this, R.array.sample_array2,
                        android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner2=(Spinner)findViewById(R.id.spinner4);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                if (spinner.getSelectedItemPosition() == 0) {
                    if (kind) {
                        Cursor cursor = researchHistory.Query("SELECT * FROM mytable WHERE kind = '0' AND type = '0'");
                        myadapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list, cursor, from, to, 0);
                        listview.setAdapter(myadapter);
                    } else {
                        Cursor cursor = researchHistory.Query("SELECT * FROM mytable WHERE kind = '1' AND type = '0'");
                        myadapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list, cursor, from, to, 0);
                        listview.setAdapter(myadapter);
                    }
                    type=true;

                } else {
                    if (kind) {
                        Cursor cursor = researchHistory.Query("SELECT * FROM mytable WHERE kind = '0' AND type = '1'");
                        myadapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list, cursor, from, to, 0);
                        listview.setAdapter(myadapter);
                    } else {
                        Cursor cursor = researchHistory.Query("SELECT * FROM mytable WHERE kind = '1' AND type = '1'");
                        myadapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.list, cursor, from, to, 0);
                        listview.setAdapter(myadapter);
                    }
                    type=false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main22, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
