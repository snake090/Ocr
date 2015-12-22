package org.nunocky.ocrtest01;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class Main2Activity extends Activity {

    private EditText editText;
    private ImageView imageView;
    private Button button;
    private TextView textView;
    private String ret;
    private Spinner selectSpinner;
    static final String DB = "sqlite_history.db";
    static final int DB_VERSION = 1;
    static final String CREATE_TABLE = "create table mytable ( _id integer primary key autoincrement, kind integer not null, research string not null, result string not null  );";
    static final String DROP_TABLE = "drop table mytable;";
    static SQLiteDatabase mydb;

    private static final String TAG = "AndroidOCR.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final TakeOverInfo takeOverInfo = (TakeOverInfo) getIntent().getSerializableExtra("key");

        imageView = (ImageView) findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button2);
        //     imageView.setImageBitmap(takeOverInfo.getBitmap());
        editText = (EditText) findViewById(R.id.editText2);
        textView = (TextView) findViewById(R.id.textView);
        if (takeOverInfo.getMozi().length() != 0) {
            editText.setText(editText.getText().toString().length() == 0 ? takeOverInfo.getMozi() : editText.getText() + " " + takeOverInfo.getMozi());
            editText.setSelection(editText.getText().toString().length());
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DictionaryConfiguration dictionaryConfiguration = new DictionaryConfiguration(takeOverInfo.isKind());
                dictionaryConfiguration.setWord(editText.getText().toString());

                if (takeOverInfo.isFunction()) {
                    Dictionary dictionary = new Dictionary(textView, dictionaryConfiguration,getApplicationContext());
                    dictionary.execute();

                } else {
                    TranslateResult translate = new TranslateResult(textView, dictionaryConfiguration,getApplicationContext());
                    translate.execute();

                }



            }
        });
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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
