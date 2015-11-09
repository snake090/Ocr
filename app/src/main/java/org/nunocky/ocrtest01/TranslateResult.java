package org.nunocky.ocrtest01;

import android.os.AsyncTask;
import android.widget.TextView;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

/**
 * Created by Owner on 2015/11/09.
 */
public class TranslateResult extends AsyncTask<Void,Void,String> {

    private TextView textView;
    private DictionaryConfiguration dictionaryConfiguration;
    private String result;

    public TranslateResult(TextView textView, DictionaryConfiguration dictionaryConfiguration) {
        super();
        this.textView = textView;
        this.dictionaryConfiguration = dictionaryConfiguration;
    }

    @Override
    protected String doInBackground(Void... value) {
        try {

            Translate.setClientId("A41410507");
            Translate.setClientSecret("RwmuBBxT3M1y7VpHAw0bJrp6zbN9vyemfhrCUtu64qk=");
            if(dictionaryConfiguration.isKind()) {
                result = Translate.execute(dictionaryConfiguration.getWord(), Language.ENGLISH, Language.JAPANESE);
            }else{

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        textView.setText(result);

    }
}
