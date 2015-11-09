package org.nunocky.ocrtest01;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.nunocky.ocrtest01.R;

public class Main3Activity extends Activity {

    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        final TakeOverInfo takeOverInfo = (TakeOverInfo) getIntent().getSerializableExtra("key");
        editText = (EditText) findViewById(R.id.editText3);
        if (takeOverInfo.getMozi().length() != 0) {
            editText.setText(editText.getText().toString().length() == 0 ? takeOverInfo.getMozi() : editText.getText() + " " + takeOverInfo.getMozi());
            editText.setSelection(editText.getText().toString().length());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main3, menu);
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
