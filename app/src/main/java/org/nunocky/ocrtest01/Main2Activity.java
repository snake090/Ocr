package org.nunocky.ocrtest01;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private Spinner selectSpinner;
    private static final int MENU_RESEARCH = 0;
    private static final int MENU_MENU = 1;
    private TakeOverInfo takeOverInfo;
    static final String DB = "sqlite_history.db";
    static final int DB_VERSION = 1;
    static final String CREATE_TABLE = "create table mytable ( _id integer primary key autoincrement, kind integer not null, research string not null, result string not null  );";
    static final String DROP_TABLE = "drop table mytable;";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("検索");
        takeOverInfo = (TakeOverInfo) getIntent().getSerializableExtra("key");
        editText = (EditText) findViewById(R.id.editText2);
        textView = (TextView) findViewById(R.id.textView);
        if (takeOverInfo.getMozi().length() != 0) {
            editText.setText(editText.getText().toString().length() == 0 ? takeOverInfo.getMozi() : editText.getText() + " " + takeOverInfo.getMozi());
            editText.setSelection(editText.getText().toString().length());
        }

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.sample_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectSpinner = (Spinner) findViewById(R.id.spinner);
        selectSpinner.setAdapter(adapter);
        selectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                if (spinner.getSelectedItemPosition() == 0) {
                    takeOverInfo.setFunction(true);
                } else {
                    takeOverInfo.setFunction(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_RESEARCH, 0, "検索");
        menu.add(0, MENU_MENU, 0, "メニュー画面に戻る");
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESEARCH:
                DictionaryConfiguration dictionaryConfiguration = new DictionaryConfiguration(takeOverInfo.isKind());
                dictionaryConfiguration.setWord(editText.getText().toString());

                if (takeOverInfo.isFunction()) {
                    Dictionary dictionary = new Dictionary(textView, dictionaryConfiguration,getApplicationContext());
                    dictionary.execute();

                } else {
                    TranslateResult translate = new TranslateResult(textView, dictionaryConfiguration,getApplicationContext());
                    translate.execute();

                }
                break;
            case MENU_MENU:
                Intent intent;

                intent = new Intent(Main2Activity.this, MainActivity.class);

                startActivity(intent);
                break;
        }
        return true;
    }

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
        public MySQLiteOpenHelper(Context c) {
            super(c, DB, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }
    }
}
