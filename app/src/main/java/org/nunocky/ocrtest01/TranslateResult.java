package org.nunocky.ocrtest01;

import android.content.Context;
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
    private Context context;

    public TranslateResult(TextView textView, DictionaryConfiguration dictionaryConfiguration,Context context) {
        super();
        this.textView = textView;
        this.dictionaryConfiguration = dictionaryConfiguration;
        this.context=context;
    }

    @Override
    protected String doInBackground(Void... value) {
        try {

            Translate.setClientId("A41410507");
            Translate.setClientSecret("RwmuBBxT3M1y7VpHAw0bJrp6zbN9vyemfhrCUtu64qk=");
            if(dictionaryConfiguration.isKind()) {
                result = Translate.execute(dictionaryConfiguration.getWord(), Language.ENGLISH, Language.JAPANESE);
            }else{
                result = Translate.execute(dictionaryConfiguration.getWord(), Language.JAPANESE,Language.ENGLISH);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        textView.setText(result);
        ResearchHistory researchHistory=new ResearchHistory(context);
        if(dictionaryConfiguration.isKind()) {
            researchHistory.Insert(1, 0, dictionaryConfiguration.getWord(), result);
        }else{
            researchHistory.Insert(1, 1, dictionaryConfiguration.getWord(), result);
        }

    }
}
